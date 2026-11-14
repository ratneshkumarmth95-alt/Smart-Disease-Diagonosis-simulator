package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.User;
import com.smartdiagnosis.utils.PasswordUtils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginUI - Login and Registration screen.
 * First screen the user sees. Clean, modern card layout.
 * Demonstrates: Swing UI, Event Handling, Validation
 */
public class LoginUI extends JFrame {

    private final AppContext ctx = AppContext.getInstance();

    // Colors
    private static final Color PRIMARY   = new Color(41, 128, 185);
    private static final Color SECONDARY = new Color(44, 62, 80);
    private static final Color BG        = new Color(236, 240, 241);
    private static final Color WHITE     = Color.WHITE;
    private static final Color SUCCESS   = new Color(39, 174, 96);
    private static final Color DANGER    = new Color(192, 57, 43);
    private static final Color TEXT      = new Color(44, 62, 80);

    // Components
    private JTabbedPane tabbedPane;
    // Login tab
    private JTextField loginUsername;
    private JPasswordField loginPassword;
    private JLabel loginStatus;
    // Register tab
    private JTextField regUsername, regName, regAge;
    private JComboBox<String> regGender;
    private JPasswordField regPassword, regPassword2;
    private JLabel regStatus;

    public LoginUI() {
        setTitle("Smart Disease Diagnosis Simulator — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(BG);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ---- HEADER ----
        JPanel header = new JPanel(new GridLayout(3, 1, 0, 4));
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel appIcon = new JLabel("\u2695\uFE0F Smart Disease Diagnosis Simulator", SwingConstants.CENTER);
        appIcon.setFont(new Font("SansSerif", Font.BOLD, 20));
        appIcon.setForeground(WHITE);

        JLabel subtitle = new JLabel("Educational Health Symptom Analysis System", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(174, 214, 241));

        JLabel disclaimer = new JLabel("Not a medical device \u2022 Always consult a doctor", SwingConstants.CENTER);
        disclaimer.setFont(new Font("SansSerif", Font.ITALIC, 11));
        disclaimer.setForeground(new Color(174, 214, 241));

        header.add(appIcon);
        header.add(subtitle);
        header.add(disclaimer);
        root.add(header, BorderLayout.NORTH);

        // ---- TABBED PANE ----
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.setBackground(WHITE);
        tabbedPane.addTab("  Login  ", buildLoginPanel());
        tabbedPane.addTab("  Register  ", buildRegisterPanel());

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(new EmptyBorder(20, 30, 20, 30));
        center.setBackground(BG);
        center.add(tabbedPane);
        root.add(center, BorderLayout.CENTER);

        // ---- FOOTER ----
        JLabel footer = new JLabel("v1.0 \u2022 Educational Project \u2022 2026", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.ITALIC, 11));
        footer.setForeground(Color.GRAY);
        footer.setBorder(new EmptyBorder(0, 0, 10, 0));
        root.add(footer, BorderLayout.SOUTH);

        add(root);
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcome.setForeground(SECONDARY);
        panel.add(welcome, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        panel.add(label("Username:"), gbc);
        gbc.gridx = 1;
        loginUsername = styledField();
        panel.add(loginUsername, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(label("Password:"), gbc);
        gbc.gridx = 1;
        loginPassword = styledPassword();
        panel.add(loginPassword, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        loginStatus = new JLabel(" ");
        loginStatus.setFont(new Font("SansSerif", Font.BOLD, 12));
        loginStatus.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(loginStatus, gbc);

        gbc.gridy++;
        JButton loginBtn = bigButton("  Login  ", PRIMARY);
        loginBtn.addActionListener(e -> doLogin());
        panel.add(loginBtn, gbc);

        // Allow Enter key to trigger login
        loginPassword.addActionListener(e -> doLogin());
        loginUsername.addActionListener(e -> loginPassword.requestFocus());

        // Demo button
        gbc.gridy++;
        JButton demoBtn = new JButton("Try Demo (no account needed)");
        demoBtn.setForeground(PRIMARY);
        demoBtn.setBorderPainted(false);
        demoBtn.setContentAreaFilled(false);
        demoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        demoBtn.addActionListener(e -> openDashboard(createDemoUser()));
        panel.add(demoBtn, gbc);

        return panel;
    }

    private JPanel buildRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 4, 5, 4);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(SECONDARY);
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        String[] fields = {"Full Name:", "Username:", "Age:", "Gender:", "Password:", "Confirm Password:"};

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(label("Full Name:"), gbc);
        gbc.gridx = 1;
        regName = styledField();
        panel.add(regName, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(label("Username:"), gbc);
        gbc.gridx = 1;
        regUsername = styledField();
        panel.add(regUsername, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(label("Age:"), gbc);
        gbc.gridx = 1;
        regAge = styledField();
        panel.add(regAge, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(label("Gender:"), gbc);
        gbc.gridx = 1;
        regGender = new JComboBox<>(new String[]{"Prefer not to say", "Male", "Female", "Other"});
        regGender.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(regGender, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(label("Password:"), gbc);
        gbc.gridx = 1;
        regPassword = styledPassword();
        panel.add(regPassword, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(label("Confirm Password:"), gbc);
        gbc.gridx = 1;
        regPassword2 = styledPassword();
        panel.add(regPassword2, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        regStatus = new JLabel(" ");
        regStatus.setFont(new Font("SansSerif", Font.BOLD, 12));
        regStatus.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(regStatus, gbc);

        gbc.gridy++;
        JButton regBtn = bigButton("  Create Account  ", SUCCESS);
        regBtn.addActionListener(e -> doRegister());
        panel.add(regBtn, gbc);

        return panel;
    }

    private void doLogin() {
        String username = loginUsername.getText().trim();
        String password = new String(loginPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showStatus(loginStatus, "Please enter username and password.", DANGER);
            return;
        }

        // Try database first
        if (ctx.isDbAvailable()) {
            var userOpt = ctx.userDAO.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                    openDashboard(user);
                    return;
                }
            }
            showStatus(loginStatus, "Invalid username or password.", DANGER);
        } else {
            // Offline mode: accept any login (demo)
            User demo = createDemoUser();
            demo.setName(username.substring(0, 1).toUpperCase() + username.substring(1));
            showStatus(loginStatus, "Offline mode: logging in as " + demo.getName(), SUCCESS);
            Timer t = new Timer(800, ev -> openDashboard(demo));
            t.setRepeats(false); t.start();
        }
    }

    private void doRegister() {
        String name     = regName.getText().trim();
        String username = regUsername.getText().trim();
        String ageStr   = regAge.getText().trim();
        String gender   = (String) regGender.getSelectedItem();
        String pass     = new String(regPassword.getPassword());
        String pass2    = new String(regPassword2.getPassword());

        // Validation
        if (name.isEmpty() || username.isEmpty() || ageStr.isEmpty() || pass.isEmpty()) {
            showStatus(regStatus, "Please fill in all required fields.", DANGER);
            return;
        }
        if (!PasswordUtils.isValidUsername(username)) {
            showStatus(regStatus, "Username: 3+ chars, letters/numbers/underscore only.", DANGER);
            return;
        }
        if (!PasswordUtils.isValidPassword(pass)) {
            showStatus(regStatus, "Password must be at least 6 characters.", DANGER);
            return;
        }
        if (!pass.equals(pass2)) {
            showStatus(regStatus, "Passwords do not match.", DANGER);
            return;
        }
        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age < 1 || age > 120) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showStatus(regStatus, "Please enter a valid age (1-120).", DANGER);
            return;
        }

        User newUser = new User(username, PasswordUtils.hashPassword(pass), name, age, gender);

        if (ctx.isDbAvailable()) {
            if (ctx.userDAO.usernameExists(username)) {
                showStatus(regStatus, "Username already taken. Choose another.", DANGER);
                return;
            }
            int id = ctx.userDAO.save(newUser);
            if (id > 0) {
                showStatus(regStatus, "Account created! Logging in...", SUCCESS);
                Timer t = new Timer(800, ev -> openDashboard(newUser));
                t.setRepeats(false); t.start();
            } else {
                showStatus(regStatus, "Error creating account. Please try again.", DANGER);
            }
        } else {
            // Offline mode: just proceed
            showStatus(regStatus, "Offline mode: account created locally.", SUCCESS);
            Timer t = new Timer(800, ev -> openDashboard(newUser));
            t.setRepeats(false); t.start();
        }
    }

    private User createDemoUser() {
        User demo = new User("demo", "demo", "Demo User", 20, "Other");
        demo.setId(0);
        demo.setExistingConditions("None");
        demo.setAllergies("None");
        return demo;
    }

    private void openDashboard(User user) {
        ctx.setCurrentUser(user);
        SwingUtilities.invokeLater(() -> {
            new DashboardUI().setVisible(true);
            dispose();
        });
    }

    // ===== Helper builders =====
    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(SECONDARY);
        return l;
    }

    private JTextField styledField() {
        JTextField f = new JTextField(18);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(6, 8, 6, 8)));
        return f;
    }

    private JPasswordField styledPassword() {
        JPasswordField f = new JPasswordField(18);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(6, 8, 6, 8)));
        return f;
    }

    private JButton bigButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        return btn;
    }

    private void showStatus(JLabel label, String msg, Color color) {
        label.setText(msg);
        label.setForeground(color);
    }
}
