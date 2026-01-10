package Model;

import java.util.Objects;

/**
 * User model class with security enhancements
 * Author: MiniMax Agent
 */
public class User {
    private int userId;
    private String fullName;
    private String email;
    private transient String password; // Marked as transient for security
    private String phone;
    private String role;
    private String createdAt;
    
    public User() {}
    
    public User(int userId, String fullName, String email, String password, 
                String phone, String role, String createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters with validation
    public int getUserId() { return userId; }
    public void setUserId(int userId) { 
        this.userId = userId; 
    }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { 
        this.fullName = (fullName != null) ? fullName.trim() : null; 
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = (email != null) ? email.trim().toLowerCase() : null; 
    }
    
    // Password getter returns null for security
    public String getPassword() { 
        return null; // Never return actual password
    }
    
    public void setPassword(String password) { 
        this.password = (password != null) ? password : null; 
    }
    
    // Internal method to access password for authentication
    public String getRawPassword() {
        return password;
    }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { 
        this.phone = (phone != null) ? phone.trim() : null; 
    }
    
    public String getRole() { return role; }
    public void setRole(String role) { 
        this.role = (role != null) ? role.trim() : "USER"; 
    }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { 
        this.createdAt = createdAt; 
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && 
               Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}