package com.smartdiagnosis.ui;

import com.smartdiagnosis.main.AppContext;
import com.smartdiagnosis.model.Disease;
import com.smartdiagnosis.model.DiseaseSymptomLink;
import com.smartdiagnosis.model.Symptom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ConditionsUI - Educational reference cards for all diseases in the KnowledgeBase.
 */
public class ConditionsUI extends JPanel {

    private final AppContext ctx = AppContext.getInstance();
    private static final Color PRIMARY   = DashboardUI.PRIMARY;
    private static final Color SECONDARY = DashboardUI.SECONDARY;
    private static final Color BG        = DashboardUI.BG;
    private static final Color WHITE     = DashboardUI.WHITE;
    private static final Color TEXT      = DashboardUI.TEXT;
    private static final Color SUBTEXT   = DashboardUI.SUBTEXT;

    private JPanel gridPanel;
    private JTextField searchField;

    public ConditionsUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBackground(BG);

        JLabel title = new JLabel("\uD83D\uDCD6 Medical Conditions Knowledge Base");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(SECONDARY);
        header.add(title, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setBackground(BG);
        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(6, 10, 6, 10)));
        searchField.addActionListener(e -> filterConditions(searchField.getText().trim()));

        JButton searchBtn = new JButton("Filter");
        searchBtn.setBackground(PRIMARY);
        searchBtn.setForeground(WHITE);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> filterConditions(searchField.getText().trim()));

        searchBar.add(new JLabel("\uD83D\uDD0D Search Condition: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtn, BorderLayout.EAST);
        header.add(searchBar, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setBackground(BG);

        filterConditions("");

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void filterConditions(String query) {
        gridPanel.removeAll();
        List<Disease> diseases = ctx.knowledgeBase.getAllDiseases();

        for (Disease d : diseases) {
            if (!query.isEmpty() &&
                    !d.getName().toLowerCase().contains(query.toLowerCase()) &&
                    !d.getCategory().toLowerCase().contains(query.toLowerCase()) &&
                    !d.getDescription().toLowerCase().contains(query.toLowerCase())) {
                continue;
            }

            JPanel card = new JPanel(new BorderLayout(0, 8));
            card.setBackground(WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(218, 225, 231), 1),
                    new EmptyBorder(15, 15, 15, 15)
            ));

            JPanel top = new JPanel(new BorderLayout());
            top.setBackground(WHITE);
            JLabel name = new JLabel(d.getName());
            name.setFont(new Font("SansSerif", Font.BOLD, 15));
            name.setForeground(PRIMARY);

            JLabel cat = new JLabel(d.getCategory() + " | " + d.getSeverityLevel() + " SEVERITY");
            cat.setFont(new Font("SansSerif", Font.BOLD, 11));
            cat.setForeground("HIGH".equals(d.getSeverityLevel()) ? DashboardUI.DANGER : SUBTEXT);

            top.add(name, BorderLayout.WEST);
            top.add(cat, BorderLayout.EAST);

            JTextArea desc = new JTextArea(d.getDescription());
            desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
            desc.setForeground(TEXT);
            desc.setLineWrap(true);
            desc.setWrapStyleWord(true);
            desc.setEditable(false);
            desc.setBackground(WHITE);

            List<String> keySymptoms = new ArrayList<>();
            for (DiseaseSymptomLink link : d.getSymptomLinks()) {
                Symptom s = ctx.knowledgeBase.getSymptomById(link.getSymptomId());
                if (s != null) keySymptoms.add(s.getName());
            }
            JLabel syms = new JLabel("<html><b>Common Symptoms:</b> " + String.join(", ", keySymptoms) + "</html>");
            syms.setFont(new Font("SansSerif", Font.PLAIN, 11));
            syms.setForeground(SECONDARY);

            card.add(top, BorderLayout.NORTH);
            card.add(desc, BorderLayout.CENTER);
            card.add(syms, BorderLayout.SOUTH);

            gridPanel.add(card);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
