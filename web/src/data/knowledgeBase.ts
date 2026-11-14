import { Symptom, Disease, HealthTip, SymptomCategory } from '../types';

export const SYMPTOMS: Symptom[] = [
  // === GENERAL SYMPTOMS ===
  { id: 1, name: 'Fever', category: 'General', isEmergency: false, weight: 1 },
  { id: 2, name: 'Chills', category: 'General', isEmergency: false, weight: 1 },
  { id: 3, name: 'Fatigue', category: 'General', isEmergency: false, weight: 1 },
  { id: 4, name: 'Weakness', category: 'General', isEmergency: false, weight: 1 },
  { id: 5, name: 'Malaise', category: 'General', isEmergency: false, weight: 1 },
  { id: 6, name: 'Dizziness', category: 'General', isEmergency: false, weight: 1 },
  { id: 7, name: 'Fainting', category: 'General', isEmergency: true, weight: 1 },
  { id: 8, name: 'Sweating', category: 'General', isEmergency: false, weight: 1 },
  { id: 9, name: 'Night sweats', category: 'General', isEmergency: false, weight: 1 },
  { id: 10, name: 'Loss of appetite', category: 'General', isEmergency: false, weight: 1 },

  // === HEAD / NEUROLOGICAL ===
  { id: 11, name: 'Headache', category: 'Neurological', isEmergency: false, weight: 1 },
  { id: 12, name: 'Migraine-like headache', category: 'Neurological', isEmergency: false, weight: 1 },
  { id: 13, name: 'Confusion', category: 'Neurological', isEmergency: true, weight: 1 },
  { id: 14, name: 'Memory difficulty', category: 'Neurological', isEmergency: false, weight: 1 },
  { id: 15, name: 'Numbness', category: 'Neurological', isEmergency: false, weight: 1 },
  { id: 16, name: 'Tingling', category: 'Neurological', isEmergency: false, weight: 1 },
  { id: 17, name: 'Tremors', category: 'Neurological', isEmergency: false, weight: 1 },
  { id: 18, name: 'Sudden severe headache', category: 'Neurological', isEmergency: true, weight: 1 },
  { id: 19, name: 'Sensitivity to light', category: 'Neurological', isEmergency: false, weight: 1 },
  { id: 20, name: 'Sensitivity to sound', category: 'Neurological', isEmergency: false, weight: 1 },
  { id: 21, name: 'Neck stiffness', category: 'Neurological', isEmergency: true, weight: 1 },
  { id: 22, name: 'Sudden one-sided weakness', category: 'Neurological', isEmergency: true, weight: 1 },

  // === RESPIRATORY ===
  { id: 23, name: 'Cough', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 24, name: 'Dry cough', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 25, name: 'Wet cough', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 26, name: 'Shortness of breath', category: 'Respiratory', isEmergency: true, weight: 1 },
  { id: 27, name: 'Wheezing', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 28, name: 'Runny nose', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 29, name: 'Nasal congestion', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 30, name: 'Sneezing', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 31, name: 'Sore throat', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 32, name: 'Chest discomfort', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 33, name: 'Severe chest pain', category: 'Respiratory', isEmergency: true, weight: 1 },
  { id: 34, name: 'Severe difficulty breathing', category: 'Respiratory', isEmergency: true, weight: 1 },
  { id: 35, name: 'Loss of smell', category: 'Respiratory', isEmergency: false, weight: 1 },
  { id: 36, name: 'Loss of taste', category: 'Respiratory', isEmergency: false, weight: 1 },

  // === DIGESTIVE ===
  { id: 37, name: 'Nausea', category: 'Digestive', isEmergency: false, weight: 1 },
  { id: 38, name: 'Vomiting', category: 'Digestive', isEmergency: false, weight: 1 },
  { id: 39, name: 'Diarrhea', category: 'Digestive', isEmergency: false, weight: 1 },
  { id: 40, name: 'Constipation', category: 'Digestive', isEmergency: false, weight: 1 },
  { id: 41, name: 'Abdominal pain', category: 'Digestive', isEmergency: false, weight: 1 },
  { id: 42, name: 'Bloating', category: 'Digestive', isEmergency: false, weight: 1 },
  { id: 43, name: 'Heartburn', category: 'Digestive', isEmergency: false, weight: 1 },
  { id: 44, name: 'Acid reflux', category: 'Digestive', isEmergency: false, weight: 1 },
  { id: 45, name: 'Blood in stool', category: 'Digestive', isEmergency: true, weight: 1 },

  // === MUSCULOSKELETAL ===
  { id: 46, name: 'Muscle pain', category: 'Musculoskeletal', isEmergency: false, weight: 1 },
  { id: 47, name: 'Joint pain', category: 'Musculoskeletal', isEmergency: false, weight: 1 },
  { id: 48, name: 'Back pain', category: 'Musculoskeletal', isEmergency: false, weight: 1 },
  { id: 49, name: 'Neck pain', category: 'Musculoskeletal', isEmergency: false, weight: 1 },
  { id: 50, name: 'Body ache', category: 'Musculoskeletal', isEmergency: false, weight: 1 },
  { id: 51, name: 'Muscle weakness', category: 'Musculoskeletal', isEmergency: false, weight: 1 },
  { id: 52, name: 'Muscle cramps', category: 'Musculoskeletal', isEmergency: false, weight: 1 },

  // === SKIN ===
  { id: 53, name: 'Rash', category: 'Skin', isEmergency: false, weight: 1 },
  { id: 54, name: 'Itching', category: 'Skin', isEmergency: false, weight: 1 },
  { id: 55, name: 'Redness', category: 'Skin', isEmergency: false, weight: 1 },
  { id: 56, name: 'Dry skin', category: 'Skin', isEmergency: false, weight: 1 },
  { id: 57, name: 'Swelling', category: 'Skin', isEmergency: false, weight: 1 },
  { id: 58, name: 'Hives', category: 'Skin', isEmergency: false, weight: 1 },
  { id: 59, name: 'Skin lesions', category: 'Skin', isEmergency: false, weight: 1 },
  { id: 60, name: 'Severe allergic reaction (throat swelling)', category: 'Skin', isEmergency: true, weight: 1 },

  // === URINARY ===
  { id: 61, name: 'Frequent urination', category: 'Urinary', isEmergency: false, weight: 1 },
  { id: 62, name: 'Painful urination', category: 'Urinary', isEmergency: false, weight: 1 },
  { id: 63, name: 'Blood in urine', category: 'Urinary', isEmergency: true, weight: 1 },
  { id: 64, name: 'Lower abdominal discomfort', category: 'Urinary', isEmergency: false, weight: 1 },
  { id: 65, name: 'Foul-smelling urine', category: 'Urinary', isEmergency: false, weight: 1 },

  // === EYE ===
  { id: 66, name: 'Eye redness', category: 'Eye', isEmergency: false, weight: 1 },
  { id: 67, name: 'Eye pain', category: 'Eye', isEmergency: false, weight: 1 },
  { id: 68, name: 'Blurred vision', category: 'Eye', isEmergency: false, weight: 1 },
  { id: 69, name: 'Watery eyes', category: 'Eye', isEmergency: false, weight: 1 },
  { id: 70, name: 'Light sensitivity', category: 'Eye', isEmergency: false, weight: 1 },

  // === EAR / NOSE / THROAT ===
  { id: 71, name: 'Ear pain', category: 'ENT', isEmergency: false, weight: 1 },
  { id: 72, name: 'Hearing difficulty', category: 'ENT', isEmergency: false, weight: 1 },
  { id: 73, name: 'Sinus pressure', category: 'ENT', isEmergency: false, weight: 1 },
  { id: 74, name: 'Difficulty swallowing', category: 'ENT', isEmergency: false, weight: 1 },
  { id: 75, name: 'Swollen lymph nodes', category: 'ENT', isEmergency: false, weight: 1 },

  // === SEVERE / EMERGENCY ===
  { id: 76, name: 'Severe dehydration', category: 'General', isEmergency: true, weight: 1 },
  { id: 77, name: 'High fever above 103F', category: 'General', isEmergency: true, weight: 1 },
  { id: 78, name: 'Persistent vomiting', category: 'Digestive', isEmergency: true, weight: 1 },
  { id: 79, name: 'Productive cough with blood', category: 'Respiratory', isEmergency: true, weight: 1 },
  { id: 80, name: 'Sudden vision loss', category: 'Eye', isEmergency: true, weight: 1 },
];

