import React, { useState } from 'react';
import { Modal } from './Modal';
import { Button } from './Button';
import { useData } from '../../context/DataContext';
import { Zap, Moon, Brain, Smile, Sparkles, CheckCircle2 } from 'lucide-react';
import confetti from 'canvas-confetti';

interface DailyCheckInModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const DailyCheckInModal: React.FC<DailyCheckInModalProps> = ({ isOpen, onClose }) => {
  const { addCheckIn } = useData();

  const [energy, setEnergy] = useState<number>(3);
  const [sleep, setSleep] = useState<number>(3);
  const [stress, setStress] = useState<number>(3);
  const [mood, setMood] = useState<number>(3);
  const [notes, setNotes] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [savedScore, setSavedScore] = useState<number | null>(null);

  // Live calculation of projected score
  const sleepContrib = (sleep / 5.0) * 30.0;
  const energyContrib = (energy / 5.0) * 30.0;
  const stressContrib = ((6 - stress) / 5.0) * 20.0;
  const moodContrib = (mood / 5.0) * 20.0;
  const currentScore = Math.round(sleepContrib + energyContrib + stressContrib + moodContrib);

  let wellnessLabel = 'Moderate';
  if (currentScore >= 80) wellnessLabel = 'Optimal';
  else if (currentScore >= 60) wellnessLabel = 'Good';
  else if (currentScore < 40) wellnessLabel = 'Low';

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const todayStr = new Date().toISOString().split('T')[0];
      const result = await addCheckIn({
        checkDate: todayStr,
        energyLevel: energy,
        sleepQuality: sleep,
        stressLevel: stress,
        moodLevel: mood,
        notes: notes.trim() || undefined,
        wellnessScore: currentScore,
      });

