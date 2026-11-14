package com.smartdiagnosis.service;

import com.smartdiagnosis.model.*;
import java.util.*;

/**
 * AdviceEngine - Generates safe, educational health advice.
 *
 * RULES:
 * - Does NOT recommend prescription medications
 * - Does NOT suggest specific drug dosages
 * - Always recommends consulting a healthcare professional
 * - Emergency advice directs user to emergency services immediately
 *
 * Demonstrates: Encapsulation, Inner Class, Enum, Polymorphism
 */
public class AdviceEngine {

    public enum AdviceCategory {
        SELF_CARE,
        MONITORING,
        MEDICAL_CONSULTATION,
        EMERGENCY
    }

    /**
     * Inner class representing a piece of advice with a category and bullet points.
     */
    public static class HealthAdvice {
        private final AdviceCategory category;
        private final String title;
        private final List<String> points;

        public HealthAdvice(AdviceCategory category, String title, List<String> points) {
            this.category = category;
            this.title = title;
            this.points = points != null ? points : new ArrayList<>();
        }

        public AdviceCategory getCategory() { return category; }
        public String getTitle()            { return title; }
        public List<String> getPoints()     { return points; }
    }

    /**
     * Generate full advice list based on the diagnosis result.
     */
    public List<HealthAdvice> generateAdvice(DiagnosisResult result) {
        List<HealthAdvice> advice = new ArrayList<>();

        // ---- EMERGENCY: Return only emergency advice ----
        if (result.isEmergencyWarning()) {
            advice.add(new HealthAdvice(AdviceCategory.EMERGENCY,
                "\u26a0\ufe0f Seek Immediate Medical Help",
                Arrays.asList(
                    "Call your local emergency number (e.g. 108, 112, 911) immediately.",
                    "Do not drive yourself to the hospital if feeling very unwell.",
                    "If possible, have someone stay with you while help arrives.",
                    "Do not use this simulator as a substitute for emergency medical care.",
                    "Stay calm, sit or lie down in a comfortable position."
                )));
            return advice;
        }

        // ---- GENERAL SELF-CARE (always applies) ----
        advice.add(new HealthAdvice(AdviceCategory.SELF_CARE,
            "\uD83C\uDF3F General Self-Care",
            Arrays.asList(
                "Rest adequately \u2014 your body heals faster with proper rest.",
                "Stay well hydrated with water, clear broths, or electrolyte drinks.",
                "Eat balanced, nutritious meals even if your appetite is reduced.",
                "Avoid alcohol and smoking as they can slow recovery.",
                "Wash hands regularly to prevent spreading illness to others."
            )));

        // ---- CONDITION-SPECIFIC SELF-CARE (from top match) ----
        if (!result.getTopMatches().isEmpty()) {
            ConditionMatch topMatch = result.getTopMatches().get(0);
            Disease disease = topMatch.getDisease();

            if (disease.getSelfCareAdvice() != null && !disease.getSelfCareAdvice().isEmpty()) {
                advice.add(new HealthAdvice(AdviceCategory.SELF_CARE,
                    "\uD83D\uDC8A Specific Self-Care Tips",
                    disease.getSelfCareAdvice()));
            }

            // Warning signs from top condition
            if (disease.getWarningSigns() != null && !disease.getWarningSigns().isEmpty()) {
                advice.add(new HealthAdvice(AdviceCategory.MONITORING,
                    "\u26a0\ufe0f Warning Signs to Watch For",
                    disease.getWarningSigns()));
            }
        }

        // ---- MONITORING ADVICE (symptom-specific) ----
        List<String> monitoringPoints = generateMonitoringPoints(result);
        if (!monitoringPoints.isEmpty()) {
            advice.add(new HealthAdvice(AdviceCategory.MONITORING,
                "\uD83D\uDCCA What to Track & Monitor",
                monitoringPoints));
        }

        // ---- DURATION-BASED ADVICE ----
        List<String> durationAdvice = generateDurationAdvice(result.getDuration());
        if (!durationAdvice.isEmpty()) {
            advice.add(new HealthAdvice(AdviceCategory.MEDICAL_CONSULTATION,
                "\uD83D\uDCC5 Duration Consideration",
                durationAdvice));
        }

        // ---- MEDICAL CONSULTATION ----
        List<String> consultPoints = new ArrayList<>();
        if (!result.getTopMatches().isEmpty()) {
            Disease disease = result.getTopMatches().get(0).getDisease();
            if (disease.getMedicalAttentionAdvice() != null) {
                consultPoints.add(disease.getMedicalAttentionAdvice());
            }
        }
        consultPoints.add("Consult a healthcare professional if symptoms persist more than 7-10 days without improvement.");
        consultPoints.add("Seek help if symptoms are worsening rather than improving.");
        consultPoints.add("If you belong to a high-risk group (elderly, pregnant, immunocompromised), consult a doctor sooner.");
        consultPoints.add("Always consult a qualified healthcare professional for proper diagnosis and treatment.");

        advice.add(new HealthAdvice(AdviceCategory.MEDICAL_CONSULTATION,
            "\uD83C\uDFE5 When to Seek Medical Help",
            consultPoints));

        // ---- DISCLAIMER (always last) ----
        advice.add(new HealthAdvice(AdviceCategory.MEDICAL_CONSULTATION,
            "\u2139\ufe0f Important Disclaimer",
            Arrays.asList(
                "This analysis is an EDUCATIONAL SIMULATION ONLY.",
                "It is NOT a medical diagnosis.",
                "Symptom Compatibility Scores are based on pattern matching, not medical testing.",
                "Please consult a qualified healthcare professional for medical evaluation.",
                "This application should NEVER replace professional medical advice."
            )));

        return advice;
    }

