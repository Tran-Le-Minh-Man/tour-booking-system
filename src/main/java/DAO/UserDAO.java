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
 * Enhanced with improved exception handling and connection management
 * Author: MiniMax Agent
 */
public class UserDAO {
    
    private static final String TABLE_NAME = "users";
    
    /**
     * Custom exception for database operations
     */
    public static class UserDAOException extends RuntimeException {
        private final String operation;
        private final String detail;
        
        public UserDAOException(String operation, String detail, Throwable cause) {
            super(detail, cause);
            this.operation = operation;
            this.detail = detail;
        }
        
        public String getOperation() {
            return operation;
        }
        
        public String getDetail() {
            return detail;
        }
    }
    
    /**
     * Check if email already exists in database
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT id FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, normalizeEmail(email));
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("emailExists", "Error checking email existence", e);
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
            
            stmt.setString(1, normalizeEmail(email));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("findByEmail", "Error finding user by email", e);
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
            throw new UserDAOException("findById", "Error finding user by ID", e);
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
            
            stmt.setString(1, normalizeEmail(user.getEmail()));
            stmt.setString(2, hashedPassword);
            stmt.setString(3, sanitizeString(user.getFullName()));
            stmt.setString(4, sanitizeString(user.getPhone()));
            stmt.setString(5, user.getRole() != null ? user.getRole() : "USER");
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            handleSQLException(e, "register");
            throw new UserDAOException("register", "Registration failed", e);
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
        boolean passwordValid = PasswordUtil.checkPassword(password, user.getRawPassword());
        
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
            
            stmt.setString(1, sanitizeString(user.getFullName()));
            stmt.setString(2, sanitizeString(user.getPhone()));
            stmt.setInt(3, user.getUserId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new UserDAOException("updateProfile", "Error updating user profile", e);
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
            throw new UserDAOException("updatePassword", "Error updating password", e);
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
    
    /**
     * Normalize email for database queries
     */
    private String normalizeEmail(String email) {
        return (email != null) ? email.trim().toLowerCase() : "";
    }
    
    /**
     * Sanitize string for database queries
     */
    private String sanitizeString(String str) {
        return (str != null) ? str.trim() : "";
    }
    
    /**
     * Handle SQL exceptions with appropriate error messages
     */
    private void handleSQLException(SQLException e, String operation) {
        String message = e.getMessage();
        
        if (message.contains("duplicate") || message.contains("unique") || message.contains("primary key")) {
            System.err.println("Duplicate entry detected during " + operation + ": " + message);
        } else if (message.contains("connection") || message.contains("timeout")) {
            System.err.println("Database connection error during " + operation + ": " + message);
        } else {
            System.err.println("Database error during " + operation + ": " + message);
        }
    }
}