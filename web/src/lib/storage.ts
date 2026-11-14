import {
  UserProfile,
  HealthCheck,
  SymptomLog,
  DiagnosisResult,
  JournalEntry,
  BADGES,
} from '../types';

const STORAGE_KEYS = {
  USER: 'sdds_current_user',
  USERS_LIST: 'sdds_users_list',
  HEALTH_CHECKS: 'sdds_health_checks',
  SYMPTOM_LOGS: 'sdds_symptom_logs',
  DIAGNOSIS_HISTORY: 'sdds_diagnosis_history',
  JOURNAL_ENTRIES: 'sdds_journal_entries',
  IS_DEMO: 'sdds_is_demo_mode',
};

export const DEFAULT_DEMO_USER: UserProfile = {
  id: 'demo-user-1',
  username: 'ratnesh',
  name: 'Ratnesh',
  age: 22,
  gender: 'Male',
  height: 175,
  weight: 70,
  bloodGroup: 'O+',
  allergies: 'Seasonal pollen',
  existingConditions: 'None',
  emergencyContact: 'Family Contact (+91 98765 43210)',
  streakDays: 4,
  totalCheckIns: 12,
  badges: [
    BADGES.FIRST_CHECKIN,
    BADGES.HYDRATION_TRACKER,
    BADGES.HEALTHY_HABITS,
    BADGES.SYMPTOM_REPORTER,
  ],
  lastCheckIn: new Date().toISOString().split('T')[0],
};

// Seed demo check-ins for the last 5 days
function generateSeedCheckIns(userId: string | number): HealthCheck[] {
  const list: HealthCheck[] = [];
  const today = new Date();
  const sampleScores = [
    { energy: 4, sleep: 4, stress: 2, mood: 4, score: 80, notes: 'Felt well-rested after 8 hrs sleep.' },
    { energy: 3, sleep: 3, stress: 3, mood: 3, score: 65, notes: 'Busy workday, took screen breaks.' },
    { energy: 4, sleep: 5, stress: 1, mood: 5, score: 90, notes: 'Hydrated well and took a long walk.' },
    { energy: 3, sleep: 4, stress: 2, mood: 4, score: 75, notes: 'Slight fatigue by evening.' },
    { energy: 4, sleep: 4, stress: 2, mood: 4, score: 80, notes: 'Morning meditation was refreshing.' },
  ];

  for (let i = 4; i >= 0; i--) {
    const d = new Date(today);
    d.setDate(d.getDate() - i);
    const dateStr = d.toISOString().split('T')[0];
    const s = sampleScores[4 - i];
    list.push({
      id: `check-${dateStr}`,
      userId,
      checkDate: dateStr,
      energyLevel: s.energy,
      sleepQuality: s.sleep,
      stressLevel: s.stress,
      moodLevel: s.mood,
      wellnessScore: s.score,
      notes: s.notes,
    });
  }
  return list;
}

function generateSeedSymptomLogs(userId: string | number): SymptomLog[] {
  const today = new Date();
  const d1 = new Date(today);
  d1.setDate(d1.getDate() - 3);
  const d2 = new Date(today);
  d2.setDate(d2.getDate() - 1);

  return [
    {
      id: 'log-1',
      userId,
      symptomId: 23,
      symptomName: 'Cough',
      logDate: d1.toISOString().split('T')[0],
      severity: 2,
      duration: '1-3 days',
      notes: 'Mild dry cough after running outdoors.',
    },
    {
      id: 'log-2',
      userId,
      symptomId: 11,
      symptomName: 'Headache',
      logDate: d2.toISOString().split('T')[0],
      severity: 3,
      duration: 'Today',
      notes: 'Frontal tension headache relieved by drinking 1L water.',
    },
  ];
}