    private List<String> generateMonitoringPoints(DiagnosisResult result) {
        List<String> points = new ArrayList<>();
        for (Symptom s : result.getSelectedSymptoms()) {
            switch (s.getId()) {
                case 1: case 77:
                    points.add("Monitor your temperature twice daily. Keep a record."); break;
                case 23: case 24: case 25:
                    points.add("Track your cough: is it dry or productive? Getting better or worse?"); break;
                case 26:
                    points.add("Monitor breathing difficulty \u2014 seek help immediately if it worsens."); break;
                case 3:
                    points.add("Rate your energy level daily on a scale of 1-5."); break;
                case 11: case 12:
                    points.add("Note headache: intensity (1-5), location, and duration."); break;
                case 39:
                    points.add("Count diarrhea episodes and watch for signs of dehydration."); break;
                case 37: case 38:
                    points.add("Track nausea/vomiting: when it occurs, how long it lasts."); break;
                default: break;
            }
        }
        points.add("Note any new symptoms that develop during your illness.");
        points.add("Track whether existing symptoms are improving, stable, or worsening.");
        return points;
    }

    private List<String> generateDurationAdvice(String duration) {
        List<String> points = new ArrayList<>();
        if (duration == null) return points;
        switch (duration) {
            case "More than 2 weeks":
                points.add("Symptoms lasting more than 2 weeks generally warrant a medical evaluation.");
                points.add("A healthcare professional can help identify and treat persistent conditions.");
                break;
            case "1-2 weeks":
                points.add("Symptoms lasting 1-2 weeks that are not improving should be evaluated by a doctor.");
                break;
            case "4-7 days":
                points.add("If symptoms are not improving after 4-7 days, consider seeing a doctor.");
                break;
            default:
                break;
        }
        return points;
    }