export const DISEASES: Disease[] = [
  {
    id: 1,
    name: 'Common Cold',
    description: 'A mild viral infection of the upper respiratory tract caused by rhinoviruses. Very common and usually resolves within 7-10 days.',
    category: 'Respiratory',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 28, weight: 3 }, // Runny nose
      { symptomId: 29, weight: 3 }, // Nasal congestion
      { symptomId: 30, weight: 2 }, // Sneezing
      { symptomId: 23, weight: 2 }, // Cough
      { symptomId: 31, weight: 2 }, // Sore throat
      { symptomId: 3, weight: 1 },  // Fatigue
      { symptomId: 1, weight: 1 },  // Fever
      { symptomId: 11, weight: 1 }, // Headache
    ],
    commonSigns: ['Runny nose', 'Nasal congestion', 'Sneezing', 'Mild cough', 'Sore throat'],
    selfCareAdvice: [
      'Rest adequately and stay hydrated.',
      'Drink warm fluids like herbal tea or warm water with honey.',
      'Use saline nasal rinse to relieve congestion.',
      'Gargle with warm salt water for sore throat relief.',
      'Eat nutritious food even if appetite is reduced.',
    ],
    warningSigns: [
      'Symptoms lasting more than 10 days without improvement.',
      'High fever above 103°F (39.4°C).',
      'Severe headache or sinus pain.',
      'Difficulty breathing or chest pain.',
    ],
    medicalAttentionAdvice: 'Consider consulting a healthcare professional if symptoms persist beyond 10 days, worsen significantly, or if you develop a high fever.',
  },
  {
    id: 2,
    name: 'Influenza-like Illness',
    description: 'A contagious respiratory illness caused by influenza viruses. Symptoms are typically more severe than a common cold and come on suddenly.',
    category: 'Respiratory',
    severityLevel: 'MODERATE',
    symptomLinks: [
      { symptomId: 1, weight: 3 },  // Fever
      { symptomId: 50, weight: 3 }, // Body ache
      { symptomId: 3, weight: 3 },  // Fatigue
      { symptomId: 11, weight: 2 }, // Headache
      { symptomId: 23, weight: 2 }, // Cough
      { symptomId: 2, weight: 2 },  // Chills
      { symptomId: 46, weight: 2 }, // Muscle pain
      { symptomId: 37, weight: 1 }, // Nausea
      { symptomId: 10, weight: 1 }, // Loss of appetite
    ],
    commonSigns: ['Sudden-onset fever', 'Body aches', 'Severe fatigue', 'Dry cough', 'Chills', 'Headache'],
    selfCareAdvice: [
      'Rest completely — your body needs energy to fight the illness.',
      'Drink plenty of fluids: water, clear broths, electrolyte drinks.',
      'Monitor your temperature regularly.',
      'Eat light, easy-to-digest foods.',
      'Avoid contact with others to prevent spreading.',
    ],
    warningSigns: [
      'Difficulty breathing or shortness of breath.',
      'Persistent chest pain or pressure.',
      'Confusion or altered mental status.',
      'Severe vomiting or inability to keep fluids down.',
      'Symptoms that improve then suddenly return with fever and worsening cough.',
    ],
    medicalAttentionAdvice: 'Seek medical evaluation if symptoms are severe, you belong to a high-risk group (elderly, pregnant, immunocompromised), or symptoms worsen after initial improvement.',
  },
  {
    id: 3,
    name: 'COVID-19-like Respiratory Illness',
    description: 'A respiratory illness with symptom patterns similar to COVID-19. This is an educational simulation — please get proper medical testing for a confirmed diagnosis.',
    category: 'Respiratory',
    severityLevel: 'MODERATE',
    symptomLinks: [
      { symptomId: 1, weight: 3 },  // Fever
      { symptomId: 24, weight: 3 }, // Dry cough
      { symptomId: 3, weight: 3 },  // Fatigue
      { symptomId: 35, weight: 3 }, // Loss of smell
      { symptomId: 36, weight: 3 }, // Loss of taste
      { symptomId: 26, weight: 2 }, // Shortness of breath
      { symptomId: 50, weight: 2 }, // Body ache
      { symptomId: 11, weight: 2 }, // Headache
      { symptomId: 31, weight: 1 }, // Sore throat
      { symptomId: 37, weight: 1 }, // Nausea
    ],
    commonSigns: ['Fever', 'Dry cough', 'Fatigue', 'Loss of smell/taste', 'Shortness of breath'],
    selfCareAdvice: [
      'Self-isolate to prevent spreading to others.',
      'Rest and stay well hydrated.',
      'Monitor oxygen levels if a pulse oximeter is available.',
      'Track your temperature daily.',
      'Seek proper medical testing for confirmation.',
    ],
    warningSigns: [
      'Difficulty breathing or shortness of breath.',
      'Persistent chest pain or pressure.',
      'Confusion or inability to wake or stay awake.',
      'Bluish lips or face.',
      'Oxygen saturation below 94%.',
    ],
    medicalAttentionAdvice: 'IMPORTANT: This simulation cannot diagnose COVID-19. Please get proper medical testing. Seek emergency care immediately if you have difficulty breathing or chest pain.',
  },
  {
    id: 4,
    name: 'Acute Bronchitis',
    description: 'Inflammation of the bronchial tubes, usually caused by a viral infection. The main symptom is a persistent cough.',
    category: 'Respiratory',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 23, weight: 3 }, // Cough
      { symptomId: 25, weight: 3 }, // Wet cough
      { symptomId: 32, weight: 2 }, // Chest discomfort
      { symptomId: 3, weight: 2 },  // Fatigue
      { symptomId: 27, weight: 2 }, // Wheezing
      { symptomId: 1, weight: 1 },  // Fever
      { symptomId: 29, weight: 1 }, // Nasal congestion
    ],
    commonSigns: ['Persistent cough (may produce mucus)', 'Chest discomfort', 'Slight fever', 'Fatigue', 'Mild wheezing'],
    selfCareAdvice: [
      'Rest and drink plenty of fluids to thin mucus.',
      'Use a humidifier to ease breathing.',
      'Avoid smoking or secondhand smoke.',
      'Steam inhalation may provide relief.',
    ],
    warningSigns: [
      'Cough lasting more than 3 weeks.',
      'Blood in coughed-up mucus.',
      'High fever above 100.4°F (38°C).',
      'Shortness of breath at rest.',
    ],
    medicalAttentionAdvice: 'Consult a healthcare professional if cough persists beyond 3 weeks, you develop high fever, or breathing becomes difficult.',
  },
  {
    id: 5,
    name: 'Sinusitis',
    description: 'Inflammation of the sinuses, often following a cold. Can be viral or bacterial. Causes facial pressure and congestion.',
    category: 'Respiratory',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 73, weight: 3 }, // Sinus pressure
      { symptomId: 29, weight: 3 }, // Nasal congestion
      { symptomId: 28, weight: 2 }, // Runny nose
      { symptomId: 11, weight: 2 }, // Headache
      { symptomId: 1, weight: 1 },  // Fever
      { symptomId: 3, weight: 1 },  // Fatigue
    ],
    commonSigns: ['Sinus pressure and pain', 'Nasal congestion', 'Thick nasal discharge', 'Headache', 'Facial tenderness'],
    selfCareAdvice: [
      'Use saline nasal sprays or rinses.',
      'Apply warm compresses to the face.',
      'Stay hydrated to thin mucus.',
      'Use a humidifier.',
      'Sleep with head elevated.',
    ],
    warningSigns: [
      'Symptoms lasting more than 10 days.',
      'Severe headache or facial pain.',
      'Vision changes or eye swelling.',
      'Stiff neck or high fever.',
    ],
    medicalAttentionAdvice: 'See a doctor if sinusitis symptoms persist more than 10 days, are severe, or if you develop vision problems.',
  },
  {
    id: 6,
    name: 'Pharyngitis (Sore Throat)',
    description: 'Inflammation of the pharynx (throat), usually caused by a viral infection. Can also be bacterial (strep throat).',
    category: 'Respiratory',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 31, weight: 3 }, // Sore throat
      { symptomId: 74, weight: 2 }, // Difficulty swallowing
      { symptomId: 75, weight: 2 }, // Swollen lymph nodes
      { symptomId: 1, weight: 2 },  // Fever
      { symptomId: 11, weight: 1 }, // Headache
      { symptomId: 3, weight: 1 },  // Fatigue
    ],
    commonSigns: ['Sore throat', 'Difficulty swallowing', 'Swollen lymph nodes', 'Fever', 'Redness in throat'],
    selfCareAdvice: [
      'Gargle with warm salt water several times a day.',
      'Drink warm fluids like herbal tea with honey.',
      'Rest your voice.',
      'Use throat lozenges for comfort.',
      'Stay hydrated.',
    ],
    warningSigns: [
      'Severe difficulty breathing or swallowing.',
      'Inability to open mouth widely.',
      'Rash appearing with sore throat.',
      'High fever persisting beyond 3 days.',
    ],
    medicalAttentionAdvice: 'See a doctor if throat pain is severe, you cannot swallow, have a high fever, or symptoms persist beyond 1 week.',
  },
  {
    id: 7,
    name: 'Pneumonia-like Symptom Pattern',
    description: 'A pattern of symptoms that may overlap with pneumonia. Pneumonia is a serious lung infection requiring prompt medical evaluation.',
    category: 'Respiratory',
    severityLevel: 'HIGH',
    requiresImmediateAttention: true,
    symptomLinks: [
      { symptomId: 1, weight: 3 },  // Fever
      { symptomId: 25, weight: 3 }, // Wet cough
      { symptomId: 26, weight: 3 }, // Shortness of breath
      { symptomId: 32, weight: 3 }, // Chest discomfort
      { symptomId: 3, weight: 2 },  // Fatigue
      { symptomId: 2, weight: 2 },  // Chills
      { symptomId: 37, weight: 1 }, // Nausea
    ],
    commonSigns: ['Productive cough', 'Fever with chills', 'Shortness of breath', 'Chest pain when breathing', 'Fatigue'],
    selfCareAdvice: [
      'IMPORTANT: Seek medical evaluation promptly.',
      'Rest and maintain hydration.',
      'Take all prescribed medications as directed.',
      'Monitor breathing rate.',
    ],
    warningSigns: [
      'Difficulty breathing at rest.',
      'Bluish lips or fingertips.',
      'Confusion or altered consciousness.',
      'Inability to keep fluids down.',
    ],
    medicalAttentionAdvice: 'These symptoms require professional medical evaluation. Please consult a doctor or visit an urgent care facility promptly.',
  },
  {
    id: 8,
    name: 'Gastroenteritis (Stomach Flu)',
    description: 'Inflammation of the stomach and intestines, usually caused by a viral or bacterial infection.',
    category: 'Digestive',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 37, weight: 3 }, // Nausea
      { symptomId: 38, weight: 3 }, // Vomiting
      { symptomId: 39, weight: 3 }, // Diarrhea
      { symptomId: 41, weight: 2 }, // Abdominal pain
      { symptomId: 1, weight: 2 },  // Fever
      { symptomId: 2, weight: 1 },  // Chills
      { symptomId: 50, weight: 1 }, // Body ache
      { symptomId: 10, weight: 2 }, // Loss of appetite
    ],
    commonSigns: ['Nausea', 'Vomiting', 'Diarrhea', 'Abdominal cramps', 'Low-grade fever'],
    selfCareAdvice: [
      'Replace lost fluids with water, clear broth, or oral rehydration solution.',
      'Rest your stomach — avoid solid foods for a few hours if vomiting.',
      'Gradually reintroduce bland foods (rice, toast, banana).',
      'Wash hands thoroughly to prevent spreading.',
      'Avoid dairy, fatty, or spicy foods until recovered.',
    ],
    warningSigns: [
      'Signs of severe dehydration (extreme thirst, no urination, dizziness).',
      'Blood in vomit or stool.',
      'Severe abdominal pain.',
      'High fever above 102°F.',
      'Symptoms lasting more than 3 days.',
    ],
    medicalAttentionAdvice: 'Seek medical attention if you show signs of severe dehydration, have blood in stool or vomit, or symptoms persist beyond 3 days.',
  },
  {
    id: 9,
    name: 'Food Poisoning',
    description: 'Illness caused by eating contaminated food. Symptoms usually appear within hours of eating contaminated food.',
    category: 'Digestive',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 37, weight: 3 }, // Nausea
      { symptomId: 38, weight: 3 }, // Vomiting
      { symptomId: 39, weight: 2 }, // Diarrhea
      { symptomId: 41, weight: 3 }, // Abdominal pain
      { symptomId: 1, weight: 1 },  // Fever
      { symptomId: 50, weight: 1 }, // Body ache
    ],
    commonSigns: ['Sudden nausea and vomiting', 'Diarrhea', 'Stomach cramps', 'Symptoms within hours of eating'],
    selfCareAdvice: [
      'Replace fluids lost through vomiting and diarrhea.',
      'Avoid solid foods until vomiting stops.',
      'Avoid caffeine, alcohol, and dairy.',
      'Rest and monitor symptoms.',
      'Identify the food source if possible.',
    ],
    warningSigns: [
      'Symptoms of dehydration.',
      'Blood in stool.',
      'Neurological symptoms (blurred vision, weakness).',
      'Symptoms in pregnant women, elderly, or immunocompromised individuals.',
    ],
    medicalAttentionAdvice: 'Seek medical attention if symptoms are severe, last more than 3 days, or if you have neurological symptoms.',
  },
  {
    id: 10,
    name: 'Gastritis',
    description: 'Inflammation of the stomach lining. Can be acute or chronic, often triggered by diet, stress, or medication.',
    category: 'Digestive',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 41, weight: 3 }, // Abdominal pain
      { symptomId: 37, weight: 2 }, // Nausea
      { symptomId: 43, weight: 2 }, // Heartburn
      { symptomId: 42, weight: 2 }, // Bloating
      { symptomId: 10, weight: 1 }, // Loss of appetite
      { symptomId: 38, weight: 1 }, // Vomiting
    ],
    commonSigns: ['Upper abdominal pain or burning', 'Nausea', 'Bloating', 'Loss of appetite', 'Heartburn'],
    selfCareAdvice: [
      'Eat smaller, more frequent meals.',
      'Avoid spicy, acidic, and fatty foods.',
      'Avoid alcohol and caffeine.',
      'Do not take NSAIDs (ibuprofen) on an empty stomach.',
      'Manage stress through relaxation techniques.',
    ],
    warningSigns: [
      'Black or tarry stools (possible bleeding).',
      'Vomiting blood.',
      'Severe abdominal pain.',
      'Unintentional weight loss.',
    ],
    medicalAttentionAdvice: 'See a doctor if symptoms persist despite dietary changes, or if you notice blood in vomit or stool.',
  },
  {
    id: 11,
    name: 'Acid Reflux / GERD',
    description: 'A condition where stomach acid flows back into the esophagus. Very common and usually manageable with lifestyle changes.',
    category: 'Digestive',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 43, weight: 3 }, // Heartburn
      { symptomId: 44, weight: 3 }, // Acid reflux
      { symptomId: 31, weight: 2 }, // Sore throat (from acid)
      { symptomId: 23, weight: 1 }, // Chronic cough
      { symptomId: 37, weight: 1 }, // Nausea
      { symptomId: 42, weight: 1 }, // Bloating
    ],
    commonSigns: ['Heartburn (burning sensation)', 'Acid taste in mouth', 'Belching', 'Difficulty swallowing', 'Chronic cough'],
    selfCareAdvice: [
      'Avoid trigger foods: spicy, fatty, citrus, chocolate, coffee.',
      'Eat smaller meals and avoid lying down right after eating.',
      'Elevate the head of your bed.',
      'Maintain a healthy weight.',
      'Avoid tight-fitting clothing around the abdomen.',
    ],
    warningSigns: [
      'Difficulty or pain swallowing.',
      'Vomiting blood.',
      'Unexplained weight loss.',
      'Chest pain (rule out heart issues first).',
    ],
    medicalAttentionAdvice: 'See a doctor if heartburn occurs more than twice a week or does not improve with lifestyle changes.',
  },
  {
    id: 12,
    name: 'Irritable Bowel Syndrome (IBS)',
    description: 'A functional disorder affecting the large intestine. Causes chronic symptoms that fluctuate over time.',
    category: 'Digestive',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 41, weight: 3 }, // Abdominal pain
      { symptomId: 42, weight: 3 }, // Bloating
      { symptomId: 39, weight: 2 }, // Diarrhea
      { symptomId: 40, weight: 2 }, // Constipation
      { symptomId: 37, weight: 1 }, // Nausea
    ],
    commonSigns: ['Abdominal cramping', 'Bloating', 'Alternating diarrhea/constipation', 'Mucus in stool', 'Relief after bowel movement'],
    selfCareAdvice: [
      'Identify and avoid trigger foods.',
      'Eat regular meals and avoid large portions.',
      'Increase fiber intake gradually.',
      'Stay well hydrated.',
      'Manage stress through exercise and relaxation.',
    ],
    warningSigns: [
      'Blood in stool.',
      'Unintentional weight loss.',
      'Symptoms waking you from sleep.',
      'New symptoms after age 50.',
    ],
    medicalAttentionAdvice: 'See a doctor for diagnosis and to rule out other conditions. Keep a food and symptom diary.',
  },
  {
    id: 13,
    name: 'Migraine',
    description: 'A neurological condition causing intense, recurring headaches, often with nausea and light sensitivity.',
    category: 'Neurological',
    severityLevel: 'MODERATE',
    symptomLinks: [
      { symptomId: 12, weight: 3 }, // Migraine-like headache
      { symptomId: 19, weight: 3 }, // Light sensitivity
      { symptomId: 20, weight: 3 }, // Sound sensitivity
      { symptomId: 37, weight: 2 }, // Nausea
      { symptomId: 38, weight: 1 }, // Vomiting
      { symptomId: 6, weight: 1 },  // Dizziness
      { symptomId: 70, weight: 1 }, // Light sensitivity (eye)
    ],
    commonSigns: ['Throbbing one-sided headache', 'Nausea', 'Light sensitivity', 'Sound sensitivity', 'Visual disturbances (aura)'],
    selfCareAdvice: [
      'Rest in a quiet, dark room.',
      'Apply cold or warm compress to head.',
      'Stay hydrated.',
      'Avoid known triggers (stress, certain foods, disrupted sleep).',
      'Keep a migraine diary to identify patterns.',
    ],
    warningSigns: [
      'Sudden, severe "thunderclap" headache.',
      'Headache with fever and stiff neck.',
      'Weakness or numbness on one side of body.',
      'Vision loss or speech difficulties.',
      'Headache after head injury.',
    ],
    medicalAttentionAdvice: 'See a doctor for a migraine management plan. Seek emergency care for sudden severe headache unlike any before.',
  },
  {
    id: 14,
    name: 'Tension Headache',
    description: 'The most common type of headache, often described as a tight band around the head. Usually related to stress, posture, or eye strain.',
    category: 'Neurological',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 11, weight: 3 }, // Headache
      { symptomId: 49, weight: 2 }, // Neck pain
      { symptomId: 3, weight: 1 },  // Fatigue
      { symptomId: 14, weight: 1 }, // Memory difficulty
    ],
    commonSigns: ['Dull pressing head pain', 'Neck and shoulder tension', 'Bilateral headache', 'Worse with stress or screen use'],
    selfCareAdvice: [
      'Take regular breaks from screens.',
      'Practice relaxation techniques and stress management.',
      'Ensure correct posture when sitting.',
      'Stay hydrated.',
      'Gentle neck stretches may help.',
      'Ensure adequate sleep.',
    ],
    warningSigns: [
      'Headache with sudden onset (worst headache ever).',
      'Headache with fever and stiff neck.',
      'Headache after head trauma.',
      'Progressive worsening over days.',
    ],
    medicalAttentionAdvice: 'See a doctor if headaches are frequent, severe, or interfering with daily life.',
  },
  {
    id: 15,
    name: 'Allergic Rhinitis',
    description: 'An allergic response causing sneezing, itching, and a runny nose. Often called hay fever when triggered by pollen.',
    category: 'Allergic',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 30, weight: 3 }, // Sneezing
      { symptomId: 28, weight: 3 }, // Runny nose
      { symptomId: 54, weight: 2 }, // Itching
      { symptomId: 29, weight: 2 }, // Nasal congestion
      { symptomId: 69, weight: 2 }, // Watery eyes
      { symptomId: 66, weight: 1 }, // Eye redness
      { symptomId: 23, weight: 1 }, // Cough
    ],
    commonSigns: ['Frequent sneezing', 'Itchy nose/eyes/throat', 'Runny nose', 'Watery eyes', 'Nasal congestion'],
    selfCareAdvice: [
      'Identify and avoid allergen triggers.',
      'Keep windows closed during high pollen season.',
      'Use air purifiers.',
      'Shower after outdoor exposure.',
      'Rinse nasal passages with saline.',
    ],
    warningSigns: [
      'Symptoms severely affecting daily functioning.',
      'Development of asthma-like symptoms.',
      'Severe allergic reaction (difficulty breathing, throat swelling).',
    ],
    medicalAttentionAdvice: 'See a doctor for allergy testing and treatment options if symptoms significantly impact your quality of life.',
  },
  {
    id: 16,
    name: 'Urinary Tract Infection (UTI)',
    description: 'A bacterial infection affecting any part of the urinary system. More common in women but can affect anyone.',
    category: 'Urinary',
    severityLevel: 'MODERATE',
    symptomLinks: [
      { symptomId: 62, weight: 3 }, // Painful urination
      { symptomId: 61, weight: 3 }, // Frequent urination
      { symptomId: 64, weight: 2 }, // Lower abdominal discomfort
      { symptomId: 65, weight: 2 }, // Foul-smelling urine
      { symptomId: 1, weight: 1 },  // Fever
      { symptomId: 63, weight: 1 }, // Blood in urine
    ],
    commonSigns: ['Burning sensation when urinating', 'Frequent urge to urinate', 'Cloudy or strong-smelling urine', 'Pelvic discomfort', 'Low-grade fever'],
    selfCareAdvice: [
      'Drink plenty of water to help flush bacteria.',
      'Urinate frequently — do not hold urine.',
      'Avoid irritants: caffeine, alcohol, spicy foods.',
      'Seek medical evaluation for antibiotic treatment.',
    ],
    warningSigns: [
      'High fever above 101°F with chills (may indicate kidney infection).',
      'Back or side pain below the ribs.',
      'Blood in urine.',
      'Vomiting.',
      'Symptoms not improving within 1-2 days of treatment.',
    ],
    medicalAttentionAdvice: 'UTIs typically require antibiotic treatment. Please consult a healthcare professional for proper diagnosis and treatment.',
  },
  {
    id: 17,
    name: 'Dehydration',
    description: 'Occurs when you lose more fluids than you take in. Can range from mild to severe.',
    category: 'General',
    severityLevel: 'MODERATE',
    symptomLinks: [
      { symptomId: 6, weight: 3 },  // Dizziness
      { symptomId: 3, weight: 2 },  // Fatigue
      { symptomId: 11, weight: 2 }, // Headache
      { symptomId: 4, weight: 2 },  // Weakness
      { symptomId: 52, weight: 1 }, // Muscle cramps
      { symptomId: 61, weight: 1 }, // Decreased urination
    ],
    commonSigns: ['Thirst', 'Dark-colored urine', 'Fatigue', 'Dizziness', 'Headache', 'Decreased urination', 'Dry mouth'],
    selfCareAdvice: [
      'Drink water and electrolyte-containing fluids immediately.',
      'Oral rehydration solution (ORS) is very effective.',
      'Avoid caffeine and alcohol.',
      'Rest in a cool environment.',
      'Monitor urine color (pale yellow = well hydrated).',
    ],
    warningSigns: [
      'No urination for 8+ hours.',
      'Extreme thirst, confusion, or rapid heartbeat.',
      'Fainting or severe dizziness.',
      'Inability to keep fluids down.',
    ],
    medicalAttentionAdvice: 'Seek medical attention for severe dehydration, especially in children, elderly, or if unable to keep fluids down.',
  },
  {
    id: 18,
    name: 'Muscle Strain',
    description: 'Overstretching or tearing of muscle fibers, usually from physical activity or poor posture.',
    category: 'Musculoskeletal',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 46, weight: 3 }, // Muscle pain
      { symptomId: 50, weight: 2 }, // Body ache
      { symptomId: 57, weight: 2 }, // Swelling
      { symptomId: 51, weight: 2 }, // Muscle weakness
    ],
    commonSigns: ['Localized muscle pain', 'Stiffness', 'Swelling in affected area', 'Limited range of motion', 'Weakness'],
    selfCareAdvice: [
      'Rest the affected muscle.',
      'Apply ice for the first 48-72 hours (20 min on, 20 min off).',
      'Use compression bandage if available.',
      'Elevate the affected area if possible.',
      'After 72 hours, gentle heat may help.',
    ],
    warningSigns: [
      'Severe pain that does not improve with rest.',
      'Significant swelling, bruising, or deformity.',
      'Inability to bear weight or move the affected area.',
      'Numbness or tingling.',
    ],
    medicalAttentionAdvice: 'See a doctor if pain is severe, swelling significant, or if the injury does not improve after a few days of rest.',
  },
  {
    id: 19,
    name: 'Iron Deficiency-like Symptom Pattern',
    description: 'A pattern of symptoms commonly associated with low iron levels. Must be confirmed with blood tests.',
    category: 'General',
    severityLevel: 'MODERATE',
    symptomLinks: [
      { symptomId: 3, weight: 3 },  // Fatigue
      { symptomId: 4, weight: 3 },  // Weakness
      { symptomId: 6, weight: 2 },  // Dizziness
      { symptomId: 26, weight: 2 }, // Shortness of breath on exertion
      { symptomId: 11, weight: 1 }, // Headache
      { symptomId: 10, weight: 1 }, // Loss of appetite
    ],
    commonSigns: ['Persistent fatigue and weakness', 'Pale skin', 'Shortness of breath on mild exertion', 'Dizziness', 'Cold hands and feet'],
    selfCareAdvice: [
      'Include iron-rich foods: red meat, beans, lentils, dark leafy vegetables, fortified cereals.',
      'Pair iron-rich foods with vitamin C to improve absorption.',
      'Avoid tea/coffee with meals (reduces iron absorption).',
      'Get a blood test for confirmation.',
    ],
    warningSigns: [
      'Extreme fatigue interfering with daily activities.',
      'Chest pain or shortness of breath.',
      'Irregular heartbeat.',
      'Unusual cravings (ice, dirt — called pica).',
    ],
    medicalAttentionAdvice: 'Get a blood test to confirm iron levels. Iron supplementation should be recommended by a healthcare professional.',
  },
  {
    id: 20,
    name: 'Vitamin Deficiency-like Symptom Pattern',
    description: 'A pattern of symptoms associated with vitamin deficiencies. Common in students and those with limited sun exposure or poor diet.',
    category: 'General',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 3, weight: 2 },  // Fatigue
      { symptomId: 50, weight: 2 }, // Body ache
      { symptomId: 46, weight: 2 }, // Muscle pain
      { symptomId: 14, weight: 1 }, // Memory difficulty
      { symptomId: 5, weight: 1 },  // Malaise
      { symptomId: 47, weight: 1 }, // Joint pain
    ],
    commonSigns: ['Fatigue and tiredness', 'Bone/muscle aches', 'Brain fog', 'Mood changes', 'Frequent illness'],
    selfCareAdvice: [
      'Spend time outdoors for natural vitamin D (sun exposure).',
      'Eat vitamin D-rich foods: fatty fish, eggs, fortified milk.',
      'Eat B12-rich foods: meat, dairy, eggs.',
      'Get blood tests to identify specific deficiencies.',
    ],
    warningSigns: [
      'Symptoms severely affecting daily function.',
      'Severe bone pain.',
      'Numbness or tingling in extremities.',
    ],
    medicalAttentionAdvice: 'Get blood tests to identify specific vitamin deficiencies. Supplementation should be guided by a healthcare professional.',
  },
  {
    id: 21,
    name: 'Dermatitis / Eczema',
    description: 'Inflammation of the skin causing itchy, red, dry patches. Can be triggered by allergens, irritants, or stress.',
    category: 'Skin',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 54, weight: 3 }, // Itching
      { symptomId: 56, weight: 3 }, // Dry skin
      { symptomId: 55, weight: 2 }, // Redness
      { symptomId: 53, weight: 2 }, // Rash
      { symptomId: 57, weight: 1 }, // Swelling
    ],
    commonSigns: ['Dry itchy skin', 'Red or brownish patches', 'Small raised bumps', 'Thickened or cracked skin', 'Worse with scratching'],
    selfCareAdvice: [
      'Moisturize affected areas frequently with gentle lotions.',
      'Avoid known triggers (soaps, detergents, certain fabrics).',
      'Use gentle, fragrance-free skincare products.',
      'Avoid scratching to prevent infection.',
      'Cool compresses may relieve itching.',
    ],
    warningSigns: [
      'Signs of skin infection (warmth, pus, increasing redness).',
      'Rash spreading rapidly.',
      'Fever accompanying skin symptoms.',
      'Severe itching disrupting sleep.',
    ],
    medicalAttentionAdvice: 'See a dermatologist if skin symptoms are severe, persistent, or infected.',
  },
  {
    id: 22,
    name: 'Urticaria (Hives)',
    description: 'An itchy skin reaction causing welts (hives). Can be triggered by allergens, stress, infections, or medications.',
    category: 'Skin',
    severityLevel: 'MODERATE',
    symptomLinks: [
      { symptomId: 58, weight: 3 }, // Hives
      { symptomId: 54, weight: 3 }, // Itching
      { symptomId: 53, weight: 2 }, // Rash
      { symptomId: 55, weight: 2 }, // Redness
      { symptomId: 57, weight: 1 }, // Swelling
    ],
    commonSigns: ['Raised itchy welts (hives)', 'Red or skin-colored bumps', 'Swelling', 'Welts that change shape/location', 'Intense itching'],
    selfCareAdvice: [
      'Identify and avoid the trigger.',
      'Apply cool compresses to affected areas.',
      'Avoid tight clothing over affected areas.',
      'Avoid hot showers.',
      'Keep the area cool.',
    ],
    warningSigns: [
      'Difficulty breathing or swallowing.',
      'Swelling of face, lips, or throat.',
      'Dizziness or fainting.',
      'Rapid heartbeat.',
    ],
    medicalAttentionAdvice: 'Seek emergency care immediately if you have difficulty breathing or throat swelling (signs of anaphylaxis). See a doctor for persistent or recurring hives.',
  },
  {
    id: 23,
    name: 'Conjunctivitis (Pink Eye)',
    description: 'Inflammation of the conjunctiva. Can be viral, bacterial, or allergic. Very common and usually mild.',
    category: 'Eye',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 66, weight: 3 }, // Eye redness
      { symptomId: 69, weight: 3 }, // Watery eyes
      { symptomId: 67, weight: 2 }, // Eye pain
      { symptomId: 54, weight: 2 }, // Itching
      { symptomId: 70, weight: 1 }, // Light sensitivity
    ],
    commonSigns: ['Eye redness', 'Watery or sticky discharge', 'Itching', 'Gritty feeling in eye', 'Swollen eyelids'],
    selfCareAdvice: [
      'Clean eyes gently with clean, damp cloth.',
      'Do not share towels or pillowcases.',
      'Avoid touching eyes.',
      'Remove contact lenses until cleared.',
      'Wash hands frequently.',
    ],
    warningSigns: [
      'Severe eye pain.',
      'Vision changes.',
      'Severe sensitivity to light.',
      'Symptoms not improving after 1 week.',
    ],
    medicalAttentionAdvice: 'See a doctor if symptoms are severe, vision is affected, or symptoms do not improve within a week.',
  },
  {
    id: 24,
    name: 'Ear Infection Pattern',
    description: 'Ear infections involve inflammation of the ear, often following a cold or respiratory infection.',
    category: 'ENT',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 71, weight: 3 }, // Ear pain
      { symptomId: 72, weight: 2 }, // Hearing difficulty
      { symptomId: 1, weight: 2 },  // Fever
      { symptomId: 29, weight: 1 }, // Nasal congestion
      { symptomId: 6, weight: 1 },  // Dizziness
    ],
    commonSigns: ['Ear pain', 'Muffled hearing', 'Feeling of fullness in ear', 'Fever', 'Possibly discharge from ear'],
    selfCareAdvice: [
      'Warm compress on the ear may help relieve pain.',
      'Stay hydrated.',
      'Keep ear dry.',
      'Seek medical evaluation for antibiotic treatment if bacterial.',
    ],
    warningSigns: [
      'Discharge or fluid from the ear.',
      'Severe hearing loss.',
      'High fever.',
      'Dizziness or balance problems.',
      'Severe pain.',
    ],
    medicalAttentionAdvice: 'See a doctor for ear infection diagnosis and treatment. Do not insert anything into the ear canal.',
  },
  {
    id: 25,
    name: 'Minor Joint Strain / Sprain',
    description: 'An injury to a ligament or joint, often from physical activity. Usually resolves with rest and care.',
    category: 'Musculoskeletal',
    severityLevel: 'LOW',
    symptomLinks: [
      { symptomId: 47, weight: 3 }, // Joint pain
      { symptomId: 57, weight: 2 }, // Swelling
      { symptomId: 55, weight: 1 }, // Redness
      { symptomId: 51, weight: 1 }, // Weakness
    ],
    commonSigns: ['Joint pain', 'Swelling', 'Limited movement', 'Tenderness to touch', 'Bruising'],
    selfCareAdvice: [
      'RICE: Rest, Ice, Compression, Elevation.',
      'Apply ice for 20 minutes several times a day.',
      'Avoid activities that cause pain.',
      'After 48-72 hours, gentle movement may help.',
    ],
    warningSigns: [
      'Joint appears deformed.',
      'Inability to bear weight.',
      'Numbness around the joint.',
      'Severe pain.',
      'Significant swelling.',
    ],
    medicalAttentionAdvice: 'See a doctor if unable to bear weight, the joint appears deformed, or pain is severe. X-ray may be needed.',
  },
];

