import {
  Symptom,
  Disease,
  ConditionMatch,
  DiagnosisResult,
  DurationOption,
  CompatibilityLevel,
} from '../types';
import { DISEASES, getSymptomById } from '../data/knowledgeBase';

const EMERGENCY_SYMPTOM_IDS = new Set<number>([
  7,  // Fainting
  13, // Confusion
  18, // Sudden severe headache
  21, // Neck stiffness
  22, // Sudden one-sided weakness
  26, // Shortness of breath
  33, // Severe chest pain
  34, // Severe difficulty breathing
  45, // Blood in stool
  60, // Severe allergic reaction
  63, // Blood in urine
  76, // Severe dehydration
  77, // High fever above 103F
  78, // Persistent vomiting
  79, // Productive cough with blood
  80, // Sudden vision loss
]);

export function checkEmergencySymptoms(
  symptoms: Symptom[],
  severityMap: Record<number, number>
): boolean {
  for (const s of symptoms) {
    if (s.isEmergency) return true;
    if (EMERGENCY_SYMPTOM_IDS.has(s.id)) return true;
    const severity = severityMap[s.id] ?? 1;
    // Severity 5 of concerning symptoms
    if (severity === 5 && (s.id === 32 || s.id === 6 || s.id === 38)) {
      return true;
    }
  }
  return false;
}

export function buildEmergencyMessage(symptoms: Symptom[]): string {
  const emergencyNames = symptoms
    .filter((s) => s.isEmergency || EMERGENCY_SYMPTOM_IDS.has(s.id))
    .map((s) => s.name);

  const symptomList =
    emergencyNames.length > 0
      ? emergencyNames.join(', ')
      : 'one or more critical warning signs';

  return (
    `Some symptoms you have entered (${symptomList}) can be associated with serious medical conditions.\n\n` +
    `Please seek urgent medical attention immediately or contact your local emergency services (e.g. 911, 112, 108).\n\n` +
    `Do NOT rely on this educational simulation for emergency situations.`
  );
}

function getDurationModifier(duration?: DurationOption): number {
  switch (duration) {
    case 'Today':
      return 0.0;
    case '1-3 days':
      return 0.1;
    case '4-7 days':
      return 0.2;
    case '1-2 weeks':
      return 0.4;
    case 'More than 2 weeks':
      return 0.6;
    default:
      return 0.0;
  }
}

function calculateMatch(
  disease: Disease,
  selectedSymptoms: Symptom[],
  severityMap: Record<number, number>,
  duration?: DurationOption
): ConditionMatch {
  const selectedIds = new Set(selectedSymptoms.map((s) => s.id));

  let totalWeightedScore = 0;
  let maxPossibleScore = 0;
  const matchedSymptoms: Symptom[] = [];
  const missingKeySymptoms: Symptom[] = [];

  for (const link of disease.symptomLinks) {
    const baseWeight = link.weight; // 1, 2, or 3
    maxPossibleScore += baseWeight;

    if (selectedIds.has(link.symptomId)) {
      const sym = getSymptomById(link.symptomId);
      if (sym) matchedSymptoms.push(sym);

      const severity = severityMap[link.symptomId] ?? 3;
      // Formula from Java source: 0.6 + (severity / 5.0 * 0.8)
      const severityMultiplier = 0.6 + (severity / 5.0) * 0.8;
      totalWeightedScore += baseWeight * severityMultiplier;
    } else {
      if (baseWeight >= 2) {
        const missed = getSymptomById(link.symptomId);
        if (missed) missingKeySymptoms.push(missed);
      }
    }
  }

  let score = maxPossibleScore > 0 ? totalWeightedScore / maxPossibleScore : 0;
  score = score * (1.0 + getDurationModifier(duration) * 0.05);
  score = Math.min(1.0, score);

  if (matchedSymptoms.length === 0) {
    score = 0;
  }

  let compatibilityLabel: CompatibilityLevel = 'LOW';
  if (score >= 0.70) {
    compatibilityLabel = 'HIGH';
  } else if (score >= 0.40) {
    compatibilityLabel = 'MODERATE';
  }

  return {
    disease,
    compatibilityScore: score,
    compatibilityLabel,
    matchedSymptoms,
    missingKeySymptoms,
    matchedCount: matchedSymptoms.length,
    totalDiseaseSymptoms: disease.symptomLinks.length,
    scorePercent: Math.round(score * 100),
  };
}

