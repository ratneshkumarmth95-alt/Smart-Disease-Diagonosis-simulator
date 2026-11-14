package com.smartdiagnosis.repository;

import com.smartdiagnosis.database.DatabaseConnection;
import com.smartdiagnosis.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.*;

/**
 * HistoryDAO - Saves and retrieves diagnosis history records.
 * Demonstrates: DAO Pattern, JDBC PreparedStatement, Date handling
 */
public class HistoryDAO {

    private static final Logger logger = Logger.getLogger(HistoryDAO.class.getName());

    /** Save a diagnosis result to history */
    public boolean save(DiagnosisResult result, int userId) {
        String sql = "INSERT INTO diagnosis_history " +
                     "(user_id, analysis_date, symptoms_selected, top_match_name, " +
                     "top_match_score, emergency_warning, overall_severity, duration) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(result.getAnalysisDate()));
            ps.setString(3, result.getSymptomsAsString());
            ps.setString(4, result.isEmergencyWarning() ? "EMERGENCY" : result.getTopMatchName());
            ps.setDouble(5, result.getTopMatchScore());
            ps.setBoolean(6, result.isEmergencyWarning());
            ps.setString(7, result.getOverallSeverity());
            ps.setString(8, result.getDuration());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) result.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving diagnosis history: " + e.getMessage(), e);
        }
        return false;
    }

    /** Get all diagnosis history for a user, most recent first */
    public List<DiagnosisResult> findByUser(int userId) {
        List<DiagnosisResult> history = new ArrayList<>();
        String sql = "SELECT * FROM diagnosis_history WHERE user_id = ? ORDER BY analysis_date DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DiagnosisResult result = new DiagnosisResult();
                    result.setId(rs.getInt("id"));
                    result.setUserId(rs.getInt("user_id"));
                    Timestamp ts = rs.getTimestamp("analysis_date");
                    if (ts != null) result.setAnalysisDate(ts.toLocalDateTime());
                    result.setOverallSeverity(rs.getString("overall_severity"));
                    result.setEmergencyWarning(rs.getBoolean("emergency_warning"));
                    result.setDuration(rs.getString("duration"));

                    // Reconstruct minimal symptom list from stored string
                    String symptomsStr = rs.getString("symptoms_selected");
                    String topMatchName = rs.getString("top_match_name");
                    double topMatchScore = rs.getDouble("top_match_score");

                    // Attach top match as a lightweight ConditionMatch
                    if (topMatchName != null && !topMatchName.isEmpty() && !result.isEmergencyWarning()) {
                        Disease d = new Disease();
                        d.setName(topMatchName);
                        ConditionMatch match = new ConditionMatch();
                        match.setDisease(d);
                        match.setCompatibilityScore(topMatchScore);
                        result.setTopMatches(Collections.singletonList(match));
                    }

                    // Reconstruct symptoms from comma-separated list
                    if (symptomsStr != null && !symptomsStr.isBlank()) {
                        List<Symptom> symptoms = new ArrayList<>();
                        for (String name : symptomsStr.split(",")) {
                            Symptom s = new Symptom();
                            s.setName(name.trim());
                            symptoms.add(s);
                        }
                        result.setSelectedSymptoms(symptoms);
                    }

                    history.add(result);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching history: " + e.getMessage(), e);
        }
        return history;
    }

    /** Count how many analyses this month */
    public int countThisMonth(int userId) {
        String sql = "SELECT COUNT(*) FROM diagnosis_history WHERE user_id = ? " +
                     "AND MONTH(analysis_date) = MONTH(CURDATE()) AND YEAR(analysis_date) = YEAR(CURDATE())";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error counting monthly analyses: " + e.getMessage(), e);
        }
        return 0;
    }
}
