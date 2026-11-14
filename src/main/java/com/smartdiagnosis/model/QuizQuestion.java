package com.smartdiagnosis.model;

import java.util.List;

/**
 * QuizQuestion - For the "Test Your Health Knowledge" quiz feature.
 * Demonstrates: Encapsulation, Collections
 */
public class QuizQuestion {
    private int id;
    private String question;
    private List<String> options;
    private int correctAnswerIndex;
    private String explanation;

    public QuizQuestion(int id, String question, List<String> options,
                        int correctAnswerIndex, String explanation) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
    }

    public int getId() { return id; }
    public String getQuestion() { return question; }
    public List<String> getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getExplanation() { return explanation; }

    public boolean isCorrect(int selected) { return selected == correctAnswerIndex; }

    public String getCorrectAnswer() {
        if (options != null && correctAnswerIndex >= 0 && correctAnswerIndex < options.size()) {
            return options.get(correctAnswerIndex);
        }
        return "";
    }
}
