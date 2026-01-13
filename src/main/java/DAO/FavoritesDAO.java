package DAO;

import Model.Tour;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User Favorites
 */
public class FavoritesDAO {
    
    private static final String TABLE_NAME = "user_favorites";
    private static final String TOURS_TABLE = "tours";
    
    /**
     * Custom exception for database operations
     */
    public static class FavoritesDAOException extends RuntimeException {
        private final String operation;
        
        public FavoritesDAOException(String operation, String message, Throwable cause) {
            super(message, cause);
            this.operation = operation;
        }
        
        public String getOperation() { return operation; }
    }
    
    /**
     * Add a tour to user's favorites
     * @param userId the user ID
     * @param tourId the tour ID
     * @return true if added successfully, false if already exists
     */
    public boolean addFavorite(int userId, int tourId) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, tour_id, created_at) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, tourId);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new FavoritesDAOException("addFavorite", "Error adding favorite", e);
        }
    }
    
    /**
     * Remove a tour from user's favorites
     * @param userId the user ID
     * @param tourId the tour ID
     * @return true if removed successfully, false if not found
     */
    public boolean removeFavorite(int userId, int tourId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE user_id = ? AND tour_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, tourId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new FavoritesDAOException("removeFavorite", "Error removing favorite", e);
        }
    }
    
    /**
     * Toggle favorite status (add if not exists, remove if exists)
     * @param userId the user ID
     * @param tourId the tour ID
     * @return true if now favorited, false if unfavorited
     */
    public boolean toggleFavorite(int userId, int tourId) {
        if (isFavorite(userId, tourId)) {
            removeFavorite(userId, tourId);
            return false;
        } else {
            addFavorite(userId, tourId);
            return true;
        }
    }
    
    /**
     * Check if a tour is in user's favorites
     * @param userId the user ID
     * @param tourId the tour ID
     * @return true if favorited, false otherwise
     */
    public boolean isFavorite(int userId, int tourId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE user_id = ? AND tour_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, tourId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new FavoritesDAOException("isFavorite", "Error checking favorite status", e);
        }
        
        return false;
    }
    
    /**
     * Get all favorite tours for a user
     * @param userId the user ID
     * @return list of favorite tours
     */
    public List<Tour> getUserFavorites(int userId) {
        String sql = "SELECT t.* FROM " + TOURS_TABLE + " t " +
                     "INNER JOIN " + TABLE_NAME + " f ON t.id = f.tour_id " +
                     "WHERE f.user_id = ? AND t.status = 'ACTIVE' " +
                     "ORDER BY f.created_at DESC";
        List<Tour> favorites = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    favorites.add(mapResultSetToTour(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new FavoritesDAOException("getUserFavorites", "Error fetching user favorites", e);
        }
        
        return favorites;
    }
    
    /**
     * Get favorite count for a user
     * @param userId the user ID
     * @return number of favorites
     */
    public int getFavoriteCount(int userId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new FavoritesDAOException("getFavoriteCount", "Error counting favorites", e);
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Tour object (simplified version)
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
        
        java.math.BigDecimal price = rs.getBigDecimal("price");
        tour.setPrice(price);
        
        tour.setMaxParticipants(rs.getInt("max_participants"));
        tour.setCurrentParticipants(rs.getInt("current_participants"));
        tour.setImageUrl(rs.getString("image_url"));
        tour.setStatus(rs.getString("status"));
        
        return tour;
    }
}
