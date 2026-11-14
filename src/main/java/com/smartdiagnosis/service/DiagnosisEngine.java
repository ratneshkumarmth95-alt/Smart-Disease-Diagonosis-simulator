package com.smartdiagnosis.service;

import com.smartdiagnosis.model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DiagnosisEngine - Core weighted symptom matching algorithm.
 *
 * HOW IT WORKS:
 * 1. Checks for emergency symptoms first (overrides everything)
 * 2. For each disease, calculates a weighted compatibility score
 * 3. Weights: 1=common symptom, 2=important, 3=highly characteristic
 * 4. Adjusts score based on severity, duration, age
 * 5. Returns top 3 ranked matches
 *
 * IMPORTANT: Results are EDUCATIONAL SIMULATIONS only — NOT medical diagnoses.
 * Demonstrates: Algorithms, Collections, Streams, Encapsulation
 */
public class DiagnosisEngine {

    // Emergency symptom IDs — if any selected, skip ranking and show warning
    private static final Set<Integer> EMERGENCY_SYMPTOM_IDS = new HashSet<>(Arrays.asList(
        7,   // Fainting
        13,  // Confusion
        18,  // Sudden severe headache
        21,  // Neck stiffness
        22,  // Sudden one-sided weakness
        26,  // Shortness of breath (marked emergency)
        33,  // Severe chest pain
        34,  // Severe difficulty breathing
        45,  // Blood in stool
        60,  // Severe allergic reaction
        63,  // Blood in urine
        76,  // Severe dehydration
        77,  // High fever above 103F
        78,  // Persistent vomiting
        79,  // Productive cough with blood
        80   // Sudden vision loss
    ));

    private final KnowledgeBase knowledgeBase;

    public DiagnosisEngine() {
        this.knowledgeBase = KnowledgeBase.getInstance();
    }

    /**
     * Main analysis method.
     *
     * @param selectedSymptoms list of symptoms the user selected
     * @param severityMap      symptomId -> severity rating (1-5)
     * @param duration         symptom duration string
     * @param userAge          user's age (for age-group modifiers)
     * @return DiagnosisResult with ranked conditions or emergency warning
     */
    public DiagnosisResult analyze(List<Symptom> selectedSymptoms,
                                   Map<Integer, Integer> severityMap,
                                   String duration,
                                   int userAge) {

        DiagnosisResult result = new DiagnosisResult();
        result.setSelectedSymptoms(new ArrayList<>(selectedSymptoms));
        result.setDuration(duration);

        if (selectedSymptoms.isEmpty()) {
            result.setOverallSeverity("NONE");
            return result;
        }

        // STEP 1: Emergency check — always takes priority
        boolean hasEmergency = checkEmergencySymptoms(selectedSymptoms, severityMap);
        if (hasEmergency) {
            result.setEmergencyWarning(true);
            result.setEmergencyMessage(buildEmergencyMessage(selectedSymptoms));
            result.setOverallSeverity("CRITICAL");
            return result; // Return immediately with emergency warning
        }

        // STEP 2: Calculate match scores for all diseases
        List<ConditionMatch> matches = new ArrayList<>();
        for (Disease disease : knowledgeBase.getAllDiseases()) {
            ConditionMatch match = calculateMatch(disease, selectedSymptoms, severityMap, duration, userAge);
            // Only include conditions with at least minimal overlap
            if (match.getCompatibilityScore() > 0.08 && match.getMatchedCount() > 0) {
                matches.add(match);
            }
        }

        // STEP 3: Sort by score descending
        matches.sort((a, b) -> Double.compare(b.getCompatibilityScore(), a.getCompatibilityScore()));

        // STEP 4: Take top 3
        List<ConditionMatch> topMatches = matches.stream().limit(3).collect(Collectors.toList());
        result.setTopMatches(topMatches);

        // STEP 5: Determine overall severity level
        result.setOverallSeverity(determineOverallSeverity(topMatches, severityMap, duration));

        return result;
    }