function determineOverallSeverity(
  topMatches: ConditionMatch[],
  severityMap: Record<number, number>,
  duration?: DurationOption
): 'CRITICAL' | 'HIGH' | 'MODERATE' | 'MILD' | 'NONE' {
  const values = Object.values(severityMap);
  if (values.length === 0) return 'MILD';

  const avgSeverity = values.reduce((a, b) => a + b, 0) / values.length;
  const topIsHigh =
    topMatches.length > 0 && topMatches[0].disease.severityLevel === 'HIGH';
  const longDuration =
    duration === '1-2 weeks' || duration === 'More than 2 weeks';

  if (avgSeverity >= 4.0 || topIsHigh) return 'HIGH';
  if (avgSeverity >= 2.5 || longDuration) return 'MODERATE';
  return 'MILD';
}

export function analyzeSymptoms(
  selectedSymptoms: Symptom[],
  severityMap: Record<number, number>,
  duration: DurationOption = '1-3 days',
  userId?: string | number
): DiagnosisResult {
  const result: DiagnosisResult = {
    userId,
    analysisDate: new Date().toISOString(),
    selectedSymptoms: [...selectedSymptoms],
    topMatches: [],
    emergencyWarning: false,
    overallSeverity: 'NONE',
    duration,
  };

  if (selectedSymptoms.length === 0) {
    return result;
  }

  // Step 1: Emergency short-circuit check
  const hasEmergency = checkEmergencySymptoms(selectedSymptoms, severityMap);
  if (hasEmergency) {
    result.emergencyWarning = true;
    result.emergencyMessage = buildEmergencyMessage(selectedSymptoms);
    result.overallSeverity = 'CRITICAL';
    return result;
  }

  // Step 2: Calculate match scores for all conditions in knowledge base
  const matches: ConditionMatch[] = [];
  for (const disease of DISEASES) {
    const match = calculateMatch(disease, selectedSymptoms, severityMap, duration);
    if (match.compatibilityScore > 0.08 && match.matchedCount > 0) {
      matches.push(match);
    }
  }

  // Step 3: Sort descending by compatibility score
  matches.sort((a, b) => b.compatibilityScore - a.compatibilityScore);

  // Step 4: Keep top 3
  result.topMatches = matches.slice(0, 3);

  // Step 5: Overall severity
  result.overallSeverity = determineOverallSeverity(result.topMatches, severityMap, duration);

  return result;
}

export function getRecommendedTracking(result: DiagnosisResult): string[] {
  const tracking = new Set<string>();
  tracking.add('Overall energy level (1–5)');
  tracking.add('Symptom severity daily');

  for (const s of result.selectedSymptoms) {
    switch (s.id) {
      case 1:
      case 77:
        tracking.add('Body temperature (twice daily)');
        break;
      case 23:
      case 24:
      case 25:
        tracking.add('Cough: frequency, mucus type, and severity');
        break;
      case 26:
      case 34:
        tracking.add('Breathing ease (1–5)');
        break;
      case 3:
        tracking.add('Fatigue level (1–5)');
        break;
      case 11:
      case 12:
        tracking.add('Headache intensity, duration, and triggers');
        break;
      case 39:
        tracking.add('Hydration level and diarrhea frequency');
        break;
      case 37:
      case 38:
        tracking.add('Nausea / vomiting episodes');
        break;
      case 61:
      case 62:
        tracking.add('Urination frequency and comfort');
        break;
    }
  }

  tracking.add('Fluid intake (aim for 8+ glasses / day)');
  tracking.add('Sleep quality (1–5)');
  tracking.add('Any new symptoms that appear');

  return Array.from(tracking);
}

export function generateWhyExplanation(match: ConditionMatch): string {
  const lines: string[] = [
    `"${match.disease.name}" appeared in your results because:`,
    '',
    `MATCHED SYMPTOMS (${match.matchedCount} of ${match.totalDiseaseSymptoms} key indicators):`,
    ...match.matchedSymptoms.map((s) => `  ✓ ${s.name}`),
  ];

  if (match.missingKeySymptoms.length > 0) {
    lines.push('', 'KEY INDICATORS NOT REPORTED:');
    lines.push(...match.missingKeySymptoms.map((s) => `  ✕ ${s.name}`));
  }

  lines.push(
    '',
    `COMPATIBILITY SCORE: ${match.scorePercent}% (${match.compatibilityLabel})`,
    '',
    'IMPORTANT: This compatibility score reflects pattern similarity with common clinical presentations.',
    'It is NOT a medical diagnosis or probability of disease.',
    'Always consult a qualified healthcare professional for evaluation.'
  );

  return lines.join('\n');
}
