package DAO;

import Model.User;
import Util.DBConnection;
import Util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for User entity - Microsoft Access Database Version
 * Author: MiniMax Agent
 */
public class UserDAO {
    
    private static final String TABLE_NAME = "users";
    
    /**
     * Check if email already exists in database
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT id FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.trim().toLowerCase());
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if email exists
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Find user by email address
     * @param email the email to search for
     * @return User object if found, null otherwise
     */
    public User findByEmail(String email) {
        String sql = "SELECT id, email, password_hash, full_name, phone, role, created_at FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.trim().toLowerCase());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            throw new RuntimeException("Database error occurred", e);
        }
        
        return null;
    }

    /**
     * Find user by ID
     * @param id the user ID
     * @return User object if found, null otherwise
     */
    public User findById(int id) {
        String sql = "SELECT id, email, password_hash, full_name, phone, role, created_at FROM " + TABLE_NAME + " WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            throw new RuntimeException("Database error occurred", e);
        }
        
        return null;
    }

    /**
     * Register a new user
     * @param user the user to register
     * @param plainPassword the plain text password (will be hashed)
     * @return true if registration successful, false otherwise
     */
    public boolean register(User user, String plainPassword) {
        // Hash the password before storing
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        
        String sql = "INSERT INTO " + TABLE_NAME + " (email, password_hash, full_name, phone, role) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getEmail().trim().toLowerCase());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getFullName().trim());
            stmt.setString(4, user.getPhone() != null ? user.getPhone().trim() : "");
            stmt.setString(5, user.getRole() != null ? user.getRole() : "USER");
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            
            // Check for duplicate entry error
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
                throw new RuntimeException("Email đã được đăng ký", e);
            }
            
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Verify user credentials
     * @param email the user email
     * @param password the plain text password
     * @return User object if credentials valid, null otherwise
     */
    public User verifyLogin(String email, String password) {
        User user = findByEmail(email);
        
        if (user == null) {
            return null;
        }
        
        // Check password
        boolean passwordValid = PasswordUtil.checkPassword(password, user.getPassword());
        
        if (!passwordValid) {
            return null;
        }
        
        return user;
    }

    /**
     * Update user profile
     * @param user the user to update
     * @return true if update successful
     */
    public boolean updateProfile(User user) {
        String sql = "UPDATE " + TABLE_NAME + " SET full_name = ?, phone = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getFullName().trim());
            stmt.setString(2, user.getPhone() != null ? user.getPhone().trim() : "");
            stmt.setInt(3, user.getUserId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user profile: " + e.getMessage());
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Update user password
     * @param userId the user ID
     * @param newPlainPassword the new plain text password (will be hashed)
     * @return true if update successful
     */
    public boolean updatePassword(int userId, String newPlainPassword) {
        String hashedPassword = PasswordUtil.hashPassword(newPlainPassword);
        
        String sql = "UPDATE " + TABLE_NAME + " SET password_hash = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Map ResultSet to User object
     * @param rs the ResultSet
     * @return User object
     * @throws SQLException if mapping fails
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        
        // Handle null phone values in Access
        String phone = rs.getString("phone");
        user.setPhone(phone != null ? phone : "");
        
        user.setRole(rs.getString("role"));
        
        // Handle created_at (Date/Time in Access)
        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toString());
        }
        
        return user;
    }
}