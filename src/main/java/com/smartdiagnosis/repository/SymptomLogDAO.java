package com.smartdiagnosis.repository;

import com.smartdiagnosis.database.DatabaseConnection;
import com.smartdiagnosis.model.SymptomLog;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;

/**
 * SymptomLogDAO - Saves and retrieves symptom tracking logs.
 * Demonstrates: DAO Pattern, JDBC, PreparedStatement
 */
public class SymptomLogDAO {

    private static final Logger logger = Logger.getLogger(SymptomLogDAO.class.getName());

    public boolean save(SymptomLog log) {
        String sql = "INSERT INTO symptom_logs (user_id, symptom_id, symptom_name, log_date, " +
                     "severity, duration, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, log.getUserId());
            ps.setInt(2, log.getSymptomId());
            ps.setString(3, log.getSymptomName());
            ps.setDate(4, Date.valueOf(log.getLogDate()));
            ps.setInt(5, log.getSeverity());
            ps.setString(6, log.getDuration());
            ps.setString(7, log.getNotes());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) log.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving symptom log: " + e.getMessage(), e);
        }
        return false;
    }

    /** Get recent symptom logs for a user */
    public List<SymptomLog> findRecentLogs(int userId, int days) {
        List<SymptomLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM symptom_logs WHERE user_id = ? " +
                     "ORDER BY log_date DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, days * 10); // rough limit
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) logs.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching symptom logs: " + e.getMessage(), e);
        }
        return logs;
    }

    /** Get logs for a specific symptom over time (for tracking trend) */
    public List<SymptomLog> findBySymptomName(int userId, String symptomName) {
        List<SymptomLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM symptom_logs WHERE user_id = ? AND symptom_name = ? " +
                     "ORDER BY log_date DESC LIMIT 30";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, symptomName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) logs.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching symptom logs by name: " + e.getMessage(), e);
        }
        return logs;
    }

    private SymptomLog mapRow(ResultSet rs) throws SQLException {
        SymptomLog log = new SymptomLog();
        log.setId(rs.getInt("id"));
        log.setUserId(rs.getInt("user_id"));
        log.setSymptomId(rs.getInt("symptom_id"));
        log.setSymptomName(rs.getString("symptom_name"));
        Date date = rs.getDate("log_date");
        if (date != null) log.setLogDate(date.toLocalDate());
        log.setSeverity(rs.getInt("severity"));
        log.setDuration(rs.getString("duration"));
        log.setNotes(rs.getString("notes"));
        return log;
    }
}
