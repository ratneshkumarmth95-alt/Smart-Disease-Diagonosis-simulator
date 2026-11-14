import React, { useState } from 'react';
import { HEALTH_TIPS, HEALTH_FACTS } from '../data/knowledgeBase';
import { Card } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { 
  Lightbulb, 
  Search, 
  Sparkles, 
  Heart, 
  Zap, 
  BookOpen, 
  Share2, 
  Check 
} from 'lucide-react';

export const HealthTips: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');
  const [copiedId, setCopiedId] = useState<number | null>(null);

  const categories = ['ALL', ...Array.from(new Set(HEALTH_TIPS.map(t => t.category)))];

  const filteredTips = HEALTH_TIPS.filter(t => {
    const matchesCat = selectedCategory === 'ALL' || t.category === selectedCategory;
    const matchesSearch = t.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          t.content.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          t.category.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCat && matchesSearch;
  });

  const handleShare = (tipText: string, id: number) => {
    navigator.clipboard.writeText(tipText);
    setCopiedId(id);
    setTimeout(() => setCopiedId(null), 2000);
  };

  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="border-b border-slate-800/80 pb-4">
        <h1 className="text-2xl md:text-3xl font-extrabold text-white tracking-tight flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-cyan-500/20 border border-cyan-500/40 flex items-center justify-center flex-shrink-0">
            <Lightbulb className="h-6 w-6 text-cyan-400" />
          </div>
          Health Tips & Physiology Facts
        </h1>
        <p className="text-slate-300 text-sm mt-2 font-medium">
          Evidence-backed preventative wellness recommendations and fascinating physiological discoveries.
        </p>
      </div>

      {/* Hero Featured Tip */}
      <div className="p-6 md:p-8 rounded-2xl bg-gradient-to-r from-cyan-950/80 via-blue-950/60 to-slate-900 border border-cyan-500/40 relative overflow-hidden shadow-2xl">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 relative z-10">
          <div className="space-y-3 max-w-2xl">
            <div className="flex items-center gap-2">
              <span className="flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-cyan-500/20 text-cyan-300 border border-cyan-500/50">
                <Sparkles className="h-3.5 w-3.5" /> Featured Daily Insight
              </span>
              <Badge variant="info" className="font-bold">{HEALTH_TIPS[0].category}</Badge>
            </div>
            <h2 className="text-2xl md:text-3xl font-extrabold text-white tracking-tight">{HEALTH_TIPS[0].title}</h2>
            <p className="text-sm md:text-base text-slate-200 leading-relaxed font-normal">{HEALTH_TIPS[0].content}</p>
          </div>
          <div className="w-20 h-20 rounded-2xl bg-cyan-500/20 border border-cyan-400/40 flex items-center justify-center flex-shrink-0 text-4xl shadow-lg shadow-cyan-500/20">
            {HEALTH_TIPS[0].icon || <Heart className="h-10 w-10 text-cyan-400 animate-pulse" />}
          </div>
        </div>
      </div>

      {/* Anatomy Facts Carousel/Grid */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h3 className="text-xl font-bold text-white flex items-center gap-2">
            <Zap className="h-5 w-5 text-amber-400" />
            Human Anatomy & Physiology Facts
          </h3>
          <span className="text-xs font-semibold text-slate-400 bg-slate-900 px-2.5 py-1 rounded-lg border border-slate-800">
            {HEALTH_FACTS.length} facts
          </span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {HEALTH_FACTS.map((fact, idx) => (
            <div
              key={idx}
              className="p-5 bg-slate-900/90 border border-slate-800 rounded-xl hover:border-amber-500/40 transition-all space-y-3 shadow-lg"
            >
              <div className="flex items-center justify-between">
                <Badge variant="warning" className="font-bold text-xs flex items-center gap-1">
                  <Zap className="w-3 h-3 text-amber-400" /> Did You Know?
                </Badge>
                <span className="text-xs text-slate-500 font-mono font-bold">#{idx + 1}</span>
              </div>
              <p className="text-sm text-slate-200 leading-relaxed font-medium">{fact}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Filter and Search */}
      <div className="flex flex-col sm:flex-row gap-3 items-center justify-between bg-slate-900/90 p-4 rounded-xl border border-slate-800 shadow-lg">
        <div className="relative w-full sm:w-80">
          <Search className="absolute left-3.5 top-3 h-4 w-4 text-slate-400" />
          <input
            type="text"
            placeholder="Search healthy habits, sleep, nutrition..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-slate-950 border border-slate-700 rounded-lg pl-10 pr-3 py-2 text-sm text-white placeholder-slate-400 focus:outline-none focus:border-cyan-400 focus:ring-1 focus:ring-cyan-400"
          />
        </div>

        <div className="flex items-center gap-1.5 overflow-x-auto w-full sm:w-auto pb-1 sm:pb-0">
          <span className="text-xs text-slate-400 font-medium mr-1 hidden lg:inline">Category:</span>
          {categories.map((c) => (
            <button
              key={c}
              onClick={() => setSelectedCategory(c)}
              className={`px-3 py-1.5 rounded-lg text-xs font-bold whitespace-nowrap transition-all border ${
                selectedCategory === c
                  ? 'bg-cyan-500 text-white border-cyan-400 shadow-md shadow-cyan-500/25'
                  : 'text-slate-300 bg-slate-800/80 hover:text-white hover:bg-slate-700 border-slate-700'
              }`}
            >
              {c}
            </button>
          ))}
        </div>
      </div>

      {/* Tips Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {filteredTips.map((tip) => (
          <Card key={tip.id} className="p-5 flex flex-col justify-between hover:border-cyan-500/40 transition-all group shadow-lg">
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <span className="text-2xl">{tip.icon}</span>
                  <Badge variant="info" className="font-bold">{tip.category}</Badge>
                </div>
                <button
                  onClick={() => handleShare(`${tip.title}: ${tip.content}`, tip.id)}
                  className="text-slate-400 hover:text-cyan-400 transition-colors p-1.5 rounded-lg hover:bg-slate-800"
                  title="Copy tip text"
                >
                  {copiedId === tip.id ? (
                    <Check className="h-4 w-4 text-emerald-400" />
                  ) : (
                    <Share2 className="h-4 w-4" />
                  )}
                </button>
              </div>

              <h4 className="font-bold text-white text-base group-hover:text-cyan-300 transition-colors">
                {tip.title}
              </h4>

              <p className="text-sm text-slate-300 leading-relaxed font-normal">
                {tip.content}
              </p>
            </div>

            <div className="pt-3 mt-4 border-t border-slate-800 flex items-center justify-between text-xs text-slate-400 font-medium">
              <span className="flex items-center gap-1.5">
                <BookOpen className="h-3.5 w-3.5 text-cyan-400" />
                Preventative Care
              </span>
              <span className="text-cyan-400 font-mono font-bold">#{tip.id}</span>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};