    /**
     * Calculate how well a single disease matches the user's symptoms.
     * Uses weighted scoring: each symptom has a weight (1-3).
     */
    private ConditionMatch calculateMatch(Disease disease,
                                          List<Symptom> selectedSymptoms,
                                          Map<Integer, Integer> severityMap,
                                          String duration,
                                          int userAge) {

        // Build a set of selected symptom IDs for fast lookup
        Set<Integer> selectedIds = selectedSymptoms.stream()
            .map(Symptom::getId)
            .collect(Collectors.toSet());

        double totalWeightedScore = 0;  // actual matched weighted score
        double maxPossibleScore = 0;    // maximum possible score for this disease
        List<Symptom> matchedSymptoms = new ArrayList<>();
        List<Symptom> missingKeySymptoms = new ArrayList<>();

        for (DiseaseSymptomLink link : disease.getSymptomLinks()) {
            int baseWeight = link.getWeight(); // 1, 2, or 3
            maxPossibleScore += baseWeight;

            if (selectedIds.contains(link.getSymptomId())) {
                // ---- Symptom matched ----
                Symptom matchedSymptom = knowledgeBase.getSymptomById(link.getSymptomId());
                if (matchedSymptom != null) matchedSymptoms.add(matchedSymptom);

                // Adjust contribution by user-reported severity
                // severity 1-5 -> multiplier 0.6 to 1.4
                int severity = severityMap.getOrDefault(link.getSymptomId(), 3);
                double severityMultiplier = 0.6 + (severity / 5.0 * 0.8);
                totalWeightedScore += baseWeight * severityMultiplier;

            } else {
                // Track missing key symptoms (weight >= 2) for display purposes
                if (baseWeight >= 2) {
                    Symptom missed = knowledgeBase.getSymptomById(link.getSymptomId());
                    if (missed != null) missingKeySymptoms.add(missed);
                }
            }
        }

        // Base compatibility score (0.0 to 1.0)
        double score = maxPossibleScore > 0 ? totalWeightedScore / maxPossibleScore : 0;

        // Duration modifier: longer illness slightly boosts recommendation to seek help
        score = score * (1.0 + getDurationModifier(duration) * 0.05);
        score = Math.min(1.0, score); // Cap at 100%

        // If zero symptoms matched, score is 0 regardless
        if (matchedSymptoms.isEmpty()) score = 0;

        // Build and return the ConditionMatch
        ConditionMatch match = new ConditionMatch();
        match.setDisease(disease);
        match.setCompatibilityScore(score);
        match.setMatchedSymptoms(matchedSymptoms);
        match.setMatchedCount(matchedSymptoms.size());
        match.setTotalDiseaseSymptoms(disease.getSymptomLinks().size());
        match.setMissingKeySymptoms(missingKeySymptoms);

        return match;
    }

    /**
     * Check if any selected symptom is an emergency symptom.
     * Also checks if severity-5 versions of moderate symptoms cross emergency threshold.
     */
    private boolean checkEmergencySymptoms(List<Symptom> symptoms,
                                            Map<Integer, Integer> severityMap) {
        for (Symptom s : symptoms) {
            // Check if symptom is flagged as emergency in KnowledgeBase
            if (s.isEmergency()) return true;
            // Check emergency ID set
            if (EMERGENCY_SYMPTOM_IDS.contains(s.getId())) return true;
            // Check if severity is Very Severe (5) for high-risk symptoms
            int severity = severityMap.getOrDefault(s.getId(), 1);
            if (severity == 5 && (s.getId() == 32 || s.getId() == 6 || s.getId() == 38)) {
                return true; // Severe chest discomfort, severe fainting, severe vomiting
            }
        }
        return false;
    }

    /**
     * Build an emergency warning message listing the concerning symptoms.
     */
    private String buildEmergencyMessage(List<Symptom> symptoms) {
        List<String> emergencyNames = new ArrayList<>();
        for (Symptom s : symptoms) {
            if (s.isEmergency() || EMERGENCY_SYMPTOM_IDS.contains(s.getId())) {
                emergencyNames.add(s.getName());
            }
        }
        String symptomList = emergencyNames.isEmpty()
            ? "one or more concerning symptoms"
            : String.join(", ", emergencyNames);

        return "Some symptoms you have entered (" + symptomList + ") can be associated with "
             + "serious medical conditions.\n\n"
             + "Please seek urgent medical attention immediately or contact your local "
             + "emergency services.\n\n"
             + "Do NOT rely on this educational simulator for emergency situations.";
    }