    /**
     * Generate a full text report suitable for display or saving.
     */
    public String generateReport(DiagnosisResult result, User user) {
        StringBuilder sb = new StringBuilder();
        String line = "=".repeat(60);

        sb.append(line).append("\n");
        sb.append("     SMART DISEASE DIAGNOSIS SIMULATOR\n");
        sb.append("         SYMPTOM ANALYSIS REPORT\n");
        sb.append(line).append("\n\n");

        sb.append("PATIENT INFORMATION\n");
        sb.append("-".repeat(30)).append("\n");
        sb.append("Name   : ").append(user != null ? user.getName() : "Guest").append("\n");
        sb.append("Age    : ").append(user != null ? user.getAge() : "N/A").append("\n");
        sb.append("Gender : ").append(user != null ? (user.getGender() != null ? user.getGender() : "N/A") : "N/A").append("\n");
        sb.append("Date   : ").append(result.getAnalysisDate().toLocalDate()).append("\n");
        sb.append("Time   : ").append(result.getAnalysisDate().toLocalTime().withNano(0)).append("\n\n");

        sb.append("SYMPTOMS REPORTED\n");
        sb.append("-".repeat(30)).append("\n");
        if (result.getSelectedSymptoms().isEmpty()) {
            sb.append("  No symptoms recorded.\n");
        } else {
            for (Symptom s : result.getSelectedSymptoms()) {
                sb.append("  \u2022 ").append(s.getName())
                  .append(" [").append(s.getCategory()).append("]\n");
            }
        }
        if (result.getDuration() != null) {
            sb.append("Duration: ").append(result.getDuration()).append("\n");
        }

        sb.append("\n");

        if (result.isEmergencyWarning()) {
            sb.append("\u26a0\ufe0f  EMERGENCY WARNING\n");
            sb.append("-".repeat(30)).append("\n");
            sb.append(result.getEmergencyMessage()).append("\n\n");
        } else {
            sb.append("TOP COMPATIBLE CONDITIONS\n");
            sb.append("-".repeat(30)).append("\n");
            sb.append("NOTE: These are Symptom Compatibility Scores, NOT probabilities.\n\n");

            int rank = 1;
            for (ConditionMatch match : result.getTopMatches()) {
                sb.append(rank++).append(". ").append(match.getDisease().getName()).append("\n");
                sb.append("   Compatibility : ").append(match.getCompatibilityLabel())
                  .append(" (").append(match.getScorePercent()).append("%)\n");
                sb.append("   Category      : ").append(match.getDisease().getCategory()).append("\n");
                sb.append("   Matched       : ");
                List<String> matched = new ArrayList<>();
                for (Symptom s : match.getMatchedSymptoms()) matched.add(s.getName());
                sb.append(String.join(", ", matched)).append("\n\n");
            }

            if (result.getTopMatches().isEmpty()) {
                sb.append("  No strong symptom pattern matches found.\n");
                sb.append("  Please consult a healthcare professional.\n");
            }

            sb.append("GENERAL SELF-CARE REMINDERS\n");
            sb.append("-".repeat(30)).append("\n");
            sb.append("  \u2022 Rest adequately and stay hydrated.\n");
            sb.append("  \u2022 Monitor your symptoms daily.\n");
            sb.append("  \u2022 Seek medical help if symptoms worsen or persist.\n");
        }

        sb.append("\n").append(line).append("\n");
        sb.append("IMPORTANT DISCLAIMER\n");
        sb.append(line).append("\n");
        sb.append("This report is an EDUCATIONAL SYMPTOM-ANALYSIS SIMULATION\n");
        sb.append("and is NOT a medical diagnosis. The results are based on\n");
        sb.append("pattern matching and are not medically validated.\n\n");
        sb.append("Please consult a QUALIFIED HEALTHCARE PROFESSIONAL for\n");
        sb.append("proper medical evaluation and treatment.\n");
        sb.append(line).append("\n");
        sb.append("Generated by: Smart Disease Diagnosis Simulator\n");
        sb.append("Version: 1.0 | Educational Use Only\n");
        sb.append(line).append("\n");

        return sb.toString();
    }
}
