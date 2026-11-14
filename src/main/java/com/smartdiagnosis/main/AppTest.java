package com.smartdiagnosis.main;

import com.smartdiagnosis.model.*;
import com.smartdiagnosis.service.*;
import com.smartdiagnosis.utils.PasswordUtils;

import java.util.*;

/**
 * AppTest - Automated Test Suite verifying all Core Simulation Logic.
 *
 * Implements Section 41 Test Cases:
 *  - Test Case 1: Fever, Cough, Body ache, Fatigue -> Influenza-like illness ranks highly
 *  - Test Case 2: Sneezing, Runny nose, Mild cough -> Common cold / Allergic rhinitis ranks highly
 *  - Test Case 3: Severe difficulty breathing -> Emergency override warning
 *  - Test Case 4: Wellness Score Calculation logic
 *  - Test Case 5: Password Hashing & Salt Verification
 *
 * Run this in terminal during your college viva demonstration:
 * java -cp bin com.smartdiagnosis.main.AppTest
 */
public class AppTest {

    private static int testsPassed = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("  SMART DISEASE DIAGNOSIS SIMULATOR - AUTOMATED TEST SUITE");
        System.out.println("==============================================================");
        System.out.println();

        KnowledgeBase kb = KnowledgeBase.getInstance();
        DiagnosisEngine engine = new DiagnosisEngine();

        // -------------------------------------------------------------
        // TEST CASE 1: Flu Symptom Pattern
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 1] Testing Influenza-like Illness Matching...");
        List<Symptom> fluSymptoms = Arrays.asList(
            kb.getSymptomByName("Fever"),
            kb.getSymptomByName("Cough"),
            kb.getSymptomByName("Body ache"),
            kb.getSymptomByName("Fatigue")
        );
        Map<Integer, Integer> fluSeverity = new HashMap<>();
        for (Symptom s : fluSymptoms) fluSeverity.put(s.getId(), 3);

        DiagnosisResult fluResult = engine.analyze(fluSymptoms, fluSeverity, "1-3 days", 25);
        boolean fluMatched = !fluResult.getTopMatches().isEmpty() &&
            fluResult.getTopMatches().get(0).getDisease().getName().contains("Influenza");

        if (fluMatched) {
            System.out.println("  PASSED: Top condition is " + fluResult.getTopMatches().get(0).getDisease().getName()
                + " (Score: " + fluResult.getTopMatches().get(0).getScorePercent() + "%)");
            testsPassed++;
        } else {
            System.out.println("  FAILED: Influenza was not the top condition.");
        }
        System.out.println();

        // -------------------------------------------------------------
        // TEST CASE 2: Common Cold Pattern
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 2] Testing Common Cold Matching...");
        List<Symptom> coldSymptoms = Arrays.asList(
            kb.getSymptomByName("Sneezing"),
            kb.getSymptomByName("Runny nose"),
            kb.getSymptomByName("Nasal congestion"),
            kb.getSymptomByName("Cough")
        );
        Map<Integer, Integer> coldSeverity = new HashMap<>();
        for (Symptom s : coldSymptoms) coldSeverity.put(s.getId(), 2);

        DiagnosisResult coldResult = engine.analyze(coldSymptoms, coldSeverity, "1-3 days", 22);
        boolean coldMatched = !coldResult.getTopMatches().isEmpty() &&
            (coldResult.getTopMatches().get(0).getDisease().getName().contains("Cold")
             || coldResult.getTopMatches().get(0).getDisease().getName().contains("Rhinitis"));

        if (coldMatched) {
            System.out.println("  PASSED: Top condition is " + coldResult.getTopMatches().get(0).getDisease().getName()
                + " (Score: " + coldResult.getTopMatches().get(0).getScorePercent() + "%)");
            testsPassed++;
        } else {
            System.out.println("  FAILED: Cold was not in top condition.");
        }
        System.out.println();

        // -------------------------------------------------------------
        // TEST CASE 3: Emergency Short-Circuit Override
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 3] Testing Emergency Warning System...");
        List<Symptom> emergencySymptoms = Arrays.asList(
            kb.getSymptomByName("Fever"),
            kb.getSymptomByName("Severe difficulty breathing")
        );
        Map<Integer, Integer> emSeverity = new HashMap<>();
        emSeverity.put(kb.getSymptomByName("Severe difficulty breathing").getId(), 5);

        DiagnosisResult emergencyResult = engine.analyze(emergencySymptoms, emSeverity, "Today", 30);

        if (emergencyResult.isEmergencyWarning() && "CRITICAL".equals(emergencyResult.getOverallSeverity())) {
            System.out.println("  PASSED: Emergency detected! Medical diagnosis bypassed.");
            System.out.println("  Warning Message Preview: " + emergencyResult.getEmergencyMessage().split("\n")[0]);
            testsPassed++;
        } else {
            System.out.println("  FAILED: Emergency symptom did not trigger warning.");
        }
        System.out.println();

        // -------------------------------------------------------------
        // TEST CASE 4: Wellness Score Calculation
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 4] Testing Wellness Score Calculator...");
        HealthCheck check = new HealthCheck(1, java.time.LocalDate.now(), 4, 4, 2, 4); // energy=4, sleep=4, stress=2 (stressContrib=4), mood=4 -> 16/20 = 80%
        int score = check.getWellnessScore();
        if (score == 80) {
            System.out.println("  PASSED: Wellness Score accurately computed as " + score + "/100 (" + check.getWellnessLabel() + ")");
            testsPassed++;
        } else {
            System.out.println("  FAILED: Unexpected score: " + score);
        }
        System.out.println();

        // -------------------------------------------------------------
        // TEST CASE 5: Security / Password Hashing
        // -------------------------------------------------------------
        totalTests++;
        System.out.println("[TEST 5] Testing Salted Password Hashing...");
        String rawPass = "SecretPass123!";
        String hashed = PasswordUtils.hashPassword(rawPass);
        boolean verifyGood = PasswordUtils.verifyPassword(rawPass, hashed);
        boolean verifyBad = PasswordUtils.verifyPassword("WrongPassword", hashed);

        if (verifyGood && !verifyBad && hashed.contains(":")) {
            System.out.println("  PASSED: Password securely hashed with salt (SHA-256) and verified.");
            testsPassed++;
        } else {
            System.out.println("  FAILED: Password verification failed.");
        }
        System.out.println();

        // -------------------------------------------------------------
        // SUMMARY
        // -------------------------------------------------------------
        System.out.println("==============================================================");
        System.out.println("  TEST RESULTS: " + testsPassed + " / " + totalTests + " PASSED");
        System.out.println("==============================================================");

        if (testsPassed == totalTests) {
            System.out.println("All verification test cases passed successfully!");
        }
    }
}