    /**
     * Returns a small modifier based on symptom duration.
     * Longer duration = slightly higher urgency suggestion.
     */
    private double getDurationModifier(String duration) {
        if (duration == null) return 0;
        switch (duration) {
            case "Today":             return 0.0;
            case "1-3 days":          return 0.1;
            case "4-7 days":          return 0.2;
            case "1-2 weeks":         return 0.4;
            case "More than 2 weeks": return 0.6;
            default:                   return 0.0;
        }
    }

    /**
     * Determine overall severity label based on matched conditions and symptom severity.
     */
    private String determineOverallSeverity(List<ConditionMatch> topMatches,
                                             Map<Integer, Integer> severityMap,
                                             String duration) {
        if (severityMap.isEmpty()) return "MILD";

        // Calculate average reported severity
        double avgSeverity = severityMap.values().stream()
            .mapToInt(Integer::intValue).average().orElse(1);

        // Check if top condition is high-severity
        boolean topIsHighSeverity = !topMatches.isEmpty() &&
            "HIGH".equals(topMatches.get(0).getDisease().getSeverityLevel());

        // Long duration increases concern
        boolean longDuration = "1-2 weeks".equals(duration) ||
            "More than 2 weeks".equals(duration);

        if (avgSeverity >= 4.0 || topIsHighSeverity) return "HIGH";
        if (avgSeverity >= 2.5 || longDuration)      return "MODERATE";
        return "MILD";
    }

    /**
     * Generate a list of things the user should track based on their symptoms.
     * Called after analysis to suggest tracking targets.
     */
    public List<String> getRecommendedTracking(DiagnosisResult result) {
        Set<String> tracking = new LinkedHashSet<>();
        tracking.add("Overall energy level (1-5)");
        tracking.add("Symptom severity daily");

        for (Symptom s : result.getSelectedSymptoms()) {
            switch (s.getId()) {
                case 1: case 77:
                    tracking.add("Body temperature (twice daily)"); break;
                case 23: case 24: case 25:
                    tracking.add("Cough: frequency, type (dry/wet), severity"); break;
                case 26: case 34:
                    tracking.add("Breathing ease (1-5)"); break;
                case 3:
                    tracking.add("Fatigue level (1-5)"); break;
                case 11: case 12:
                    tracking.add("Headache intensity, duration, and location"); break;
                case 39:
                    tracking.add("Diarrhea frequency and signs of dehydration"); break;
                case 37: case 38:
                    tracking.add("Nausea/vomiting episodes"); break;
                case 61: case 62:
                    tracking.add("Urination frequency and comfort"); break;
                default: break;
            }
        }
        tracking.add("Fluid intake (aim 8+ glasses/day)");
        tracking.add("Sleep quality (1-5)");
        tracking.add("Any new symptoms that appear");

        return new ArrayList<>(tracking);
    }

    /**
     * Generate explanation text for "Why this result?" feature.
     */
    public String generateWhyExplanation(ConditionMatch match) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(match.getDisease().getName()).append("\" appeared in your results because:\n\n");

        sb.append("MATCHED SYMPTOMS (").append(match.getMatchedCount())
          .append(" of ").append(match.getTotalDiseaseSymptoms()).append(" key symptoms):\n");
        for (Symptom s : match.getMatchedSymptoms()) {
            sb.append("  \u2713 ").append(s.getName()).append("\n");
        }

        if (!match.getMissingKeySymptoms().isEmpty()) {
            sb.append("\nKEY SYMPTOMS NOT REPORTED:\n");
            for (Symptom s : match.getMissingKeySymptoms()) {
                sb.append("  \u2717 ").append(s.getName()).append("\n");
            }
        }

        sb.append("\nCOMPATIBILITY SCORE: ")
          .append(match.getScorePercent()).append("% (")
          .append(match.getCompatibilityLabel()).append(")\n\n");

        sb.append("IMPORTANT: This score is a Symptom Compatibility Score based on\n")
          .append("pattern matching. It is NOT a probability of having this condition.\n")
          .append("Please consult a qualified healthcare professional for proper diagnosis.");

        return sb.toString();
    }
}
