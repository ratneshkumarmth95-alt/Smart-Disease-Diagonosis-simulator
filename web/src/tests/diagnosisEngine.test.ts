import { describe, it, expect } from 'vitest';
import { analyzeSymptoms, checkEmergencySymptoms } from '../services/diagnosisEngine';
import { getSymptomById } from '../data/knowledgeBase';
import { Symptom } from '../types';

describe('DiagnosisEngine Algorithm Parity Tests', () => {
  it('correctly matches Common Cold with high score given runny nose, sneezing, sore throat', () => {
    // Symptom IDs: 28 (Runny nose), 30 (Sneezing), 31 (Sore throat)
    const s1 = getSymptomById(28) as Symptom;
    const s2 = getSymptomById(30) as Symptom;
    const s3 = getSymptomById(31) as Symptom;

    expect(s1).toBeDefined();
    expect(s2).toBeDefined();
    expect(s3).toBeDefined();

    const selected = [s1, s2, s3];
    const severityMap: Record<number, number> = {
      28: 2,
      30: 2,
      31: 2,
    };

    const result = analyzeSymptoms(selected, severityMap, '1-3 days', 'test-user');
    expect(result.topMatches.length).toBeGreaterThan(0);

    const topMatch = result.topMatches[0];
    expect(topMatch.disease.id).toBe(1); // Common Cold
    expect(topMatch.scorePercent).toBeGreaterThanOrEqual(40);
    expect(topMatch.compatibilityLabel).toBe('MODERATE');
    expect(result.emergencyWarning).toBe(false);
  });

  it('correctly matches Influenza (Flu) when fever, chills, fatigue, and muscle pain are present', () => {
    // Symptom IDs: 1 (Fever), 2 (Chills), 3 (Fatigue), 46 (Muscle pain)
    const s1 = getSymptomById(1) as Symptom;
    const s2 = getSymptomById(2) as Symptom;
    const s3 = getSymptomById(3) as Symptom;
    const s4 = getSymptomById(46) as Symptom;

    const selected = [s1, s2, s3, s4];
    const severityMap: Record<number, number> = {
      1: 4,
      2: 3,
      3: 4,
      46: 4,
    };

    const result = analyzeSymptoms(selected, severityMap, '1-3 days', 'test-user');
    expect(result.topMatches.length).toBeGreaterThan(0);

    const fluMatch = result.topMatches.find(m => m.disease.id === 2); // Influenza
    expect(fluMatch).toBeDefined();
    expect(fluMatch!.scorePercent).toBeGreaterThanOrEqual(60);
  });

  it('triggers emergency override flag when critical symptoms are selected', () => {
    // Symptom ID 33 is Severe chest pain (isEmergency: true)
    const s1 = getSymptomById(33) as Symptom;
    const s2 = getSymptomById(26) as Symptom; // Shortness of breath

    const selected = [s1, s2];
    const severityMap: Record<number, number> = {
      33: 5,
      26: 4,
    };

    const isEmergency = checkEmergencySymptoms(selected, severityMap);
    expect(isEmergency).toBe(true);

    const result = analyzeSymptoms(selected, severityMap, 'Today', 'test-user');
    expect(result.emergencyWarning).toBe(true);
    expect(result.overallSeverity).toBe('CRITICAL');
  });
});