function generateSeedJournal(userId: string | number): JournalEntry[] {
  const today = new Date();
  const d1 = new Date(today);
  d1.setDate(d1.getDate() - 2);

  return [
    {
      id: 'journal-1',
      userId,
      entryDate: d1.toISOString(),
      title: 'Post-workout recovery & evening herbal tea',
      content:
        'Stretched for 15 minutes after cardio. Head tension completely cleared up after staying away from phone screens before bed.',
      mood: 'GREAT',
    },
    {
      id: 'journal-2',
      userId,
      entryDate: today.toISOString(),
      title: 'Feeling energized and consistent',
      content:
        'Maintained my 4-day check-in streak. Remembering to drink water hourly makes a noticeable difference in energy.',
      mood: 'GOOD',
    },
  ];
}

export const LocalStore = {
  getUser(): UserProfile | null {
    const raw = localStorage.getItem(STORAGE_KEYS.USER);
    if (!raw) return null;
    try {
      const u = JSON.parse(raw);
      if (u && (u.name === 'Alex Johnson' || u.name === 'Alex' || u.username === 'demo_user')) {
        u.name = 'Ratnesh';
        u.username = 'ratnesh';
        localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(u));
      }
      return u;
    } catch {
      return null;
    }
  },

  setUser(user: UserProfile | null): void {
    if (user) {
      localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(user));
    } else {
      localStorage.removeItem(STORAGE_KEYS.USER);
    }
  },

  initDemoData(): UserProfile {
    const user = { ...DEFAULT_DEMO_USER };
    this.setUser(user);
    if (!localStorage.getItem(STORAGE_KEYS.HEALTH_CHECKS)) {
      localStorage.setItem(
        STORAGE_KEYS.HEALTH_CHECKS,
        JSON.stringify(generateSeedCheckIns(user.id))
      );
    }
    if (!localStorage.getItem(STORAGE_KEYS.SYMPTOM_LOGS)) {
      localStorage.setItem(
        STORAGE_KEYS.SYMPTOM_LOGS,
        JSON.stringify(generateSeedSymptomLogs(user.id))
      );
    }
    if (!localStorage.getItem(STORAGE_KEYS.JOURNAL_ENTRIES)) {
      localStorage.setItem(
        STORAGE_KEYS.JOURNAL_ENTRIES,
        JSON.stringify(generateSeedJournal(user.id))
      );
    }
    localStorage.setItem(STORAGE_KEYS.IS_DEMO, 'true');
    return user;
  },

  getHealthChecks(userId: string | number): HealthCheck[] {
    const raw = localStorage.getItem(STORAGE_KEYS.HEALTH_CHECKS);
    if (!raw) return [];
    try {
      const all: HealthCheck[] = JSON.parse(raw);
      return all
        .filter((c) => String(c.userId) === String(userId))
        .sort((a, b) => b.checkDate.localeCompare(a.checkDate));
    } catch {
      return [];
    }
  },

  saveHealthCheck(check: HealthCheck): void {
    const raw = localStorage.getItem(STORAGE_KEYS.HEALTH_CHECKS);
    let all: HealthCheck[] = [];
    try {
      if (raw) all = JSON.parse(raw);
    } catch {
      all = [];
    }

    // Replace if exists for same user & date
    const index = all.findIndex(
      (c) =>
        String(c.userId) === String(check.userId) &&
        c.checkDate === check.checkDate
    );

    if (index >= 0) {
      all[index] = check;
    } else {
      all.push(check);
    }

    localStorage.setItem(STORAGE_KEYS.HEALTH_CHECKS, JSON.stringify(all));
  },

  getSymptomLogs(userId: string | number): SymptomLog[] {
    const raw = localStorage.getItem(STORAGE_KEYS.SYMPTOM_LOGS);
    if (!raw) return [];
    try {
      const all: SymptomLog[] = JSON.parse(raw);
      return all
        .filter((l) => String(l.userId) === String(userId))
        .sort((a, b) => b.logDate.localeCompare(a.logDate));
    } catch {
      return [];
    }
  },

  saveSymptomLog(log: SymptomLog): void {
    const raw = localStorage.getItem(STORAGE_KEYS.SYMPTOM_LOGS);
    let all: SymptomLog[] = [];
    try {
      if (raw) all = JSON.parse(raw);
    } catch {
      all = [];
    }
    all.unshift(log);
    localStorage.setItem(STORAGE_KEYS.SYMPTOM_LOGS, JSON.stringify(all));
  },

  deleteSymptomLog(id: string | number): void {
    const raw = localStorage.getItem(STORAGE_KEYS.SYMPTOM_LOGS);
    if (!raw) return;
    try {
      const all: SymptomLog[] = JSON.parse(raw);
      const filtered = all.filter((l) => String(l.id) !== String(id));
      localStorage.setItem(STORAGE_KEYS.SYMPTOM_LOGS, JSON.stringify(filtered));
    } catch {
      // ignore
    }
  },

  getDiagnosisHistory(userId: string | number): DiagnosisResult[] {
    const raw = localStorage.getItem(STORAGE_KEYS.DIAGNOSIS_HISTORY);
    if (!raw) return [];
    try {
      const all: DiagnosisResult[] = JSON.parse(raw);
      return all
        .filter((r) => String(r.userId) === String(userId))
        .sort((a, b) => b.analysisDate.localeCompare(a.analysisDate));
    } catch {
      return [];
    }
  },

  saveDiagnosisResult(result: DiagnosisResult): void {
    const raw = localStorage.getItem(STORAGE_KEYS.DIAGNOSIS_HISTORY);
    let all: DiagnosisResult[] = [];
    try {
      if (raw) all = JSON.parse(raw);
    } catch {
      all = [];
    }
    all.unshift(result);
    localStorage.setItem(STORAGE_KEYS.DIAGNOSIS_HISTORY, JSON.stringify(all));
  },

  getJournalEntries(userId: string | number): JournalEntry[] {
    const raw = localStorage.getItem(STORAGE_KEYS.JOURNAL_ENTRIES);
    if (!raw) return [];
    try {
      const all: JournalEntry[] = JSON.parse(raw);
      return all
        .filter((j) => String(j.userId) === String(userId))
        .sort((a, b) => b.entryDate.localeCompare(a.entryDate));
    } catch {
      return [];
    }
  },

  saveJournalEntry(entry: JournalEntry): void {
    const raw = localStorage.getItem(STORAGE_KEYS.JOURNAL_ENTRIES);
    let all: JournalEntry[] = [];
    try {
      if (raw) all = JSON.parse(raw);
    } catch {
      all = [];
    }

    const index = all.findIndex((j) => String(j.id) === String(entry.id));
    if (index >= 0) {
      all[index] = entry;
    } else {
      all.unshift(entry);
    }
    localStorage.setItem(STORAGE_KEYS.JOURNAL_ENTRIES, JSON.stringify(all));
  },

  deleteJournalEntry(id: string | number): void {
    const raw = localStorage.getItem(STORAGE_KEYS.JOURNAL_ENTRIES);
    if (!raw) return;
    try {
      const all: JournalEntry[] = JSON.parse(raw);
      const filtered = all.filter((j) => String(j.id) !== String(id));
      localStorage.setItem(STORAGE_KEYS.JOURNAL_ENTRIES, JSON.stringify(filtered));
    } catch {
      // ignore
    }
  },

  clearAll(): void {
    localStorage.removeItem(STORAGE_KEYS.USER);
    localStorage.removeItem(STORAGE_KEYS.HEALTH_CHECKS);
    localStorage.removeItem(STORAGE_KEYS.SYMPTOM_LOGS);
    localStorage.removeItem(STORAGE_KEYS.DIAGNOSIS_HISTORY);
    localStorage.removeItem(STORAGE_KEYS.JOURNAL_ENTRIES);
    localStorage.removeItem(STORAGE_KEYS.IS_DEMO);
  },
};
