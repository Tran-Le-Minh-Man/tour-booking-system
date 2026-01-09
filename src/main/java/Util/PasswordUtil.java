package Util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password utility class for secure password handling using BCrypt
 * Author: MiniMax Agent
 */
public class PasswordUtil {
    // BCrypt work factor (higher = more secure but slower)
    private static final int WORK_FACTOR = 10;

    /**
     * Hash a plain text password using BCrypt
     * @param plainPassword the plain text password
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        // BCrypt hash with work factor
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
        return hashed;
    }

    /**
     * Verify a password against a hashed password
     * @param plainPassword the plain text password to verify
     * @param hashedPassword the hashed password to compare against
     * @return true if password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            // If hashing fails, return false
            System.err.println("Password verification error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate password strength
     * @param password the password to validate
     * @return true if password meets minimum requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // Check for at least one uppercase letter
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        // Check for at least one lowercase letter
        boolean hasLowerCase = password.matches(".*[a-z].*");
        // Check for at least one digit
        boolean hasDigit = password.matches(".*\\d.*");
        // Check for at least one special character
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        
        // Require at least 3 of the 4 criteria, or minimum 12 characters
        int criteriaMet = 0;
        if (hasUpperCase) criteriaMet++;
        if (hasLowerCase) criteriaMet++;
        if (hasDigit) criteriaMet++;
        if (hasSpecial) criteriaMet++;
        
        return password.length() >= 12 || criteriaMet >= 3;
    }
}