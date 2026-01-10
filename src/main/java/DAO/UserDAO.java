package DAO;

import Model.User;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entity
 * Handles all database operations for users
 * Author: MiniMax Agent
 */
public class UserDAO {
    
    private static final String TABLE_NAME = "users";
    
    /**
     * Custom exception for database operations
     */
    public static class UserDAOException extends RuntimeException {
        private final String operation;
        
        public UserDAOException(String operation, String message, Throwable cause) {
            super(message, cause);
            this.operation = operation;
        }
        
        public String getOperation() { return operation; }
    }
    
    /**
     * Register a new user with password hashing
     * @param user the user object (without password set)
     * @param plainPassword the plain text password
     * @return true if registration successful, false otherwise
     */
    public boolean register(User user, String plainPassword) {
        // Hash the password using BCrypt
        String hashedPassword = hashPassword(plainPassword);
        
        // Set the hashed password on user object
        user.setPassword(hashedPassword);
        
        // Insert user into database
        return insert(user);
    }
    
    /**
     * Hash password using BCrypt
     * @param plainPassword the plain text password
     * @return hashed password
     */
    private String hashPassword(String plainPassword) {
        try {
            return org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt(10));
        } catch (Exception e) {
            throw new UserDAOException("hashPassword", "Error hashing password", e);
        }
    }
    
    /**
     * Find user by ID
     * @param id the user ID
     * @return User object if found, null otherwise
     */
    public User findById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        
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
     * Find user by email
     * @param email the email to search
     * @return User object if found, null otherwise
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.toLowerCase().trim());
            
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
     * Verify user login credentials
     * @param email user email
     * @param password user password (plain text, will be hashed)
     * @return User object if credentials valid, null otherwise
     */
    public User verifyLogin(String email, String password) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.toLowerCase().trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    
                    // Get stored password hash
                    String storedHash = rs.getString("password_hash");
                    
                    // Verify password using BCrypt
                    if (checkPassword(password, storedHash)) {
                        return user;
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("verifyLogin", "Error verifying login", e);
        }
        
        return null;
    }
    
    /**
     * Check if password matches hashed password
     * @param plainPassword the plain text password
     * @param hashedPassword the hashed password
     * @return true if password matches
     */
    private boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return org.mindrot.jbcrypt.BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get all users
     * @return list of all users
     */
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("getAllUsers", "Error fetching all users", e);
        }
        
        return users;
    }
    
    /**
     * Get users by role
     * @param role the role to filter (ADMIN, USER)
     * @return list of users with specified role
     */
    public List<User> getUsersByRole(String role) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE role = ? ORDER BY id DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("getUsersByRole", "Error fetching users by role", e);
        }
        
        return users;
    }
    
    /**
     * Filter users by role AND search by keyword (name or email)
     * @param role the role to filter (ADMIN, USER)
     * @param keyword the search keyword for name or email
     * @return list of matching users
     */
    public List<User> filterUsersByRoleAndSearch(String role, String keyword) {
        String sql = "SELECT * FROM " + TABLE_NAME + 
                     " WHERE role = ? AND (full_name LIKE ? OR email LIKE ?) " +
                     "ORDER BY id DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            String searchPattern = "%" + keyword + "%";
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("filterUsersByRoleAndSearch", "Error filtering users by role and search", e);
        }
        
        return users;
    }
    
    /**
     * Insert a new user
     * @param user the user to insert (password should already be hashed)
     * @return true if insertion successful, false otherwise
     */
    public boolean insert(User user) {
        String sql = "INSERT INTO " + TABLE_NAME + 
                     " (email, password_hash, full_name, phone, role, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getEmail().toLowerCase().trim());
            stmt.setString(2, user.getPassword()); // Already hashed
            stmt.setString(3, sanitizeString(user.getFullName()));
            stmt.setString(4, sanitizeString(user.getPhone()));
            stmt.setString(5, user.getRole() != null ? user.getRole() : "USER");
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new UserDAOException("insert", "Error inserting user", e);
        }
    }
    
    /**
     * Update user
     * @param user the user to update
     * @return true if update successful, false otherwise
     */
    public boolean update(User user) {
        String sql = "UPDATE " + TABLE_NAME + 
                     " SET full_name = ?, phone = ?, role = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sanitizeString(user.getFullName()));
            stmt.setString(2, sanitizeString(user.getPhone()));
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getUserId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new UserDAOException("update", "Error updating user", e);
        }
    }
    
    /**
     * Update user password
     * @param userId the user ID
     * @param newPlainPassword the new plain text password
     * @return true if update successful
     */
    public boolean updatePassword(int userId, String newPlainPassword) {
        String hashedPassword = hashPassword(newPlainPassword);
        
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
     * Update user role
     * @param userId the user ID
     * @param role the new role (ADMIN or USER)
     * @return true if update successful
     */
    public boolean updateRole(int userId, String role) {
        String sql = "UPDATE " + TABLE_NAME + " SET role = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new UserDAOException("updateRole", "Error updating user role", e);
        }
    }
    
    /**
     * Delete user by ID
     * @param userId the user ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean delete(int userId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new UserDAOException("delete", "Error deleting user", e);
        }
    }
    
    /**
     * Check if email already exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.toLowerCase().trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("emailExists", "Error checking email existence", e);
        }
        
        return false;
    }
    
    /**
     * Get user count by role
     * @param role the role to count
     * @return number of users with specified role
     */
    public int countByRole(String role) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE role = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("countByRole", "Error counting users by role", e);
        }
        
        return 0;
    }
    
    /**
     * Get total user count
     * @return total number of users
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("getTotalCount", "Error counting users", e);
        }
        
        return 0;
    }
    
    /**
     * Search users by name or email
     * @param keyword the search keyword
     * @return list of matching users
     */
    public List<User> searchUsers(String keyword) {
        String sql = "SELECT * FROM " + TABLE_NAME + 
                     " WHERE full_name LIKE ? OR email LIKE ? " +
                     "ORDER BY id DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("searchUsers", "Error searching users", e);
        }
        
        return users;
    }
    
    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        
        // For security, don't return the actual password hash
        // The setPassword with null ensures getPassword() returns null
        user.setPassword(null);
        
        user.setFullName(rs.getString("full_name"));
        
        String phone = rs.getString("phone");
        user.setPhone(phone != null ? phone : "");
        
        user.setRole(rs.getString("role"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toString());
        } else {
            user.setCreatedAt("");
        }
        
        return user;
    }
    
    /**
     * Sanitize string for database queries
     */
    private String sanitizeString(String str) {
        return (str != null) ? str.trim() : "";
    }
    
}