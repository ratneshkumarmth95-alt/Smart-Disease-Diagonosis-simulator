import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import {
  HealthCheck,
  SymptomLog,
  DiagnosisResult,
  JournalEntry,
} from '../types';
import { useAuth } from './AuthContext';
import { LocalStore } from '../lib/storage';
import { supabase, isSupabaseConfigured } from '../lib/supabase';
import {
  updateStreak,
  checkAndAwardBadges,
  generateWeeklySummary,
} from '../services/gamificationService';

interface DataContextType {
  healthChecks: HealthCheck[];
  symptomLogs: SymptomLog[];
  diagnosisHistory: DiagnosisResult[];
  journalEntries: JournalEntry[];
  todayCheckIn?: HealthCheck;
  weeklySummary: {
    checkInsCount: number;
    avgWellness: number;
    avgEnergy: number;
    avgSleep: number;
    symptomsTrackedCount: number;
  };
  addCheckIn: (
    check: Omit<HealthCheck, 'id' | 'userId'>
  ) => Promise<{ success: boolean; newBadges: string[] }>;
  addSymptomLog: (log: Omit<SymptomLog, 'id' | 'userId'>) => Promise<boolean>;
  deleteSymptomLog: (id: string | number) => Promise<boolean>;
  addDiagnosisResult: (
    result: Omit<DiagnosisResult, 'id' | 'userId'>
  ) => Promise<{ success: boolean; newBadges: string[] }>;
  addJournalEntry: (
    entry: Omit<JournalEntry, 'id' | 'userId'>
  ) => Promise<{ success: boolean; newBadges: string[] }>;
  deleteJournalEntry: (id: string | number) => Promise<boolean>;
  refreshAll: () => Promise<void>;
}

const DataContext = createContext<DataContextType | undefined>(undefined);

