export type SymptomCategory =
  | 'General'
  | 'Neurological'
  | 'Respiratory'
  | 'Digestive'
  | 'Musculoskeletal'
  | 'Skin'
  | 'Urinary'
  | 'Eye'
  | 'ENT';

export type SeverityLevel = 'LOW' | 'MODERATE' | 'HIGH';
export type CompatibilityLevel = 'HIGH' | 'MODERATE' | 'LOW';
export type DurationOption = 'Today' | '1-3 days' | '4-7 days' | '1-2 weeks' | 'More than 2 weeks';
export type MoodType = 'GREAT' | 'GOOD' | 'OKAY' | 'BAD' | 'TERRIBLE';

export interface Symptom {
  id: number;
  name: string;
  category: SymptomCategory;
  isEmergency: boolean;
  description?: string;
  weight?: number; // 1=common, 2=important, 3=highly characteristic
}

export interface DiseaseSymptomLink {
  symptomId: number;
  weight: number; // 1, 2, or 3
}

export interface Disease {
  id: number;
  name: string;
  description: string;
  category: string;
  severityLevel: SeverityLevel;
  symptomLinks: DiseaseSymptomLink[];
  commonSigns?: string[];
  selfCareAdvice?: string[];
  warningSigns?: string[];
  medicalAttentionAdvice?: string;
  requiresImmediateAttention?: boolean;
}

export interface ConditionMatch {
  disease: Disease;
  compatibilityScore: number; // 0.0 to 1.0
  compatibilityLabel: CompatibilityLevel;
  matchedSymptoms: Symptom[];
  missingKeySymptoms: Symptom[];
  matchedCount: number;
  totalDiseaseSymptoms: number;
  scorePercent: number; // 0 to 100
}

export interface DiagnosisResult {
  id?: number | string;
  userId?: number | string;
  analysisDate: string; // ISO string
  selectedSymptoms: Symptom[];
  topMatches: ConditionMatch[];
  emergencyWarning: boolean;
  emergencyMessage?: string;
  overallSeverity: 'CRITICAL' | 'HIGH' | 'MODERATE' | 'MILD' | 'NONE';
  duration?: DurationOption;
}

export interface HealthCheck {
  id?: number | string;
  userId: number | string;
  checkDate: string; // YYYY-MM-DD
  energyLevel: number; // 1-5
  sleepQuality: number; // 1-5
  stressLevel: number; // 1-5
  moodLevel: number; // 1-5
  notes?: string;
  wellnessScore: number; // 0-100
}

export interface SymptomLog {
  id?: number | string;
  userId: number | string;
  symptomId: number;
  symptomName: string;
  logDate: string; // YYYY-MM-DD
  severity: number; // 1-5
  duration: DurationOption | string;
  notes?: string;
}

export interface JournalEntry {
  id?: number | string;
  userId: number | string;
  entryDate: string; // ISO string
  title: string;
  content: string;
  mood: MoodType;
}

export interface HealthTip {
  id: number;
  category: string;
  title: string;
  content: string;
  icon: string;
}

export interface QuizQuestion {
  id: number;
  question: string;
  options: string[];
  correctAnswerIndex: number;
  explanation: string;
}

export interface UserProfile {
  id: string | number;
  username: string;
  name: string;
  age: number;
  gender: string;
  height?: number; // cm
  weight?: number; // kg
  bloodGroup?: string;
  dateOfBirth?: string;
  allergies?: string;
  existingConditions?: string;
  emergencyContact?: string;
  createdAt?: string;
  streakDays: number;
  totalCheckIns: number;
  badges: string[];
  lastCheckIn?: string;
}

export interface HealthAdvice {
  category: 'SELF_CARE' | 'MONITORING' | 'MEDICAL_CONSULTATION' | 'EMERGENCY';
  title: string;
  points: string[];
}

export const BADGES = {
  FIRST_CHECKIN: '🏆 First Check-in',
  HYDRATION_TRACKER: '💧 Hydration Tracker',
  SEVEN_DAY_TRACKER: '📊 7-Day Tracker',
  HEALTHY_HABITS: '🌱 Healthy Habits',
  CONSISTENT_TRACKER: '📅 Consistent Tracker',
  SYMPTOM_REPORTER: '⚕️ Symptom Reporter',
  KNOWLEDGE_SEEKER: '📚 Knowledge Seeker',
  WELLNESS_CHAMPION: '🌟 Wellness Champion',
  MONTH_TRACKER: '🔥 30-Day Streak',
} as const;
