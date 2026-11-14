package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.*;
import com.smartdiagnosis.service.AdviceEngine;
import com.smartdiagnosis.service.GamificationService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * SymptomCheckerUI - Multi-step symptom selection and analysis wizard.
 * Step 1: Select symptoms by category or search
 * Step 2: Set severity and duration
 * Step 3: View results with matched conditions
 * Demonstrates: CardLayout, Multi-step UI, Event Handling, Collections
 */
public class SymptomCheckerUI extends JPanel {

    private final AppContext ctx = AppContext.getInstance();
    private final DashboardUI parent;

    // Colors (from DashboardUI)
    private static final Color PRIMARY   = DashboardUI.PRIMARY;
    private static final Color SECONDARY = DashboardUI.SECONDARY;
    private static final Color BG        = DashboardUI.BG;
    private static final Color WHITE     = DashboardUI.WHITE;
    private static final Color SUCCESS   = DashboardUI.SUCCESS;
    private static final Color DANGER    = DashboardUI.DANGER;
    private static final Color WARNING   = DashboardUI.WARNING;
    private static final Color TEXT      = DashboardUI.TEXT;
    private static final Color SUBTEXT   = DashboardUI.SUBTEXT;

    // Wizard state
    private final List<Symptom> selectedSymptoms = new ArrayList<>();
    private final Map<Integer, Integer> severityMap = new HashMap<>();
    private final Map<Integer, JSlider> severitySliders = new HashMap<>();
    private String selectedDuration = "1-3 days";
    private DiagnosisResult lastResult;

    // UI components
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel symptomListPanel;
    private JTextField searchField;
    private JLabel selectedCountLabel;
    private JPanel selectedPreviewPanel;

