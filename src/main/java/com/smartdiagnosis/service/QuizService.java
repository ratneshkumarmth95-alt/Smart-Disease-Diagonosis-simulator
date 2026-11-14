package com.smartdiagnosis.service;

import com.smartdiagnosis.model.*;
import java.util.*;

/**
 * QuizService - Provides health knowledge quiz questions.
 * Demonstrates: Collections, encapsulation, educational content
 */
public class QuizService {

    private final List<QuizQuestion> questions;

    public QuizService() {
        this.questions = new ArrayList<>();
        initializeQuestions();
    }

    private void initializeQuestions() {
        questions.add(new QuizQuestion(1,
            "How many glasses of water should a healthy adult drink per day?",
            Arrays.asList("2-3 glasses", "4-5 glasses", "8-10 glasses", "15+ glasses"),
            2,
            "Most adults need about 8 glasses (approximately 2 liters) of water daily, though individual needs vary based on activity, climate, and health."
        ));
        questions.add(new QuizQuestion(2,
            "What does the 20-20-20 rule help prevent?",
            Arrays.asList("Dehydration", "Eye strain from screens", "Headaches from stress", "Back pain"),
            1,
            "The 20-20-20 rule: every 20 minutes, look at something 20 feet away for 20 seconds. This reduces eye strain caused by prolonged screen use."
        ));
        questions.add(new QuizQuestion(3,
            "How long should you wash your hands with soap?",
            Arrays.asList("5 seconds", "10 seconds", "20 seconds", "60 seconds"),
            2,
            "Health organizations recommend washing hands with soap for at least 20 seconds — roughly the time it takes to sing 'Happy Birthday' twice."
        ));
        questions.add(new QuizQuestion(4,
            "How many hours of sleep do most adults need per night?",
            Arrays.asList("4-5 hours", "6-7 hours", "7-9 hours", "10-12 hours"),
            2,
            "The CDC and sleep experts recommend 7-9 hours of sleep per night for adults aged 18-64 to maintain good physical and mental health."
        ));
        questions.add(new QuizQuestion(5,
            "What is a common sign of dehydration?",
            Arrays.asList("Bright clear urine", "Dark yellow urine", "Increased energy", "Decreased thirst"),
            1,
            "Dark-colored urine is a classic sign of dehydration. Well-hydrated urine should be pale yellow. Very dark urine indicates you need to drink more fluids."
        ));
        questions.add(new QuizQuestion(6,
            "Which vitamin does sunlight help the body produce?",
            Arrays.asList("Vitamin A", "Vitamin B12", "Vitamin C", "Vitamin D"),
            3,
            "Sunlight triggers Vitamin D production in the skin. Vitamin D is important for bone health, immunity, and mood regulation."
        ));
        questions.add(new QuizQuestion(7,
            "How many minutes of moderate exercise per day is recommended?",
            Arrays.asList("5-10 minutes", "15-20 minutes", "30+ minutes", "60+ minutes"),
            2,
            "Health guidelines recommend at least 30 minutes of moderate physical activity on most days of the week for good health."
        ));
        questions.add(new QuizQuestion(8,
            "What food type is highest in natural iron?",
            Arrays.asList("White rice", "Dark leafy greens and lean red meat", "White bread", "Soft drinks"),
            1,
            "Dark leafy vegetables (spinach, kale) and lean red meat are excellent sources of iron. Pairing them with Vitamin C improves absorption."
        ));
        questions.add(new QuizQuestion(9,
            "Which symptom combination is most associated with influenza?",
            Arrays.asList("Runny nose only", "Sudden fever, body aches, and fatigue", "Heartburn and bloating", "Eye redness only"),
            1,
            "Influenza typically presents with sudden-onset fever, body aches, and fatigue — often more severe than a common cold."
        ));
        questions.add(new QuizQuestion(10,
            "What does RICE stand for in sports injury first aid?",
            Arrays.asList("Run, Ignore, Cool, Eat", "Rest, Ice, Compression, Elevation", "Relax, Inject, Compress, Exercise", "Rest, Ibuprofen, Cool, Elevate"),
            1,
            "RICE = Rest, Ice, Compression, Elevation. This is the standard first-aid approach for sprains, strains, and minor musculoskeletal injuries."
        ));
        questions.add(new QuizQuestion(11,
            "Which of these is NOT a common symptom of dehydration?",
            Arrays.asList("Dizziness", "Headache", "Increased appetite for water", "Fatigue"),
            2,
            "Dehydration causes dizziness, headache, fatigue, dark urine, and dry mouth. 'Increased appetite for water' isn't a recognized symptom — the actual symptom is thirst."
        ));
        questions.add(new QuizQuestion(12,
            "What is the best position to sleep for most people?",
            Arrays.asList("Face down (prone)", "On your back or side", "Sitting upright", "Any position is equally good"),
            1,
            "Sleeping on your back or side is generally recommended. Sleeping on your side can help with snoring and acid reflux. Face-down (prone) can strain the neck and back."
        ));
    }

    public List<QuizQuestion> getAllQuestions() { return Collections.unmodifiableList(questions); }

    public List<QuizQuestion> getRandomQuestions(int count) {
        List<QuizQuestion> shuffled = new ArrayList<>(questions);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    public int getTotalCount() { return questions.size(); }
}
