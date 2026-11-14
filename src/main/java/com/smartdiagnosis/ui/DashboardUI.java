package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.*;
import com.smartdiagnosis.service.GamificationService;
import com.smartdiagnosis.utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DashboardUI - Main home screen shown after login.
 * Shows: Welcome, Daily Tip, Health Fact, Quick Actions, Recent History, Wellness Score.
 * Demonstrates: Complex Swing layout, Event-driven programming
 */
public class DashboardUI extends JFrame {

    private final AppContext ctx = AppContext.getInstance();

    // Design constants
    static final Color PRIMARY     = new Color(41, 128, 185);
    static final Color SECONDARY   = new Color(44, 62, 80);
    static final Color BG          = new Color(236, 240, 241);
    static final Color WHITE       = Color.WHITE;
    static final Color SUCCESS     = new Color(39, 174, 96);
    static final Color DANGER      = new Color(192, 57, 43);
    static final Color WARNING     = new Color(243, 156, 18);
    static final Color LIGHT_BLUE  = new Color(214, 234, 248);
    static final Color LIGHT_GREEN = new Color(212, 239, 223);
    static final Color TEXT        = new Color(44, 62, 80);
    static final Color SUBTEXT     = new Color(127, 140, 141);

    private JLabel streakLabel;
    private JPanel contentPanel;

    public DashboardUI() {
        setTitle("Smart Disease Diagnosis Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ---- SIDEBAR NAVIGATION ----
        root.add(buildSidebar(), BorderLayout.WEST);

        // ---- MAIN CONTENT ----
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        showDashboard();
        root.add(contentPanel, BorderLayout.CENTER);

        add(root);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SECONDARY);
        sidebar.setPreferredSize(new Dimension(190, getHeight()));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        // App logo area
        JPanel logoArea = new JPanel(new GridLayout(2, 1, 0, 2));
        logoArea.setBackground(PRIMARY);
        logoArea.setBorder(new EmptyBorder(18, 15, 18, 15));
        logoArea.setMaximumSize(new Dimension(190, 80));

        JLabel logo = new JLabel("\u2695\uFE0F  Health Sim");
        logo.setFont(new Font("SansSerif", Font.BOLD, 15));
        logo.setForeground(WHITE);
        JLabel ver = new JLabel("v1.0  \u2022  Educational");
        ver.setFont(new Font("SansSerif", Font.PLAIN, 10));
        ver.setForeground(new Color(174, 214, 241));
        logoArea.add(logo);
        logoArea.add(ver);
        sidebar.add(logoArea);

        sidebar.add(Box.createVerticalStrut(10));

        // Nav buttons
        String[][] navItems = {
            {"\uD83C\uDFE0  Dashboard",         "dashboard"},
            {"\uD83D\uDD0D  Symptom Check",      "checker"},
            {"\uD83D\uDCCA  Health Tracker",      "tracker"},
            {"\uD83D\uDCDC  Health History",      "history"},
            {"\uD83D\uDCD3  Health Journal",      "journal"},
            {"\uD83D\uDCA1  Health Tips",         "tips"},
            {"\uD83D\uDCD6  Conditions",          "conditions"},
            {"\uD83C\uDFAE  Health Quiz",         "quiz"},
            {"\uD83D\uDC64  My Profile",          "profile"},
        };

        for (String[] item : navItems) {
            JButton btn = navButton(item[0], item[1]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout button
        JButton logout = navButton("\uD83D\uDEAA  Logout", "logout");
        logout.setBackground(new Color(80, 60, 60));
        sidebar.add(logout);
        sidebar.add(Box.createVerticalStrut(10));

        return sidebar;
    }

    private JButton navButton(String text, String action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setForeground(new Color(189, 195, 199));
        btn.setBackground(SECONDARY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 10));
        btn.setMaximumSize(new Dimension(190, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)  { btn.setBackground(new Color(60, 85, 110)); btn.setForeground(WHITE); }
            public void mouseExited(MouseEvent e)   { btn.setBackground(SECONDARY); btn.setForeground(new Color(189, 195, 199)); }
        });

        btn.addActionListener(e -> handleNav(action));
        return btn;
    }

