package com.smartdiagnosis.repository;

import com.smartdiagnosis.database.DatabaseConnection;
import com.smartdiagnosis.model.JournalEntry;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.*;

/**
 * JournalDAO - Database operations for health journal entries.
 * Demonstrates: DAO Pattern, JDBC, PreparedStatement
 */
public class JournalDAO {

    private static final Logger logger = Logger.getLogger(JournalDAO.class.getName());

    public boolean save(JournalEntry entry) {
        String sql = "INSERT INTO health_journal (user_id, entry_date, title, content, mood) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, entry.getUserId());
            ps.setTimestamp(2, Timestamp.valueOf(
                entry.getEntryDate() != null ? entry.getEntryDate() : LocalDateTime.now()));
            ps.setString(3, entry.getTitle());
            ps.setString(4, entry.getContent());
            ps.setString(5, entry.getMood());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entry.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving journal entry: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean delete(int entryId) {
        String sql = "DELETE FROM health_journal WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entryId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting journal entry: " + e.getMessage(), e);
        }
        return false;
    }

    /** Get all journal entries for a user, most recent first */
    public List<JournalEntry> findByUser(int userId) {
        List<JournalEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM health_journal WHERE user_id = ? ORDER BY entry_date DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) entries.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching journal entries: " + e.getMessage(), e);
        }
        return entries;
    }

    private JournalEntry mapRow(ResultSet rs) throws SQLException {
        JournalEntry entry = new JournalEntry();
        entry.setId(rs.getInt("id"));
        entry.setUserId(rs.getInt("user_id"));
        Timestamp ts = rs.getTimestamp("entry_date");
        if (ts != null) entry.setEntryDate(ts.toLocalDateTime());
        entry.setTitle(rs.getString("title"));
        entry.setContent(rs.getString("content"));
        entry.setMood(rs.getString("mood"));
        return entry;
    }
}