export const DataProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user, updateUser, isDemo } = useAuth();
  const [healthChecks, setHealthChecks] = useState<HealthCheck[]>([]);
  const [symptomLogs, setSymptomLogs] = useState<SymptomLog[]>([]);
  const [diagnosisHistory, setDiagnosisHistory] = useState<DiagnosisResult[]>([]);
  const [journalEntries, setJournalEntries] = useState<JournalEntry[]>([]);

  const refreshAll = useCallback(async () => {
    if (!user) {
      setHealthChecks([]);
      setSymptomLogs([]);
      setDiagnosisHistory([]);
      setJournalEntries([]);
      return;
    }

    if (isSupabaseConfigured && supabase && !isDemo) {
      try {
        const [checksRes, logsRes, histRes, jourRes] = await Promise.all([
          supabase
            .from('health_checks')
            .select('*')
            .eq('user_id', user.id)
            .order('check_date', { ascending: false }),
          supabase
            .from('symptom_logs')
            .select('*')
            .eq('user_id', user.id)
            .order('log_date', { ascending: false }),
          supabase
            .from('diagnosis_history')
            .select('*')
            .eq('user_id', user.id)
            .order('analysis_date', { ascending: false }),
          supabase
            .from('health_journal')
            .select('*')
            .eq('user_id', user.id)
            .order('entry_date', { ascending: false }),
        ]);

        if (checksRes.data) {
          setHealthChecks(
            checksRes.data.map((c) => ({
              id: c.id,
              userId: c.user_id,
              checkDate: c.check_date,
              energyLevel: c.energy_level,
              sleepQuality: c.sleep_quality,
              stressLevel: c.stress_level,
              moodLevel: c.mood_level,
              notes: c.notes,
              wellnessScore: c.wellness_score,
            }))
          );
        }

        if (logsRes.data) {
          setSymptomLogs(
            logsRes.data.map((l) => ({
              id: l.id,
              userId: l.user_id,
              symptomId: l.symptom_id,
              symptomName: l.symptom_name,
              logDate: l.log_date,
              severity: l.severity,
              duration: l.duration,
              notes: l.notes,
            }))
          );
        }

        if (histRes.data) {
          setDiagnosisHistory(
            histRes.data.map((h) => ({
              id: h.id,
              userId: h.user_id,
              analysisDate: h.analysis_date,
              selectedSymptoms: h.selected_symptoms,
              topMatches: h.top_matches,
              emergencyWarning: h.emergency_warning,
              emergencyMessage: h.emergency_message,
              overallSeverity: h.overall_severity,
              duration: h.duration,
            }))
          );
        }

        if (jourRes.data) {
          setJournalEntries(
            jourRes.data.map((j) => ({
              id: j.id,
              userId: j.user_id,
              entryDate: j.entry_date,
              title: j.title,
              content: j.content,
              mood: j.mood,
            }))
          );
        }
        return;
      } catch (err) {
        console.error('Error fetching Supabase records', err);
      }
    }

    // Local Storage
    setHealthChecks(LocalStore.getHealthChecks(user.id));
    setSymptomLogs(LocalStore.getSymptomLogs(user.id));
    setDiagnosisHistory(LocalStore.getDiagnosisHistory(user.id));
    setJournalEntries(LocalStore.getJournalEntries(user.id));
  }, [user, isDemo]);

  useEffect(() => {
    refreshAll();
  }, [refreshAll]);

  const todayStr = new Date().toISOString().split('T')[0];
  const todayCheckIn = healthChecks.find((c) => c.checkDate === todayStr);

  const weeklySummary = generateWeeklySummary(
    healthChecks.slice(0, 7),
    symptomLogs.slice(0, 14)
  );

  const addCheckIn = async (
    checkData: Omit<HealthCheck, 'id' | 'userId'>
  ): Promise<{ success: boolean; newBadges: string[] }> => {
    if (!user) return { success: false, newBadges: [] };

    const newCheck: HealthCheck = {
      ...checkData,
      id: `check-${Date.now()}`,
      userId: user.id,
    };

    // Update streak and badges
    const updatedUser = { ...user };
    updateStreak(updatedUser, checkData.checkDate);
    const newBadges = checkAndAwardBadges(
      updatedUser,
      [newCheck, ...healthChecks],
      false,
      false
    );

    await updateUser(updatedUser);

    if (isSupabaseConfigured && supabase && !isDemo) {
      try {
        await supabase.from('health_checks').upsert({
          user_id: user.id,
          check_date: newCheck.checkDate,
          energy_level: newCheck.energyLevel,
          sleep_quality: newCheck.sleepQuality,
          stress_level: newCheck.stressLevel,
          mood_level: newCheck.moodLevel,
          notes: newCheck.notes,
          wellness_score: newCheck.wellnessScore,
        });
      } catch (err) {
        console.error('Supabase checkin save error', err);
      }
    } else {
      LocalStore.saveHealthCheck(newCheck);
    }

    setHealthChecks((prev) => {
      const filtered = prev.filter((c) => c.checkDate !== newCheck.checkDate);
      return [newCheck, ...filtered];
    });

    return { success: true, newBadges };
  };

  const addSymptomLog = async (
    logData: Omit<SymptomLog, 'id' | 'userId'>
  ): Promise<boolean> => {
    if (!user) return false;

    const newLog: SymptomLog = {
      ...logData,
      id: `log-${Date.now()}`,
      userId: user.id,
    };

    if (isSupabaseConfigured && supabase && !isDemo) {
      try {
        await supabase.from('symptom_logs').insert({
          user_id: user.id,
          symptom_id: newLog.symptomId,
          symptom_name: newLog.symptomName,
          log_date: newLog.logDate,
          severity: newLog.severity,
          duration: newLog.duration,
          notes: newLog.notes,
        });
      } catch (err) {
        console.error('Supabase symptom log insert error', err);
      }
    } else {
      LocalStore.saveSymptomLog(newLog);
    }

    setSymptomLogs((prev) => [newLog, ...prev]);
    return true;
  };

  const deleteSymptomLog = async (id: string | number): Promise<boolean> => {
    if (!user) return false;

    if (isSupabaseConfigured && supabase && !isDemo) {
      try {
        await supabase.from('symptom_logs').delete().eq('id', id);
      } catch (err) {
        console.error('Supabase symptom log delete error', err);
      }
    } else {
      LocalStore.deleteSymptomLog(id);
    }

    setSymptomLogs((prev) => prev.filter((l) => String(l.id) !== String(id)));
    return true;
  };

  const addDiagnosisResult = async (
    resData: Omit<DiagnosisResult, 'id' | 'userId'>
  ): Promise<{ success: boolean; newBadges: string[] }> => {
    if (!user) return { success: false, newBadges: [] };

    const newRes: DiagnosisResult = {
      ...resData,
      id: `diag-${Date.now()}`,
      userId: user.id,
    };

    const updatedUser = { ...user };
    const newBadges = checkAndAwardBadges(updatedUser, healthChecks, true, false);
    await updateUser(updatedUser);

    if (isSupabaseConfigured && supabase && !isDemo) {
      try {
        await supabase.from('diagnosis_history').insert({
          user_id: user.id,
          analysis_date: newRes.analysisDate,
          selected_symptoms: newRes.selectedSymptoms,
          top_matches: newRes.topMatches,
          emergency_warning: newRes.emergencyWarning,
          emergency_message: newRes.emergencyMessage,
          overall_severity: newRes.overallSeverity,
          duration: newRes.duration,
        });
      } catch (err) {
        console.error('Supabase diagnosis history insert error', err);
      }
    } else {
      LocalStore.saveDiagnosisResult(newRes);
    }

    setDiagnosisHistory((prev) => [newRes, ...prev]);
    return { success: true, newBadges };
  };

  const addJournalEntry = async (
    entryData: Omit<JournalEntry, 'id' | 'userId'>
  ): Promise<{ success: boolean; newBadges: string[] }> => {
    if (!user) return { success: false, newBadges: [] };

    const newEntry: JournalEntry = {
      ...entryData,
      id: `journal-${Date.now()}`,
      userId: user.id,
    };

    const updatedUser = { ...user };
    const newBadges = checkAndAwardBadges(updatedUser, healthChecks, false, true);
    await updateUser(updatedUser);

    if (isSupabaseConfigured && supabase && !isDemo) {
      try {
        await supabase.from('health_journal').insert({
          user_id: user.id,
          entry_date: newEntry.entryDate,
          title: newEntry.title,
          content: newEntry.content,
          mood: newEntry.mood,
        });
      } catch (err) {
        console.error('Supabase journal insert error', err);
      }
    } else {
      LocalStore.saveJournalEntry(newEntry);
    }

    setJournalEntries((prev) => [newEntry, ...prev]);
    return { success: true, newBadges };
  };

  const deleteJournalEntry = async (id: string | number): Promise<boolean> => {
    if (!user) return false;

    if (isSupabaseConfigured && supabase && !isDemo) {
      try {
        await supabase.from('health_journal').delete().eq('id', id);
      } catch (err) {
        console.error('Supabase journal delete error', err);
      }
    } else {
      LocalStore.deleteJournalEntry(id);
    }

    setJournalEntries((prev) => prev.filter((j) => String(j.id) !== String(id)));
    return true;
  };

  return (
    <DataContext.Provider
      value={{
        healthChecks,
        symptomLogs,
        diagnosisHistory,
        journalEntries,
        todayCheckIn,
        weeklySummary,
        addCheckIn,
        addSymptomLog,
        deleteSymptomLog,
        addDiagnosisResult,
        addJournalEntry,
        deleteJournalEntry,
        refreshAll,
      }}
    >
      {children}
    </DataContext.Provider>
  );
};

export const useData = () => {
  const context = useContext(DataContext);
  if (!context) throw new Error('useData must be used within a DataProvider');
  return context;
};
