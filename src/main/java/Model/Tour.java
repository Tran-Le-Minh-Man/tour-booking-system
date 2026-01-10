package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Tour model class
 */
public class Tour {
    private int tourId;
    private String name;
    private String description;
    private String destination;
    private Timestamp departureDate;
    private int duration; // Số ngày
    private BigDecimal price;
    private int maxParticipants;
    private int currentParticipants;
    private String imageUrl;
    private String status; // ACTIVE, INACTIVE
    
    public Tour() {}
    
    public Tour(int tourId, String name, String description, String destination,
                Timestamp departureDate, int duration, BigDecimal price,
                int maxParticipants, int currentParticipants, String imageUrl, String status) {
        this.tourId = tourId;
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.departureDate = departureDate;
        this.duration = duration;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.imageUrl = imageUrl;
        this.status = status;
    }
    
    // Getters and Setters
    public int getTourId() { return tourId; }
    public void setTourId(int tourId) { this.tourId = tourId; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = (name != null) ? name.trim() : null; 
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = (description != null) ? description.trim() : null; 
    }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { 
        this.destination = (destination != null) ? destination.trim() : null; 
    }
    
    public Timestamp getDepartureDate() { return departureDate; }
    public void setDepartureDate(Timestamp departureDate) { this.departureDate = departureDate; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }
    
    public int getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(int currentParticipants) { this.currentParticipants = currentParticipants; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { 
        this.imageUrl = (imageUrl != null) ? imageUrl.trim() : null; 
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = (status != null) ? status.trim() : "ACTIVE"; 
    }
    
    // Helper methods
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }
    
    public int getAvailableSlots() {
        return Math.max(0, maxParticipants - currentParticipants);
    }
    
    public String getFormattedPrice() {
        if (price == null) return "0đ";
        return String.format("%,.0fđ", price);
    }
    
    public String getFormattedDepartureDate() {
        if (departureDate == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(departureDate);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return tourId == tour.tourId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tourId);
    }
    
    @Override
    public String toString() {
        return "Tour{" +
                "tourId=" + tourId +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}