package com.smartdiagnosis.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseConnection - Singleton pattern for managing MySQL JDBC connection.
 *
 * HOW TO SET UP:
 * 1. Install MySQL (https://dev.mysql.com/downloads/)
 * 2. Create database: CREATE DATABASE smart_diagnosis_db;
 * 3. Set DB_USER and DB_PASSWORD below to match your MySQL credentials
 * 4. Add mysql-connector-java-8.x.x.jar to your project's lib/ folder
 *
 * Demonstrates: Singleton Design Pattern, JDBC, Exception Handling
 */
public class DatabaseConnection {

    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    // ===== CONFIGURE THESE TO MATCH YOUR MYSQL SETUP =====
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/smart_diagnosis_db"
                                             + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";  // <-- Set your MySQL password here
    // ======================================================

    private static DatabaseConnection instance;
    private Connection connection;

    // Private constructor — prevents external instantiation (Singleton)
    private DatabaseConnection() {}

    /** Returns the single shared instance of DatabaseConnection */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Get a live database connection. Creates one if none exists.
     * @throws SQLException if the connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                logger.info("Database connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "MySQL JDBC Driver not found! Please add mysql-connector-java.jar to lib/ folder.", e);
            throw new SQLException("MySQL JDBC Driver not found. Please add mysql-connector-java.jar to your project.", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to connect to database: " + e.getMessage(), e);
            throw e;
        }
        return connection;
    }

    /** Close the database connection */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing database connection.", e);
        }
    }

    /** Check if currently connected */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
