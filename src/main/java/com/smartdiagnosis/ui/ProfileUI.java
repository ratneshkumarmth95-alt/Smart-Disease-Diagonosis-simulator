package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ProfileUI - Displays and edits user profile, metrics (BMI), badges, and streak.
 */
public class ProfileUI extends JPanel {

    private final AppContext ctx = AppContext.getInstance();
    private final DashboardUI parent;

    private static final Color PRIMARY   = DashboardUI.PRIMARY;
    private static final Color SECONDARY = DashboardUI.SECONDARY;
    private static final Color BG        = DashboardUI.BG;
    private static final Color WHITE     = DashboardUI.WHITE;
    private static final Color SUCCESS   = DashboardUI.SUCCESS;
    private static final Color TEXT      = DashboardUI.TEXT;
    private static final Color SUBTEXT   = DashboardUI.SUBTEXT;

    private JTextField nameField, ageField, heightField, weightField, bloodField, allergiesField, conditionsField, contactField;
    private JComboBox<String> genderBox;

    public ProfileUI(DashboardUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout(0, 15));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);

        JLabel title = new JLabel("\uD83D\uDC64 My Health Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(SECONDARY);
        header.add(title, BorderLayout.WEST);

        User user = ctx.getCurrentUser();
        if (user != null) {
            JLabel badgeCount = new JLabel("\uD83C\uDFC6 Badges: " + user.getBadges().size() + " | \uD83D\uDD25 Streak: " + user.getStreakDays() + " days");
            badgeCount.setFont(new Font("SansSerif", Font.BOLD, 13));
            badgeCount.setForeground(PRIMARY);
            header.add(badgeCount, BorderLayout.EAST);
        }
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Badges section
        if (user != null && !user.getBadges().isEmpty()) {
            JPanel badgeSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            badgeSection.setBackground(new Color(245, 247, 250));
            badgeSection.setBorder(BorderFactory.createTitledBorder("Earned Badges"));
            badgeSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
            for (String b : user.getBadges()) {
                JLabel bLabel = new JLabel(b);
                bLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
                bLabel.setForeground(PRIMARY);
                bLabel.setBorder(new EmptyBorder(2, 6, 2, 6));
                badgeSection.add(bLabel);
            }
            mainPanel.add(badgeSection);
            mainPanel.add(Box.createVerticalStrut(15));
        }

        // Form Fields
        JPanel form = new JPanel(new GridLayout(9, 2, 10, 10));
        form.setBackground(WHITE);

        nameField = new JTextField(user != null ? user.getName() : "");
        ageField = new JTextField(user != null ? String.valueOf(user.getAge()) : "");
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other", "Prefer not to say"});
        if (user != null && user.getGender() != null) genderBox.setSelectedItem(user.getGender());

        heightField = new JTextField(user != null && user.getHeight() > 0 ? String.valueOf(user.getHeight()) : "");
        weightField = new JTextField(user != null && user.getWeight() > 0 ? String.valueOf(user.getWeight()) : "");
        bloodField = new JTextField(user != null && user.getBloodGroup() != null ? user.getBloodGroup() : "");
        allergiesField = new JTextField(user != null && user.getAllergies() != null ? user.getAllergies() : "");
        conditionsField = new JTextField(user != null && user.getExistingConditions() != null ? user.getExistingConditions() : "");
        contactField = new JTextField(user != null && user.getEmergencyContact() != null ? user.getEmergencyContact() : "");

        form.add(fieldLabel("Full Name:"));
        form.add(nameField);
        form.add(fieldLabel("Age:"));
        form.add(ageField);
        form.add(fieldLabel("Gender:"));
        form.add(genderBox);
        form.add(fieldLabel("Height (cm):"));
        form.add(heightField);
        form.add(fieldLabel("Weight (kg):"));
        form.add(weightField);
        form.add(fieldLabel("Blood Group (optional):"));
        form.add(bloodField);
        form.add(fieldLabel("Known Allergies:"));
        form.add(allergiesField);
        form.add(fieldLabel("Existing Conditions:"));
        form.add(conditionsField);
        form.add(fieldLabel("Emergency Contact:"));
        form.add(contactField);

        mainPanel.add(form);
        mainPanel.add(Box.createVerticalStrut(15));

        // BMI display
        if (user != null && user.getBMI() > 0) {
            JLabel bmiLabel = new JLabel("Calculated BMI: " + user.getBMI() + " (" + user.getBMICategory() + ")");
            bmiLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            bmiLabel.setForeground(SUCCESS);
            mainPanel.add(bmiLabel);
            mainPanel.add(Box.createVerticalStrut(10));
        }

        JButton saveBtn = new JButton("Save Profile Changes");
        saveBtn.setBackground(PRIMARY);
        saveBtn.setForeground(WHITE);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> saveProfile());
        mainPanel.add(saveBtn);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        add(scroll, BorderLayout.CENTER);
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(SECONDARY);
        return l;
    }

    private void saveProfile() {
        User u = ctx.getCurrentUser();
        if (u == null) return;

        try {
            u.setName(nameField.getText().trim());
            u.setAge(Integer.parseInt(ageField.getText().trim()));
            u.setGender((String) genderBox.getSelectedItem());

            if (!heightField.getText().isBlank()) u.setHeight(Double.parseDouble(heightField.getText().trim()));
            if (!weightField.getText().isBlank()) u.setWeight(Double.parseDouble(weightField.getText().trim()));

            u.setBloodGroup(bloodField.getText().trim());
            u.setAllergies(allergiesField.getText().trim());
            u.setExistingConditions(conditionsField.getText().trim());
            u.setEmergencyContact(contactField.getText().trim());

            if (ctx.isDbAvailable()) {
                ctx.userDAO.update(u);
            }

            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
            if (parent != null) parent.navigateTo("profile");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Age, Height, and Weight.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
