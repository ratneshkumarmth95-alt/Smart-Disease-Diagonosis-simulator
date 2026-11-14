import { QuizQuestion } from '../types';

export const QUIZ_QUESTIONS: QuizQuestion[] = [
  {
    id: 1,
    question: 'How many glasses of water should a healthy adult drink per day on average?',
    options: ['2–3 glasses', '4–5 glasses', '8–10 glasses', '15+ glasses'],
    correctAnswerIndex: 2,
    explanation:
      'Most adults need roughly 8 glasses (approximately 2 liters) of fluid daily, varying based on body size, physical activity, and environmental climate.',
  },
  {
    id: 2,
    question: 'What does the ergonomic "20-20-20 rule" help prevent during study or computer use?',
    options: ['Dehydration', 'Eye strain and screen fatigue', 'Tension neck strain', 'Carpal tunnel syndrome'],
    correctAnswerIndex: 1,
    explanation:
      'The 20-20-20 rule recommends looking at an object 20 feet away for at least 20 seconds every 20 minutes to relax ocular muscles.',
  },
  {
    id: 3,
    question: 'How long should you wash your hands with soap and running water for effective hygiene?',
    options: ['5 seconds', '10 seconds', 'At least 20 seconds', '60 seconds minimum'],
    correctAnswerIndex: 2,
    explanation:
      'The WHO and CDC recommend scrubbing all hand surfaces with soap for at least 20 seconds — about the time to hum "Happy Birthday" twice.',
  },
  {
    id: 4,
    question: 'How many hours of restorative sleep do most adults require per night?',
    options: ['4–5 hours', '6–7 hours', '7–9 hours', '10–12 hours'],
    correctAnswerIndex: 2,
    explanation:
      'Clinical sleep guidelines recommend 7–9 hours for adults aged 18–64 for optimal cellular repair, cognitive consolidation, and immune defense.',
  },
  {
    id: 5,
    question: 'Which of the following is an early physical indicator of mild dehydration?',
    options: ['Clear, pale urine', 'Dark amber urine and dry lips', 'Rapid nail growth', 'Lowered core temperature'],
    correctAnswerIndex: 1,
    explanation:
      'Dark amber urine is a classic sign of concentrated waste resulting from reduced fluid intake. Well-hydrated urine is pale, straw-colored.',
  },
  {
    id: 6,
    question: 'Which essential vitamin does human skin synthesize upon exposure to natural sunlight (UVB)?',
    options: ['Vitamin A', 'Vitamin B12', 'Vitamin C', 'Vitamin D'],
    correctAnswerIndex: 3,
    explanation:
      'Sunlight initiates the cutaneous synthesis of Vitamin D3, essential for calcium metabolism, bone density, and immune modulation.',
  },
  {
    id: 7,
    question: 'How much moderate aerobic physical activity is recommended weekly for adults?',
    options: ['15 minutes total', '45 minutes total', 'At least 150 minutes (approx. 30 min/day, 5 days)', '600 minutes'],
    correctAnswerIndex: 2,
    explanation:
      'Public health guidelines advise at least 150 minutes of moderate aerobic exercise (such as brisk walking) spread across the week.',
  },
  {
    id: 8,
    question: 'Which dietary source provides significant amounts of bioavailable dietary iron?',
    options: ['White bread', 'Dark leafy greens and lean legumes/meats', 'Refined sugar', 'Carbonated sodas'],
    correctAnswerIndex: 1,
    explanation:
      'Dark leafy greens (spinach, kale), legumes, and lean meats are rich in iron. Pairing them with Vitamin C enhances intestinal absorption.',
  },
  {
    id: 9,
    question: 'Which symptom constellation is most characteristic of acute influenza compared to a mild cold?',
    options: ['Sneezing alone', 'Sudden high fever, diffuse body aches, and prostrating fatigue', 'Heartburn and bloating', 'Localized rash on fingers'],
    correctAnswerIndex: 1,
    explanation:
      'Influenza typically presents with abrupt onset, high fever, systemic myalgia (body aches), and marked exhaustion, unlike the gradual upper-airway focus of rhinitis.',
  },
  {
    id: 10,
    question: 'What does the first-aid acronym "RICE" stand for in acute joint or muscle strains?',
    options: ['Run, Ingest, Chill, Exercise', 'Rest, Ice, Compression, Elevation', 'Relax, Inject, Compress, Elevate', 'Rotate, Ice, Correct, Examine'],
    correctAnswerIndex: 1,
    explanation:
      'RICE stands for Rest, Ice, Compression, and Elevation — the clinical first-aid standard for minimizing swelling in minor sprains.',
  },
  {
    id: 11,
    question: 'Which of the following is NOT a common physical manifestation of dehydration?',
    options: ['Postural dizziness', 'Tension headache', 'Dry oral mucosa', 'Excessive saliva production'],
    correctAnswerIndex: 3,
    explanation:
      'Dehydration reduces salivary secretion rather than increasing it, resulting in xerostomia (dry mouth).',
  },
  {
    id: 12,
    question: 'Why is avoiding high-intensity blue light screens recommended 60 minutes before bedtime?',
    options: ['Screens consume too much power', 'Blue light suppresses melatonin release in the brain', 'Screens increase body temperature', 'It drains device battery'],
    correctAnswerIndex: 1,
    explanation:
      'Short-wavelength blue light stimulates intrinsically photosensitive retinal ganglion cells, suppressing pineal melatonin synthesis and delaying sleep onset.',
  },
];

export function getRandomQuestions(count = 5): QuizQuestion[] {
  const shuffled = [...QUIZ_QUESTIONS].sort(() => 0.5 - Math.random());
  return shuffled.slice(0, Math.min(count, shuffled.length));
}
