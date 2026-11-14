package com.smartdiagnosis.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DiagnosisResult - Holds the complete output of a symptom analysis session.
 * Demonstrates: Composition, Collections usage
 */
public class DiagnosisResult {
    private int id;
    private int userId;
    private LocalDateTime analysisDate;
    private List<Symptom> selectedSymptoms;
    private List<ConditionMatch> topMatches;
    private boolean emergencyWarning;
    private String emergencyMessage;
    private String overallSeverity;
    private String duration;

    public DiagnosisResult() {
        this.selectedSymptoms = new ArrayList<>();
        this.topMatches = new ArrayList<>();
        this.analysisDate = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getAnalysisDate() { return analysisDate; }
    public void setAnalysisDate(LocalDateTime analysisDate) { this.analysisDate = analysisDate; }

    public List<Symptom> getSelectedSymptoms() { return selectedSymptoms; }
    public void setSelectedSymptoms(List<Symptom> selectedSymptoms) { this.selectedSymptoms = selectedSymptoms; }

    public List<ConditionMatch> getTopMatches() { return topMatches; }
    public void setTopMatches(List<ConditionMatch> topMatches) { this.topMatches = topMatches; }

    public boolean isEmergencyWarning() { return emergencyWarning; }
    public void setEmergencyWarning(boolean emergencyWarning) { this.emergencyWarning = emergencyWarning; }

    public String getEmergencyMessage() { return emergencyMessage; }
    public void setEmergencyMessage(String emergencyMessage) { this.emergencyMessage = emergencyMessage; }

    public String getOverallSeverity() { return overallSeverity; }
    public void setOverallSeverity(String overallSeverity) { this.overallSeverity = overallSeverity; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getTopMatchName() {
        if (topMatches != null && !topMatches.isEmpty()) {
            return topMatches.get(0).getDisease().getName();
        }
        return "No match found";
    }

    public double getTopMatchScore() {
        if (topMatches != null && !topMatches.isEmpty()) {
            return topMatches.get(0).getCompatibilityScore();
        }
        return 0.0;
    }

    public String getSymptomsAsString() {
        if (selectedSymptoms == null || selectedSymptoms.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedSymptoms.size(); i++) {
            sb.append(selectedSymptoms.get(i).getName());
            if (i < selectedSymptoms.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
