import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useData } from '../context/DataContext';
import { getDailyTip, getTodaysFact } from '../data/knowledgeBase';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import {
  Stethoscope,
  Activity,
  Flame,
  Sparkles,
  ArrowRight,
  TrendingUp,
  BookOpen,
  HelpCircle,
  Clock,
  HeartPulse,
  AlertCircle,
} from 'lucide-react';
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';
import { motion } from 'motion/react';

export const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const {
    todayCheckIn,
    weeklySummary,
    symptomLogs,
    diagnosisHistory,
    healthChecks,
  } = useData();
  const navigate = useNavigate();

  const tip = getDailyTip();
  const fact = getTodaysFact();

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 18) return 'Good afternoon';
    return 'Good evening';
  };

  // Prepare 7-day chart data
  const chartData = [...healthChecks]
    .slice(0, 7)
    .reverse()
    .map((c) => {
      const parts = c.checkDate.split('-');
      const label = `${parts[1]}/${parts[2]}`;
      return {
        date: label,
        score: c.wellnessScore,
        energy: c.energyLevel * 20,
        sleep: c.sleepQuality * 20,
      };
    });

  const recentAnalyses = diagnosisHistory.slice(0, 3);

  return (
    <div className="space-y-6 pb-12">
      {/* 1. HERO BANNER */}
      <motion.div
        initial={{ opacity: 0, y: 16 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
        className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-cyan-950/40 to-slate-900 text-white p-6 sm:p-8 shadow-2xl border border-cyan-500/30"
      >
        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="max-w-xl space-y-3">
            <div className="flex items-center gap-2">
              <Badge variant="purple" size="sm" className="bg-purple-950/80 text-purple-300 border-purple-500/50">
                <Sparkles className="w-3 h-3 text-purple-300" />
                Personalized Health Assistant
              </Badge>
              {user?.streakDays ? (
                <Badge variant="warning" size="sm" className="bg-amber-950/80 text-amber-300 border-amber-500/50">
                  <Flame className="w-3 h-3 text-amber-400 fill-amber-400" />
                  {user.streakDays}-Day Streak
                </Badge>
              ) : null}
            </div>

            <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-white">
              {getGreeting()}, {user?.name ? user.name.split(' ')[0] : 'Ratnesh'} 👋
            </h1>
            <p className="text-sm text-slate-300 leading-relaxed font-normal">
              How are you feeling today? Check your symptoms against our knowledge base of 80+ clinical indicators or log your daily wellness.
            </p>
          </div>

          <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-3 shrink-0">
            <Button
              variant="primary"
              size="lg"
              leftIcon={<Stethoscope className="w-5 h-5 text-white" />}
              rightIcon={<ArrowRight className="w-4 h-4 text-white/80" />}
              onClick={() => navigate('/checker')}
              className="bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 text-white shadow-lg shadow-cyan-500/30 text-sm font-bold"
            >
              Start Symptom Check
            </Button>
          </div>
        </div>

        {/* Ambient background glow */}
        <div className="absolute right-0 top-0 -mt-12 -mr-12 w-96 h-96 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none" />
      </motion.div>

      {/* 2. STATS ROW */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Today's Score */}
        <Card className="p-4 sm:p-5 flex flex-col justify-between" hoverEffect>
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">
              Today's Score
            </span>
            <div className="w-8 h-8 rounded-xl bg-cyan-500/10 text-cyan-400 flex items-center justify-center">
              <HeartPulse className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-3">
            <div className="text-2xl sm:text-3xl font-extrabold text-white">
              {todayCheckIn ? (
                <>
                  {todayCheckIn.wellnessScore}
                  <span className="text-xs font-semibold text-slate-400 ml-1">/ 100</span>
                </>
              ) : (
                <span className="text-base text-slate-400 font-semibold">Not checked in</span>
              )}
            </div>
            <p className="text-xs text-slate-400 mt-1">
              {todayCheckIn ? 'Daily habit tracking score' : 'Tap Daily Check-in to record'}
            </p>
          </div>
        </Card>

        {/* 7-Day Average */}
        <Card className="p-4 sm:p-5 flex flex-col justify-between" hoverEffect>
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">
              7-Day Avg
            </span>
            <div className="w-8 h-8 rounded-xl bg-emerald-500/10 text-emerald-400 flex items-center justify-center">
              <TrendingUp className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-3">
            <div className="text-2xl sm:text-3xl font-extrabold text-white">
              {weeklySummary.avgWellness > 0 ? (
                <>
                  {weeklySummary.avgWellness}
                  <span className="text-xs font-semibold text-slate-400 ml-1">/ 100</span>
                </>
              ) : (
                <span className="text-base text-slate-400 font-semibold">No records</span>
              )}
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Over {weeklySummary.checkInsCount} recent check-in{weeklySummary.checkInsCount === 1 ? '' : 's'}
            </p>
          </div>
        </Card>

        {/* Symptoms Tracked */}
        <Card className="p-4 sm:p-5 flex flex-col justify-between" hoverEffect>
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">
              Active Logs
            </span>
            <div className="w-8 h-8 rounded-xl bg-amber-500/10 text-amber-400 flex items-center justify-center">
              <Activity className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-3">
            <div className="text-2xl sm:text-3xl font-extrabold text-white">
              {symptomLogs.length}
            </div>
            <p className="text-xs text-slate-400 mt-1">
              {symptomLogs.length > 0 ? 'Recorded entries in tracker' : 'No active symptoms'}
            </p>
          </div>
        </Card>

        {/* Badges Earned */}
        <Card className="p-4 sm:p-5 flex flex-col justify-between" hoverEffect>
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">
              Badges
            </span>
            <div className="w-8 h-8 rounded-xl bg-purple-500/10 text-purple-400 flex items-center justify-center">
              <Sparkles className="w-4 h-4" />
            </div>
          </div>
          <div className="mt-3">
            <div className="text-2xl sm:text-3xl font-extrabold text-white">
              {user?.badges?.length || 0}
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Milestones & consistency rewards
            </p>
          </div>
        </Card>
      </div>

      {/* 3. CHART & QUICK ACTIONS */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Weekly Trend Chart */}
        <Card className="p-5 lg:col-span-2">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <TrendingUp className="w-4 h-4 text-cyan-400" />
                Wellness Trends
              </h3>
              <p className="text-xs text-slate-400 mt-0.5">
                Educational wellness trajectory across your recent check-ins
              </p>
            </div>
            <Link to="/tracker" className="text-xs font-semibold text-cyan-400 hover:text-cyan-300">
              View Tracker →
            </Link>
          </div>

          {chartData.length > 1 ? (
            <div className="h-64 w-full pt-2">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={chartData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                  <defs>
                    <linearGradient id="scoreGrad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#06b6d4" stopOpacity={0.4} />
                      <stop offset="95%" stopColor="#06b6d4" stopOpacity={0.0} />
                    </linearGradient>
                  </defs>
                  <XAxis dataKey="date" tickLine={false} stroke="#64748b" fontSize={11} />
                  <YAxis domain={[0, 100]} tickLine={false} stroke="#64748b" fontSize={11} />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: '#0f172a',
                      borderRadius: '12px',
                      border: '1px solid #334155',
                      color: '#fff',
                      fontSize: '12px',
                    }}
                  />
                  <Area
                    type="monotone"
                    dataKey="score"
                    name="Wellness Score"
                    stroke="#06b6d4"
                    strokeWidth={2.5}
                    fillOpacity={1}
                    fill="url(#scoreGrad)"
                  />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          ) : (
            <div className="h-64 flex flex-col items-center justify-center text-center p-6 bg-slate-950/60 rounded-2xl border border-dashed border-slate-800">
              <div className="w-12 h-12 rounded-full bg-cyan-500/10 text-cyan-400 flex items-center justify-center mb-3">
                <HeartPulse className="w-6 h-6" />
              </div>
              <p className="text-sm font-semibold text-slate-300">Need more daily records</p>
              <p className="text-xs text-slate-500 mt-1 max-w-xs">
                Complete your daily check-in for at least 2 days to visualize your score progression here.
              </p>
            </div>
          )}
        </Card>

        {/* Quick Actions Grid */}
        <div className="space-y-3 flex flex-col justify-between">
          <Card
            className="p-4 cursor-pointer hover:border-cyan-500/50 group"
            hoverEffect
            onClick={() => navigate('/checker')}
          >
            <div className="flex items-start gap-3">
              <div className="w-10 h-10 rounded-xl bg-cyan-500/10 text-cyan-400 flex items-center justify-center shrink-0 group-hover:bg-cyan-500 group-hover:text-white transition-colors">
                <Stethoscope className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-sm font-bold text-white group-hover:text-cyan-300 transition-colors">
                  Check Symptoms
                </h4>
                <p className="text-xs text-slate-400 mt-0.5">
                  Select signs and run weighted compatibility analysis.
                </p>
              </div>
            </div>
          </Card>

          <Card
            className="p-4 cursor-pointer hover:border-emerald-500/50 group"
            hoverEffect
            onClick={() => navigate('/tracker')}
          >
            <div className="flex items-start gap-3">
              <div className="w-10 h-10 rounded-xl bg-emerald-500/10 text-emerald-400 flex items-center justify-center shrink-0 group-hover:bg-emerald-500 group-hover:text-white transition-colors">
                <Activity className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-sm font-bold text-white group-hover:text-emerald-300 transition-colors">
                  Symptom Tracker
                </h4>
                <p className="text-xs text-slate-400 mt-0.5">
                  Log symptoms over days with severity bars.
                </p>
              </div>
            </div>
          </Card>

          <Card
            className="p-4 cursor-pointer hover:border-amber-500/50 group"
            hoverEffect
            onClick={() => navigate('/journal')}
          >
            <div className="flex items-start gap-3">
              <div className="w-10 h-10 rounded-xl bg-amber-500/10 text-amber-400 flex items-center justify-center shrink-0 group-hover:bg-amber-500 group-hover:text-white transition-colors">
                <BookOpen className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-sm font-bold text-white group-hover:text-amber-300 transition-colors">
                  Health Journal
                </h4>
                <p className="text-xs text-slate-400 mt-0.5">
                  Document thoughts, sleep habits, and recovery notes.
                </p>
              </div>
            </div>
          </Card>

          <Card
            className="p-4 cursor-pointer hover:border-purple-500/50 group"
            hoverEffect
            onClick={() => navigate('/quiz')}
          >
            <div className="flex items-start gap-3">
              <div className="w-10 h-10 rounded-xl bg-purple-500/10 text-purple-400 flex items-center justify-center shrink-0 group-hover:bg-purple-500 group-hover:text-white transition-colors">
                <HelpCircle className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-sm font-bold text-white group-hover:text-purple-300 transition-colors">
                  Health Knowledge Quiz
                </h4>
                <p className="text-xs text-slate-400 mt-0.5">
                  Test your understanding of sleep, hydration, and first aid.
                </p>
              </div>
            </div>
          </Card>
        </div>
      </div>

      {/* 4. TIP, FACT & RECENT ACTIVITY */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {/* Daily Health Tip */}
        <Card className="p-5 bg-gradient-to-br from-cyan-950/40 to-slate-900 border-cyan-500/30 flex flex-col justify-between">
          <div>
            <div className="flex items-center justify-between mb-3">
              <Badge variant="info" size="sm">
                {tip.category}
              </Badge>
              <span className="text-xl">{tip.icon}</span>
            </div>
            <h4 className="text-sm font-bold text-white mb-1.5">{tip.title}</h4>
            <p className="text-xs text-slate-300 leading-relaxed font-normal">{tip.content}</p>
          </div>
          <Link
            to="/tips"
            className="text-xs font-semibold text-cyan-400 hover:text-cyan-300 pt-4 flex items-center gap-1"
          >
            Browse all 20 Health Tips →
          </Link>
        </Card>

        {/* Did You Know Health Fact */}
        <Card className="p-5 bg-gradient-to-br from-emerald-950/30 to-slate-900 border-emerald-500/30 flex flex-col justify-between">
          <div>
            <div className="flex items-center justify-between mb-3">
              <Badge variant="success" size="sm">
                Daily Physiology Fact
              </Badge>
              <span className="text-xl">💡</span>
            </div>
            <h4 className="text-sm font-bold text-white mb-1.5">Did You Know?</h4>
            <p className="text-xs text-slate-300 leading-relaxed font-normal">{fact}</p>
          </div>
          <Link
            to="/conditions"
            className="text-xs font-semibold text-emerald-400 hover:text-emerald-300 pt-4 flex items-center gap-1"
          >
            Explore Condition Knowledge Base →
          </Link>
        </Card>

        {/* Recent Analysis Sessions */}
        <Card className="p-5 md:col-span-2 lg:col-span-1">
          <div className="flex items-center justify-between mb-3">
            <h4 className="text-sm font-bold text-white flex items-center gap-2">
              <Clock className="w-4 h-4 text-slate-400" />
              Recent Analyses
            </h4>
            <Link to="/history" className="text-xs font-semibold text-cyan-400 hover:text-cyan-300">
              History →
            </Link>
          </div>

          {recentAnalyses.length > 0 ? (
            <div className="space-y-3">
              {recentAnalyses.map((rec, i) => (
                <div
                  key={i}
                  className="p-3 rounded-xl bg-slate-950/70 border border-slate-800 flex items-center justify-between"
                >
                  <div className="min-w-0 pr-2">
                    <div className="text-xs font-bold text-white truncate">
                      {rec.emergencyWarning
                        ? '⚠️ Emergency Alert'
                        : rec.topMatches[0]?.disease.name || 'General Symptom Check'}
                    </div>
                    <div className="text-[11px] text-slate-400 mt-0.5">
                      {new Date(rec.analysisDate).toLocaleDateString()} •{' '}
                      {rec.selectedSymptoms.length} signs
                    </div>
                  </div>
                  {rec.emergencyWarning ? (
                    <Badge variant="danger" size="sm">
                      Urgent
                    </Badge>
                  ) : (
                    <Badge variant="info" size="sm">
                      {rec.topMatches[0]?.scorePercent || 0}% Match
                    </Badge>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8">
              <AlertCircle className="w-7 h-7 text-slate-500 mx-auto mb-2" />
              <p className="text-xs font-semibold text-slate-300">No previous analyses</p>
              <p className="text-[11px] text-slate-500 mt-0.5">
                Run your first symptom simulation to view match history.
              </p>
            </div>
          )}
        </Card>
      </div>
    </div>
  );
};
