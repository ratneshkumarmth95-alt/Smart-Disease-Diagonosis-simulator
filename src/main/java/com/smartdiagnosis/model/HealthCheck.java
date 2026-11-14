package com.smartdiagnosis.model;

import java.time.LocalDate;

/**
 * HealthCheck - Daily wellness check-in data.
 * Demonstrates: Constructor chaining, encapsulation
 */
public class HealthCheck {
    private int id;
    private int userId;
    private LocalDate checkDate;
    private int energyLevel;   // 1-5
    private int sleepQuality;  // 1-5
    private int stressLevel;   // 1-5
    private int moodLevel;     // 1-5
    private String notes;
    private int wellnessScore; // calculated 0-100

    public HealthCheck() {}

    public HealthCheck(int userId, LocalDate checkDate, int energy, int sleep, int stress, int mood) {
        this.userId = userId;
        this.checkDate = checkDate;
        this.energyLevel = energy;
        this.sleepQuality = sleep;
        this.stressLevel = stress;
        this.moodLevel = mood;
        this.wellnessScore = calculateWellnessScore(energy, sleep, stress, mood);
    }

    private int calculateWellnessScore(int energy, int sleep, int stress, int mood) {
        // Stress inversely affects wellness (5=high stress = bad)
        int stressContrib = (6 - stress); // invert: stress 5 -> 1 contrib
        double raw = (energy + sleep + stressContrib + mood) / 20.0;
        return (int)(raw * 100);
    }

    public void recalculateWellnessScore() {
        this.wellnessScore = calculateWellnessScore(energyLevel, sleepQuality, stressLevel, moodLevel);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDate getCheckDate() { return checkDate; }
    public void setCheckDate(LocalDate checkDate) { this.checkDate = checkDate; }

    public int getEnergyLevel() { return energyLevel; }
    public void setEnergyLevel(int energyLevel) { this.energyLevel = energyLevel; }

    public int getSleepQuality() { return sleepQuality; }
    public void setSleepQuality(int sleepQuality) { this.sleepQuality = sleepQuality; }

    public int getStressLevel() { return stressLevel; }
    public void setStressLevel(int stressLevel) { this.stressLevel = stressLevel; }

    public int getMoodLevel() { return moodLevel; }
    public void setMoodLevel(int moodLevel) { this.moodLevel = moodLevel; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public int getWellnessScore() { return wellnessScore; }
    public void setWellnessScore(int wellnessScore) { this.wellnessScore = wellnessScore; }

    public String getWellnessLabel() {
        if (wellnessScore >= 85) return "Excellent";
        if (wellnessScore >= 70) return "Good";
        if (wellnessScore >= 55) return "Fair";
        if (wellnessScore >= 40) return "Below Average";
        return "Needs Attention";
    }
}