    private void handleNav(String action) {
        contentPanel.removeAll();
        switch (action) {
            case "dashboard":  showDashboard();         break;
            case "checker":    showSymptomChecker();    break;
            case "tracker":    showTracker();           break;
            case "history":    showHistory();           break;
            case "journal":    showJournal();           break;
            case "tips":       showHealthTips();        break;
            case "conditions": showConditions();        break;
            case "quiz":       showQuiz();              break;
            case "profile":    showProfile();           break;
            case "logout":     doLogout();              break;
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ==================== DASHBOARD VIEW ====================
    private void showDashboard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TOP: Welcome + Streak ---
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(BG);

        User user = ctx.getCurrentUser();
        String greeting = getGreeting();
        JLabel welcomeLabel = new JLabel(greeting + ", " + (user != null ? user.getName() : "Guest") + "! \uD83D\uDC4B");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeLabel.setForeground(SECONDARY);
        topRow.add(welcomeLabel, BorderLayout.WEST);

        // Streak badge
        int streak = user != null ? user.getStreakDays() : 0;
        JLabel streakBadge = new JLabel("\uD83D\uDD25 " + streak + " Day Streak");
        streakBadge.setFont(new Font("SansSerif", Font.BOLD, 13));
        streakBadge.setForeground(WARNING);
        topRow.add(streakBadge, BorderLayout.EAST);

        panel.add(topRow, BorderLayout.NORTH);

        // --- CENTER: Cards ---
        JPanel cardsArea = new JPanel(new GridLayout(2, 2, 15, 15));
        cardsArea.setBackground(BG);

        // Card 1: Start Symptom Check
        cardsArea.add(buildActionCard(
            "\uD83D\uDD0D Start Symptom Check",
            "Analyze your symptoms\nand find possible conditions",
            PRIMARY, "checker"
        ));

        // Card 2: Daily Check-in
        boolean checkedInToday = ctx.isDbAvailable() && user != null
            && ctx.healthCheckDAO.hasCheckedInToday(user.getId());
        String checkInTitle = checkedInToday ? "\u2705 Checked In Today" : "\uD83D\uDCCB Daily Check-In";
        cardsArea.add(buildActionCard(
            checkInTitle,
            checkedInToday ? "You have already checked in today!" : "How are you feeling\ntoday?",
            checkedInToday ? SUCCESS : new Color(142, 68, 173), "checkin"
        ));

        // Card 3: Health Tracker
        cardsArea.add(buildActionCard(
            "\uD83D\uDCCA Symptom Tracker",
            "Track your symptoms\nover time",
            new Color(22, 160, 133), "tracker"
        ));

        // Card 4: Health Journal
        cardsArea.add(buildActionCard(
            "\uD83D\uDCD3 Health Journal",
            "Write about how\nyou are feeling",
            new Color(230, 126, 34), "journal"
        ));

        // --- BOTTOM: Health Tip + Fact ---
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomRow.setBackground(BG);

        // Today's health tip
        HealthTip tip = ctx.knowledgeBase.getDailyTip();
        JPanel tipCard = buildInfoCard(
            tip.getTitle(), tip.getContent(), LIGHT_BLUE, PRIMARY
        );
        bottomRow.add(tipCard);

        // Health fact
        JPanel factCard = buildInfoCard(
            "\uD83E\uDDE0 Did You Know?",
            getTodaysFact(),
            LIGHT_GREEN, SUCCESS
        );
        bottomRow.add(factCard);

        // Combine center and bottom
        JPanel centerAndBottom = new JPanel(new BorderLayout(0, 15));
        centerAndBottom.setBackground(BG);
        centerAndBottom.add(cardsArea, BorderLayout.CENTER);
        centerAndBottom.add(bottomRow, BorderLayout.SOUTH);

        panel.add(centerAndBottom, BorderLayout.CENTER);
        contentPanel.add(panel);
    }

    private JPanel buildActionCard(String title, String desc, Color bg, String action) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(bg);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel("<html><b>" + title + "</b></html>");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setForeground(WHITE);

        JLabel descLabel = new JLabel("<html>" + desc.replace("\n", "<br>") + "</html>");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLabel.setForeground(new Color(255, 255, 255, 200));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);

