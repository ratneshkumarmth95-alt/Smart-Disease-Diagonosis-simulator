package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.DiagnosisResult;
import com.smartdiagnosis.model.Symptom;
import com.smartdiagnosis.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * HistoryUI - Displays past symptom analysis records.
 */
public class HistoryUI extends JPanel {

    private final AppContext ctx = AppContext.getInstance();
    private static final Color PRIMARY   = DashboardUI.PRIMARY;
    private static final Color SECONDARY = DashboardUI.SECONDARY;
    private static final Color BG        = DashboardUI.BG;
    private static final Color WHITE     = DashboardUI.WHITE;
    private static final Color TEXT      = DashboardUI.TEXT;
    private static final Color SUBTEXT   = DashboardUI.SUBTEXT;

    public HistoryUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);

        JLabel title = new JLabel("\uD83D\uDCDC Health Analysis History");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(SECONDARY);
        header.add(title, BorderLayout.WEST);

        User user = ctx.getCurrentUser();
        int monthlyCount = (ctx.isDbAvailable() && user != null) ? ctx.historyDAO.countThisMonth(user.getId()) : 0;
        JLabel stats = new JLabel("Analyses this month: " + monthlyCount);
        stats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        stats.setForeground(SUBTEXT);
        header.add(stats, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(WHITE);

        List<DiagnosisResult> history = (ctx.isDbAvailable() && user != null)
                ? ctx.historyDAO.findByUser(user.getId())
                : new ArrayList<>();

        if (history.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            emptyPanel.setBackground(WHITE);
            emptyPanel.setBorder(new EmptyBorder(40, 20, 40, 20));
            JLabel emptyLabel = new JLabel("No past diagnosis records found. Run a Symptom Check to see history here!");
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            emptyLabel.setForeground(SUBTEXT);
            emptyPanel.add(emptyLabel);
            listPanel.add(emptyPanel);
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            for (DiagnosisResult r : history) {
                JPanel card = new JPanel(new BorderLayout(10, 8));
                card.setBackground(WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                        new EmptyBorder(12, 15, 12, 15)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

                String dateStr = r.getAnalysisDate() != null ? r.getAnalysisDate().format(dtf) : "Recent";
                JLabel dateLbl = new JLabel(dateStr);
                dateLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                dateLbl.setForeground(PRIMARY);

                String symList = r.getSymptomsAsString();
                if (symList.isBlank() && r.getSelectedSymptoms() != null) {
                    List<String> names = new ArrayList<>();
                    for (Symptom s : r.getSelectedSymptoms()) names.add(s.getName());
                    symList = String.join(", ", names);
                }
                JLabel symLbl = new JLabel("Symptoms: " + (symList.isBlank() ? "N/A" : symList));
                symLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
                symLbl.setForeground(TEXT);

                String topMatch = r.isEmergencyWarning() ? "\u26A0 EMERGENCY WARNING" : r.getTopMatchName();
                JLabel matchLbl = new JLabel("Top Match: " + topMatch);
                matchLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                matchLbl.setForeground(r.isEmergencyWarning() ? DashboardUI.DANGER : DashboardUI.SUCCESS);

                JPanel left = new JPanel(new GridLayout(2, 1, 0, 4));
                left.setBackground(WHITE);
                left.add(dateLbl);
                left.add(symLbl);

                JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                right.setBackground(WHITE);
                right.add(matchLbl);

                card.add(left, BorderLayout.CENTER);
                card.add(right, BorderLayout.EAST);
                listPanel.add(card);
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }
}