      if (result.success) {
        setSavedScore(currentScore);
        confetti({
          particleCount: 70,
          spread: 60,
          origin: { y: 0.6 },
        });
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDone = () => {
    setSavedScore(null);
    onClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title="Daily Health Check-in"
      description="Track your daily energy, sleep, and wellness habits"
      maxWidth="lg"
    >
      {savedScore !== null ? (
        <div className="py-6 text-center space-y-4">
          <div className="w-16 h-16 bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 rounded-full flex items-center justify-center mx-auto">
            <CheckCircle2 className="w-9 h-9" />
          </div>
          <h4 className="text-2xl font-bold text-white">Check-in Complete!</h4>
          <div className="bg-slate-950 border border-slate-800 rounded-2xl p-6 max-w-sm mx-auto">
            <div className="text-xs font-semibold text-slate-400 uppercase tracking-wider">
              Today's Wellness Score
            </div>
            <div className="text-4xl font-extrabold text-cyan-400 mt-2">
              {savedScore} <span className="text-lg text-slate-400 font-medium">/ 100</span>
            </div>
            <div className="text-sm font-semibold text-emerald-300 bg-emerald-950/60 px-3 py-1 rounded-full inline-block mt-3 border border-emerald-500/40">
              {wellnessLabel}
            </div>
          </div>
          <p className="text-xs text-slate-400 max-w-xs mx-auto">
            Keep up your daily streak! This score reflects habit consistency, not a clinical assessment.
          </p>
          <div className="pt-2">
            <Button variant="primary" onClick={handleDone} className="w-full">
              Back to Dashboard
            </Button>
          </div>
        </div>
      ) : (
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Sliders */}
          <div className="space-y-5">
            {/* Energy */}
            <div className="bg-slate-950/70 p-4 rounded-2xl border border-slate-800">
              <div className="flex items-center justify-between mb-2">
                <span className="flex items-center gap-2 text-sm font-semibold text-white">
                  <Zap className="w-4 h-4 text-amber-400" />
                  Energy Level
                </span>
                <span className="text-sm font-bold text-cyan-400 bg-cyan-950/60 px-2.5 py-0.5 rounded-lg border border-cyan-800/60">
                  {energy} / 5
                </span>
              </div>
              <input
                type="range"
                min="1"
                max="5"
                value={energy}
                onChange={(e) => setEnergy(Number(e.target.value))}
                className="w-full accent-cyan-400 cursor-pointer h-2 bg-slate-800 rounded-lg"
              />
              <div className="flex justify-between text-[11px] text-slate-400 mt-1.5 font-medium">
                <span>1 - Exhausted</span>
                <span>3 - Moderate</span>
                <span>5 - Vibrant</span>
              </div>
            </div>

            {/* Sleep */}
            <div className="bg-slate-950/70 p-4 rounded-2xl border border-slate-800">
              <div className="flex items-center justify-between mb-2">
                <span className="flex items-center gap-2 text-sm font-semibold text-white">
                  <Moon className="w-4 h-4 text-indigo-400" />
                  Sleep Quality
                </span>
                <span className="text-sm font-bold text-cyan-400 bg-cyan-950/60 px-2.5 py-0.5 rounded-lg border border-cyan-800/60">
                  {sleep} / 5
                </span>
              </div>
              <input
                type="range"
                min="1"
                max="5"
                value={sleep}
                onChange={(e) => setSleep(Number(e.target.value))}
                className="w-full accent-cyan-400 cursor-pointer h-2 bg-slate-800 rounded-lg"
              />
              <div className="flex justify-between text-[11px] text-slate-400 mt-1.5 font-medium">
                <span>1 - Insomnia/Poor</span>
                <span>3 - Average</span>
                <span>5 - Restful (8h)</span>
              </div>
            </div>

            {/* Stress */}
            <div className="bg-slate-950/70 p-4 rounded-2xl border border-slate-800">
              <div className="flex items-center justify-between mb-2">
                <span className="flex items-center gap-2 text-sm font-semibold text-white">
                  <Brain className="w-4 h-4 text-rose-400" />
                  Stress Level
                </span>
                <span className="text-sm font-bold text-rose-400 bg-rose-950/60 px-2.5 py-0.5 rounded-lg border border-rose-800/60">
                  {stress} / 5
                </span>
              </div>
              <input
                type="range"
                min="1"
                max="5"
                value={stress}
                onChange={(e) => setStress(Number(e.target.value))}
                className="w-full accent-rose-400 cursor-pointer h-2 bg-slate-800 rounded-lg"
              />
              <div className="flex justify-between text-[11px] text-slate-400 mt-1.5 font-medium">
                <span>1 - Very Calm</span>
                <span>3 - Moderate</span>
                <span>5 - Overwhelmed</span>
              </div>
            </div>

            {/* Mood */}
            <div className="bg-slate-950/70 p-4 rounded-2xl border border-slate-800">
              <div className="flex items-center justify-between mb-2">
                <span className="flex items-center gap-2 text-sm font-semibold text-white">
                  <Smile className="w-4 h-4 text-emerald-400" />
                  Overall Mood
                </span>
                <span className="text-sm font-bold text-emerald-400 bg-emerald-950/60 px-2.5 py-0.5 rounded-lg border border-emerald-800/60">
                  {mood} / 5
                </span>
              </div>
              <input
                type="range"
                min="1"
                max="5"
                value={mood}
                onChange={(e) => setMood(Number(e.target.value))}
                className="w-full accent-cyan-400 cursor-pointer h-2 bg-slate-800 rounded-lg"
              />
              <div className="flex justify-between text-[11px] text-slate-400 mt-1.5 font-medium">
                <span>1 - Down / Anxious</span>
                <span>3 - Neutral</span>
                <span>5 - Optimistic</span>
              </div>
            </div>

            {/* Notes */}
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                Daily Reflection (Optional)
              </label>
              <textarea
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                rows={2}
                placeholder="Any notable diet, exercise, or study habits today..."
                className="w-full text-sm rounded-xl border border-slate-700 p-3 focus:outline-none focus:ring-2 focus:ring-cyan-500 focus:border-transparent resize-none bg-slate-950 text-white placeholder-slate-500"
              />
            </div>
          </div>

          {/* Live Preview Bar */}
          <div className="flex items-center justify-between bg-cyan-950/30 p-3.5 rounded-xl border border-cyan-800/40">
            <div className="flex items-center gap-2">
              <Sparkles className="w-4 h-4 text-cyan-400" />
              <span className="text-xs font-medium text-slate-200">
                Projected Wellness: <strong className="text-cyan-300 font-bold">{currentScore} / 100</strong> ({wellnessLabel})
              </span>
            </div>
          </div>

          {/* Actions */}
          <div className="flex gap-3 justify-end pt-2">
            <Button variant="ghost" onClick={onClose} type="button" className="text-slate-400 hover:text-white">
              Cancel
            </Button>
            <Button variant="primary" type="submit" isLoading={isSubmitting}>
              Save Check-in
            </Button>
          </div>
        </form>
      )}
    </Modal>
  );
};
