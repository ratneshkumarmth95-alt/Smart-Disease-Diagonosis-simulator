package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.QuizQuestion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * QuizUI - Interactive "Test Your Health Knowledge" trivia/quiz game.
 */
public class QuizUI extends JPanel {

    private final AppContext ctx = AppContext.getInstance();
    private static final Color PRIMARY   = DashboardUI.PRIMARY;
    private static final Color SECONDARY = DashboardUI.SECONDARY;
    private static final Color BG        = DashboardUI.BG;
    private static final Color WHITE     = DashboardUI.WHITE;
    private static final Color SUCCESS   = DashboardUI.SUCCESS;
    private static final Color DANGER    = DashboardUI.DANGER;
    private static final Color TEXT      = DashboardUI.TEXT;

    private List<QuizQuestion> currentQuiz;
    private int currentIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private ButtonGroup optionsGroup;
    private JRadioButton[] optionButtons;
    private JLabel feedbackLabel;
    private JButton nextBtn;
    private JLabel progressLabel;

    public QuizUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));
        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        JLabel title = new JLabel("\uD83C\uDFAE Test Your Health Knowledge");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(SECONDARY);
        header.add(title, BorderLayout.WEST);

        progressLabel = new JLabel("Question 1 of 5");
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        progressLabel.setForeground(PRIMARY);
        header.add(progressLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(25, 30, 25, 30)
        ));

        questionLabel = new JLabel("Question text goes here...");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        questionLabel.setForeground(SECONDARY);
        card.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBackground(WHITE);

        optionsGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton("Option " + (i + 1));
            optionButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            optionButtons[i].setBackground(WHITE);
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        card.add(optionsPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setBackground(WHITE);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        bottom.add(feedbackLabel, BorderLayout.CENTER);

        nextBtn = new JButton("Submit Answer");
        nextBtn.setBackground(PRIMARY);
        nextBtn.setForeground(WHITE);
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        nextBtn.setFocusPainted(false);
        nextBtn.setBorderPainted(false);
        nextBtn.addActionListener(e -> handleNext());
        bottom.add(nextBtn, BorderLayout.EAST);

        card.add(bottom, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);

        startNewQuiz();
    }

    private void startNewQuiz() {
        currentQuiz = ctx.quizService.getRandomQuestions(5);
        currentIndex = 0;
        score = 0;
        loadQuestion(currentIndex);
    }

    private void loadQuestion(int index) {
        if (index >= currentQuiz.size()) {
            showSummary();
            return;
        }

        QuizQuestion q = currentQuiz.get(index);
        progressLabel.setText("Question " + (index + 1) + " of " + currentQuiz.size());
        questionLabel.setText("<html>" + q.getQuestion() + "</html>");

        optionsGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setEnabled(true);
            if (i < q.getOptions().size()) {
                optionButtons[i].setText(q.getOptions().get(i));
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }
        feedbackLabel.setText(" ");
        nextBtn.setText("Submit Answer");
    }

    private void handleNext() {
        if (currentIndex >= currentQuiz.size()) {
            startNewQuiz();
            return;
        }

        QuizQuestion q = currentQuiz.get(currentIndex);

        if ("Submit Answer".equals(nextBtn.getText())) {
            int selected = -1;
            for (int i = 0; i < 4; i++) {
                if (optionButtons[i].isSelected()) {
                    selected = i;
                    break;
                }
            }

            if (selected == -1) {
                feedbackLabel.setText("Please pick an option!");
                feedbackLabel.setForeground(DANGER);
                return;
            }

            for (JRadioButton b : optionButtons) b.setEnabled(false);

            if (q.isCorrect(selected)) {
                score++;
                feedbackLabel.setText("\u2705 Correct! " + q.getExplanation());
                feedbackLabel.setForeground(SUCCESS);
            } else {
                feedbackLabel.setText("\u274C Incorrect. " + q.getExplanation());
                feedbackLabel.setForeground(DANGER);
            }
            nextBtn.setText(currentIndex == currentQuiz.size() - 1 ? "See Results" : "Next Question \u27A1");
        } else {
            currentIndex++;
            loadQuestion(currentIndex);
        }
    }

    private void showSummary() {
        progressLabel.setText("Quiz Completed");
        questionLabel.setText("Your Final Score: " + score + " / " + currentQuiz.size());
        for (JRadioButton b : optionButtons) b.setVisible(false);

        String badgeInfo = (score >= 4) ? " \uD83C\uDFC6 Great job! You really know your health!" : " Keep learning daily health tips!";
        feedbackLabel.setText("<html>Wellness Knowledge Score: " + (score * 20) + "%." + badgeInfo + "</html>");
        feedbackLabel.setForeground(PRIMARY);
        nextBtn.setText("Play Again \uD83D\uDD04");
    }
}
