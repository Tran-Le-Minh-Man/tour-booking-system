package Util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password utility class for secure password handling using BCrypt
 * Provides centralized password validation logic
 */
public class PasswordUtil {
    // BCrypt work factor (higher = more secure but slower)
    // Recommended range: 10-12 for most applications
    private static final int WORK_FACTOR = 10;
    
    // Minimum password requirements
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int RECOMMENDED_PASSWORD_LENGTH = 12;

    /**
     * Hash a plain text password using BCrypt
     * @param plainPassword the plain text password
     * @return the hashed password
     * @throws IllegalArgumentException if password is null or empty
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
     * Requirements:
     * - Minimum 8 characters
     * - AND either:
     *   - At least 12 characters, OR
     *   - At least 3 of 4 character types (uppercase, lowercase, digit, special)
     * 
     * @param password the password to validate
     * @return true if password meets minimum requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        
        // Count character types
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        
        // Calculate criteria met
        int criteriaMet = 0;
        if (hasUpperCase) criteriaMet++;
        if (hasLowerCase) criteriaMet++;
        if (hasDigit) criteriaMet++;
        if (hasSpecial) criteriaMet++;
        
        // Password is valid if it's long enough OR has enough variety
        return password.length() >= RECOMMENDED_PASSWORD_LENGTH || criteriaMet >= 3;
    }

    /**
     * Get password strength error message
     * @param password the password to check
     * @return error message or null if valid
     */
    public static String getPasswordErrorMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Mật khẩu không được để trống";
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Mật khẩu phải có ít nhất " + MIN_PASSWORD_LENGTH + " ký tự";
        }
        
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        
        int criteriaMet = 0;
        if (hasUpperCase) criteriaMet++;
        if (hasLowerCase) criteriaMet++;
        if (hasDigit) criteriaMet++;
        if (hasSpecial) criteriaMet++;
        
        if (password.length() < RECOMMENDED_PASSWORD_LENGTH && criteriaMet < 3) {
            return String.format("Mật khẩu phải có ít nhất %d ký tự HOẶC kết hợp ít nhất 3 trong 4 loại ký tự: chữ hoa, chữ thường, số, ký tự đặc biệt", 
                RECOMMENDED_PASSWORD_LENGTH);
        }
        
        return null; // Valid
    }
    
    /**
     * Estimate password crack time based on BCrypt work factor
     * @param workFactor the BCrypt work factor
     * @return estimated crack time in seconds
     */
    public static int estimateCrackTime(int workFactor) {
        // Rough estimate: 2^workFactor operations per second on modern hardware
        return (int) Math.pow(2, workFactor);
    }
}