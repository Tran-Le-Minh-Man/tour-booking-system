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
        
        System.out.println("=== USERDAO FIND BY EMAIL ===");
        System.out.println("Searching for email: " + email.toLowerCase().trim());
        System.out.println("Database URL: " + DBConnection.getDatabaseUrl());
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.toLowerCase().trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User found! ID: " + rs.getInt("id") + ", Email: " + rs.getString("email"));
                    return mapResultSetToUser(rs);
                } else {
                    System.out.println("User NOT found in database");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("findByEmail error: " + e.getMessage());
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
        System.out.println("=== USERDAO VERIFY LOGIN ===");
        System.out.println("Email: " + email);
        System.out.println("Database URL: " + DBConnection.getDatabaseUrl());
        
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.toLowerCase().trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    
                    // Get stored password hash
                    String storedHash = rs.getString("password_hash");
                    System.out.println("Found user: " + user.getEmail());
                    System.out.println("Stored hash: " + (storedHash != null ? storedHash.substring(0, Math.min(10, storedHash.length())) + "..." : "NULL"));
                    
                    // Verify password using BCrypt
                    if (checkPassword(password, storedHash)) {
                        System.out.println("Password verified successfully!");
                        return user;
                    } else {
                        System.out.println("Password verification FAILED");
                    }
                } else {
                    System.out.println("User NOT found in database");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("verifyLogin error: " + e.getMessage());
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
                     " (email, password_hash, full_name, phone, role, created_at, remember_token, token_expiry) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        System.out.println("=== USERDAO INSERT DEBUG ===");
        System.out.println("Database URL: " + DBConnection.getDatabaseUrl());
        System.out.println("UserDAO.insert - Email: " + user.getEmail());
        System.out.println("UserDAO.insert - Password hash: " + (user.getHashedPassword() != null ? user.getHashedPassword().substring(0, Math.min(10, user.getHashedPassword().length())) + "..." : "NULL"));
        System.out.println("UserDAO.insert - FullName: " + user.getFullName());
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            System.out.println("Connection obtained: " + (conn != null));
            System.out.println("AutoCommit: " + conn.getAutoCommit());
            
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, user.getEmail().toLowerCase().trim());
            stmt.setString(2, user.getHashedPassword());
            stmt.setString(3, sanitizeString(user.getFullName()));
            stmt.setString(4, sanitizeString(user.getPhone()));
            stmt.setString(5, user.getRole() != null ? user.getRole() : "USER");
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.setString(7, null);
            stmt.setTimestamp(8, null);
            
            System.out.println("Executing insert...");
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            
            // ALWAYS force commit to ensure data is written to file
            if (rowsAffected > 0) {
                try {
                    conn.commit();
                    System.out.println("FORCE COMMIT executed - data saved to database file");
                    
                    // Verify the insert by reading back
                    if (verifyUserExists(user.getEmail())) {
                        System.out.println("VERIFIED: User exists in database after insert");
                    } else {
                        System.err.println("WARNING: User NOT found after insert! Data may be lost!");
                    }
                } catch (SQLException commitEx) {
                    System.err.println("Commit failed: " + commitEx.getMessage());
                }
            }
            
            System.out.println("=== INSERT SUCCESS ===");
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("=== INSERT FAILED ===");
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            
            // Try to rollback if something went wrong
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Rolled back transaction");
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            
            throw new UserDAOException("insert", "Error inserting user", e);
        } finally {
            // Close statement
            if (stmt != null) {
                try {
                    stmt.close();
                    System.out.println("Statement closed");
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
            // Release connection back to pool
            if (conn != null) {
                DBConnection.releaseConnection(conn);
                System.out.println("Connection released");
            }
        }
    }
    
    /**
     * Verify user exists in database after insert
     */
    private boolean verifyUserExists(String email) {
        String sql = "SELECT COUNT(*) as cnt FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.toLowerCase().trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("cnt");
                    System.out.println("Verification - User count for " + email + ": " + count);
                    return count > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Verification failed: " + e.getMessage());
        }
        
        return false;
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
        
        // Map remember_token and token_expiry
        user.setRememberToken(rs.getString("remember_token"));
        
        Timestamp tokenExpiry = rs.getTimestamp("token_expiry");
        if (tokenExpiry != null) {
            user.setTokenExpiry(tokenExpiry.toString());
        } else {
            user.setTokenExpiry(null);
        }
        
        return user;
    }
    
    /**
     * Update user remember token
     * @param userId the user ID
     * @param token the remember token
     * @param expiryDate the expiry date for the token
     * @return true if update successful
     */
    public boolean updateRememberToken(int userId, String token, java.sql.Timestamp expiryDate) {
        String sql = "UPDATE " + TABLE_NAME + " SET remember_token = ?, token_expiry = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            stmt.setTimestamp(2, expiryDate);
            stmt.setInt(3, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new UserDAOException("updateRememberToken", "Error updating remember token", e);
        }
    }
    
    /**
     * Clear user remember token (logout)
     * @param userId the user ID
     * @return true if update successful
     */
    public boolean clearRememberToken(int userId) {
        String sql = "UPDATE " + TABLE_NAME + " SET remember_token = NULL, token_expiry = NULL WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new UserDAOException("clearRememberToken", "Error clearing remember token", e);
        }
    }
    
    /**
     * Find user by remember token
     * @param token the remember token
     * @return User object if found and token valid, null otherwise
     */
    public User findByRememberToken(String token) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE remember_token = ? AND token_expiry > ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            stmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new UserDAOException("findByRememberToken", "Error finding user by remember token", e);
        }
        
        return null;
    }
    
    /**
     * Sanitize string for database queries
     */
    private String sanitizeString(String str) {
        return (str != null) ? str.trim() : "";
    }
    
}