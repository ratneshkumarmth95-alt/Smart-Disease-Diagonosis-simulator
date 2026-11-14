import React, { useState } from 'react';
import { useData } from '../context/DataContext';
import { useAuth } from '../context/AuthContext';
import { DiagnosisResult } from '../types';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Modal } from '../components/ui/Modal';
import { 
  FileText, 
  Download, 
  Eye, 
  Calendar, 
  AlertTriangle, 
  ShieldCheck, 
  Search,
  Clock
} from 'lucide-react';
import { generateReport } from '../services/adviceEngine';

export const HealthHistory: React.FC = () => {
  const { diagnosisHistory } = useData();
  const { user } = useAuth();
  const [selectedResult, setSelectedResult] = useState<DiagnosisResult | null>(null);
  const [searchFilter, setSearchFilter] = useState('');

  const filteredHistory = diagnosisHistory.filter(h => {
    const topName = h.topMatches[0]?.disease.name.toLowerCase() || '';
    const symptomsStr = h.selectedSymptoms.map(s => s.name.toLowerCase()).join(' ');
    const q = searchFilter.toLowerCase();
    return topName.includes(q) || symptomsStr.includes(q);
  });

  const handleDownload = (result: DiagnosisResult) => {
    const reportText = generateReport(result, user);

    const blob = new Blob([reportText], { type: 'text/plain;charset=utf-8' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `Health_Analysis_${new Date(result.analysisDate).toISOString().slice(0, 10)}.txt`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

  const getRiskBadge = (risk: string) => {
    switch (risk) {
      case 'HIGH':
        return <Badge variant="danger">HIGH RISK</Badge>;
      case 'MODERATE':
        return <Badge variant="warning">MODERATE</Badge>;
      default:
        return <Badge variant="success">LOW RISK</Badge>;
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl md:text-3xl font-bold text-white tracking-tight flex items-center gap-3">
            <FileText className="h-8 w-8 text-cyan-400" />
            Diagnosis Analysis History
          </h1>
          <p className="text-slate-400 text-sm mt-1">
            Review previous symptom evaluations, clinical compatibility breakdowns, and educational reports.
          </p>
        </div>

        {diagnosisHistory.length > 0 && (
          <div className="relative w-full md:w-72">
            <Search className="absolute left-3 top-3 h-4 w-4 text-slate-400" />
            <input
              type="text"
              placeholder="Search past conditions or symptoms..."
              value={searchFilter}
              onChange={(e) => setSearchFilter(e.target.value)}
              className="w-full bg-slate-900 border border-slate-800 rounded-xl pl-9 pr-4 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
            />
          </div>
        )}
      </div>

      {diagnosisHistory.length === 0 ? (
        <Card className="p-12 text-center">
          <FileText className="h-14 w-14 text-slate-600 mx-auto mb-4" />
          <h3 className="text-lg font-bold text-white">No Analysis Sessions Recorded</h3>
          <p className="text-sm text-slate-400 max-w-md mx-auto mt-2">
            You haven&apos;t run any symptom simulations yet. Use the Symptom Checker to perform your first evaluation.
          </p>
          <div className="mt-6">
            <a href="/checker">
              <Button variant="primary">Launch Symptom Checker</Button>
            </a>
          </div>
        </Card>
      ) : filteredHistory.length === 0 ? (
        <Card className="p-8 text-center text-slate-400 text-sm">
          No records match your search criteria &quot;{searchFilter}&quot;.
        </Card>
      ) : (
        <div className="space-y-4">
          {filteredHistory.slice().reverse().map((item, idx) => {
            const topMatch = item.topMatches[0];
            return (
              <Card key={item.id ?? idx} className="p-5 hover:border-slate-700 transition-all">
                <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-6">
                  {/* Left Column: Condition Info */}
                  <div className="space-y-2 flex-1">
                    <div className="flex flex-wrap items-center gap-2">
                      <span className="text-lg font-bold text-white">
                        {topMatch ? topMatch.disease.name : 'Clinical Evaluation'}
                      </span>
                      {topMatch && getRiskBadge(topMatch.disease.severityLevel)}
                      {item.emergencyWarning && (
                        <span className="flex items-center gap-1 text-xs text-rose-400 font-semibold px-2 py-0.5 rounded bg-rose-500/10 border border-rose-500/30">
                          <AlertTriangle className="h-3 w-3" /> Critical Override Triggered
                        </span>
                      )}
                    </div>

                    <div className="flex flex-wrap items-center gap-4 text-xs text-slate-400">
                      <span className="flex items-center gap-1.5">
                        <Calendar className="h-3.5 w-3.5 text-slate-500" />
                        {new Date(item.analysisDate).toLocaleString()}
                      </span>
                      <span className="flex items-center gap-1.5">
                        <Clock className="h-3.5 w-3.5 text-slate-500" />
                        Reported Duration: {item.duration || 'Not specified'}
                      </span>
                      {topMatch && (
                        <span className="text-cyan-400 font-semibold">
                          Match Score: {topMatch.scorePercent}%
                        </span>
                      )}
                    </div>

                    {/* Symptoms Tagged */}
                    <div className="pt-2">
                      <div className="text-[11px] font-medium text-slate-400 mb-1.5">
                        Input Symptoms ({item.selectedSymptoms.length}):
                      </div>
                      <div className="flex flex-wrap gap-1.5">
                        {item.selectedSymptoms.map((symp) => (
                          <span
                            key={symp.id}
                            className="text-xs px-2 py-0.5 rounded-md bg-slate-900 border border-slate-800 text-slate-300"
                          >
                            {symp.name}
                          </span>
                        ))}
                      </div>
                    </div>
                  </div>

                  {/* Right Column: Actions */}
                  <div className="flex items-center gap-2 self-end lg:self-center">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setSelectedResult(item)}
                      className="text-xs"
                    >
                      <Eye className="h-3.5 w-3.5 mr-1.5 text-cyan-400" />
                      View Details
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDownload(item)}
                      className="text-xs text-slate-300 hover:text-white"
                      title="Download Analysis Report (.txt)"
                    >
                      <Download className="h-3.5 w-3.5" />
                    </Button>
                  </div>
                </div>
              </Card>
            );
          })}
        </div>
      )}

      {/* Detail Modal */}
      {selectedResult && (
        <Modal
          isOpen={!!selectedResult}
          onClose={() => setSelectedResult(null)}
          title={`Clinical Simulation Details — ${new Date(selectedResult.analysisDate).toLocaleDateString()}`}
          maxWidth="2xl"
        >
          <div className="space-y-6 text-sm">
            {/* Top Matches Breakdown */}
            <div>
              <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">
                Condition Compatibility Ranking
              </h4>
              <div className="space-y-3">
                {selectedResult.topMatches.map((m, idx) => (
                  <div key={m.disease.id} className="p-3.5 bg-slate-900/70 border border-slate-800 rounded-xl space-y-2">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-mono text-cyan-400 font-bold">#{idx + 1}</span>
                        <span className="font-bold text-white">{m.disease.name}</span>
                        {getRiskBadge(m.disease.severityLevel)}
                      </div>
                      <span className="text-cyan-400 font-bold">{m.scorePercent}% Compatibility</span>
                    </div>

                    <p className="text-xs text-slate-300">{m.disease.description}</p>

                    <div className="text-[11px] text-slate-400 bg-slate-950/60 p-2.5 rounded-lg border border-slate-800/80">
                      <div className="font-medium text-slate-300 mb-1">Matched Symptoms ({m.matchedSymptoms.length}):</div>
                      {m.matchedSymptoms.map(s => s.name).join(', ') || 'None'}
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Self-Care & Warning Signs */}
            {selectedResult.topMatches[0] && (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="p-4 bg-emerald-950/20 border border-emerald-800/30 rounded-xl space-y-2">
                  <div className="flex items-center gap-2 text-emerald-400 font-semibold text-xs uppercase tracking-wide">
                    <ShieldCheck className="h-4 w-4" /> Recommended Self-Care Measures
                  </div>
                  <ul className="text-xs text-slate-300 space-y-1.5 list-disc pl-4">
                    {(selectedResult.topMatches[0].disease.selfCareAdvice || []).map((a, i) => (
                      <li key={i}>{a}</li>
                    ))}
                  </ul>
                </div>

                <div className="p-4 bg-rose-950/20 border border-rose-800/30 rounded-xl space-y-2">
                  <div className="flex items-center gap-2 text-rose-400 font-semibold text-xs uppercase tracking-wide">
                    <AlertTriangle className="h-4 w-4" /> Immediate Red-Flag Warning Signs
                  </div>
                  <ul className="text-xs text-slate-300 space-y-1.5 list-disc pl-4">
                    {(selectedResult.topMatches[0].disease.warningSigns || []).map((w, i) => (
                      <li key={i}>{w}</li>
                    ))}
                  </ul>
                </div>
              </div>
            )}

            <div className="pt-4 border-t border-slate-800 flex justify-between items-center">
              <span className="text-[11px] text-slate-500 italic">
                Educational simulation record. Not a medical chart.
              </span>
              <Button
                variant="outline"
                size="sm"
                onClick={() => handleDownload(selectedResult)}
              >
                <Download className="h-3.5 w-3.5 mr-1.5" />
                Download Report (.txt)
              </Button>
            </div>
          </div>
        </Modal>
      )}
    </div>
  );
};
