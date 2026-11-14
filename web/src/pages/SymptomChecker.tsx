import React, { useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { useData } from '../context/DataContext';
import { useAuth } from '../context/AuthContext';
import {
  CATEGORIES,
  searchSymptoms,
} from '../data/knowledgeBase';
import {
  analyzeSymptoms,
  generateWhyExplanation,
  getRecommendedTracking,
} from '../services/diagnosisEngine';
import { generateAdvice, generateReport } from '../services/adviceEngine';
import {
  Symptom,
  DurationOption,
  DiagnosisResult,
  ConditionMatch,
} from '../types';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Modal } from '../components/ui/Modal';
import {
  Search,
  Check,
  X,
  ArrowRight,
  ArrowLeft,
  RotateCcw,
  Sparkles,
  HelpCircle,
  Activity,
  ShieldAlert,
  Calendar,
} from 'lucide-react';
import { motion } from 'motion/react';
import confetti from 'canvas-confetti';

export const SymptomChecker: React.FC = () => {
  const { addDiagnosisResult } = useData();
  const { user } = useAuth();
  const navigate = useNavigate();

  // Wizard Step: 1 = Select Symptoms, 2 = Severity & Duration, 3 = Results
  const [step, setStep] = useState<1 | 2 | 3>(1);

  // Selections
  const [selectedSymptoms, setSelectedSymptoms] = useState<Symptom[]>([]);
  const [severityMap, setSeverityMap] = useState<Record<number, number>>({});
  const [duration, setDuration] = useState<DurationOption>('1-3 days');

  // Search & Filter (Step 1)
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('All');

  // Results (Step 3)
  const [analysisResult, setAnalysisResult] = useState<DiagnosisResult | null>(null);
  const [whyModalMatch, setWhyModalMatch] = useState<ConditionMatch | null>(null);
  const [isReportOpen, setIsReportOpen] = useState(false);

  // Filtered symptoms list
  const filteredSymptoms = useMemo(() => {
    return searchSymptoms(searchQuery, selectedCategory);
  }, [searchQuery, selectedCategory]);

  const toggleSymptom = (symptom: Symptom) => {
    setSelectedSymptoms((prev) => {
      const exists = prev.some((s) => s.id === symptom.id);
      if (exists) {
        // remove
        const updated = prev.filter((s) => s.id !== symptom.id);
        const copySev = { ...severityMap };
        delete copySev[symptom.id];
        setSeverityMap(copySev);
        return updated;
      } else {
        // add
        setSeverityMap((m) => ({ ...m, [symptom.id]: 3 })); // default moderate severity
        return [...prev, symptom];
      }
    });
  };

  const handleClearAll = () => {
    setSelectedSymptoms([]);
    setSeverityMap({});
  };

  // Run analysis when transitioning to Step 3
  const handleProceedToAnalysis = async () => {
    const res = analyzeSymptoms(selectedSymptoms, severityMap, duration, user?.id);
    setAnalysisResult(res);
    setStep(3);

    // Save to history & trigger gamification
    const { newBadges } = await addDiagnosisResult(res);
    if (newBadges.length > 0 && !res.emergencyWarning) {
      try {
        confetti({ particleCount: 70, spread: 60, origin: { y: 0.6 } });
      } catch {
        // ignore
      }
    }
  };

  const restartChecker = () => {
    setSelectedSymptoms([]);
    setSeverityMap({});
    setDuration('1-3 days');
    setAnalysisResult(null);
    setStep(1);
  };

  const severityLabels: Record<number, { text: string; color: string }> = {
    1: { text: 'Very Mild', color: 'text-emerald-600' },
    2: { text: 'Mild', color: 'text-teal-600' },
    3: { text: 'Moderate', color: 'text-amber-600' },
    4: { text: 'Severe', color: 'text-rose-600' },
    5: { text: 'Very Severe', color: 'text-rose-700 font-extrabold' },
  };

  return (
    <div className="space-y-6 max-w-5xl mx-auto pb-16">
      {/* Wizard Step Progress Bar */}
      <div className="bg-white rounded-2xl p-4 sm:p-5 border border-slate-200/80 shadow-card flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="flex items-center gap-2">
            {[1, 2, 3].map((num) => (
              <React.Fragment key={num}>
                <div
                  className={`w-8 h-8 rounded-xl font-bold text-xs flex items-center justify-center transition-all ${
                    step === num
                      ? 'bg-brand-600 text-white shadow-md shadow-brand-500/30'
                      : step > num
                      ? 'bg-emerald-100 text-emerald-700 font-bold'
                      : 'bg-slate-100 text-slate-400'
                  }`}
                >
                  {step > num ? '✓' : num}
                </div>
                {num < 3 && (
                  <div
                    className={`w-6 sm:w-12 h-1 rounded-full transition-colors ${
                      step > num ? 'bg-emerald-400' : 'bg-slate-200'
                    }`}
                  />
                )}
              </React.Fragment>
            ))}
          </div>
          <span className="text-xs sm:text-sm font-bold text-slate-800 ml-2">
            {step === 1 && 'Step 1: Select Symptoms'}
            {step === 2 && 'Step 2: Rate Severity & Duration'}
            {step === 3 && 'Step 3: Analysis Results'}
          </span>
        </div>

        {selectedSymptoms.length > 0 && step !== 3 && (
          <Badge variant="info" size="md">
            {selectedSymptoms.length} Selected
          </Badge>
        )}
      </div>

      {/* ================= STEP 1: SELECT SYMPTOMS ================= */}
      {step === 1 && (
        <motion.div
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          {/* Search & Filter Header */}
          <Card className="p-4 sm:p-6 space-y-4">
            <div className="flex flex-col sm:flex-row gap-3">
              <div className="relative flex-1">
                <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  placeholder="Search 80+ symptoms (e.g. fever, cough, headache, dizziness)..."
                  className="w-full pl-10 pr-4 py-2.5 bg-slate-950 border border-slate-700 rounded-xl text-sm text-white placeholder-slate-400 focus:outline-none focus:border-cyan-400 focus:ring-1 focus:ring-cyan-400"
                />
                {searchQuery && (
                  <button
                    onClick={() => setSearchQuery('')}
                    className="absolute right-3.5 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                  >
                    <X className="w-4 h-4" />
                  </button>
                )}
              </div>

              {selectedSymptoms.length > 0 && (
                <Button variant="ghost" size="sm" onClick={handleClearAll} className="text-rose-600">
                  Clear All ({selectedSymptoms.length})
                </Button>
              )}
            </div>

            {/* Category Pills */}
            <div className="flex items-center gap-1.5 overflow-x-auto pb-1 scrollbar-none">
              {['All', ...CATEGORIES].map((cat) => (
                <button
                  key={cat}
                  onClick={() => setSelectedCategory(cat)}
                  className={`px-3 py-1.5 rounded-xl text-xs font-semibold whitespace-nowrap transition-all ${
                    selectedCategory === cat
                      ? 'bg-cyan-500 text-white shadow-md shadow-cyan-500/25 border border-cyan-400'
                      : 'bg-slate-800 text-slate-300 hover:bg-slate-700 hover:text-white border border-slate-700'
                  }`}
                >
                  {cat}
                </button>
              ))}
            </div>
          </Card>

          {/* Active Selection Chips Tray */}
          {selectedSymptoms.length > 0 && (
            <Card className="p-4 bg-brand-50/50 border-brand-200/80">
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-bold text-brand-900 uppercase tracking-wider flex items-center gap-1.5">
                  <Check className="w-4 h-4 text-brand-600" />
                  Currently Selected Symptoms ({selectedSymptoms.length})
                </span>
                <span className="text-[11px] text-slate-500">Tap tag to remove</span>
              </div>
              <div className="flex flex-wrap gap-2 pt-1">
                {selectedSymptoms.map((sym) => (
                  <button
                    key={sym.id}
                    onClick={() => toggleSymptom(sym)}
                    className="inline-flex items-center gap-1.5 px-3 py-1 bg-white border border-brand-300 rounded-xl text-xs font-semibold text-brand-900 shadow-sm hover:bg-rose-50 hover:border-rose-300 hover:text-rose-700 transition-colors group"
                  >
                    <span>{sym.name}</span>
                    <X className="w-3.5 h-3.5 text-slate-400 group-hover:text-rose-600" />
                  </button>
                ))}
              </div>
            </Card>
          )}

          {/* Symptoms Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
            {filteredSymptoms.map((sym) => {
              const isSelected = selectedSymptoms.some((s) => s.id === sym.id);
              return (
                <div
                  key={sym.id}
                  onClick={() => toggleSymptom(sym)}
                  className={`p-3.5 rounded-2xl border transition-all cursor-pointer flex items-center justify-between select-none ${
                    isSelected
                      ? 'bg-brand-50 border-brand-500 shadow-sm ring-1 ring-brand-500'
                      : 'bg-white border-slate-200 hover:border-slate-300 hover:shadow-card'
                  }`}
                >
                  <div className="min-w-0 pr-2">
                    <div className="flex items-center gap-1.5">
                      <span
                        className={`text-sm font-semibold truncate ${
                          isSelected ? 'text-brand-950 font-bold' : 'text-slate-800'
                        }`}
                      >
                        {sym.name}
                      </span>
                      {sym.isEmergency && (
                        <span
                          className="shrink-0 text-[10px] font-bold text-rose-600 bg-rose-50 border border-rose-200 px-1.5 py-0.5 rounded-md"
                          title="Potentially serious warning symptom"
                        >
                          ⚠️ Red Flag
                        </span>
                      )}
                    </div>
                    <span className="text-[11px] text-slate-600 block mt-0.5">
                      {sym.category}
                    </span>
                  </div>

                  <div
                    className={`w-6 h-6 rounded-lg flex items-center justify-center shrink-0 transition-colors ${
                      isSelected
                        ? 'bg-brand-600 text-white'
                        : 'border border-slate-300 bg-slate-50 text-transparent'
                    }`}
                  >
                    <Check className="w-3.5 h-3.5" />
                  </div>
                </div>
              );
            })}
          </div>

          {filteredSymptoms.length === 0 && (
            <div className="text-center py-16 bg-white rounded-3xl border border-dashed border-slate-200 p-8">
              <Search className="w-10 h-10 text-slate-300 mx-auto mb-3" />
              <h4 className="text-base font-bold text-slate-800">No matching symptoms found</h4>
              <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
                Try a different keyword or switch to the "All" category filter to view all 80 symptoms.
              </p>
            </div>
          )}

          {/* Bottom Action Footer */}
          <div className="flex items-center justify-between pt-4 border-t border-slate-200">
            <span className="text-xs text-slate-500">
              Select at least 1 symptom to proceed to severity analysis.
            </span>
            <Button
              variant="primary"
              size="lg"
              disabled={selectedSymptoms.length === 0}
              rightIcon={<ArrowRight className="w-4 h-4" />}
              onClick={() => setStep(2)}
            >
              Continue to Step 2 ({selectedSymptoms.length})
            </Button>
          </div>
        </motion.div>
      )}

      {/* ================= STEP 2: SEVERITY & DURATION ================= */}
      {step === 2 && (
        <motion.div
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          className="space-y-6"
        >
          {/* Duration Selector */}
          <Card className="p-6">
            <div className="flex items-center gap-2 mb-3">
              <Calendar className="w-5 h-5 text-brand-600" />
              <h3 className="text-base font-bold text-white">
                How long have these symptoms been present?
              </h3>
            </div>
            <p className="text-xs text-slate-400 mb-4">
              Duration influences the weighted algorithm and medical consultation recommendations.
            </p>

            <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3">
              {(
                ['Today', '1-3 days', '4-7 days', '1-2 weeks', 'More than 2 weeks'] as DurationOption[]
              ).map((dur) => (
                <button
                  key={dur}
                  type="button"
                  onClick={() => setDuration(dur)}
                  className={`p-3.5 rounded-2xl text-xs font-bold border transition-all text-center ${
                    duration === dur
                      ? 'bg-cyan-500 text-white border-cyan-400 shadow-md shadow-cyan-500/20'
                      : 'bg-slate-900 border-slate-800 text-slate-300 hover:bg-slate-800 hover:border-slate-700'
                  }`}
                >
                  {dur}
                </button>
              ))}
            </div>
          </Card>

          {/* Severity Sliders for Selected Symptoms */}
          <Card className="p-6 space-y-5">
            <div>
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <Activity className="w-5 h-5 text-amber-400" />
                Rate Severity for Each Symptom
              </h3>
              <p className="text-xs text-slate-400 mt-1">
                Rate from 1 (Very Mild) to 5 (Very Severe). This fine-tunes condition compatibility multipliers.
              </p>
            </div>

            <div className="space-y-4 pt-2">
              {selectedSymptoms.map((sym) => {
                const currentSev = severityMap[sym.id] ?? 3;
                const labelInfo = severityLabels[currentSev];

                return (
                  <div
                    key={sym.id}
                    className="p-4 rounded-2xl bg-slate-50/80 border border-slate-200/80 space-y-2"
                  >
                    <div className="flex items-center justify-between">
                      <span className="text-sm font-bold text-slate-800 flex items-center gap-1.5">
                        {sym.isEmergency && <span className="text-rose-500">⚠️</span>}
                        {sym.name}
                      </span>
                      <span className={`text-xs font-bold ${labelInfo.color}`}>
                        {labelInfo.text} ({currentSev} / 5)
                      </span>
                    </div>

                    <input
                      type="range"
                      min="1"
                      max="5"
                      value={currentSev}
                      onChange={(e) =>
                        setSeverityMap((prev) => ({
                          ...prev,
                          [sym.id]: Number(e.target.value),
                        }))
                      }
                      className="w-full h-2 bg-slate-200 rounded-lg accent-brand-600 cursor-pointer"
                    />

                    <div className="flex justify-between text-[11px] text-slate-600 font-medium">
                      <span>1 - Barely noticeable</span>
                      <span>3 - Moderate / uncomfortable</span>
                      <span>5 - Severe / incapacitating</span>
                    </div>
                  </div>
                );
              })}
            </div>
          </Card>

          {/* Navigation Controls */}
          <div className="flex items-center justify-between pt-4 border-t border-slate-200">
            <Button
              variant="outline"
              size="lg"
              leftIcon={<ArrowLeft className="w-4 h-4" />}
              onClick={() => setStep(1)}
            >
              Back to Symptoms
            </Button>
            <Button
              variant="primary"
              size="lg"
              rightIcon={<Sparkles className="w-4 h-4 text-white" />}
              onClick={handleProceedToAnalysis}
            >
              Run Compatibility Analysis
            </Button>
          </div>
        </motion.div>
      )}

      {/* ================= STEP 3: RESULTS SCREEN ================= */}
      {step === 3 && analysisResult && (
        <motion.div
          initial={{ opacity: 0, scale: 0.98 }}
          animate={{ opacity: 1, scale: 1 }}
          className="space-y-6"
        >
          {/* CRITICAL EMERGENCY WARNING (Short-Circuit) */}
          {analysisResult.emergencyWarning ? (
            <div className="bg-rose-600 text-white rounded-3xl p-6 sm:p-8 shadow-xl shadow-rose-600/20 border-2 border-rose-500 space-y-4">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-2xl bg-white/20 flex items-center justify-center shrink-0">
                  <ShieldAlert className="w-7 h-7 text-white" />
                </div>
                <div>
                  <h3 className="text-xl sm:text-2xl font-black tracking-tight">
                    URGENT MEDICAL ATTENTION MAY BE NEEDED
                  </h3>
                  <p className="text-xs text-rose-100 font-semibold uppercase tracking-wider mt-0.5">
                    Critical Red-Flag Symptoms Detected
                  </p>
                </div>
              </div>

              <div className="bg-rose-700/60 rounded-2xl p-4 sm:p-5 text-sm text-rose-50 leading-relaxed whitespace-pre-line border border-rose-500/50">
                {analysisResult.emergencyMessage}
              </div>

              <div className="bg-white/10 rounded-2xl p-4 text-xs text-rose-100">
                <strong>Emergency Priority Override:</strong> In medical software guidelines, critical symptoms override automated disease ranking to prevent users from delaying necessary urgent clinical care.
              </div>

              <div className="flex flex-wrap gap-3 pt-2">
                <Button
                  variant="outline"
                  size="md"
                  className="bg-white text-rose-700 hover:bg-rose-50 border-white font-bold"
                  onClick={() => setIsReportOpen(true)}
                >
                  View Summary Report
                </Button>
                <Button
                  variant="ghost"
                  size="md"
                  className="text-white hover:bg-rose-700 font-semibold"
                  onClick={restartChecker}
                >
                  Start New Check
                </Button>
              </div>
            </div>
          ) : (
            <>
              {/* Analysis Success Banner */}
              <div className="bg-gradient-to-r from-emerald-600 to-teal-700 text-white rounded-3xl p-6 sm:p-7 shadow-lg flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div>
                  <Badge variant="success" size="sm" className="bg-white/20 text-white border-white/30 mb-2">
                    Analysis Complete
                  </Badge>
                  <h2 className="text-xl sm:text-2xl font-extrabold tracking-tight">
                    Symptom Pattern Compatibility
                  </h2>
                  <p className="text-xs text-emerald-100 mt-1 max-w-xl">
                    Evaluated {analysisResult.selectedSymptoms.length} reported signs against 25 condition presentations.
                  </p>
                </div>
                <div className="flex items-center gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    className="bg-white/10 text-white border-white/30 hover:bg-white/20 font-semibold"
                    onClick={() => setIsReportOpen(true)}
                  >
                    Export Report
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    className="bg-white text-emerald-800 border-white font-bold hover:bg-emerald-50"
                    onClick={restartChecker}
                  >
                    New Check
                  </Button>
                </div>
              </div>

              {/* TOP 3 CONDITIONS */}
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <h3 className="text-base font-bold text-white">
                    Top Compatible Presentations
                  </h3>
                  <span className="text-xs text-slate-400">
                    Ranked by weighted similarity score
                  </span>
                </div>

                {analysisResult.topMatches.map((match, idx) => {
                  const rankColors = [
                    { border: 'border-emerald-500', badge: 'success' },
                    { border: 'border-cyan-500', badge: 'info' },
                    { border: 'border-slate-600', badge: 'default' },
                  ] as const;

                  const colorInfo = rankColors[idx] || rankColors[2];

                  return (
                    <Card
                      key={match.disease.id}
                      className={`p-5 sm:p-6 border-l-4 ${colorInfo.border}`}
                      hoverEffect
                    >
                      <div className="flex flex-col sm:flex-row sm:items-start justify-between gap-3">
                        <div>
                          <div className="flex items-center gap-2 flex-wrap">
                            <span className="text-xs font-bold px-2 py-0.5 rounded-md bg-slate-800 text-slate-200">
                              Rank #{idx + 1}
                            </span>
                            <h4 className="text-lg font-extrabold text-white">
                              {match.disease.name}
                            </h4>
                            <Badge variant={colorInfo.badge} size="sm">
                              {match.compatibilityLabel} Compatibility
                            </Badge>
                            <span className="text-xs text-slate-400 font-medium">
                              ({match.disease.category})
                            </span>
                          </div>
                          <p className="text-xs text-slate-300 mt-2 max-w-2xl leading-relaxed">
                            {match.disease.description}
                          </p>
                        </div>

                        {/* Compatibility Score Circle */}
                        <div className="text-right shrink-0 bg-slate-50 p-3 rounded-2xl border border-slate-100 min-w-[110px]">
                          <div className="text-2xl font-black text-brand-600">
                            {match.scorePercent}%
                          </div>
                          <span className="text-[10px] text-slate-600 uppercase tracking-wider font-semibold">
                            Compatibility
                          </span>
                        </div>
                      </div>

                      {/* Matched & Missing Indicators */}
                      <div className="mt-4 pt-4 border-t border-slate-100 grid grid-cols-1 md:grid-cols-2 gap-3 text-xs">
                        <div className="bg-emerald-50/70 p-3 rounded-xl border border-emerald-100">
                          <span className="font-bold text-emerald-900 block mb-1">
                            Matched Signs ({match.matchedCount}/{match.totalDiseaseSymptoms}):
                          </span>
                          <div className="flex flex-wrap gap-1.5">
                            {match.matchedSymptoms.map((s) => (
                              <span
                                key={s.id}
                                className="inline-flex items-center gap-1 bg-white px-2 py-0.5 rounded-md text-[11px] font-semibold text-emerald-800 border border-emerald-200"
                              >
                                ✓ {s.name}
                              </span>
                            ))}
                          </div>
                        </div>

                        {match.missingKeySymptoms.length > 0 && (
                          <div className="bg-slate-50 p-3 rounded-xl border border-slate-200/80">
                            <span className="font-bold text-slate-700 block mb-1">
                              Characteristic Signs Not Reported:
                            </span>
                            <div className="flex flex-wrap gap-1.5">
                              {match.missingKeySymptoms.map((s) => (
                                <span
                                  key={s.id}
                                  className="inline-flex items-center gap-1 bg-white px-2 py-0.5 rounded-md text-[11px] font-medium text-slate-600 border border-slate-200"
                                >
                                  ✕ {s.name}
                                </span>
                              ))}
                            </div>
                          </div>
                        )}
                      </div>

                      {/* Why Button */}
                      <div className="mt-3 flex justify-end">
                        <Button
                          variant="ghost"
                          size="sm"
                          leftIcon={<HelpCircle className="w-3.5 h-3.5 text-brand-600" />}
                          onClick={() => setWhyModalMatch(match)}
                          className="text-xs font-semibold text-brand-700"
                        >
                          Why did this condition match?
                        </Button>
                      </div>
                    </Card>
                  );
                })}

                {analysisResult.topMatches.length === 0 && (
                  <Card className="p-8 text-center text-slate-500">
                    <p className="text-sm font-semibold">No strong condition patterns identified.</p>
                    <p className="text-xs mt-1">Consider consulting a doctor if you feel unwell.</p>
                  </Card>
                )}
              </div>

              {/* RECOMMENDED TRACKING */}
              <Card className="p-6 bg-brand-50/50 border-brand-200">
                <div className="flex items-center gap-2 mb-2">
                  <Activity className="w-5 h-5 text-brand-600" />
                  <h4 className="text-sm font-bold text-brand-950 uppercase tracking-wider">
                    Recommended Items to Track
                  </h4>
                </div>
                <p className="text-xs text-slate-600 mb-3">
                  Based on your reported symptoms, monitoring these key metrics will help observe recovery:
                </p>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 text-xs">
                  {getRecommendedTracking(analysisResult).map((trackItem, i) => (
                    <div
                      key={i}
                      className="flex items-center gap-2 p-2 rounded-xl bg-white border border-brand-200/60 font-medium text-slate-800"
                    >
                      <span className="w-2 h-2 rounded-full bg-brand-500 shrink-0" />
                      <span>{trackItem}</span>
                    </div>
                  ))}
                </div>
              </Card>

              {/* SAFE ADVICE & SELF-CARE POINTS */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {generateAdvice(analysisResult).map((adv, idx) => (
                  <Card key={idx} className="p-5 space-y-2.5">
                    <h4 className="text-sm font-bold text-white flex items-center gap-2">
                      <span className="w-2 h-2 rounded-full bg-cyan-400" />
                      {adv.title}
                    </h4>
                    <ul className="space-y-1.5 text-xs text-slate-300 pl-4 list-disc">
                      {adv.points.map((p, pIdx) => (
                        <li key={pIdx} className="leading-relaxed">
                          {p}
                        </li>
                      ))}
                    </ul>
                  </Card>
                ))}
              </div>
            </>
          )}

          {/* Action Row */}
          <div className="flex flex-wrap items-center justify-between gap-3 pt-6 border-t border-slate-200">
            <Button variant="outline" size="md" onClick={() => navigate('/')}>
              Return to Dashboard
            </Button>
            <Button
              variant="primary"
              size="md"
              leftIcon={<RotateCcw className="w-4 h-4" />}
              onClick={restartChecker}
            >
              Start New Analysis
            </Button>
          </div>
        </motion.div>
      )}

      {/* "WHY THIS RESULT?" MODAL */}
      <Modal
        isOpen={Boolean(whyModalMatch)}
        onClose={() => setWhyModalMatch(null)}
        title="Why Did This Condition Match?"
        description="Transparent breakdown of the pattern compatibility formula"
        maxWidth="md"
      >
        {whyModalMatch && (
          <div className="space-y-4 text-xs text-slate-700 whitespace-pre-line leading-relaxed font-mono bg-slate-50 p-4 rounded-2xl border border-slate-200">
            {generateWhyExplanation(whyModalMatch)}
          </div>
        )}
        <div className="mt-4 flex justify-end">
          <Button variant="secondary" size="sm" onClick={() => setWhyModalMatch(null)}>
            Close
          </Button>
        </div>
      </Modal>

      {/* FULL REPORT MODAL */}
      <Modal
        isOpen={isReportOpen}
        onClose={() => setIsReportOpen(false)}
        title="Symptom Analysis Report"
        description="Formatted simulation summary report"
        maxWidth="lg"
      >
        {analysisResult && (
          <div className="space-y-4">
            <textarea
              readOnly
              rows={18}
              value={generateReport(analysisResult, user)}
              className="w-full font-mono text-xs p-4 bg-slate-950 text-slate-200 rounded-2xl border border-slate-800 leading-relaxed focus:outline-none resize-none"
            />
            <div className="flex justify-between items-center text-xs text-slate-500">
              <span>Simulation date: {new Date(analysisResult.analysisDate).toLocaleDateString()}</span>
              <Button
                variant="primary"
                size="sm"
                onClick={() => {
                  navigator.clipboard.writeText(generateReport(analysisResult, user));
                  alert('Report copied to clipboard!');
                }}
              >
                Copy to Clipboard
              </Button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};