export const HEALTH_TIPS: HealthTip[] = [
  { id: 1, category: 'HYDRATION', title: 'Stay Hydrated', content: 'Remember to drink enough fluids throughout the day, especially during hot weather or physical activity. Aim for 8 glasses (2 liters) of water daily.', icon: '💧' },
  { id: 2, category: 'SLEEP', title: 'Quality Sleep', content: 'Adults need 7-9 hours of sleep per night. Good sleep improves memory, mood, immune function, and overall health. Maintain a consistent sleep schedule.', icon: '🌙' },
  { id: 3, category: 'NUTRITION', title: 'Balanced Diet', content: 'Include a variety of fruits, vegetables, whole grains, and lean proteins in your diet. Colorful plates are usually nutritious plates!', icon: '🥗' },
  { id: 4, category: 'EXERCISE', title: 'Stay Active', content: 'Aim for at least 30 minutes of moderate physical activity most days. Even short walks count! Physical activity boosts mood and health.', icon: '🏃' },
  { id: 5, category: 'STRESS', title: 'Manage Stress', content: 'Chronic stress can affect physical health. Try deep breathing, meditation, or mindfulness for 10 minutes daily to reduce stress.', icon: '🧘' },
  { id: 6, category: 'HYGIENE', title: 'Hand Hygiene', content: 'Wash hands for at least 20 seconds with soap and water, especially before eating and after using the bathroom. It is the best defense against many infections.', icon: '🧼' },
  { id: 7, category: 'SEASONAL', title: 'Seasonal Health', content: 'During cold and flu season, boost your immunity with adequate sleep, nutrition, and hand hygiene. Stay warm and avoid touching your face in public.', icon: '🌡️' },
  { id: 8, category: 'STUDY', title: 'Study Health', content: 'Take a 5-minute break every 25-30 minutes while studying. The Pomodoro technique helps maintain focus and reduces mental fatigue.', icon: '📚' },
  { id: 9, category: 'EYECARE', title: 'Eye Care', content: 'Follow the 20-20-20 rule: Every 20 minutes, look at something 20 feet away for 20 seconds to reduce eye strain from screen use.', icon: '👁️' },
  { id: 10, category: 'HYDRATION', title: 'Mindful Caffeine', content: 'Moderate caffeine (1-2 cups of tea/coffee) can be fine, but excessive amounts may cause anxiety, sleep disruption, or dehydration.', icon: '☕' },
  { id: 11, category: 'NUTRITION', title: 'Eat More Greens', content: 'Leafy green vegetables are rich in iron, calcium, vitamins, and fiber. Try to include spinach, kale, or any local greens in your daily meals.', icon: '🌿' },
  { id: 12, category: 'SLEEP', title: 'Limit Screen Time', content: 'Avoid screens for at least 1 hour before bedtime. Blue light from phones disrupts melatonin production and delays sleep.', icon: '📱' },
  { id: 13, category: 'EXERCISE', title: 'Move More Daily', content: 'Simple habit changes like taking the stairs, walking during phone calls, or doing stretches while studying add up to meaningful physical activity.', icon: '🧗' },
  { id: 14, category: 'STRESS', title: 'Deep Breathing', content: 'Try box breathing: inhale for 4 counts, hold for 4, exhale for 4, hold for 4. Repeat 5 times when feeling stressed or anxious.', icon: '🫁' },
  { id: 15, category: 'EYECARE', title: 'Protect Your Eyes', content: 'Wear sunglasses outdoors to protect eyes from UV radiation. Good quality sunglasses reduce the risk of cataracts and other eye conditions.', icon: '☀️' },
  { id: 16, category: 'NUTRITION', title: 'Breakfast Matters', content: 'Eating a nutritious breakfast kickstarts your metabolism, improves concentration, and gives you energy to start the day well.', icon: '🍳' },
  { id: 17, category: 'HYGIENE', title: 'Oral Hygiene', content: 'Brush your teeth twice daily and floss once a day. Good oral hygiene is linked to better overall health, including heart health.', icon: '🦷' },
  { id: 18, category: 'SLEEP', title: 'Consistent Schedule', content: 'Going to bed and waking up at the same time every day helps regulate your internal clock, making it easier to fall asleep and wake up refreshed.', icon: '⏰' },
  { id: 19, category: 'STRESS', title: 'Connect Socially', content: 'Maintaining social connections is important for mental health. Even a short conversation with a friend or family member can boost your mood.', icon: '👫' },
  { id: 20, category: 'EXERCISE', title: 'Stretch Daily', content: 'Spending just 5-10 minutes stretching daily improves flexibility, reduces muscle tension, and can help prevent injuries.', icon: '🤸' },
];

