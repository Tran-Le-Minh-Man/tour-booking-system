package Util;

import java.util.regex.Pattern;

/**
 * Validation utility class for input validation
 * Author: MiniMax Agent
 */
public class ValidationUtil {
    
    // Email pattern (RFC 5322 standard)
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    // Vietnamese phone number patterns
    private static final String PHONE_PATTERN_VN = 
        "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5|9]|9[0-4|6-9])[0-9]{7}$";
    
    // Vietnamese name pattern (allows letters, spaces, Vietnamese characters)
    private static final String NAME_PATTERN = 
        "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀẾỂỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪỬỮỰỲỴÝỶỸửữựỳýỵỷỹ\\s]+$";

    /**
     * Validate email format
     * @param email the email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Pattern.compile(EMAIL_PATTERN).matcher(email.trim()).matches();
    }

    /**
     * Validate Vietnamese phone number
     * @param phone the phone number to validate
     * @return true if valid Vietnamese phone number
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return Pattern.compile(PHONE_PATTERN_VN).matcher(phone.trim().replaceAll("\\s+", "")).matches();
    }

    /**
     * Validate full name (allows Vietnamese characters)
     * @param name the name to validate
     * @return true if valid name format
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        if (trimmed.length() < 2 || trimmed.length() > 100) {
            return false;
        }
        return Pattern.compile(NAME_PATTERN).matcher(trimmed).matches();
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

    /**
     * Get password strength error message
     * @param password the password to check
     * @return error message or null if valid
     */
    public static String getPasswordErrorMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Mật khẩu không được để trống";
        }
        if (password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự";
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
        
        if (password.length() < 12 && criteriaMet < 3) {
            return "Mật khẩu phải có ít nhất 12 ký tự HOẶC kết hợp ít nhất 3 trong 4 loại ký tự: chữ hoa, chữ thường, số, ký tự đặc biệt";
        }
        
        return null; // Valid
    }

    /**
     * Validate and sanitize user input to prevent XSS
     * @param input the input string to sanitize
     * @return sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove potentially dangerous characters
        String sanitized = input.replaceAll("<", "&lt;")
                               .replaceAll(">", "&gt;")
                               .replaceAll("\"", "&quot;")
                               .replaceAll("'", "&#x27;")
                               .replaceAll("/", "&#x2F;")
                               .trim();
        
        return sanitized;
    }

    /**
     * Trim and validate string input
     * @param input the input string
     * @param fieldName the field name for error message
     * @return trimmed string or null if empty
     */
    public static String trimInput(String input, String fieldName) {
        if (input == null) {
            return null;
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed;
    }
}