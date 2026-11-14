import React, { useState } from 'react';
import { getRandomQuestions } from '../services/quizService';
import { QuizQuestion } from '../types';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { 
  HelpCircle, 
  CheckCircle2, 
  XCircle, 
  RotateCcw, 
  Award, 
  ArrowRight,
  Lightbulb
} from 'lucide-react';
import confetti from 'canvas-confetti';

export const HealthQuiz: React.FC = () => {
  const [questions, setQuestions] = useState<QuizQuestion[]>(() => getRandomQuestions(5));
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedOption, setSelectedOption] = useState<number | null>(null);
  const [isAnswered, setIsAnswered] = useState(false);
  const [score, setScore] = useState(0);
  const [isFinished, setIsFinished] = useState(false);

  const currentQ = questions[currentIndex];

  const handleSelectOption = (idx: number) => {
    if (isAnswered) return;
    setSelectedOption(idx);
    setIsAnswered(true);

    if (idx === currentQ.correctAnswerIndex) {
      setScore(prev => prev + 1);
    }
  };

  const handleNext = () => {
    if (currentIndex + 1 < questions.length) {
      setCurrentIndex(prev => prev + 1);
      setSelectedOption(null);
      setIsAnswered(false);
    } else {
      setIsFinished(true);
      if (score >= 3) {
        confetti({
          particleCount: 80,
          spread: 70,
          origin: { y: 0.6 }
        });
      }
    }
  };

  const handleRestart = () => {
    setQuestions(getRandomQuestions(5));
    setCurrentIndex(0);
    setSelectedOption(null);
    setIsAnswered(false);
    setScore(0);
    setIsFinished(false);
  };

  const getRank = (finalScore: number, total: number) => {
    const pct = (finalScore / total) * 100;
    if (pct === 100) return { title: 'Health Grandmaster 🏆', desc: 'Flawless medical literacy and preventative knowledge!' };
    if (pct >= 80) return { title: 'Wellness Scholar 🎓', desc: 'Impressive health and physiology comprehension.' };
    if (pct >= 60) return { title: 'Health Enthusiast 🌟', desc: 'Solid everyday health awareness.' };
    return { title: 'Curious Learner 📖', desc: 'Great effort! Health knowledge grows with practice.' };
  };

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl md:text-3xl font-bold text-white tracking-tight flex items-center gap-3">
          <HelpCircle className="h-8 w-8 text-cyan-400" />
          Health & Medical Trivia Quiz
        </h1>
        <p className="text-slate-400 text-sm mt-1">
          Challenge your clinical knowledge, physiology facts, and healthy lifestyle guidelines.
        </p>
      </div>

      {!isFinished && currentQ ? (
        <Card className="p-6 md:p-8 space-y-6">
          {/* Progress Header */}
          <div className="flex items-center justify-between border-b border-slate-800 pb-4">
            <div className="flex items-center gap-2">
              <Badge variant="info">Question #{currentQ.id}</Badge>
              <span className="text-xs text-slate-400">
                Question {currentIndex + 1} of {questions.length}
              </span>
            </div>
            <div className="text-xs font-semibold text-slate-300">
              Score: <span className="text-cyan-400">{score}</span>
            </div>
          </div>

          {/* Progress Bar */}
          <div className="w-full bg-slate-900 h-1.5 rounded-full overflow-hidden">
            <div 
              className="bg-gradient-to-r from-cyan-500 to-blue-500 h-full transition-all duration-300"
              style={{ width: `${((currentIndex + 1) / questions.length) * 100}%` }}
            />
          </div>

          {/* Question Text */}
          <div className="pt-2">
            <h2 className="text-lg md:text-xl font-bold text-white leading-snug">
              {currentQ.question}
            </h2>
          </div>

          {/* Options */}
          <div className="space-y-3">
            {currentQ.options.map((option, idx) => {
              let optionStyle = 'bg-slate-900 border-slate-800 text-slate-300 hover:border-slate-700';

              if (isAnswered) {
                if (idx === currentQ.correctAnswerIndex) {
                  optionStyle = 'bg-emerald-500/20 border-emerald-500 text-emerald-200 font-semibold';
                } else if (idx === selectedOption) {
                  optionStyle = 'bg-rose-500/20 border-rose-500 text-rose-200';
                } else {
                  optionStyle = 'bg-slate-900/40 border-slate-800/40 text-slate-500';
                }
              }

              return (
                <button
                  key={idx}
                  onClick={() => handleSelectOption(idx)}
                  disabled={isAnswered}
                  className={`w-full p-4 rounded-xl border text-left text-sm transition-all flex items-center justify-between ${optionStyle}`}
                >
                  <div className="flex items-center gap-3">
                    <span className="w-7 h-7 rounded-lg bg-slate-800/80 flex items-center justify-center text-xs font-bold font-mono">
                      {String.fromCharCode(65 + idx)}
                    </span>
                    <span>{option}</span>
                  </div>

                  {isAnswered && idx === currentQ.correctAnswerIndex && (
                    <CheckCircle2 className="h-5 w-5 text-emerald-400 flex-shrink-0" />
                  )}
                  {isAnswered && idx === selectedOption && idx !== currentQ.correctAnswerIndex && (
                    <XCircle className="h-5 w-5 text-rose-400 flex-shrink-0" />
                  )}
                </button>
              );
            })}
          </div>

          {/* Explanation Box */}
          {isAnswered && (
            <div className="p-4 rounded-xl bg-cyan-950/20 border border-cyan-800/40 space-y-1.5 animate-in fade-in">
              <div className="flex items-center gap-2 text-cyan-400 font-semibold text-xs">
                <Lightbulb className="h-4 w-4" /> Clinical Explanation:
              </div>
              <p className="text-xs text-slate-300 leading-relaxed">
                {currentQ.explanation}
              </p>
            </div>
          )}

          {/* Next Button */}
          {isAnswered && (
            <div className="flex justify-end pt-2">
              <Button onClick={handleNext} variant="primary" className="flex items-center gap-2">
                <span>{currentIndex + 1 === questions.length ? 'See Final Score' : 'Next Question'}</span>
                <ArrowRight className="h-4 w-4" />
              </Button>
            </div>
          )}
        </Card>
      ) : (
        /* Results View */
        <Card className="p-8 md:p-12 text-center space-y-6">
          <div className="w-20 h-20 rounded-3xl bg-gradient-to-tr from-cyan-500/20 to-blue-500/20 border border-cyan-500/30 flex items-center justify-center mx-auto">
            <Award className="h-10 w-10 text-cyan-400" />
          </div>

          <div className="space-y-2">
            <h2 className="text-2xl font-bold text-white">Quiz Completed!</h2>
            <p className="text-cyan-400 font-semibold text-lg">{getRank(score, questions.length).title}</p>
            <p className="text-xs text-slate-400 max-w-sm mx-auto">{getRank(score, questions.length).desc}</p>
          </div>

          <div className="p-6 bg-slate-900/60 border border-slate-800 rounded-2xl max-w-xs mx-auto">
            <div className="text-4xl font-extrabold text-white">
              {score} <span className="text-slate-500 text-2xl font-normal">/ {questions.length}</span>
            </div>
            <div className="text-xs text-slate-400 mt-1">
              Score: {Math.round((score / questions.length) * 100)}%
            </div>
          </div>

          <div className="pt-4 flex justify-center gap-3">
            <Button onClick={handleRestart} variant="primary" className="flex items-center gap-2">
              <RotateCcw className="h-4 w-4" />
              Try Another 5 Questions
            </Button>
          </div>
        </Card>
      )}
    </div>
  );
};
