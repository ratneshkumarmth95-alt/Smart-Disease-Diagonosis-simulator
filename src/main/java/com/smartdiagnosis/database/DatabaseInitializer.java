package com.smartdiagnosis.database;

import java.sql.*;
import java.util.logging.*;

/**
 * DatabaseInitializer - Creates all required tables if they don't already exist.
 * Uses CREATE TABLE IF NOT EXISTS, so it's safe to run on every startup.
 * Demonstrates: JDBC, DDL statements, Exception Handling
 */
public class DatabaseInitializer {

    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());

    /**
     * Initialize the database — creates all tables.
     * @return true if successful, false if database unavailable
     */
    public static boolean initializeDatabase() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            if (conn != null) {
                createTables(conn);
                logger.info("Database initialized successfully.");
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Database not available: " + e.getMessage()
                + "\nApp will run in offline mode (no data persistence).", e);
        }
        return false;
    }

    private static void createTables(Connection conn) throws SQLException {
        String[] tableStatements = {

            // === USERS TABLE ===
            "CREATE TABLE IF NOT EXISTS users (" +
            "  id               INT AUTO_INCREMENT PRIMARY KEY," +
            "  username         VARCHAR(50)  UNIQUE NOT NULL," +
            "  password_hash    VARCHAR(255) NOT NULL," +
            "  name             VARCHAR(100) NOT NULL," +
            "  age              INT," +
            "  gender           VARCHAR(20)," +
            "  height           DOUBLE," +
            "  weight           DOUBLE," +
            "  blood_group      VARCHAR(10)," +
            "  date_of_birth    DATE," +
            "  allergies        TEXT," +
            "  existing_conditions TEXT," +
            "  emergency_contact VARCHAR(200)," +
            "  created_at       DATE," +
            "  streak_days      INT  DEFAULT 0," +
            "  total_check_ins  INT  DEFAULT 0," +
            "  badges           TEXT DEFAULT ''," +
            "  last_check_in    DATE" +
            ")",

            // === DIAGNOSIS HISTORY TABLE ===
            "CREATE TABLE IF NOT EXISTS diagnosis_history (" +
            "  id               INT AUTO_INCREMENT PRIMARY KEY," +
            "  user_id          INT  NOT NULL," +
            "  analysis_date    DATETIME," +
            "  symptoms_selected TEXT," +
            "  top_match_name   VARCHAR(100)," +
            "  top_match_score  DOUBLE," +
            "  all_matches_json TEXT," +
            "  emergency_warning BOOLEAN DEFAULT FALSE," +
            "  overall_severity VARCHAR(20)," +
            "  duration         VARCHAR(50)," +
            "  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
            ")",

            // === HEALTH CHECKS (daily check-in) TABLE ===
            "CREATE TABLE IF NOT EXISTS health_checks (" +
            "  id              INT AUTO_INCREMENT PRIMARY KEY," +
            "  user_id         INT NOT NULL," +
            "  check_date      DATE NOT NULL," +
            "  energy_level    INT," +
            "  sleep_quality   INT," +
            "  stress_level    INT," +
            "  mood_level      INT," +
            "  notes           TEXT," +
            "  wellness_score  INT," +
            "  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
            "  UNIQUE KEY unique_user_date (user_id, check_date)" +
            ")",

            // === SYMPTOM LOGS (tracker) TABLE ===
            "CREATE TABLE IF NOT EXISTS symptom_logs (" +
            "  id           INT AUTO_INCREMENT PRIMARY KEY," +
            "  user_id      INT NOT NULL," +
            "  symptom_id   INT," +
            "  symptom_name VARCHAR(100)," +
            "  log_date     DATE NOT NULL," +
            "  severity     INT," +
            "  duration     VARCHAR(50)," +
            "  notes        TEXT," +
            "  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
            ")",

            // === HEALTH JOURNAL TABLE ===
            "CREATE TABLE IF NOT EXISTS health_journal (" +
            "  id          INT AUTO_INCREMENT PRIMARY KEY," +
            "  user_id     INT NOT NULL," +
            "  entry_date  DATETIME," +
            "  title       VARCHAR(200)," +
            "  content     TEXT," +
            "  mood        VARCHAR(20)," +
            "  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
            ")"
        };

        for (String sql : tableStatements) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error creating table: " + e.getMessage(), e);
                throw e;
            }
        }
        logger.info("All tables created/verified successfully.");
    }
}
