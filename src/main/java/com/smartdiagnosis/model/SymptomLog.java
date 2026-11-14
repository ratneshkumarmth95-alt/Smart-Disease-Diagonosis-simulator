package com.smartdiagnosis.model;

import java.time.LocalDate;

/**
 * SymptomLog - Tracks a user's symptom severity over time for the Symptom Tracker feature.
 * Demonstrates: Encapsulation, Date handling
 */
public class SymptomLog {
    private int id;
    private int userId;
    private int symptomId;
    private String symptomName;
    private LocalDate logDate;
    private int severity; // 1-5
    private String duration; // "Today", "1-3 days", "4-7 days", "1-2 weeks", "More than 2 weeks"
    private String notes;

    public SymptomLog() {}

    public SymptomLog(int userId, int symptomId, String symptomName,
                      LocalDate logDate, int severity, String duration) {
        this.userId = userId;
        this.symptomId = symptomId;
        this.symptomName = symptomName;
        this.logDate = logDate;
        this.severity = severity;
        this.duration = duration;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSymptomId() { return symptomId; }
    public void setSymptomId(int symptomId) { this.symptomId = symptomId; }

    public String getSymptomName() { return symptomName; }
    public void setSymptomName(String symptomName) { this.symptomName = symptomName; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public int getSeverity() { return severity; }
    public void setSeverity(int severity) { this.severity = severity; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getSeverityLabel() {
        switch (severity) {
            case 1: return "Very Mild";
            case 2: return "Mild";
            case 3: return "Moderate";
            case 4: return "Severe";
            case 5: return "Very Severe";
            default: return "Unknown";
        }
    }

    // Returns a visual bar representation like "███░░" for severity
    public String getSeverityBar() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            sb.append(i <= severity ? "█" : "░");
        }
        return sb.toString();
    }
}
