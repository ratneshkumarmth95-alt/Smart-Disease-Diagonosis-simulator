package com.smartdiagnosis.service;

import com.smartdiagnosis.model.*;
import java.util.*;

/**
 * GamificationService - Manages badges, streaks, and wellness scoring.
 * Demonstrates: Business logic separation, Collections, Conditionals
 */
public class GamificationService {

    // Badge constants
    public static final String BADGE_FIRST_CHECKIN      = "\uD83C\uDFC6 First Check-in";
    public static final String BADGE_HYDRATION_TRACKER  = "\uD83D\uDCA7 Hydration Tracker";
    public static final String BADGE_SEVEN_DAY_TRACKER  = "\uD83D\uDCCA 7-Day Tracker";
    public static final String BADGE_HEALTHY_HABITS     = "\uD83C\uDF31 Healthy Habits";
    public static final String BADGE_CONSISTENT_TRACKER = "\uD83D\uDCC5 Consistent Tracker";
    public static final String BADGE_SYMPTOM_REPORTER   = "\u2695\uFE0F Symptom Reporter";
    public static final String BADGE_KNOWLEDGE_SEEKER   = "\uD83D\uDCDA Knowledge Seeker";
    public static final String BADGE_WELLNESS_CHAMPION  = "\uD83C\uDF1F Wellness Champion";
    public static final String BADGE_MONTH_TRACKER      = "\uD83D\uDD25 30-Day Streak";

    /**
     * Check and award badges to the user based on their activity.
     * Returns a list of newly earned badge names.
     */
    public List<String> checkAndAwardBadges(User user, List<HealthCheck> recentChecks,
                                             boolean didSymptomCheck, boolean didJournalEntry) {
        List<String> newBadges = new ArrayList<>();
        if (user == null) return newBadges;

        // First check-in badge
        if (user.getTotalCheckIns() >= 1 && !user.getBadges().contains(BADGE_FIRST_CHECKIN)) {
            user.addBadge(BADGE_FIRST_CHECKIN);
            newBadges.add(BADGE_FIRST_CHECKIN);
        }

        // 7-day streak badge
        if (user.getStreakDays() >= 7 && !user.getBadges().contains(BADGE_SEVEN_DAY_TRACKER)) {
            user.addBadge(BADGE_SEVEN_DAY_TRACKER);
            newBadges.add(BADGE_SEVEN_DAY_TRACKER);
        }

        // 30-day streak badge
        if (user.getStreakDays() >= 30 && !user.getBadges().contains(BADGE_MONTH_TRACKER)) {
            user.addBadge(BADGE_MONTH_TRACKER);
            newBadges.add(BADGE_MONTH_TRACKER);
        }

        // Symptom reporter badge
        if (didSymptomCheck && !user.getBadges().contains(BADGE_SYMPTOM_REPORTER)) {
            user.addBadge(BADGE_SYMPTOM_REPORTER);
            newBadges.add(BADGE_SYMPTOM_REPORTER);
        }

        // Journal entry badge
        if (didJournalEntry && !user.getBadges().contains(BADGE_KNOWLEDGE_SEEKER)) {
            user.addBadge(BADGE_KNOWLEDGE_SEEKER);
            newBadges.add(BADGE_KNOWLEDGE_SEEKER);
        }

        // Wellness champion: average wellness >= 75 over 7 days
        if (recentChecks != null && recentChecks.size() >= 7) {
            double avgWellness = recentChecks.stream()
                .mapToInt(HealthCheck::getWellnessScore).average().orElse(0);
            if (avgWellness >= 75 && !user.getBadges().contains(BADGE_WELLNESS_CHAMPION)) {
                user.addBadge(BADGE_WELLNESS_CHAMPION);
                newBadges.add(BADGE_WELLNESS_CHAMPION);
            }
        }

        // Consistent tracker: 5 or more total check-ins
        if (user.getTotalCheckIns() >= 5 && !user.getBadges().contains(BADGE_CONSISTENT_TRACKER)) {
            user.addBadge(BADGE_CONSISTENT_TRACKER);
            newBadges.add(BADGE_CONSISTENT_TRACKER);
        }

        // Healthy habits: 10 check-ins
        if (user.getTotalCheckIns() >= 10 && !user.getBadges().contains(BADGE_HEALTHY_HABITS)) {
            user.addBadge(BADGE_HEALTHY_HABITS);
            newBadges.add(BADGE_HEALTHY_HABITS);
        }

        return newBadges;
    }

    /**
     * Update the user's check-in streak.
     */
    public void updateStreak(User user, java.time.LocalDate today) {
        if (user == null || today == null) return;

        java.time.LocalDate lastCheckIn = user.getLastCheckIn();
        if (lastCheckIn == null) {
            user.setStreakDays(1);
        } else if (lastCheckIn.equals(today.minusDays(1))) {
            // Consecutive day — increment streak
            user.setStreakDays(user.getStreakDays() + 1);
        } else if (!lastCheckIn.equals(today)) {
            // Missed a day — reset streak
            user.setStreakDays(1);
        }
        // If lastCheckIn == today, don't change streak (already checked in today)

        user.setLastCheckIn(today);
        user.setTotalCheckIns(user.getTotalCheckIns() + 1);
    }

    /**
     * Generate weekly summary statistics.
     */
    public Map<String, Object> generateWeeklySummary(List<HealthCheck> weekChecks,
                                                       List<SymptomLog> weekSymptoms) {
        Map<String, Object> summary = new LinkedHashMap<>();

        summary.put("checkIns", weekChecks.size());
        summary.put("maxStreak", weekChecks.size()); // simplified

        if (!weekChecks.isEmpty()) {
            double avgWellness = weekChecks.stream()
                .mapToInt(HealthCheck::getWellnessScore).average().orElse(0);
            double avgEnergy = weekChecks.stream()
                .mapToInt(HealthCheck::getEnergyLevel).average().orElse(0);
            double avgSleep = weekChecks.stream()
                .mapToInt(HealthCheck::getSleepQuality).average().orElse(0);
            summary.put("avgWellness", (int) avgWellness);
            summary.put("avgEnergy",   Math.round(avgEnergy * 10.0) / 10.0);
            summary.put("avgSleep",    Math.round(avgSleep * 10.0) / 10.0);
        }

        // Count unique symptoms tracked
        Set<String> uniqueSymptoms = new HashSet<>();
        for (SymptomLog log : weekSymptoms) uniqueSymptoms.add(log.getSymptomName());
        summary.put("symptomsTracked", uniqueSymptoms.size());

        return summary;
    }

    /**
     * Generate a progress bar string (e.g. "████████░░" for 80%).
     */
    public static String progressBar(int value, int max, int barWidth) {
        int filled = (int)((value / (double)max) * barWidth);
        filled = Math.max(0, Math.min(barWidth, filled));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < barWidth; i++) sb.append(i < filled ? "\u2588" : "\u2591");
        return sb.toString();
    }

    public static String weeklyConsistencyBar(int checkIns, int totalDays) {
        int pct = (int)((checkIns / (double)totalDays) * 100);
        return progressBar(pct, 100, 10) + " " + pct + "%";
    }
}
