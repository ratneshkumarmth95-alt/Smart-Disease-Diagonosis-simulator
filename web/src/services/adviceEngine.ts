import { DiagnosisResult, HealthAdvice, UserProfile } from '../types';

export function generateAdvice(result: DiagnosisResult): HealthAdvice[] {
  const advice: HealthAdvice[] = [];

  // Emergency short-circuit
  if (result.emergencyWarning) {
    advice.push({
      category: 'EMERGENCY',
      title: 'Seek Immediate Emergency Care',
      points: [
        'Call your local emergency medical number (e.g., 911, 112, 108) immediately.',
        'Do not attempt to drive yourself to the hospital if experiencing severe chest pain, shortness of breath, or dizziness.',
        'Have someone remain with you while emergency services arrive.',
        'Do not rely on this simulator for emergency triage.',
        'Remain calm, seated or reclining comfortably while assistance arrives.',
      ],
    });
    return advice;
  }

  // General self-care
  advice.push({
    category: 'SELF_CARE',
    title: 'General Rest & Recovery',
    points: [
      'Prioritize restorative sleep — your immune system recovers faster with rest.',
      'Maintain steady hydration with water, clear broths, or electrolyte solutions.',
      'Eat nourishing, digestible meals even if appetite is mild.',
      'Avoid smoking, vaping, and alcohol, which increase mucosal irritation.',
      'Practice regular hand hygiene to prevent spreading infection.',
    ],
  });

  // Condition-specific advice from top match
  if (result.topMatches.length > 0) {
    const topMatch = result.topMatches[0];
    const disease = topMatch.disease;

    if (disease.selfCareAdvice && disease.selfCareAdvice.length > 0) {
      advice.push({
        category: 'SELF_CARE',
        title: `Self-Care Suggestions for ${disease.name}`,
        points: disease.selfCareAdvice,
      });
    }

    if (disease.warningSigns && disease.warningSigns.length > 0) {
      advice.push({
        category: 'MONITORING',
        title: `Warning Signs to Watch For (${disease.name})`,
        points: disease.warningSigns,
      });
    }
  }

  // Symptom-specific monitoring points
  const monitoringPoints: string[] = [];
  for (const s of result.selectedSymptoms) {
    switch (s.id) {
      case 1:
      case 77:
        monitoringPoints.push('Track body temperature twice daily with a digital thermometer.');
        break;
      case 23:
      case 24:
      case 25:
        monitoringPoints.push('Observe cough progression: note changes from dry to productive or increasing severity.');
        break;
      case 26:
        monitoringPoints.push('Monitor breathing effort — seek urgent evaluation if shortness of breath increases at rest.');
        break;
      case 3:
        monitoringPoints.push('Log daily energy levels (1–5) to observe recovery trajectory.');
        break;
      case 11:
      case 12:
        monitoringPoints.push('Note headache duration, light/sound sensitivity, and response to hydration/dark rest.');
        break;
      case 39:
        monitoringPoints.push('Monitor hydration markers: dry mouth, dark urine, and lightheadedness.');
        break;
      case 37:
      case 38:
        monitoringPoints.push('Keep track of nausea episodes and ability to hold down small sips of water.');
        break;
    }
  }
  monitoringPoints.push('Take note of any brand-new symptoms that develop over the next 48 hours.');
  monitoringPoints.push('Track whether your overall condition is trending upward, stable, or declining.');

  advice.push({
    category: 'MONITORING',
    title: 'Active Symptom Monitoring',
    points: monitoringPoints,
  });

  // Medical consultation points
  const consultPoints: string[] = [];
  if (result.topMatches.length > 0 && result.topMatches[0].disease.medicalAttentionAdvice) {
    consultPoints.push(result.topMatches[0].disease.medicalAttentionAdvice);
  }
  consultPoints.push('Consult a licensed physician if symptoms persist longer than 7–10 days without clear improvement.');
  consultPoints.push('Schedule a clinic visit if symptoms steadily worsen rather than resolve.');
  consultPoints.push('High-risk individuals (elderly, pregnant, diabetic, or immunocompromised) should seek evaluation early.');
  consultPoints.push('This educational platform cannot provide prescription medications or individualized diagnoses.');

  advice.push({
    category: 'MEDICAL_CONSULTATION',
    title: 'When to Consult a Physician',
    points: consultPoints,
  });

  // Standard legal & medical disclaimer
  advice.push({
    category: 'MEDICAL_CONSULTATION',
    title: 'Important Educational Disclaimer',
    points: [
      'This analysis is strictly an EDUCATIONAL SIMULATION.',
      'It does NOT constitute a clinical diagnosis or medical treatment plan.',
      'Symptom Compatibility Scores represent statistical pattern overlap, not clinical certainty.',
      'Always consult a qualified healthcare professional for medical questions.',
    ],
  });

  return advice;
}

export function generateReport(result: DiagnosisResult, user?: UserProfile | null): string {
  const line = '='.repeat(64);
  const subline = '-'.repeat(36);
  const dateStr = new Date(result.analysisDate).toLocaleString();

  const lines: string[] = [
    line,
    '       SMART DISEASE DIAGNOSIS SIMULATOR — ANALYSIS REPORT',
    '                 EDUCATIONAL SIMULATION ONLY',
    line,
    '',
    'PATIENT INFORMATION',
    subline,
    `Name     : ${user?.name || 'Guest User'}`,
    `Age      : ${user?.age ? user.age : 'N/A'}`,
    `Gender   : ${user?.gender || 'N/A'}`,
    `Date/Time: ${dateStr}`,
    `Duration : ${result.duration || 'Not specified'}`,
    '',
    'REPORTED SYMPTOMS',
    subline,
  ];

  if (result.selectedSymptoms.length === 0) {
    lines.push('  No symptoms recorded.');
  } else {
    for (const s of result.selectedSymptoms) {
      lines.push(`  • ${s.name} [${s.category}]`);
    }
  }

  lines.push('');

  if (result.emergencyWarning) {
    lines.push('⚠️  EMERGENCY WARNING ISSUED', subline, result.emergencyMessage || '', '');
  } else {
    lines.push('TOP COMPATIBLE CLINICAL PATTERNS', subline);
    lines.push('NOTE: Scores reflect symptom overlap, NOT medical probabilities.', '');

    let rank = 1;
    for (const match of result.topMatches) {
      lines.push(
        `${rank++}. ${match.disease.name}`,
        `   Compatibility : ${match.compatibilityLabel} (${match.scorePercent}%)`,
        `   Category      : ${match.disease.category}`,
        `   Matched Signs : ${match.matchedSymptoms.map((s) => s.name).join(', ') || 'None'}`
      );
      if (match.missingKeySymptoms.length > 0) {
        lines.push(`   Absent Signs  : ${match.missingKeySymptoms.map((s) => s.name).join(', ')}`);
      }
      lines.push('');
    }

    if (result.topMatches.length === 0) {
      lines.push('  No strong symptom patterns identified. Consider medical consultation.', '');
    }
  }

  lines.push(
    line,
    'IMPORTANT DISCLAIMER:',
    'This document is generated by an educational symptom simulation engine.',
    'It does NOT replace medical consultation, laboratory testing, or physician care.',
    'For any medical concerns, contact a licensed healthcare provider.',
    line
  );

  return lines.join('\n');
}
