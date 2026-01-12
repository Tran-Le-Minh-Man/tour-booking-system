package DAO;

import Model.Tour;
import Util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Tour entity
 */
public class TourDAO {
    
    private static final String TABLE_NAME = "tours";
    
    /**
     * Custom exception for database operations
     */
    public static class TourDAOException extends RuntimeException {
        private final String operation;
        
        public TourDAOException(String operation, String message, Throwable cause) {
            super(message, cause);
            this.operation = operation;
        }
        
        public String getOperation() { return operation; }
    }
    
    /**
     * Get all tours
     * @return list of all tours
     */
    public List<Tour> getAllTours() {
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC";
        List<Tour> tours = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tours.add(mapResultSetToTour(rs));
            }
            
        } catch (SQLException e) {
            throw new TourDAOException("getAllTours", "Error fetching all tours", e);
        }
        
        return tours;
    }
    
    /**
     * Get tours by status
     * @param status the status to filter (ACTIVE, INACTIVE)
     * @return list of tours with specified status
     */
    public List<Tour> getToursByStatus(String status) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE status = ? ORDER BY id DESC";
        List<Tour> tours = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tours.add(mapResultSetToTour(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new TourDAOException("getToursByStatus", "Error fetching tours by status", e);
        }
        
        return tours;
    }
    
    /**
     * Get active tours for customers
     * @return list of active tours
     */
    public List<Tour> getActiveTours() {
        return getToursByStatus("ACTIVE");
    }
    
    /**
     * Find tour by ID
     * @param id the tour ID
     * @return Tour object if found, null otherwise
     */
    public Tour findById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTour(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new TourDAOException("findById", "Error finding tour by ID", e);
        }
        
        return null;
    }
    
    /**
     * Search tours by name or destination
     * @param keyword the search keyword
     * @return list of matching tours
     */
    public List<Tour> searchTours(String keyword) {
        String sql = "SELECT * FROM " + TABLE_NAME + 
                     " WHERE name LIKE ? OR destination LIKE ? " +
                     "ORDER BY id DESC";
        List<Tour> tours = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tours.add(mapResultSetToTour(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new TourDAOException("searchTours", "Error searching tours", e);
        }
        
        return tours;
    }
    
    /**
     * Insert a new tour
     * @param tour the tour to insert
     * @return true if insertion successful, false otherwise
     */
    public boolean insert(Tour tour) {
        String sql = "INSERT INTO " + TABLE_NAME + 
                     " (name, description, destination, departure_date, duration, price, " +
                     "max_participants, current_participants, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sanitizeString(tour.getName()));
            stmt.setString(2, sanitizeString(tour.getDescription()));
            stmt.setString(3, sanitizeString(tour.getDestination()));
            
            if (tour.getDepartureDate() != null) {
                stmt.setTimestamp(4, tour.getDepartureDate());
            } else {
                stmt.setTimestamp(4, null);
            }
            
            stmt.setInt(5, tour.getDuration());
            stmt.setBigDecimal(6, tour.getPrice());
            stmt.setInt(7, tour.getMaxParticipants());
            stmt.setInt(8, tour.getCurrentParticipants());
            stmt.setString(9, sanitizeString(tour.getImageUrl()));
            stmt.setString(10, tour.getStatus() != null ? tour.getStatus() : "ACTIVE");
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new TourDAOException("insert", "Error inserting tour", e);
        }
    }
    
    /**
     * Update an existing tour
     * @param tour the tour to update
     * @return true if update successful, false otherwise
     */
    public boolean update(Tour tour) {
        String sql = "UPDATE " + TABLE_NAME + 
                     " SET name = ?, description = ?, destination = ?, " +
                     "departure_date = ?, duration = ?, price = ?, " +
                     "max_participants = ?, current_participants = ?, " +
                     "image_url = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sanitizeString(tour.getName()));
            stmt.setString(2, sanitizeString(tour.getDescription()));
            stmt.setString(3, sanitizeString(tour.getDestination()));
            
            if (tour.getDepartureDate() != null) {
                stmt.setTimestamp(4, tour.getDepartureDate());
            } else {
                stmt.setTimestamp(4, null);
            }
            
            stmt.setInt(5, tour.getDuration());
            stmt.setBigDecimal(6, tour.getPrice());
            stmt.setInt(7, tour.getMaxParticipants());
            stmt.setInt(8, tour.getCurrentParticipants());
            stmt.setString(9, sanitizeString(tour.getImageUrl()));
            stmt.setString(10, tour.getStatus());
            stmt.setInt(11, tour.getTourId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new TourDAOException("update", "Error updating tour", e);
        }
    }
    
    /**
     * Delete a tour by ID
     * @param id the tour ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new TourDAOException("delete", "Error deleting tour", e);
        }
    }
    
    /**
     * Update tour status
     * @param id the tour ID
     * @param status the new status
     * @return true if update successful
     */
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE " + TABLE_NAME + " SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new TourDAOException("updateStatus", "Error updating tour status", e);
        }
    }
    
    /**
     * Get tour count by status
     * @param status the status to count
     * @return number of tours with specified status
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE status = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new TourDAOException("countByStatus", "Error counting tours", e);
        }
        
        return 0;
    }
    
    /**
     * Get total tour count
     * @return total number of tours
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
            throw new TourDAOException("getTotalCount", "Error counting tours", e);
        }
        
        return 0;
    }
    
    /**
     * Get filtered tours based on various criteria
     * @param destination the destination to filter (null for all)
     * @param minPrice the minimum price (null for no minimum)
     * @param maxPrice the maximum price (null for no maximum)
     * @param duration the duration in days (null for all)
     * @param departureDate the departure date to filter (null for all)
     * @return list of filtered tours
     */
    public List<Tour> getFilteredTours(String destination, String minPrice, 
                                        String maxPrice, String duration, String departureDate) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_NAME + " WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        // Filter by destination
        if (destination != null && !destination.trim().isEmpty() && !destination.equals("all")) {
            sql.append(" AND destination LIKE ?");
            params.add("%" + destination.trim() + "%");
        }
        
        // Filter by minimum price
        if (minPrice != null && !minPrice.trim().isEmpty()) {
            try {
                double minPriceValue = Double.parseDouble(minPrice.trim());
                sql.append(" AND price >= ?");
                params.add(minPriceValue);
            } catch (NumberFormatException e) {
                // Ignore invalid price values
            }
        }
        
        // Filter by maximum price
        if (maxPrice != null && !maxPrice.trim().isEmpty()) {
            try {
                double maxPriceValue = Double.parseDouble(maxPrice.trim());
                sql.append(" AND price <= ?");
                params.add(maxPriceValue);
            } catch (NumberFormatException e) {
                // Ignore invalid price values
            }
        }
        
        // Filter by duration
        if (duration != null && !duration.trim().isEmpty() && !duration.equals("all")) {
            try {
                int durationValue = Integer.parseInt(duration.trim());
                sql.append(" AND duration = ?");
                params.add(durationValue);
            } catch (NumberFormatException e) {
                // Ignore invalid duration values
            }
        }
        
        // Filter by departure date
        if (departureDate != null && !departureDate.trim().isEmpty()) {
            try {
                java.sql.Date dateValue = java.sql.Date.valueOf(departureDate.trim());
                sql.append(" AND departure_date >= ?");
                params.add(dateValue);
            } catch (IllegalArgumentException e) {
                // Ignore invalid date format
            }
        }
        
        sql.append(" AND status = 'ACTIVE' ORDER BY id DESC");
        
        List<Tour> tours = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof String) {
                    stmt.setString(i + 1, (String) params.get(i));
                } else if (params.get(i) instanceof Double) {
                    stmt.setDouble(i + 1, (Double) params.get(i));
                } else if (params.get(i) instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) params.get(i));
                }
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tours.add(mapResultSetToTour(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new TourDAOException("getFilteredTours", "Error fetching filtered tours", e);
        }
        
        return tours;
    }
    
    /**
     * Get all unique destinations
     * @return list of unique destination names
     */
    public List<String> getAllDestinations() {
        String sql = "SELECT DISTINCT destination FROM " + TABLE_NAME + " WHERE status = 'ACTIVE' ORDER BY destination";
        List<String> destinations = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String dest = rs.getString("destination");
                if (dest != null && !dest.trim().isEmpty()) {
                    destinations.add(dest.trim());
                }
            }
            
        } catch (SQLException e) {
            throw new TourDAOException("getAllDestinations", "Error fetching destinations", e);
        }
        
        return destinations;
    }
    
    /**
     * Map ResultSet to Tour object
     */
    private Tour mapResultSetToTour(ResultSet rs) throws SQLException {
        Tour tour = new Tour();
        tour.setTourId(rs.getInt("id"));
        tour.setName(rs.getString("name"));
        tour.setDescription(rs.getString("description"));
        tour.setDestination(rs.getString("destination"));
        
        Timestamp departureDate = rs.getTimestamp("departure_date");
        tour.setDepartureDate(departureDate);
        
        tour.setDuration(rs.getInt("duration"));
        
        // Handle potential null for price (currency in Access)
        java.math.BigDecimal price = rs.getBigDecimal("price");
        tour.setPrice(price);
        
        tour.setMaxParticipants(rs.getInt("max_participants"));
        tour.setCurrentParticipants(rs.getInt("current_participants"));
        tour.setImageUrl(rs.getString("image_url"));
        tour.setStatus(rs.getString("status"));
        
        return tour;
    }
    
    /**
     * Sanitize string for database queries
     */
    private String sanitizeString(String str) {
        return (str != null) ? str.trim() : "";
    }
}