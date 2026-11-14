package com.smartdiagnosis.repository;

import com.smartdiagnosis.database.DatabaseConnection;
import com.smartdiagnosis.model.User;
import com.smartdiagnosis.utils.ValidationUtils;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;

/**
 * UserDAO - Data Access Object for User database operations.
 * Implements CRUD operations using JDBC with Prepared Statements (SQL injection safe).
 * Demonstrates: DAO Pattern, JDBC, PreparedStatement, Exception Handling
 */
public class UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    /** Save a new user to the database. Returns the generated ID, or -1 if failed. */
    public int save(User user) {
        String sql = "INSERT INTO users (username, password_hash, name, age, gender, height, weight, " +
                     "blood_group, allergies, existing_conditions, emergency_contact, created_at, " +
                     "streak_days, total_check_ins, badges) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ValidationUtils.sanitizeInput(user.getUsername()));
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, ValidationUtils.sanitizeInput(user.getName()));
            ps.setInt(4, user.getAge());
            ps.setString(5, user.getGender());
            ps.setDouble(6, user.getHeight());
            ps.setDouble(7, user.getWeight());
            ps.setString(8, user.getBloodGroup());
            ps.setString(9, user.getAllergies());
            ps.setString(10, user.getExistingConditions());
            ps.setString(11, user.getEmergencyContact());
            ps.setDate(12, user.getCreatedAt() != null
                ? Date.valueOf(user.getCreatedAt()) : Date.valueOf(LocalDate.now()));
            ps.setInt(13, user.getStreakDays());
            ps.setInt(14, user.getTotalCheckIns());
            ps.setString(15, user.getBadgesAsString());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        user.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving user: " + e.getMessage(), e);
        }
        return -1;
    }

    /** Update an existing user's profile */
    public boolean update(User user) {
        String sql = "UPDATE users SET name=?, age=?, gender=?, height=?, weight=?, blood_group=?, " +
                     "allergies=?, existing_conditions=?, emergency_contact=?, streak_days=?, " +
                     "total_check_ins=?, badges=?, last_check_in=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ValidationUtils.sanitizeInput(user.getName()));
            ps.setInt(2, user.getAge());
            ps.setString(3, user.getGender());
            ps.setDouble(4, user.getHeight());
            ps.setDouble(5, user.getWeight());
            ps.setString(6, user.getBloodGroup());
            ps.setString(7, user.getAllergies());
            ps.setString(8, user.getExistingConditions());
            ps.setString(9, user.getEmergencyContact());
            ps.setInt(10, user.getStreakDays());
            ps.setInt(11, user.getTotalCheckIns());
            ps.setString(12, user.getBadgesAsString());
            ps.setDate(13, user.getLastCheckIn() != null ? Date.valueOf(user.getLastCheckIn()) : null);
            ps.setInt(14, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user: " + e.getMessage(), e);
        }
        return false;
    }

    /** Find user by username */
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ValidationUtils.sanitizeInput(username));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user by username: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /** Find user by ID */
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /** Check if username already exists */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error checking username: " + e.getMessage(), e);
        }
        return false;
    }

    /** Map a ResultSet row to a User object */
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setName(rs.getString("name"));
        user.setAge(rs.getInt("age"));
        user.setGender(rs.getString("gender"));
        user.setHeight(rs.getDouble("height"));
        user.setWeight(rs.getDouble("weight"));
        user.setBloodGroup(rs.getString("blood_group"));
        user.setAllergies(rs.getString("allergies"));
        user.setExistingConditions(rs.getString("existing_conditions"));
        user.setEmergencyContact(rs.getString("emergency_contact"));
        user.setStreakDays(rs.getInt("streak_days"));
        user.setTotalCheckIns(rs.getInt("total_check_ins"));
        user.setBadgesFromString(rs.getString("badges"));

        Date dob = rs.getDate("date_of_birth");
        if (dob != null) user.setDateOfBirth(dob.toLocalDate());
        Date created = rs.getDate("created_at");
        if (created != null) user.setCreatedAt(created.toLocalDate());
        Date lastCI = rs.getDate("last_check_in");
        if (lastCI != null) user.setLastCheckIn(lastCI.toLocalDate());
        return user;
    }
}
