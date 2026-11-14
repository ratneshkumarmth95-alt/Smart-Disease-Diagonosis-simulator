package com.smartdiagnosis.repository;

import com.smartdiagnosis.database.DatabaseConnection;
import com.smartdiagnosis.model.HealthCheck;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;

/**
 * HealthCheckDAO - Database operations for daily wellness check-ins.
 * Demonstrates: DAO Pattern, JDBC, PreparedStatement, Date handling
 */
public class HealthCheckDAO {

    private static final Logger logger = Logger.getLogger(HealthCheckDAO.class.getName());

    /** Save a new health check-in. Returns true if saved successfully. */
    public boolean save(HealthCheck check) {
        String sql = "INSERT INTO health_checks (user_id, check_date, energy_level, sleep_quality, " +
                     "stress_level, mood_level, notes, wellness_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE energy_level=VALUES(energy_level), " +
                     "sleep_quality=VALUES(sleep_quality), stress_level=VALUES(stress_level), " +
                     "mood_level=VALUES(mood_level), notes=VALUES(notes), wellness_score=VALUES(wellness_score)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, check.getUserId());
            ps.setDate(2, Date.valueOf(check.getCheckDate()));
            ps.setInt(3, check.getEnergyLevel());
            ps.setInt(4, check.getSleepQuality());
            ps.setInt(5, check.getStressLevel());
            ps.setInt(6, check.getMoodLevel());
            ps.setString(7, check.getNotes());
            ps.setInt(8, check.getWellnessScore());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) check.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving health check: " + e.getMessage(), e);
        }
        return false;
    }

    /** Get today's check-in for a user */
    public Optional<HealthCheck> findTodayCheck(int userId) {
        return findByDate(userId, LocalDate.now());
    }

    /** Get check-in for a specific date */
    public Optional<HealthCheck> findByDate(int userId, LocalDate date) {
        String sql = "SELECT * FROM health_checks WHERE user_id = ? AND check_date = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding check by date: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /** Get recent check-ins for a user (last N days) */
    public List<HealthCheck> findRecentChecks(int userId, int days) {
        List<HealthCheck> checks = new ArrayList<>();
        String sql = "SELECT * FROM health_checks WHERE user_id = ? " +
                     "ORDER BY check_date DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, days);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) checks.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding recent checks: " + e.getMessage(), e);
        }
        return checks;
    }

    /** Check if user has checked in today */
    public boolean hasCheckedInToday(int userId) {
        return findTodayCheck(userId).isPresent();
    }

    private HealthCheck mapRow(ResultSet rs) throws SQLException {
        HealthCheck check = new HealthCheck();
        check.setId(rs.getInt("id"));
        check.setUserId(rs.getInt("user_id"));
        Date date = rs.getDate("check_date");
        if (date != null) check.setCheckDate(date.toLocalDate());
        check.setEnergyLevel(rs.getInt("energy_level"));
        check.setSleepQuality(rs.getInt("sleep_quality"));
        check.setStressLevel(rs.getInt("stress_level"));
        check.setMoodLevel(rs.getInt("mood_level"));
        check.setNotes(rs.getString("notes"));
        check.setWellnessScore(rs.getInt("wellness_score"));
        return check;
    }
}
