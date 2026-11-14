package com.smartdiagnosis.main;

import com.smartdiagnosis.database.DatabaseInitializer;
import com.smartdiagnosis.ui.LoginUI;

import javax.swing.*;

/**
 * Main - Entry point for the Smart Disease Diagnosis Simulator application.
 *
 * It initializes:
 * 1. Native look-and-feel
 * 2. Database connection check & schema setup
 * 3. Launches the Login & Registration UI
 */
public class Main {

    public static void main(String[] args) {
        // Set Look and Feel to System default for crisp appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Attempt database initialization in background
        boolean dbOk = DatabaseInitializer.initializeDatabase();
        AppContext.getInstance().setDbAvailable(dbOk);

        if (dbOk) {
            System.out.println("[INFO] Connected to MySQL Database successfully.");
        } else {
            System.out.println("[INFO] Running in Offline/In-Memory Mode (no MySQL configured).");
        }

        // Launch UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginUI login = new LoginUI();
            login.setVisible(true);
        });
    }
}
