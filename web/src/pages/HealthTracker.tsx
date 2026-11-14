import React, { useState } from 'react';
import { useData } from '../context/DataContext';
import { SYMPTOMS } from '../data/knowledgeBase';
import { DurationOption, Symptom } from '../types';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { 
  Activity, 
  PlusCircle, 
  History, 
  TrendingUp, 
  Trash2, 
  Search, 
  Calendar, 
  AlertTriangle,
  Clock,
  Sparkles,
  CheckCircle2
} from 'lucide-react';
import { 
  LineChart, 
  Line, 
  XAxis, 
  YAxis, 
  CartesianGrid, 
  Tooltip, 
  ResponsiveContainer,
  BarChart,
  Bar
} from 'recharts';

const DURATION_LIST: DurationOption[] = ['Today', '1-3 days', '4-7 days', '1-2 weeks', 'More than 2 weeks'];

export const HealthTracker: React.FC = () => {
  const { symptomLogs, addSymptomLog, deleteSymptomLog, healthChecks } = useData();
  const [activeTab, setActiveTab] = useState<'log' | 'history' | 'trends'>('log');

  // Form State
  const [selectedSymptomId, setSelectedSymptomId] = useState<number | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [severity, setSeverity] = useState<number>(3);
  const [duration, setDuration] = useState<DurationOption>('1-3 days');
  const [notes, setNotes] = useState<string>('');
  const [saveSuccess, setSaveSuccess] = useState(false);

  const filteredSymptoms = SYMPTOMS.filter(s => 
    s.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    s.category.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const selectedSymptom: Symptom | undefined = SYMPTOMS.find(s => s.id === selectedSymptomId);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedSymptom) return;

    await addSymptomLog({
      symptomId: selectedSymptom.id,
      symptomName: selectedSymptom.name,
      severity,
      duration,
      notes: notes.trim(),
      logDate: new Date().toISOString().slice(0, 10),
    });

    setSaveSuccess(true);
    setSelectedSymptomId(null);
    setSearchQuery('');
    setSeverity(3);
    setNotes('');
    setTimeout(() => setSaveSuccess(false), 3000);
  };

  const getSeverityColor = (sev: number) => {
    if (sev <= 2) return 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20';
    if (sev === 3) return 'text-amber-400 bg-amber-500/10 border-amber-500/20';
    return 'text-rose-400 bg-rose-500/10 border-rose-500/20';
  };

  // Prepare Trends Data
  const sortedChecks = [...healthChecks].sort((a, b) => new Date(a.checkDate).getTime() - new Date(b.checkDate).getTime());
  const trendData = sortedChecks.slice(-7).map(c => ({
    date: new Date(c.checkDate).toLocaleDateString('en-US', { weekday: 'short', month: 'numeric', day: 'numeric' }),
    score: c.wellnessScore,
    energy: c.energyLevel,
    sleep: c.sleepQuality
  }));

  // Symptom frequency count
  const symptomCounts: { [name: string]: number } = {};
  symptomLogs.forEach(log => {
    symptomCounts[log.symptomName] = (symptomCounts[log.symptomName] || 0) + 1;
  });
  const frequencyData = Object.entries(symptomCounts)
    .map(([name, count]) => ({ name, count }))
    .sort((a, b) => b.count - a.count)
    .slice(0, 5);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl md:text-3xl font-bold text-white tracking-tight flex items-center gap-3">
            <Activity className="h-8 w-8 text-cyan-400" />
            Symptom & Wellness Tracker
          </h1>
          <p className="text-slate-400 text-sm mt-1">
            Monitor bodily signals, log symptom episodes, and analyze longitudinal wellness trends.
          </p>
        </div>

        {/* Tab Controls */}
        <div className="flex p-1 bg-slate-900/80 border border-slate-800 rounded-xl max-w-fit">
          <button
            onClick={() => setActiveTab('log')}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${
              activeTab === 'log'
                ? 'bg-gradient-to-r from-cyan-500 to-blue-600 text-white shadow-lg shadow-cyan-500/25'
                : 'text-slate-400 hover:text-white hover:bg-slate-800/50'
            }`}
          >
            <PlusCircle className="h-4 w-4" />
            Log Symptom
          </button>
          <button
            onClick={() => setActiveTab('history')}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${
              activeTab === 'history'
                ? 'bg-gradient-to-r from-cyan-500 to-blue-600 text-white shadow-lg shadow-cyan-500/25'
                : 'text-slate-400 hover:text-white hover:bg-slate-800/50'
            }`}
          >
            <History className="h-4 w-4" />
            History ({symptomLogs.length})
          </button>
          <button
            onClick={() => setActiveTab('trends')}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${
              activeTab === 'trends'
                ? 'bg-gradient-to-r from-cyan-500 to-blue-600 text-white shadow-lg shadow-cyan-500/25'
                : 'text-slate-400 hover:text-white hover:bg-slate-800/50'
            }`}
          >
            <TrendingUp className="h-4 w-4" />
            Trends & Analytics
          </button>
        </div>
      </div>

      {saveSuccess && (
        <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 p-4 rounded-xl flex items-center gap-3 animate-in fade-in slide-in-from-top-2">
          <CheckCircle2 className="h-5 w-5 flex-shrink-0" />
          <p className="text-sm font-medium">Symptom successfully recorded to your health journal!</p>
        </div>
      )}

      {/* TAB 1: LOG SYMPTOM */}
      {activeTab === 'log' && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="p-6">
              <form onSubmit={handleSave} className="space-y-6">
                <div>
                  <label className="block text-sm font-medium text-slate-300 mb-2">
                    1. Select Symptom to Track
                  </label>
                  <div className="relative mb-3">
                    <Search className="absolute left-3 top-3 h-4 w-4 text-slate-400" />
                    <input
                      type="text"
                      placeholder="Search symptom (e.g., headache, fever, fatigue)..."
                      value={searchQuery}
                      onChange={(e) => setSearchQuery(e.target.value)}
                      className="w-full bg-slate-900 border border-slate-800 rounded-xl pl-9 pr-4 py-2.5 text-sm text-white focus:outline-none focus:border-cyan-500 focus:ring-1 focus:ring-cyan-500"
                    />
                  </div>

                  {/* Symptom Selection Grid */}
                  <div className="grid grid-cols-2 sm:grid-cols-3 gap-2 max-h-48 overflow-y-auto p-1 border border-slate-800/70 rounded-xl bg-slate-950/40">
                    {filteredSymptoms.map((s) => (
                      <button
                        key={s.id}
                        type="button"
                        onClick={() => setSelectedSymptomId(s.id)}
                        className={`text-left p-2.5 rounded-lg border text-xs font-medium transition-all ${
                          selectedSymptomId === s.id
                            ? 'bg-cyan-500/20 border-cyan-500 text-cyan-200'
                            : 'bg-slate-900/50 border-slate-800 text-slate-300 hover:border-slate-700'
                        }`}
                      >
                        <div className="font-semibold truncate">{s.name}</div>
                        <div className="text-[10px] text-slate-400 mt-0.5">{s.category}</div>
                      </button>
                    ))}
                  </div>

                  {selectedSymptom && (
                    <div className="mt-3 p-3 bg-cyan-950/20 border border-cyan-800/40 rounded-xl flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <Badge variant="info">{selectedSymptom.category}</Badge>
                        <span className="text-sm font-semibold text-white">{selectedSymptom.name}</span>
                      </div>
                      {selectedSymptom.isEmergency && (
                        <span className="flex items-center gap-1 text-xs text-rose-400 font-medium">
                          <AlertTriangle className="h-3.5 w-3.5" /> High Clinical Significance
                        </span>
                      )}
                    </div>
                  )}
                </div>

                {/* Severity Slider */}
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <label className="text-sm font-medium text-slate-300">
                      2. Intensity / Severity Rating
                    </label>
                    <span className="text-xs font-semibold px-2.5 py-0.5 rounded-full bg-slate-800 text-cyan-400 border border-slate-700">
                      Grade {severity}/5 — {
                        severity === 1 ? 'Mild / Barely Noticeable' :
                        severity === 2 ? 'Discomforting' :
                        severity === 3 ? 'Moderate / Interferes with tasks' :
                        severity === 4 ? 'Severe / Substantial limitation' :
                        'Extreme / Incapacitating'
                      }
                    </span>
                  </div>
                  <input
                    type="range"
                    min="1"
                    max="5"
                    step="1"
                    value={severity}
                    onChange={(e) => setSeverity(Number(e.target.value))}
                    className="w-full h-2 bg-slate-800 rounded-lg appearance-none cursor-pointer accent-cyan-400"
                  />
                  <div className="flex justify-between text-[11px] text-slate-400 mt-1">
                    <span>1 (Mild)</span>
                    <span>2</span>
                    <span>3 (Moderate)</span>
                    <span>4</span>
                    <span>5 (Severe)</span>
                  </div>
                </div>

                {/* Duration */}
                <div>
                  <label className="block text-sm font-medium text-slate-300 mb-2">
                    3. Duration of Current Episode
                  </label>
                  <div className="grid grid-cols-2 sm:grid-cols-3 gap-2">
                    {DURATION_LIST.map((opt) => (
                      <button
                        key={opt}
                        type="button"
                        onClick={() => setDuration(opt)}
                        className={`p-2.5 rounded-xl border text-xs font-medium transition-all ${
                          duration === opt
                            ? 'bg-cyan-500/15 border-cyan-500 text-cyan-300'
                            : 'bg-slate-900 border-slate-800 text-slate-400 hover:border-slate-700'
                        }`}
                      >
                        {opt}
                      </button>
                    ))}
                  </div>
                </div>

                {/* Notes */}
                <div>
                  <label className="block text-sm font-medium text-slate-300 mb-2">
                    4. Contextual Notes (Optional)
                  </label>
                  <textarea
                    rows={3}
                    placeholder="e.g., Occurred 1 hour after dinner, worsened while standing, accompanied by mild tension..."
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl p-3 text-sm text-white focus:outline-none focus:border-cyan-500 focus:ring-1 focus:ring-cyan-500 placeholder-slate-500"
                  />
                </div>

                <Button
                  type="submit"
                  disabled={selectedSymptomId === null}
                  className="w-full py-3"
                  variant="primary"
                >
                  <PlusCircle className="h-4 w-4 mr-2" />
                  Log to Health Timeline
                </Button>
              </form>
            </Card>
          </div>

          {/* Quick Guide & Insight */}
          <div className="space-y-6">
            <Card className="p-5 border-cyan-500/20 bg-gradient-to-br from-cyan-950/20 via-slate-900/60 to-slate-900/90">
              <div className="flex items-center gap-2 text-cyan-400 font-semibold mb-3">
                <Sparkles className="h-5 w-5" />
                <h3>Why Track Daily Symptoms?</h3>
              </div>
              <p className="text-xs text-slate-300 leading-relaxed">
                Documenting symptoms longitudinally eliminates recency bias when consulting a doctor. Medical professionals benefit significantly from knowing exact onset, duration, and fluctuating severity patterns.
              </p>
              <div className="mt-4 pt-4 border-t border-slate-800/80 space-y-2 text-xs text-slate-400">
                <div className="flex items-center gap-2">
                  <span className="w-1.5 h-1.5 rounded-full bg-cyan-400" />
                  Helps distinguish acute from chronic issues
                </div>
                <div className="flex items-center gap-2">
                  <span className="w-1.5 h-1.5 rounded-full bg-cyan-400" />
                  Correlates fatigue with sleep and hydration
                </div>
                <div className="flex items-center gap-2">
                  <span className="w-1.5 h-1.5 rounded-full bg-cyan-400" />
                  Provides evidence-based consultation notes
                </div>
              </div>
            </Card>

            <Card className="p-5">
              <h4 className="text-sm font-semibold text-white mb-3 flex items-center gap-2">
                <Clock className="h-4 w-4 text-slate-400" />
                Recent Logs Overview
              </h4>
              {symptomLogs.length === 0 ? (
                <p className="text-xs text-slate-500 italic">No symptoms recorded yet.</p>
              ) : (
                <div className="space-y-2.5">
                  {symptomLogs.slice(-3).reverse().map((log, idx) => (
                    <div key={log.id ?? idx} className="p-2.5 bg-slate-900/60 border border-slate-800 rounded-lg flex items-center justify-between text-xs">
                      <div>
                        <div className="font-semibold text-slate-200">{log.symptomName}</div>
                        <div className="text-[11px] text-slate-400">{log.duration}</div>
                      </div>
                      <span className={`px-2 py-0.5 rounded border text-[11px] font-semibold ${getSeverityColor(log.severity)}`}>
                        Level {log.severity}/5
                      </span>
                    </div>
                  ))}
                </div>
              )}
            </Card>
          </div>
        </div>
      )}

      {/* TAB 2: HISTORY */}
      {activeTab === 'history' && (
        <Card className="p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="text-lg font-bold text-white">Recorded Symptom History</h3>
              <p className="text-xs text-slate-400">Chronological ledger of all logged physical and mental health indicators.</p>
            </div>
            <Badge variant="info">{symptomLogs.length} total entries</Badge>
          </div>

          {symptomLogs.length === 0 ? (
            <div className="text-center py-12 text-slate-500">
              <History className="h-12 w-12 mx-auto mb-3 opacity-30" />
              <p className="text-sm font-medium">No recorded symptoms found.</p>
              <Button onClick={() => setActiveTab('log')} variant="outline" className="mt-4 text-xs">
                Log Your First Symptom
              </Button>
            </div>
          ) : (
            <div className="space-y-3">
              {symptomLogs.slice().reverse().map((log, idx) => (
                <div
                  key={log.id ?? idx}
                  className="p-4 bg-slate-900/70 border border-slate-800 rounded-xl flex flex-col md:flex-row md:items-center justify-between gap-4 hover:border-slate-700 transition-colors"
                >
                  <div className="space-y-1">
                    <div className="flex items-center gap-3">
                      <span className="font-semibold text-white text-base">{log.symptomName}</span>
                      <span className={`text-xs px-2 py-0.5 rounded border font-semibold ${getSeverityColor(log.severity)}`}>
                        Severity {log.severity} of 5
                      </span>
                      {/* Visual gauge representation */}
                      <span className="text-xs tracking-widest text-cyan-400 font-mono hidden sm:inline">
                        {'█'.repeat(log.severity)}{'░'.repeat(5 - log.severity)}
                      </span>
                    </div>

                    <div className="flex flex-wrap items-center gap-4 text-xs text-slate-400">
                      <span className="flex items-center gap-1">
                        <Clock className="h-3.5 w-3.5 text-slate-500" />
                        Duration: {log.duration}
                      </span>
                      <span className="flex items-center gap-1">
                        <Calendar className="h-3.5 w-3.5 text-slate-500" />
                        {log.logDate}
                      </span>
                    </div>

                    {log.notes && (
                      <p className="text-xs text-slate-300 italic pt-1 border-t border-slate-800/60 mt-1">
                        &quot;{log.notes}&quot;
                      </p>
                    )}
                  </div>

                  {log.id && (
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => deleteSymptomLog(log.id!)}
                      className="text-slate-400 hover:text-rose-400 self-end md:self-center"
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  )}
                </div>
              ))}
            </div>
          )}
        </Card>
      )}

      {/* TAB 3: TRENDS & ANALYTICS */}
      {activeTab === 'trends' && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <Card className="p-5">
              <div className="text-xs text-slate-400 font-medium">Total Symptom Incidents</div>
              <div className="text-3xl font-bold text-white mt-2">{symptomLogs.length}</div>
              <div className="text-xs text-cyan-400 mt-1">Across all logged timeframes</div>
            </Card>
            <Card className="p-5">
              <div className="text-xs text-slate-400 font-medium">Average Symptom Severity</div>
              <div className="text-3xl font-bold text-amber-400 mt-2">
                {symptomLogs.length > 0 
                  ? (symptomLogs.reduce((acc, curr) => acc + curr.severity, 0) / symptomLogs.length).toFixed(1)
                  : '0.0'} / 5.0
              </div>
              <div className="text-xs text-slate-500 mt-1">Based on patient self-ratings</div>
            </Card>
            <Card className="p-5">
              <div className="text-xs text-slate-400 font-medium">Daily Check-ins Recorded</div>
              <div className="text-3xl font-bold text-emerald-400 mt-2">{healthChecks.length}</div>
              <div className="text-xs text-emerald-400 mt-1">Wellness telemetry tracking</div>
            </Card>
          </div>

          <Card className="p-6">
            <h3 className="text-base font-bold text-white mb-2">7-Day Wellness Score Trajectory</h3>
            <p className="text-xs text-slate-400 mb-6">Composite score calculated from water intake, sleep, activity, and stress management.</p>
            {trendData.length === 0 ? (
              <div className="h-64 flex items-center justify-center text-xs text-slate-500">
                Complete daily check-ins on the Dashboard to populate telemetry trends.
              </div>
            ) : (
              <div className="h-72 w-full">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart data={trendData}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                    <XAxis dataKey="date" stroke="#64748b" fontSize={11} />
                    <YAxis domain={[0, 100]} stroke="#64748b" fontSize={11} />
                    <Tooltip 
                      contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px' }}
                      labelStyle={{ color: '#94a3b8' }}
                    />
                    <Line type="monotone" dataKey="score" stroke="#06b6d4" strokeWidth={3} dot={{ fill: '#06b6d4', r: 4 }} name="Wellness Score" />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            )}
          </Card>

          {frequencyData.length > 0 && (
            <Card className="p-6">
              <h3 className="text-base font-bold text-white mb-2">Most Prevalent Symptoms</h3>
              <p className="text-xs text-slate-400 mb-6">Symptoms logged with highest recurrence rate.</p>
              <div className="h-60 w-full">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={frequencyData}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                    <XAxis dataKey="name" stroke="#64748b" fontSize={11} />
                    <YAxis allowDecimals={false} stroke="#64748b" fontSize={11} />
                    <Tooltip 
                      contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px' }}
                    />
                    <Bar dataKey="count" fill="#3b82f6" radius={[4, 4, 0, 0]} name="Occurrences" />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </Card>
          )}
        </div>
      )}
    </div>
  );
};