    public SymptomCheckerUI(DashboardUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BG);
        initUI();
    }

    private void initUI() {
        // Page header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(15, 20, 10, 20));
        JLabel title = new JLabel("\uD83D\uDD0D  Symptom Checker");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(SECONDARY);
        header.add(title, BorderLayout.WEST);

        JLabel safetyNote = new JLabel("Educational simulation only \u2022 Not a medical diagnosis");
        safetyNote.setFont(new Font("SansSerif", Font.ITALIC, 11));
        safetyNote.setForeground(SUBTEXT);
        header.add(safetyNote, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Card layout for steps
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG);
        cardPanel.add(buildStep1(), "step1");
        cardPanel.add(buildStep2(), "step2");
        cardPanel.add(buildStep3(), "step3");

        add(cardPanel, BorderLayout.CENTER);
    }

    // ==================== STEP 1: SELECT SYMPTOMS ====================
    private JPanel buildStep1() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(5, 20, 15, 20));

        // Step indicator
        panel.add(stepIndicator("Step 1 of 3: Select Your Symptoms", 1), BorderLayout.NORTH);

        // Center: Search + Category + Symptoms
        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setBackground(BG);

        // Search bar
        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setBackground(BG);
        JLabel searchIcon = new JLabel("\uD83D\uDD0D");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(6, 10, 6, 10)));
        searchField.putClientProperty("JTextField.placeholderText", "Search symptoms...");
        searchBar.add(searchIcon, BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(PRIMARY);
        searchBtn.setForeground(WHITE);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> refreshSymptomList(searchField.getText()));
        searchField.addActionListener(e -> refreshSymptomList(searchField.getText()));
        searchBar.add(searchBtn, BorderLayout.EAST);
        center.add(searchBar, BorderLayout.NORTH);

        // Category tabs + symptom list
        JPanel bodyArea = new JPanel(new BorderLayout(8, 0));
        bodyArea.setBackground(BG);

        // Category buttons (vertical)
        JPanel catPanel = new JPanel();
        catPanel.setLayout(new BoxLayout(catPanel, BoxLayout.Y_AXIS));
        catPanel.setBackground(WHITE);
        catPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        catPanel.setPreferredSize(new Dimension(135, 0));

        JLabel catTitle = new JLabel(" Categories");
        catTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        catTitle.setForeground(SUBTEXT);
        catPanel.add(catTitle);
        catPanel.add(Box.createVerticalStrut(4));

        String[] categories = {"All", "General", "Neurological", "Respiratory",
                               "Digestive", "Musculoskeletal", "Skin", "Urinary", "Eye", "ENT"};
        String[] catIcons = {"\uD83D\uDCCB","✳️","\uD83E\uDDE0","\uD83E\uDEB1",
                             "\uD83E\uDEB4","\uD83E\uDDB4","\uD83E\uDDD6","\uD83D\uDCA7","👁️","\uD83D\uDC42"};

        for (int i = 0; i < categories.length; i++) {
            final String cat = categories[i];
            JButton catBtn = new JButton(catIcons[i] + " " + cat);
            catBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
            catBtn.setBorderPainted(false);
            catBtn.setFocusPainted(false);
            catBtn.setBackground(WHITE);
            catBtn.setForeground(TEXT);
            catBtn.setHorizontalAlignment(SwingConstants.LEFT);
            catBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            catBtn.setMaximumSize(new Dimension(130, 30));
            catBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            catBtn.addActionListener(e -> {
                searchField.setText("");
                refreshSymptomList(cat.equals("All") ? "" : cat);
            });
            catBtn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e)  { catBtn.setBackground(DashboardUI.LIGHT_BLUE); }
                public void mouseExited(MouseEvent e)   { catBtn.setBackground(WHITE); }
            });
            catPanel.add(catBtn);
            catPanel.add(Box.createVerticalStrut(2));
        }

        // Symptom checkboxes list
        symptomListPanel = new JPanel();
        symptomListPanel.setLayout(new BoxLayout(symptomListPanel, BoxLayout.Y_AXIS));
        symptomListPanel.setBackground(WHITE);
        refreshSymptomList("");

        JScrollPane symptomScroll = new JScrollPane(symptomListPanel);
        symptomScroll.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        symptomScroll.getVerticalScrollBar().setUnitIncrement(16);

        bodyArea.add(new JScrollPane(catPanel), BorderLayout.WEST);
        bodyArea.add(symptomScroll, BorderLayout.CENTER);

        center.add(bodyArea, BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);

        // Bottom: selected preview + Next button
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        bottom.setBackground(BG);
        bottom.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel selectedArea = new JPanel(new BorderLayout(5, 0));
        selectedArea.setBackground(new Color(214, 234, 248));
        selectedArea.setBorder(new EmptyBorder(8, 10, 8, 10));

        selectedCountLabel = new JLabel("0 symptoms selected");
        selectedCountLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        selectedCountLabel.setForeground(PRIMARY);
        selectedArea.add(selectedCountLabel, BorderLayout.WEST);

        JButton clearBtn = new JButton("Clear All");
        clearBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        clearBtn.setBorderPainted(false);
        clearBtn.setBackground(new Color(214, 234, 248));
        clearBtn.setForeground(DANGER);
        clearBtn.addActionListener(e -> {
            selectedSymptoms.clear();
            updateSelectedCount();
            refreshSymptomList(searchField.getText());
        });
        selectedArea.add(clearBtn, BorderLayout.EAST);

        bottom.add(selectedArea, BorderLayout.CENTER);

        JButton nextBtn = new JButton("Next: Set Severity \u27A1");
        nextBtn.setBackground(PRIMARY);
        nextBtn.setForeground(WHITE);
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        nextBtn.setBorderPainted(false);
        nextBtn.setFocusPainted(false);
        nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        nextBtn.addActionListener(e -> {
            if (selectedSymptoms.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please select at least one symptom before proceeding.",
                    "No Symptoms Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            buildStep2Content();
            cardLayout.show(cardPanel, "step2");
        });
        bottom.add(nextBtn, BorderLayout.EAST);

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshSymptomList(String filter) {
        symptomListPanel.removeAll();
        List<Symptom> symptoms;
        if (filter.isBlank()) {
            symptoms = ctx.knowledgeBase.getAllSymptoms();
        } else {
            // Check if filter is a category name
            boolean isCategory = ctx.knowledgeBase.getAllCategories().stream()
                .anyMatch(c -> c.equalsIgnoreCase(filter));
            if (isCategory) {
                symptoms = ctx.knowledgeBase.getSymptomsByCategory(filter);
            } else {
                symptoms = ctx.knowledgeBase.searchSymptoms(filter);
            }
        }

        // Group by category
        Map<String, List<Symptom>> grouped = new LinkedHashMap<>();
        for (Symptom s : symptoms) {
            grouped.computeIfAbsent(s.getCategory(), k -> new ArrayList<>()).add(s);
        }

        for (Map.Entry<String, List<Symptom>> entry : grouped.entrySet()) {
            // Category header
            JLabel catHeader = new JLabel("  " + entry.getKey());
            catHeader.setFont(new Font("SansSerif", Font.BOLD, 11));
            catHeader.setForeground(SUBTEXT);
            catHeader.setBorder(new EmptyBorder(6, 5, 2, 5));
            catHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            symptomListPanel.add(catHeader);

            for (Symptom symptom : entry.getValue()) {
                JPanel row = new JPanel(new BorderLayout(5, 0));
                row.setBackground(WHITE);
                row.setBorder(new EmptyBorder(3, 8, 3, 8));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

                JCheckBox cb = new JCheckBox(symptom.getName());
                cb.setFont(new Font("SansSerif", Font.PLAIN, 12));
                cb.setBackground(WHITE);
                cb.setForeground(symptom.isEmergency() ? DANGER : TEXT);
                cb.setSelected(selectedSymptoms.contains(symptom));
                cb.addActionListener(e -> {
                    if (cb.isSelected()) {
                        if (!selectedSymptoms.contains(symptom)) selectedSymptoms.add(symptom);
                    } else {
                        selectedSymptoms.remove(symptom);
                    }
                    updateSelectedCount();
                });
                row.add(cb, BorderLayout.WEST);

                if (symptom.isEmergency()) {
                    JLabel warning = new JLabel("\u26a0\ufe0f Emergency");
                    warning.setFont(new Font("SansSerif", Font.BOLD, 10));
                    warning.setForeground(DANGER);
                    row.add(warning, BorderLayout.EAST);
                }

                // Hover highlight
                row.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { row.setBackground(DashboardUI.LIGHT_BLUE); cb.setBackground(DashboardUI.LIGHT_BLUE); }
                    public void mouseExited(MouseEvent e)  { row.setBackground(WHITE); cb.setBackground(WHITE); }
                    public void mouseClicked(MouseEvent e) { cb.doClick(); }
                });

                symptomListPanel.add(row);
            }
        }

        if (symptoms.isEmpty()) {
            JLabel noResult = new JLabel("  No symptoms found for: \"" + filter + "\"");
            noResult.setFont(new Font("SansSerif", Font.ITALIC, 12));
            noResult.setForeground(SUBTEXT);
            symptomListPanel.add(noResult);
        }

        symptomListPanel.revalidate();
        symptomListPanel.repaint();
    }

    private void updateSelectedCount() {
        selectedCountLabel.setText(selectedSymptoms.size() + " symptom(s) selected: "
            + selectedSymptoms.stream().map(Symptom::getName)
              .reduce((a, b) -> a + ", " + b).orElse(""));
    }

    // ==================== STEP 2: SEVERITY & DURATION ====================
    private JPanel step2Panel;

    private JPanel buildStep2() {
        step2Panel = new JPanel(new BorderLayout(0, 10));
        step2Panel.setBackground(BG);
        step2Panel.setBorder(new EmptyBorder(5, 20, 15, 20));
        return step2Panel;
    }

    private void buildStep2Content() {
        step2Panel.removeAll();
        severitySliders.clear();

        step2Panel.add(stepIndicator("Step 2 of 3: Rate Severity & Duration", 2), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(BG);

        // Duration selection
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        durationPanel.setBackground(WHITE);
        durationPanel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(10, 15, 10, 15)));

        JLabel durLabel = new JLabel("\uD83D\uDCC5 How long have you had these symptoms?  ");
        durLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        durLabel.setForeground(SECONDARY);
        durationPanel.add(durLabel);

        String[] durations = {"Today", "1-3 days", "4-7 days", "1-2 weeks", "More than 2 weeks"};
        ButtonGroup durGroup = new ButtonGroup();
        for (String dur : durations) {
            JRadioButton rb = new JRadioButton(dur);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 12));
            rb.setBackground(WHITE);
            rb.setForeground(TEXT);
            rb.addActionListener(e -> selectedDuration = dur);
            if (dur.equals("1-3 days")) rb.setSelected(true);
            durGroup.add(rb);
            durationPanel.add(rb);
        }

        JPanel durWrapper = new JPanel(new BorderLayout());
        durWrapper.setBackground(BG);
        durWrapper.setBorder(new EmptyBorder(0, 0, 10, 0));
        durWrapper.add(durationPanel);
        durWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        center.add(durWrapper);

        // Severity sliders for each selected symptom
        JLabel sevTitle = new JLabel("  Rate the severity of each symptom (1=Very Mild, 5=Very Severe):");
        sevTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        sevTitle.setForeground(SECONDARY);
        sevTitle.setBorder(new EmptyBorder(5, 0, 5, 0));
        center.add(sevTitle);

        JPanel severityGrid = new JPanel(new GridLayout(0, 1, 0, 6));
        severityGrid.setBackground(WHITE);
        severityGrid.setBorder(new EmptyBorder(10, 15, 10, 15));

        for (Symptom symptom : selectedSymptoms) {
            JPanel row = new JPanel(new BorderLayout(15, 0));
            row.setBackground(WHITE);

            String prefix = symptom.isEmergency() ? "\u26a0\ufe0f " : "";
            JLabel lbl = new JLabel(prefix + symptom.getName());
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            lbl.setForeground(symptom.isEmergency() ? DANGER : TEXT);
            lbl.setPreferredSize(new Dimension(220, 30));

            JSlider slider = new JSlider(1, 5, 3);
            slider.setMajorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setBackground(WHITE);

            Hashtable<Integer, JLabel> labels = new Hashtable<>();
            labels.put(1, new JLabel("Very Mild"));
            labels.put(3, new JLabel("Moderate"));
            labels.put(5, new JLabel("Very Severe"));
            slider.setLabelTable(labels);

            severityMap.put(symptom.getId(), 3); // default
            slider.addChangeListener(e -> severityMap.put(symptom.getId(), slider.getValue()));
            severitySliders.put(symptom.getId(), slider);

            row.add(lbl, BorderLayout.WEST);
            row.add(slider, BorderLayout.CENTER);
            severityGrid.add(row);
        }

        JScrollPane sevScroll = new JScrollPane(severityGrid);
        sevScroll.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        sevScroll.getVerticalScrollBar().setUnitIncrement(16);
        center.add(sevScroll);

        step2Panel.add(new JScrollPane(center), BorderLayout.CENTER);

        // Navigation
        JPanel navRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        navRow.setBackground(BG);

        JButton backBtn = new JButton("\u2B05 Back");
        styleNavButton(backBtn, new Color(127, 140, 141));
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "step1"));

        JButton analyzeBtn = new JButton("\uD83D\uDD0E Analyze Symptoms");
        styleNavButton(analyzeBtn, PRIMARY);
        analyzeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        analyzeBtn.addActionListener(e -> doAnalysis());

        navRow.add(backBtn);
        navRow.add(analyzeBtn);
        step2Panel.add(navRow, BorderLayout.SOUTH);

        step2Panel.revalidate();
        step2Panel.repaint();
    }

    // ==================== STEP 3: RESULTS ====================
    private JPanel step3Panel;

    private JPanel buildStep3() {
        step3Panel = new JPanel(new BorderLayout());
        step3Panel.setBackground(BG);
        return step3Panel;
    }

    private void doAnalysis() {
        // Collect final severity values from sliders
        for (Map.Entry<Integer, JSlider> entry : severitySliders.entrySet()) {
            severityMap.put(entry.getKey(), entry.getValue().getValue());
        }

        int userAge = ctx.getCurrentUser() != null ? ctx.getCurrentUser().getAge() : 25;
        lastResult = ctx.diagnosisEngine.analyze(selectedSymptoms, severityMap, selectedDuration, userAge);
        lastResult.setUserId(ctx.getCurrentUser() != null ? ctx.getCurrentUser().getId() : 0);

        // Save to history
        if (ctx.isDbAvailable() && ctx.getCurrentUser() != null) {
            ctx.historyDAO.save(lastResult, ctx.getCurrentUser().getId());
            // Check for new badges
            User user = ctx.getCurrentUser();
            List<String> newBadges = ctx.gamificationService.checkAndAwardBadges(
                user, Collections.emptyList(), true, false);
            if (!newBadges.isEmpty()) ctx.userDAO.update(user);
        }

        buildResultsView();
        cardLayout.show(cardPanel, "step3");
    }

    private void buildResultsView() {
        step3Panel.removeAll();
        step3Panel.add(stepIndicator("Step 3 of 3: Analysis Results", 3), BorderLayout.NORTH);

        JPanel resultsContent = new JPanel();
        resultsContent.setLayout(new BoxLayout(resultsContent, BoxLayout.Y_AXIS));
        resultsContent.setBackground(BG);
        resultsContent.setBorder(new EmptyBorder(10, 20, 10, 20));

        // --- EMERGENCY WARNING ---
        if (lastResult.isEmergencyWarning()) {
            JPanel emergencyPanel = new JPanel(new BorderLayout(10, 10));
            emergencyPanel.setBackground(new Color(231, 76, 60));
            emergencyPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            emergencyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

            JLabel icon = new JLabel("\u26A0\uFE0F  URGENT MEDICAL ATTENTION MAY BE NEEDED");
            icon.setFont(new Font("SansSerif", Font.BOLD, 18));
            icon.setForeground(WHITE);
            emergencyPanel.add(icon, BorderLayout.NORTH);

            JTextArea msg = new JTextArea(lastResult.getEmergencyMessage());
            msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
            msg.setForeground(WHITE);
            msg.setBackground(new Color(231, 76, 60));
            msg.setWrapStyleWord(true);
            msg.setLineWrap(true);
            msg.setEditable(false);
            msg.setBorder(null);
            emergencyPanel.add(msg, BorderLayout.CENTER);

            resultsContent.add(emergencyPanel);
            resultsContent.add(Box.createVerticalStrut(10));

        } else {
            // ---- SYMPTOM ANALYSIS COMPLETE HEADER ----
            JLabel header = new JLabel("SYMPTOM ANALYSIS COMPLETE");
            header.setFont(new Font("SansSerif", Font.BOLD, 16));
            header.setForeground(SUCCESS);
            header.setAlignmentX(Component.LEFT_ALIGNMENT);
            resultsContent.add(header);
            resultsContent.add(Box.createVerticalStrut(8));

            // Safety disclaimer
            JLabel disclaimer = new JLabel("These are Symptom Compatibility Scores \u2014 NOT a medical diagnosis");
            disclaimer.setFont(new Font("SansSerif", Font.ITALIC, 11));
            disclaimer.setForeground(WARNING);
            disclaimer.setAlignmentX(Component.LEFT_ALIGNMENT);
            resultsContent.add(disclaimer);
            resultsContent.add(Box.createVerticalStrut(12));

            // ---- TOP 3 CONDITION CARDS ----
            if (lastResult.getTopMatches().isEmpty()) {
                JLabel noMatch = new JLabel("No strong symptom patterns found. Please consult a healthcare professional.");
                noMatch.setFont(new Font("SansSerif", Font.BOLD, 13));
                noMatch.setForeground(SUBTEXT);
                noMatch.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultsContent.add(noMatch);
            } else {
                Color[] rankColors = {new Color(39, 174, 96), new Color(243, 156, 18), new Color(127, 140, 141)};
                String[] rankLabels = {"\uD83E\uDD47 Top Match", "\uD83E\uDD48 2nd Match", "\uD83E\uDD49 3rd Match"};
                int rank = 0;
                for (ConditionMatch match : lastResult.getTopMatches()) {
                    JPanel card = buildResultCard(match, rankColors[rank % 3], rankLabels[rank % 3]);
                    card.setAlignmentX(Component.LEFT_ALIGNMENT);
                    resultsContent.add(card);
                    resultsContent.add(Box.createVerticalStrut(10));
                    rank++;
                }
            }

            // ---- ADVICE SECTION ----
            List<AdviceEngine.HealthAdvice> adviceList = ctx.adviceEngine.generateAdvice(lastResult);
            for (AdviceEngine.HealthAdvice advice : adviceList) {
                JPanel advPanel = buildAdvicePanel(advice);
                advPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultsContent.add(advPanel);
                resultsContent.add(Box.createVerticalStrut(8));
            }

            // ---- RECOMMENDED TRACKING ----
            List<String> tracking = ctx.diagnosisEngine.getRecommendedTracking(lastResult);
            JPanel trackPanel = new JPanel(new BorderLayout(0, 6));
            trackPanel.setBackground(DashboardUI.LIGHT_BLUE);
            trackPanel.setBorder(new EmptyBorder(12, 15, 12, 15));
            trackPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            JLabel trackTitle = new JLabel("\uD83D\uDCCB Recommended Items to Track");
            trackTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
            trackTitle.setForeground(PRIMARY);
            StringBuilder trackContent = new StringBuilder("<html>");
            for (String t : tracking) trackContent.append("\u2611 ").append(t).append("<br>");
            trackContent.append("</html>");
            JLabel trackList = new JLabel(trackContent.toString());
            trackList.setFont(new Font("SansSerif", Font.PLAIN, 12));
            trackList.setForeground(TEXT);
            trackPanel.add(trackTitle, BorderLayout.NORTH);
            trackPanel.add(trackList, BorderLayout.CENTER);
            trackPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            resultsContent.add(trackPanel);
        }

        JScrollPane scroll = new JScrollPane(resultsContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        step3Panel.add(scroll, BorderLayout.CENTER);

        // Navigation bar
        JPanel navRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        navRow.setBackground(BG);

        JButton newCheckBtn = new JButton("\uD83D\uDD04 New Check");
        styleNavButton(newCheckBtn, new Color(127, 140, 141));
        newCheckBtn.addActionListener(e -> {
            selectedSymptoms.clear();
            severityMap.clear();
            cardLayout.show(cardPanel, "step1");
            refreshSymptomList("");
        });

        JButton dashBtn = new JButton("\uD83C\uDFE0 Dashboard");
        styleNavButton(dashBtn, PRIMARY);
        dashBtn.addActionListener(e -> {
            if (parent != null) parent.navigateTo("dashboard");
        });

        JButton reportBtn = new JButton("\uD83D\uDCC4 View Report");
        styleNavButton(reportBtn, SUCCESS);
        reportBtn.addActionListener(e -> showFullReport());

        navRow.add(newCheckBtn);
        navRow.add(reportBtn);
        navRow.add(dashBtn);
        step3Panel.add(navRow, BorderLayout.SOUTH);

        step3Panel.revalidate();
        step3Panel.repaint();
    }

    private JPanel buildResultCard(ConditionMatch match, Color headerColor, String rankLabel) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(headerColor, 2),
            new EmptyBorder(12, 15, 12, 15)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        // Header row
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(WHITE);
        JLabel rankLbl = new JLabel(rankLabel + "   |   " + match.getDisease().getName());
        rankLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        rankLbl.setForeground(SECONDARY);

        JLabel scoreLbl = new JLabel("Compatibility: " + match.getCompatibilityLabel()
            + " (" + match.getScorePercent() + "%)");
        scoreLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        scoreLbl.setForeground(headerColor);
        headerRow.add(rankLbl, BorderLayout.WEST);
        headerRow.add(scoreLbl, BorderLayout.EAST);
        card.add(headerRow, BorderLayout.NORTH);

        // Matched symptoms
        StringBuilder matchedHtml = new StringBuilder("<html><b>Matched symptoms:</b> ");
        for (Symptom s : match.getMatchedSymptoms()) {
            matchedHtml.append("\u2713 ").append(s.getName()).append("  ");
        }
        matchedHtml.append("</html>");
        JLabel matchedLbl = new JLabel(matchedHtml.toString());
        matchedLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        matchedLbl.setForeground(SUCCESS);

        // Description
        JTextArea descArea = new JTextArea(match.getDisease().getDescription());
        descArea.setFont(new Font("SansSerif", Font.ITALIC, 11));
        descArea.setForeground(SUBTEXT);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setBackground(WHITE);
        descArea.setBorder(null);

        // "Why this result?" button
        JButton whyBtn = new JButton("Why this result? \uD83D\uDCAC");
        whyBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        whyBtn.setForeground(PRIMARY);
        whyBtn.setBorderPainted(false);
        whyBtn.setContentAreaFilled(false);
        whyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        whyBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
            ctx.diagnosisEngine.generateWhyExplanation(match),
            "Why This Result?", JOptionPane.INFORMATION_MESSAGE));

        JPanel body = new JPanel(new GridLayout(3, 1, 0, 4));
        body.setBackground(WHITE);
        body.add(matchedLbl);
        body.add(descArea);
        body.add(whyBtn);
        card.add(body, BorderLayout.CENTER);

        // Safety note
        JLabel safetyNote = new JLabel("\u26a0 This is a symptom compatibility pattern only. Not a diagnosis.");
        safetyNote.setFont(new Font("SansSerif", Font.ITALIC, 10));
        safetyNote.setForeground(WARNING);
        card.add(safetyNote, BorderLayout.SOUTH);

        return card;
    }

    private JPanel buildAdvicePanel(AdviceEngine.HealthAdvice advice) {
        Color bg;
        Color titleColor;
        switch (advice.getCategory()) {
            case EMERGENCY:            bg = new Color(231, 76, 60);   titleColor = WHITE;  break;
            case SELF_CARE:            bg = DashboardUI.LIGHT_GREEN;  titleColor = SUCCESS; break;
            case MONITORING:           bg = DashboardUI.LIGHT_BLUE;   titleColor = PRIMARY; break;
            case MEDICAL_CONSULTATION: bg = new Color(253, 235, 208); titleColor = WARNING; break;
            default:                   bg = new Color(240, 240, 240); titleColor = SECONDARY;
        }

        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(bg);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel titleLbl = new JLabel(advice.getTitle());
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLbl.setForeground(titleColor);

        StringBuilder html = new StringBuilder("<html>");
        for (String point : advice.getPoints()) {
            html.append("\u2022 ").append(point).append("<br>");
        }
        html.append("</html>");
        JLabel pointsLbl = new JLabel(html.toString());
        pointsLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pointsLbl.setForeground(advice.getCategory() == AdviceEngine.AdviceCategory.EMERGENCY ? WHITE : TEXT);

        panel.add(titleLbl, BorderLayout.NORTH);
        panel.add(pointsLbl, BorderLayout.CENTER);
        return panel;
    }

    private void showFullReport() {
        if (lastResult == null) return;
        String report = ctx.adviceEngine.generateReport(lastResult, ctx.getCurrentUser());

        JTextArea reportArea = new JTextArea(report);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(reportArea);
        scroll.setPreferredSize(new Dimension(600, 500));

        JOptionPane.showMessageDialog(this, scroll, "Symptom Analysis Report",
            JOptionPane.PLAIN_MESSAGE);
    }

    // ==================== HELPERS ====================

    private JPanel stepIndicator(String text, int step) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBackground(BG);

        for (int i = 1; i <= 3; i++) {
            JLabel dot = new JLabel(String.valueOf(i));
            dot.setFont(new Font("SansSerif", Font.BOLD, 13));
            dot.setOpaque(true);
            dot.setBackground(i <= step ? PRIMARY : new Color(189, 195, 199));
            dot.setForeground(WHITE);
            dot.setBorder(new EmptyBorder(4, 9, 4, 9));
            panel.add(dot);
            if (i < 3) {
                JLabel dash = new JLabel("\u2500\u2500\u2500");
                dash.setForeground(i < step ? PRIMARY : new Color(189, 195, 199));
                panel.add(dash);
            }
        }

        JLabel stepText = new JLabel("  " + text);
        stepText.setFont(new Font("SansSerif", Font.BOLD, 14));
        stepText.setForeground(SECONDARY);
        panel.add(stepText);
        return panel;
    }

    private void styleNavButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
    }
}
