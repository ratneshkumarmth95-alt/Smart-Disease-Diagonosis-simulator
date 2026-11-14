package com.smartdiagnosis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ConditionMatch - Represents how well a disease matches the user's symptoms.
 * Demonstrates: Composition, Encapsulation
 */
public class ConditionMatch {
    private Disease disease;
    private double compatibilityScore; // 0.0 to 1.0
    private String compatibilityLabel; // HIGH, MODERATE, LOW
    private List<Symptom> matchedSymptoms;
    private List<Symptom> missingKeySymptoms;
    private int matchedCount;
    private int totalDiseaseSymptoms;

    public ConditionMatch() {
        this.matchedSymptoms = new ArrayList<>();
        this.missingKeySymptoms = new ArrayList<>();
    }

    public ConditionMatch(Disease disease, double score, List<Symptom> matched) {
        this();
        this.disease = disease;
        this.compatibilityScore = score;
        this.matchedSymptoms = matched != null ? matched : new ArrayList<>();
        this.matchedCount = this.matchedSymptoms.size();
        updateLabel();
    }

    private void updateLabel() {
        if (compatibilityScore >= 0.70) this.compatibilityLabel = "HIGH";
        else if (compatibilityScore >= 0.40) this.compatibilityLabel = "MODERATE";
        else this.compatibilityLabel = "LOW";
    }

    public Disease getDisease() { return disease; }
    public void setDisease(Disease disease) { this.disease = disease; }

    public double getCompatibilityScore() { return compatibilityScore; }
    public void setCompatibilityScore(double compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
        updateLabel();
    }

    public String getCompatibilityLabel() { return compatibilityLabel; }
    public void setCompatibilityLabel(String compatibilityLabel) { this.compatibilityLabel = compatibilityLabel; }

    public List<Symptom> getMatchedSymptoms() { return matchedSymptoms; }
    public void setMatchedSymptoms(List<Symptom> matchedSymptoms) {
        this.matchedSymptoms = matchedSymptoms != null ? matchedSymptoms : new ArrayList<>();
    }

    public List<Symptom> getMissingKeySymptoms() { return missingKeySymptoms; }
    public void setMissingKeySymptoms(List<Symptom> missingKeySymptoms) {
        this.missingKeySymptoms = missingKeySymptoms != null ? missingKeySymptoms : new ArrayList<>();
    }

    public int getMatchedCount() { return matchedCount; }
    public void setMatchedCount(int matchedCount) { this.matchedCount = matchedCount; }

    public int getTotalDiseaseSymptoms() { return totalDiseaseSymptoms; }
    public void setTotalDiseaseSymptoms(int totalDiseaseSymptoms) { this.totalDiseaseSymptoms = totalDiseaseSymptoms; }

    public int getScorePercent() { return (int)(compatibilityScore * 100); }
}
