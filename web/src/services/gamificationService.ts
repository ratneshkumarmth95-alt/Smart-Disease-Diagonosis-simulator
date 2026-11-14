import { UserProfile, HealthCheck, SymptomLog, BADGES } from '../types';

export function checkAndAwardBadges(
  user: UserProfile,
  recentChecks: HealthCheck[] = [],
  didSymptomCheck = false,
  didJournalEntry = false
): string[] {
  const newBadges: string[] = [];
  const currentBadges = new Set(user.badges || []);

  const grant = (badge: string) => {
    if (!currentBadges.has(badge)) {
      currentBadges.add(badge);
      newBadges.push(badge);
    }
  };

  // 1. First Check-in
  if (user.totalCheckIns >= 1) grant(BADGES.FIRST_CHECKIN);

  // 2. 7-Day Tracker
  if (user.streakDays >= 7) grant(BADGES.SEVEN_DAY_TRACKER);

  // 3. 30-Day Streak
  if (user.streakDays >= 30) grant(BADGES.MONTH_TRACKER);

  // 4. Symptom Reporter
  if (didSymptomCheck) grant(BADGES.SYMPTOM_REPORTER);

  // 5. Knowledge Seeker (Journal)
  if (didJournalEntry) grant(BADGES.KNOWLEDGE_SEEKER);

  // 6. Consistent Tracker
  if (user.totalCheckIns >= 5) grant(BADGES.CONSISTENT_TRACKER);

  // 7. Healthy Habits
  if (user.totalCheckIns >= 10) grant(BADGES.HEALTHY_HABITS);

  // 8. Wellness Champion (7-day avg >= 75)
  if (recentChecks.length >= 7) {
    const avg =
      recentChecks.reduce((sum, c) => sum + c.wellnessScore, 0) /
      recentChecks.length;
    if (avg >= 75) grant(BADGES.WELLNESS_CHAMPION);
  }

  user.badges = Array.from(currentBadges);
  return newBadges;
}

export function updateStreak(user: UserProfile, todayStr: string): void {
  const last = user.lastCheckIn;

  if (!last) {
    user.streakDays = 1;
  } else if (last === todayStr) {
    // Already checked in today, keep streak
  } else {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    const yesterdayStr = yesterday.toISOString().split('T')[0];

    if (last === yesterdayStr) {
      user.streakDays += 1;
    } else {
      user.streakDays = 1;
    }
  }

  user.lastCheckIn = todayStr;
  user.totalCheckIns += 1;
}

export function calculateWellnessScore(
  energy: number,
  sleep: number,
  stress: number,
  mood: number
): number {
  // Stress inversely affects wellness (5 stress -> 1 point; 1 stress -> 5 points)
  const stressContrib = 6 - stress;
  const raw = (energy + sleep + stressContrib + mood) / 20.0;
  return Math.round(raw * 100);
}

export function getWellnessLabel(score: number): string {
  if (score >= 85) return 'Excellent';
  if (score >= 70) return 'Good';
  if (score >= 55) return 'Fair';
  if (score >= 40) return 'Below Average';
  return 'Needs Attention';
}

export function generateProgressBar(value: number, max: number, barWidth = 10): string {
  const filled = Math.max(0, Math.min(barWidth, Math.round((value / max) * barWidth)));
  return '█'.repeat(filled) + '░'.repeat(barWidth - filled);
}

export function generateWeeklySummary(
  checks: HealthCheck[],
  logs: SymptomLog[]
) {
  const avgWellness =
    checks.length > 0
      ? Math.round(checks.reduce((sum, c) => sum + c.wellnessScore, 0) / checks.length)
      : 0;

  const avgEnergy =
    checks.length > 0
      ? Number((checks.reduce((sum, c) => sum + c.energyLevel, 0) / checks.length).toFixed(1))
      : 0;

  const avgSleep =
    checks.length > 0
      ? Number((checks.reduce((sum, c) => sum + c.sleepQuality, 0) / checks.length).toFixed(1))
      : 0;

  const uniqueSymptoms = new Set(logs.map((l) => l.symptomName)).size;

  return {
    checkInsCount: checks.length,
    avgWellness,
    avgEnergy,
    avgSleep,
    symptomsTrackedCount: uniqueSymptoms,
  };
}
