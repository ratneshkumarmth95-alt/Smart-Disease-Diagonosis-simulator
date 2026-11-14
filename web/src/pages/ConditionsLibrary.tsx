import React, { useState } from 'react';
import { DISEASES, SYMPTOMS } from '../data/knowledgeBase';
import { Disease } from '../types';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Modal } from '../components/ui/Modal';
import { 
  Stethoscope, 
  Search, 
  AlertTriangle, 
  ShieldCheck, 
  Info, 
  Activity, 
  ChevronRight
} from 'lucide-react';

export const ConditionsLibrary: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');
  const [selectedDisease, setSelectedDisease] = useState<Disease | null>(null);

  const categories = ['ALL', ...Array.from(new Set(DISEASES.map(c => c.category)))];

  const filteredConditions = DISEASES.filter(c => {
    const matchesCategory = selectedCategory === 'ALL' || c.category === selectedCategory;
    const matchesSearch = c.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          c.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          c.category.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCategory && matchesSearch;
  });

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

  const getSymptomName = (id: number) => {
    const s = SYMPTOMS.find(item => item.id === id);
    return s ? s.name : `Symptom #${id}`;
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl md:text-3xl font-bold text-white tracking-tight flex items-center gap-3">
          <Stethoscope className="h-8 w-8 text-cyan-400" />
          Conditions Knowledge Base
        </h1>
        <p className="text-slate-400 text-sm mt-1">
          Explore comprehensive educational summaries for 25 medical conditions, clinical symptom weights, and self-care protocols.
        </p>
      </div>

      {/* Search and Filters */}
      <div className="flex flex-col md:flex-row gap-3 items-center justify-between bg-slate-900/60 p-3.5 rounded-xl border border-slate-800">
        <div className="relative w-full md:w-80">
          <Search className="absolute left-3 top-2.5 h-4 w-4 text-slate-400" />
          <input
            type="text"
            placeholder="Search conditions, descriptions..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg pl-9 pr-3 py-1.5 text-xs text-white focus:outline-none focus:border-cyan-500"
          />
        </div>

        <div className="flex items-center gap-1.5 overflow-x-auto w-full md:w-auto pb-1 md:pb-0">
          <span className="text-xs text-slate-400 mr-1 hidden lg:inline">Category:</span>
          {categories.map((cat) => (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-3 py-1 rounded-lg text-xs font-medium whitespace-nowrap transition-all ${
                selectedCategory === cat
                  ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/40'
                  : 'text-slate-400 hover:text-white hover:bg-slate-800'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>
      </div>

      {/* Condition Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {filteredConditions.map((condition) => (
          <Card
            key={condition.id}
            className="p-5 flex flex-col justify-between hover:border-cyan-500/40 transition-all group"
          >
            <div className="space-y-3">
              <div className="flex items-start justify-between gap-2">
                <div>
                  <Badge variant="info" className="mb-1.5">{condition.category}</Badge>
                  <h3 className="font-bold text-white text-base group-hover:text-cyan-300 transition-colors">
                    {condition.name}
                  </h3>
                </div>
                {getRiskBadge(condition.severityLevel)}
              </div>

              <p className="text-xs text-slate-300 line-clamp-3 leading-relaxed">
                {condition.description}
              </p>

              <div className="pt-2 border-t border-slate-800/80">
                <div className="text-[11px] text-slate-400 mb-1.5 flex items-center gap-1">
                  <Activity className="h-3 w-3 text-cyan-400" />
                  Primary Markers ({condition.symptomLinks.length} associated):
                </div>
                <div className="flex flex-wrap gap-1">
                  {condition.symptomLinks.slice(0, 4).map((sw) => (
                    <span
                      key={sw.symptomId}
                      className="text-[10px] px-2 py-0.5 rounded bg-slate-900 border border-slate-800 text-slate-300"
                    >
                      {getSymptomName(sw.symptomId)}
                    </span>
                  ))}
                  {condition.symptomLinks.length > 4 && (
                    <span className="text-[10px] px-1.5 py-0.5 text-slate-500">
                      +{condition.symptomLinks.length - 4} more
                    </span>
                  )}
                </div>
              </div>
            </div>

            <Button
              variant="outline"
              size="sm"
              onClick={() => setSelectedDisease(condition)}
              className="mt-5 w-full text-xs flex items-center justify-center gap-1 group-hover:border-cyan-500/60"
            >
              <span>Explore Clinical Details</span>
              <ChevronRight className="h-3.5 w-3.5 group-hover:translate-x-0.5 transition-transform" />
            </Button>
          </Card>
        ))}
      </div>

      {/* Condition Deep-Dive Modal */}
      {selectedDisease && (
        <Modal
          isOpen={!!selectedDisease}
          onClose={() => setSelectedDisease(null)}
          title={selectedDisease.name}
          maxWidth="2xl"
        >
          <div className="space-y-6 text-sm">
            {/* Header info */}
            <div className="flex flex-wrap items-center justify-between gap-3 p-4 bg-slate-900/80 border border-slate-800 rounded-xl">
              <div>
                <span className="text-xs text-slate-400">Category Classification</span>
                <div className="text-sm font-bold text-white">{selectedDisease.category}</div>
              </div>
              <div>
                <span className="text-xs text-slate-400">Clinical Risk Classification</span>
                <div>{getRiskBadge(selectedDisease.severityLevel)}</div>
              </div>
              <div>
                <span className="text-xs text-slate-400">Condition Code</span>
                <div className="text-xs font-mono text-cyan-400">#{selectedDisease.id}</div>
              </div>
            </div>

            {/* Description */}
            <div>
              <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-1.5">Overview & Pathology</h4>
              <p className="text-xs text-slate-300 leading-relaxed bg-slate-950/50 p-3.5 rounded-xl border border-slate-800/80">
                {selectedDisease.description}
              </p>
            </div>

            {/* Clinical Symptom Association Weights */}
            <div>
              <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-2">
                Recognized Diagnostic Symptom Correlations
              </h4>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                {selectedDisease.symptomLinks.map((sw) => (
                  <div
                    key={sw.symptomId}
                    className="p-2.5 bg-slate-900/60 border border-slate-800 rounded-lg flex items-center justify-between text-xs"
                  >
                    <span className="text-slate-200 font-medium">{getSymptomName(sw.symptomId)}</span>
                    <span className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                      sw.weight === 3 ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/30' :
                      sw.weight === 2 ? 'bg-blue-500/20 text-blue-300 border border-blue-500/30' :
                      'bg-slate-800 text-slate-400'
                    }`}>
                      {sw.weight === 3 ? 'Primary (3x)' : sw.weight === 2 ? 'Secondary (2x)' : 'Supportive (1x)'}
                    </span>
                  </div>
                ))}
              </div>
            </div>

            {/* Self-Care & Warning Signs */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="p-4 bg-emerald-950/20 border border-emerald-800/30 rounded-xl space-y-2">
                <div className="flex items-center gap-2 text-emerald-400 font-semibold text-xs uppercase tracking-wide">
                  <ShieldCheck className="h-4 w-4" /> Evidence-Based Self-Care
                </div>
                <ul className="text-xs text-slate-300 space-y-1.5 list-disc pl-4">
                  {(selectedDisease.selfCareAdvice || []).map((a, i) => (
                    <li key={i}>{a}</li>
                  ))}
                </ul>
              </div>

              <div className="p-4 bg-rose-950/20 border border-rose-800/30 rounded-xl space-y-2">
                <div className="flex items-center gap-2 text-rose-400 font-semibold text-xs uppercase tracking-wide">
                  <AlertTriangle className="h-4 w-4" /> Physician Escalation Triggers
                </div>
                <ul className="text-xs text-slate-300 space-y-1.5 list-disc pl-4">
                  {(selectedDisease.warningSigns || []).map((w, i) => (
                    <li key={i}>{w}</li>
                  ))}
                </ul>
              </div>
            </div>

            <div className="p-3 bg-amber-500/10 border border-amber-500/20 rounded-xl text-xs text-amber-300 flex items-start gap-2">
              <Info className="h-4 w-4 flex-shrink-0 mt-0.5" />
              <span>
                <strong>Educational Simulation Note:</strong> Disease descriptions are compiled for educational simulation and health literacy purposes only. Never initiate medication without consulting a registered physician.
              </span>
            </div>
          </div>
        </Modal>
      )}
    </div>
  );
};
