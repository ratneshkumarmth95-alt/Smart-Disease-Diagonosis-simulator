package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.JournalEntry;
import com.smartdiagnosis.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * JournalUI - Allows users to log personal reflections, daily health notes, and track mood.
 */
public class JournalUI extends JPanel {

    private final AppContext ctx = AppContext.getInstance();
    private static final Color PRIMARY   = DashboardUI.PRIMARY;
    private static final Color SECONDARY = DashboardUI.SECONDARY;
    private static final Color BG        = DashboardUI.BG;
    private static final Color WHITE     = DashboardUI.WHITE;
    private static final Color TEXT      = DashboardUI.TEXT;
    private static final Color SUBTEXT   = DashboardUI.SUBTEXT;

    private JPanel entriesListPanel;

    public JournalUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);

        JLabel title = new JLabel("\uD83D\uDCD3 Health & Wellness Journal");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(SECONDARY);
        header.add(title, BorderLayout.WEST);

        JButton newEntryBtn = new JButton("+ New Entry");
        newEntryBtn.setBackground(PRIMARY);
        newEntryBtn.setForeground(WHITE);
        newEntryBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        newEntryBtn.setFocusPainted(false);
        newEntryBtn.setBorderPainted(false);
        newEntryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newEntryBtn.addActionListener(e -> showNewEntryDialog());
        header.add(newEntryBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        entriesListPanel = new JPanel();
        entriesListPanel.setLayout(new BoxLayout(entriesListPanel, BoxLayout.Y_AXIS));
        entriesListPanel.setBackground(WHITE);

        refreshEntries();

        JScrollPane scroll = new JScrollPane(entriesListPanel);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void refreshEntries() {
        entriesListPanel.removeAll();
        User user = ctx.getCurrentUser();
        List<JournalEntry> entries = (ctx.isDbAvailable() && user != null)
                ? ctx.journalDAO.findByUser(user.getId())
                : new ArrayList<>();

        if (entries.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            emptyPanel.setBackground(WHITE);
            emptyPanel.setBorder(new EmptyBorder(40, 20, 40, 20));
            JLabel emptyLabel = new JLabel("No journal entries yet. Click '+ New Entry' to record your thoughts!");
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            emptyLabel.setForeground(SUBTEXT);
            emptyPanel.add(emptyLabel);
            entriesListPanel.add(emptyPanel);
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            for (JournalEntry entry : entries) {
                JPanel card = new JPanel(new BorderLayout(8, 8));
                card.setBackground(WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                        new EmptyBorder(12, 15, 12, 15)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

                String dateStr = entry.getEntryDate() != null ? entry.getEntryDate().format(dtf) : "Recent";
                JLabel titleLbl = new JLabel(entry.getMoodEmoji() + " " + entry.getTitle() + "  \u2022  " + dateStr);
                titleLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
                titleLbl.setForeground(PRIMARY);

                JTextArea contentArea = new JTextArea(entry.getContent());
                contentArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
                contentArea.setForeground(TEXT);
                contentArea.setLineWrap(true);
                contentArea.setWrapStyleWord(true);
                contentArea.setEditable(false);
                contentArea.setBackground(WHITE);

                card.add(titleLbl, BorderLayout.NORTH);
                card.add(contentArea, BorderLayout.CENTER);
                entriesListPanel.add(card);
            }
        }
        entriesListPanel.revalidate();
        entriesListPanel.repaint();
    }

    private void showNewEntryDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Journal Entry", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(4, 1, 5, 5));
        form.setBackground(WHITE);

        JTextField titleField = new JTextField();
        titleField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        form.add(new JLabel("Title / Topic:"));
        form.add(titleField);

        JComboBox<String> moodBox = new JComboBox<>(new String[]{"GREAT", "GOOD", "OKAY", "BAD", "TERRIBLE"});
        form.add(new JLabel("Mood:"));
        form.add(moodBox);

        panel.add(form, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea();
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Notes / Reflection:"));
        panel.add(scroll, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Entry");
        saveBtn.setBackground(PRIMARY);
        saveBtn.setForeground(WHITE);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        saveBtn.addActionListener(e -> {
            String t = titleField.getText().trim();
            String c = contentArea.getText().trim();
            if (t.isEmpty() || c.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter both title and content.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            User u = ctx.getCurrentUser();
            if (u != null && ctx.isDbAvailable()) {
                JournalEntry entry = new JournalEntry(u.getId(), t, c, (String) moodBox.getSelectedItem());
                ctx.journalDAO.save(entry);
                ctx.gamificationService.checkAndAwardBadges(u, null, false, true);
            }
            dialog.dispose();
            refreshEntries();
        });

        panel.add(saveBtn, BorderLayout.SOUTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }
}
