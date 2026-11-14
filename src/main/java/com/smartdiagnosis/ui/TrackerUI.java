package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.*;
import com.smartdiagnosis.service.GamificationService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * TrackerUI - Symptom and wellness tracker panel.
 * Shows symptom logs over time with visual progress bars.
 * Demonstrates: Dynamic UI, Collections, Date handling
 */
public class TrackerUI extends JPanel {

    private final AppContext ctx = AppContext.getInstance();
    private static final Color PRIMARY   = DashboardUI.PRIMARY;
    private static final Color SECONDARY = DashboardUI.SECONDARY;
    private static final Color BG        = DashboardUI.BG;
    private static final Color WHITE     = DashboardUI.WHITE;
    private static final Color SUCCESS   = DashboardUI.SUCCESS;
    private static final Color TEXT      = DashboardUI.TEXT;
    private static final Color SUBTEXT   = DashboardUI.SUBTEXT;

    public TrackerUI() {
        setLayout(new BorderLayout(0, 10));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
    }

    private void initUI() {
        JLabel title = new JLabel("\uD83D\uDCCA  Symptom Tracker");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(SECONDARY);
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("Log Symptom", buildLogSymptomTab());
        tabs.addTab("My Symptom History", buildHistoryTab());
        tabs.addTab("Wellness Trend", buildWellnessTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildLogSymptomTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridwidth = 2;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel heading = new JLabel("Log a Symptom Today");
        heading.setFont(new Font("SansSerif", Font.BOLD, 17));
        heading.setForeground(SECONDARY);
        panel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(boldLabel("Symptom:"), gbc);
        gbc.gridx = 1;
        // Searchable symptom dropdown
        List<Symptom> symptoms = ctx.knowledgeBase.getAllSymptoms();
        String[] symNames = symptoms.stream().map(Symptom::getName).toArray(String[]::new);
        JComboBox<String> symBox = new JComboBox<>(symNames);
        symBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(symBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(boldLabel("Severity (1-5):"), gbc);
        gbc.gridx = 1;
        JSlider sevSlider = new JSlider(1, 5, 3);
        sevSlider.setMajorTickSpacing(1);
        sevSlider.setPaintTicks(true);
        sevSlider.setPaintLabels(true);
        sevSlider.setBackground(WHITE);
        panel.add(sevSlider, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(boldLabel("Duration:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> durBox = new JComboBox<>(new String[]{
            "Today", "1-3 days", "4-7 days", "1-2 weeks", "More than 2 weeks"
        });
        durBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(durBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(boldLabel("Notes (optional):"), gbc);
        gbc.gridx = 1;
        JTextField notesField = new JTextField();
        notesField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(notesField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel, gbc);

        gbc.gridy = 6;
        JButton saveBtn = new JButton("Save Symptom Log");
        saveBtn.setBackground(PRIMARY);
        saveBtn.setForeground(WHITE);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            User user = ctx.getCurrentUser();
            if (user == null) return;
            String symName = (String) symBox.getSelectedItem();
            Symptom selected = ctx.knowledgeBase.getSymptomByName(symName);
            int symptomId = selected != null ? selected.getId() : 0;

            SymptomLog log = new SymptomLog(user.getId(), symptomId, symName,
                LocalDate.now(), sevSlider.getValue(), (String) durBox.getSelectedItem());
            if (!notesField.getText().isBlank()) log.setNotes(notesField.getText());

            boolean saved = !ctx.isDbAvailable() || ctx.symptomLogDAO.save(log);
            statusLabel.setText(saved ? "\u2705 Symptom logged successfully!" : "\u274C Error saving log.");
            statusLabel.setForeground(saved ? SUCCESS : DashboardUI.DANGER);
        });
        panel.add(saveBtn, gbc);

        return panel;
    }

    private JPanel buildHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Recent Symptom Logs");
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        heading.setForeground(SECONDARY);
        panel.add(heading, BorderLayout.NORTH);

        JPanel logsPanel = new JPanel();
        logsPanel.setLayout(new BoxLayout(logsPanel, BoxLayout.Y_AXIS));
        logsPanel.setBackground(WHITE);

        User user = ctx.getCurrentUser();
        List<SymptomLog> logs = (ctx.isDbAvailable() && user != null)
            ? ctx.symptomLogDAO.findRecentLogs(user.getId(), 14)
            : new ArrayList<>();

        if (logs.isEmpty()) {
            JLabel empty = new JLabel("No symptom logs yet. Use the 'Log Symptom' tab to start tracking!");
            empty.setFont(new Font("SansSerif", Font.ITALIC, 13));
            empty.setForeground(SUBTEXT);
            logsPanel.add(empty);
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
            for (SymptomLog log : logs) {
                JPanel row = new JPanel(new BorderLayout(10, 0));
                row.setBackground(new Color(248, 249, 250));
                row.setBorder(new CompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    new EmptyBorder(8, 10, 8, 10)));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                JLabel nameLbl = new JLabel(log.getSymptomName());
                nameLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                nameLbl.setForeground(TEXT);

                JLabel barLbl = new JLabel(log.getSeverityBar() + " " + log.getSeverityLabel());
                barLbl.setFont(new Font("Monospaced", Font.PLAIN, 13));
                barLbl.setForeground(getSeverityColor(log.getSeverity()));

                JLabel dateLbl = new JLabel(log.getLogDate() != null ? log.getLogDate().format(fmt) : "");
                dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
                dateLbl.setForeground(SUBTEXT);

                row.add(nameLbl, BorderLayout.WEST);
                row.add(barLbl, BorderLayout.CENTER);
                row.add(dateLbl, BorderLayout.EAST);
                logsPanel.add(row);
            }
        }

        JScrollPane scroll = new JScrollPane(logsPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildWellnessTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel heading = new JLabel("Recent Wellness Scores");
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        heading.setForeground(SECONDARY);
        panel.add(heading, BorderLayout.NORTH);

        User user = ctx.getCurrentUser();
        List<HealthCheck> checks = (ctx.isDbAvailable() && user != null)
            ? ctx.healthCheckDAO.findRecentChecks(user.getId(), 7)
            : new ArrayList<>();

        JPanel wellnessPanel = new JPanel();
        wellnessPanel.setLayout(new BoxLayout(wellnessPanel, BoxLayout.Y_AXIS));
        wellnessPanel.setBackground(WHITE);

        if (checks.isEmpty()) {
            JLabel empty = new JLabel("No wellness check-ins yet. Complete your daily check-in from the Dashboard!");
            empty.setFont(new Font("SansSerif", Font.ITALIC, 13));
            empty.setForeground(SUBTEXT);
            wellnessPanel.add(empty);
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM");
            for (HealthCheck check : checks) {
                JPanel row = new JPanel(new BorderLayout(10, 0));
                row.setBackground(WHITE);
                row.setBorder(new EmptyBorder(6, 5, 6, 5));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

                String dateStr = check.getCheckDate() != null ? check.getCheckDate().format(fmt) : "N/A";
                JLabel dateLbl = new JLabel(dateStr);
                dateLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                dateLbl.setForeground(SECONDARY);
                dateLbl.setPreferredSize(new Dimension(60, 24));

                int score = check.getWellnessScore();
                String bar = GamificationService.progressBar(score, 100, 20);
                JLabel barLbl = new JLabel(bar);
                barLbl.setFont(new Font("Monospaced", Font.PLAIN, 13));
                barLbl.setForeground(score >= 70 ? SUCCESS : (score >= 50 ? DashboardUI.WARNING : DashboardUI.DANGER));

                JLabel scoreLbl = new JLabel(score + "/100 " + check.getWellnessLabel());
                scoreLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                scoreLbl.setForeground(SUBTEXT);

                row.add(dateLbl, BorderLayout.WEST);
                row.add(barLbl, BorderLayout.CENTER);
                row.add(scoreLbl, BorderLayout.EAST);
                wellnessPanel.add(row);
            }

            // Average
            double avg = checks.stream().mapToInt(HealthCheck::getWellnessScore).average().orElse(0);
            JLabel avgLbl = new JLabel("7-day Average Wellness Score: " + (int)avg + "/100");
            avgLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
            avgLbl.setForeground(PRIMARY);
            avgLbl.setBorder(new EmptyBorder(10, 0, 0, 0));
            wellnessPanel.add(avgLbl);
        }

        JLabel note = new JLabel("\u2139 Wellness score is an educational tracking tool, not a medical assessment.");
        note.setFont(new Font("SansSerif", Font.ITALIC, 10));
        note.setForeground(SUBTEXT);
        panel.add(note, BorderLayout.SOUTH);

        panel.add(new JScrollPane(wellnessPanel), BorderLayout.CENTER);
        return panel;
    }

    private Color getSeverityColor(int severity) {
        switch (severity) {
            case 1: return new Color(39, 174, 96);
            case 2: return new Color(39, 174, 96);
            case 3: return DashboardUI.WARNING;
            case 4: return DashboardUI.DANGER;
            case 5: return new Color(150, 0, 0);
            default: return TEXT;
        }
    }

    private JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(SECONDARY);
        return l;
    }
}