export const HEALTH_FACTS = [
  'The human body contains about 37 trillion cells.',
  'Your heart beats approximately 100,000 times per day.',
  'The average adult brain weighs about 1.4 kg (3 pounds).',
  'Humans shed about 30,000-40,000 dead skin cells every hour.',
  'The small intestine is about 6 meters (20 feet) long.',
  'Your lungs can hold about 6 liters of air at maximum capacity.',
  'It takes about 12 hours for food to fully digest in the stomach.',
  'The human eye can distinguish about 10 million different colors.',
  'Adults have 206 bones; babies are born with about 270.',
  'The largest organ in the human body is the skin.',
  'Yawning helps cool the brain and increase alertness.',
  'Laughing lowers stress hormones and strengthens the immune system.',
];

export const CATEGORIES: SymptomCategory[] = [
  'General',
  'Neurological',
  'Respiratory',
  'Digestive',
  'Musculoskeletal',
  'Skin',
  'Urinary',
  'Eye',
  'ENT',
];

export function getSymptomById(id: number): Symptom | undefined {
  return SYMPTOMS.find((s) => s.id === id);
}

export function getSymptomByName(name: string): Symptom | undefined {
  return SYMPTOMS.find((s) => s.name.toLowerCase() === name.toLowerCase());
}

export function getDiseaseById(id: number): Disease | undefined {
  return DISEASES.find((d) => d.id === id);
}

export function searchSymptoms(query: string, category?: string): Symptom[] {
  let list = SYMPTOMS;
  if (category && category !== 'All') {
    list = list.filter((s) => s.category.toLowerCase() === category.toLowerCase());
  }
  if (!query.trim()) return list;
  const q = query.toLowerCase().trim();
  return list.filter(
    (s) => s.name.toLowerCase().includes(q) || s.category.toLowerCase().includes(q)
  );
}

export function getDailyTip(): HealthTip {
  const dayOfYear = Math.floor(
    (Date.now() - new Date(new Date().getFullYear(), 0, 0).getTime()) / 1000 / 60 / 60 / 24
  );
  return HEALTH_TIPS[dayOfYear % HEALTH_TIPS.length];
}

export function getTodaysFact(): string {
  const dayOfYear = Math.floor(
    (Date.now() - new Date(new Date().getFullYear(), 0, 0).getTime()) / 1000 / 60 / 60 / 24
  );
  return HEALTH_FACTS[dayOfYear % HEALTH_FACTS.length];
}
