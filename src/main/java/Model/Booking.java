package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Booking model class
 * Author: MiniMax Agent
 */
public class Booking {
    private int bookingId;
    private int userId;
    private int tourId;
    private Timestamp bookingDate;
    private String status; // PENDING, CONFIRMED, CANCELLED, COMPLETED
    private int numParticipants;
    private BigDecimal totalPrice;
    private String notes;
    
    // Additional fields for display
    private String userName;
    private String userEmail;
    private String tourName;
    private String tourDestination;
    
    public Booking() {}
    
    public Booking(int bookingId, int userId, int tourId, Timestamp bookingDate,
                   String status, int numParticipants, BigDecimal totalPrice, String notes) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.tourId = tourId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.numParticipants = numParticipants;
        this.totalPrice = totalPrice;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getTourId() { return tourId; }
    public void setTourId(int tourId) { this.tourId = tourId; }
    
    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = (status != null) ? status.trim().toUpperCase() : "PENDING"; 
    }
    
    public int getNumParticipants() { return numParticipants; }
    public void setNumParticipants(int numParticipants) { this.numParticipants = numParticipants; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = (notes != null) ? notes.trim() : null; 
    }
    
    // Display fields
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public String getTourName() { return tourName; }
    public void setTourName(String tourName) { this.tourName = tourName; }
    
    public String getTourDestination() { return tourDestination; }
    public void setTourDestination(String tourDestination) { this.tourDestination = tourDestination; }
    
    // Helper methods
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }
    
    public boolean isConfirmed() {
        return "CONFIRMED".equalsIgnoreCase(status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }
    
    public String getFormattedPrice() {
        if (totalPrice == null) return "0đ";
        return String.format("%,.0fđ", totalPrice);
    }
    
    public String getFormattedDate() {
        if (bookingDate == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(bookingDate);
    }
    
    public String getFormattedStatus() {
        switch (status) {
            case "PENDING": return "Chờ xác nhận";
            case "CONFIRMED": return "Đã xác nhận";
            case "CANCELLED": return "Đã hủy";
            case "COMPLETED": return "Hoàn thành";
            default: return status;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return bookingId == booking.bookingId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
}