        // Click to navigate
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if ("checkin".equals(action)) showDailyCheckIn();
                else handleNav(action);
            }
            public void mouseEntered(MouseEvent e) { card.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e)  { card.setBackground(bg); }
        });

        return card;
    }

    private JPanel buildInfoCard(String title, String content, Color bg, Color titleColor) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(bg);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLabel.setForeground(titleColor);

        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        contentArea.setForeground(TEXT);
        contentArea.setBackground(bg);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setBorder(null);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentArea, BorderLayout.CENTER);
        return card;
    }

    // ==================== SYMPTOM CHECKER VIEW ====================
    private void showSymptomChecker() {
        SymptomCheckerUI checkerUI = new SymptomCheckerUI(this);
        contentPanel.add(checkerUI);
    }

    // ==================== HEALTH TRACKER VIEW ====================
    private void showTracker() {
        TrackerUI trackerUI = new TrackerUI();
        contentPanel.add(trackerUI);
    }

    // ==================== HISTORY VIEW ====================
    private void showHistory() {
        HistoryUI historyUI = new HistoryUI();
        contentPanel.add(historyUI);
    }

    // ==================== JOURNAL VIEW ====================
    private void showJournal() {
        JournalUI journalUI = new JournalUI();
        contentPanel.add(journalUI);
    }

    // ==================== HEALTH TIPS VIEW ====================
    private void showHealthTips() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("\uD83D\uDCA1  Health Tips & Wellness Advice");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(SECONDARY);
        panel.add(title, BorderLayout.NORTH);

        JPanel tipsGrid = new JPanel(new GridLayout(0, 2, 12, 12));
        tipsGrid.setBackground(BG);

        List<HealthTip> tips = ctx.knowledgeBase.getAllHealthTips();
        Color[] colors = {LIGHT_BLUE, LIGHT_GREEN, new Color(253, 235, 208),
                          new Color(232, 218, 239), new Color(209, 236, 241)};
        int ci = 0;
        for (HealthTip tip : tips) {
            JPanel tipCard = new JPanel(new BorderLayout(0, 6));
            tipCard.setBackground(colors[ci % colors.length]);
            tipCard.setBorder(new EmptyBorder(14, 14, 14, 14));

            JLabel tipTitle = new JLabel(tip.getTitle());
            tipTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
            tipTitle.setForeground(SECONDARY);

            JTextArea tipContent = new JTextArea(tip.getContent());
            tipContent.setWrapStyleWord(true);
            tipContent.setLineWrap(true);
            tipContent.setEditable(false);
            tipContent.setBackground(colors[ci % colors.length]);
            tipContent.setFont(new Font("SansSerif", Font.PLAIN, 12));
            tipContent.setForeground(TEXT);
            tipContent.setBorder(null);

            JLabel catLabel = new JLabel(tip.getCategory());
            catLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
            catLabel.setForeground(SUBTEXT);

            tipCard.add(tipTitle, BorderLayout.NORTH);
            tipCard.add(tipContent, BorderLayout.CENTER);
            tipCard.add(catLabel, BorderLayout.SOUTH);

            tipsGrid.add(tipCard);
            ci++;
        }

        JScrollPane scroll = new JScrollPane(tipsGrid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        contentPanel.add(panel);
    }

    // ==================== CONDITIONS VIEW ====================
    private void showConditions() {
        ConditionsUI conditionsUI = new ConditionsUI();
        contentPanel.add(conditionsUI);
    }

    // ==================== QUIZ VIEW ====================
    private void showQuiz() {
        QuizUI quizUI = new QuizUI();
        contentPanel.add(quizUI);
    }

    // ==================== PROFILE VIEW ====================
    private void showProfile() {
        ProfileUI profileUI = new ProfileUI(this);
        contentPanel.add(profileUI);
    }

    // ==================== DAILY CHECK-IN ====================
    public void showDailyCheckIn() {
        JDialog dialog = new JDialog(this, "Daily Health Check-In", true);
        dialog.setSize(450, 480);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 4, 8, 4);
        gbc.gridwidth = 2;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel title = new JLabel("\uD83D\uDCCB  How are you feeling today?");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(SECONDARY);
        panel.add(title, gbc);

        // Sliders
        JSlider energySlider = makeSlider();
        JSlider sleepSlider  = makeSlider();
        JSlider stressSlider = makeSlider();
        JSlider moodSlider   = makeSlider();

        addSliderRow(panel, gbc, "\u26A1 Energy Level", energySlider, 1);
        addSliderRow(panel, gbc, "\uD83C\uDF19 Sleep Quality", sleepSlider, 2);
        addSliderRow(panel, gbc, "\uD83E\uDDD8 Stress Level", stressSlider, 3);
        addSliderRow(panel, gbc, "\uD83D\uDE0A Mood", moodSlider, 4);

        gbc.gridy = 5; gbc.gridwidth = 2;
        JTextField notesField = new JTextField("Any notes for today...");
        notesField.setFont(new Font("SansSerif", Font.ITALIC, 12));
        notesField.setForeground(SUBTEXT);
        notesField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (notesField.getText().equals("Any notes for today...")) {
                    notesField.setText(""); notesField.setForeground(TEXT);
                }
            }
        });
        panel.add(notesField, gbc);

        gbc.gridy = 6;
        JButton saveBtn = new JButton("Save Check-In");
        saveBtn.setBackground(SUCCESS);
        saveBtn.setForeground(WHITE);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            User user = ctx.getCurrentUser();
            if (user != null) {
                HealthCheck check = new HealthCheck(user.getId(), LocalDate.now(),
                    energySlider.getValue(), sleepSlider.getValue(),
                    stressSlider.getValue(), moodSlider.getValue());
                String notes = notesField.getText();
                if (!notes.equals("Any notes for today...")) check.setNotes(notes);

                // Update streak and badges
                ctx.gamificationService.updateStreak(user, LocalDate.now());
                List<String> newBadges = ctx.gamificationService.checkAndAwardBadges(
                    user, ctx.isDbAvailable() ? ctx.healthCheckDAO.findRecentChecks(user.getId(), 7) : java.util.Collections.emptyList(),
                    false, false);

                if (ctx.isDbAvailable()) {
                    ctx.healthCheckDAO.save(check);
                    ctx.userDAO.update(user);
                }

                dialog.dispose();

                // Show wellness score
                String badgeMsg = newBadges.isEmpty() ? "" : "\n\nNew Badges Earned:\n" + String.join("\n", newBadges);
                JOptionPane.showMessageDialog(this,
                    "Check-in saved!\n\nWellness Score: " + check.getWellnessScore() + "/100\n"
                    + "(" + check.getWellnessLabel() + ")" + badgeMsg,
                    "Check-In Complete", JOptionPane.INFORMATION_MESSAGE);

                // Refresh dashboard
                handleNav("dashboard");
            }
        });
        panel.add(saveBtn, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JSlider makeSlider() {
        JSlider s = new JSlider(1, 5, 3);
        s.setMajorTickSpacing(1);
        s.setPaintTicks(true);
        s.setPaintLabels(true);
        s.setBackground(WHITE);
        return s;
    }

    private void addSliderRow(JPanel panel, GridBagConstraints gbc, String labelText, JSlider slider, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(SECONDARY);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(slider, gbc);
    }

    private void doLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            ctx.logout();
            SwingUtilities.invokeLater(() -> {
                new LoginUI().setVisible(true);
                dispose();
            });
        }
    }

    // Public method so child UIs can navigate back
    public void navigateTo(String section) {
        contentPanel.removeAll();
        handleNav(section);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private String getGreeting() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour < 12) return "Good Morning";
        if (hour < 17) return "Good Afternoon";
        return "Good Evening";
    }

    private String getTodaysFact() {
        String[] facts = {
            "The human body contains about 37 trillion cells.",
            "Your heart beats approximately 100,000 times per day.",
            "The average adult brain weighs about 1.4 kg (3 pounds).",
            "Humans shed about 30,000-40,000 dead skin cells every hour.",
            "The small intestine is about 6 meters (20 feet) long.",
            "Your lungs can hold about 6 liters of air at maximum capacity.",
            "It takes about 12 hours for food to fully digest in the stomach.",
            "The human eye can distinguish about 10 million different colors.",
            "Adults have 206 bones; babies are born with about 270.",
            "The largest organ in the human body is the skin.",
            "Yawning helps cool the brain and increase alertness.",
            "Laughing lowers stress hormones and strengthens the immune system."
        };
        int idx = LocalDate.now().getDayOfYear() % facts.length;
        return "\uD83E\uDDE0 " + facts[idx];
    }
}
