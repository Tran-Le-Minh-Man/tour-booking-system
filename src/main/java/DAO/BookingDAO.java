package DAO;

import Model.Booking;
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
 * Data Access Object for Booking entity
 * Author: MiniMax Agent
 */
public class BookingDAO {
    
    private static final String TABLE_NAME = "bookings";
    
    /**
     * Custom exception for database operations
     */
    public static class BookingDAOException extends RuntimeException {
        private final String operation;
        
        public BookingDAOException(String operation, String message, Throwable cause) {
            super(message, cause);
            this.operation = operation;
        }
        
        public String getOperation() { return operation; }
    }
    
    /**
     * Get all bookings with user and tour info
     */
    public List<Booking> getAllBookings() {
        String sql = "SELECT b.*, u.full_name as user_name, u.email as user_email, " +
                     "t.name as tour_name, t.destination as tour_destination " +
                     "FROM " + TABLE_NAME + " b " +
                     "LEFT JOIN users u ON b.user_id = u.id " +
                     "LEFT JOIN tours t ON b.tour_id = t.id " +
                     "ORDER BY b.id DESC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            throw new BookingDAOException("getAllBookings", "Error fetching all bookings", e);
        }
        
        return bookings;
    }
    
    /**
     * Search bookings by keyword (user name, email, tour name, destination)
     * THÊM MỚI: Tìm kiếm đặt tour
     */
    public List<Booking> searchBookings(String keyword) {
        String sql = "SELECT b.*, u.full_name as user_name, u.email as user_email, " +
                     "t.name as tour_name, t.destination as tour_destination " +
                     "FROM " + TABLE_NAME + " b " +
                     "LEFT JOIN users u ON b.user_id = u.id " +
                     "LEFT JOIN tours t ON b.tour_id = t.id " +
                     "WHERE u.full_name LIKE ? OR u.email LIKE ? " +
                     "OR t.name LIKE ? OR t.destination LIKE ? " +
                     "ORDER BY b.id DESC";
        
        List<Booking> bookings = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new BookingDAOException("searchBookings", "Error searching bookings", e);
        }
        
        return bookings;
    }
    
    /**
     * Get bookings by status
     * THÊM MỚI: Lọc đặt tour theo trạng thái
     */
    public List<Booking> getBookingsByStatus(String status) {
        String sql = "SELECT b.*, u.full_name as user_name, u.email as user_email, " +
                     "t.name as tour_name, t.destination as tour_destination " +
                     "FROM " + TABLE_NAME + " b " +
                     "LEFT JOIN users u ON b.user_id = u.id " +
                     "LEFT JOIN tours t ON b.tour_id = t.id " +
                     "WHERE b.status = ? " +
                     "ORDER BY b.id DESC";
        
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new BookingDAOException("getBookingsByStatus", "Error filtering bookings by status", e);
        }
        
        return bookings;
    }
    
    /**
     * Filter bookings by both status and search keyword
     * THÊM MỚI: Lọc kết hợp trạng thái và từ khóa tìm kiếm
     */
    public List<Booking> filterBookingsByStatusAndSearch(String status, String keyword) {
        String sql = "SELECT b.*, u.full_name as user_name, u.email as user_email, " +
                     "t.name as tour_name, t.destination as tour_destination " +
                     "FROM " + TABLE_NAME + " b " +
                     "LEFT JOIN users u ON b.user_id = u.id " +
                     "LEFT JOIN tours t ON b.tour_id = t.id " +
                     "WHERE b.status = ? " +
                     "AND (u.full_name LIKE ? OR u.email LIKE ? " +
                     "OR t.name LIKE ? OR t.destination LIKE ?) " +
                     "ORDER BY b.id DESC";
        
        List<Booking> bookings = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setString(5, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new BookingDAOException("filterBookingsByStatusAndSearch", "Error filtering bookings", e);
        }
        
        return bookings;
    }
    
    /**
     * Find booking by ID
     */
    public Booking findById(int id) {
        String sql = "SELECT b.*, u.full_name as user_name, u.email as user_email, " +
                     "t.name as tour_name, t.destination as tour_destination " +
                     "FROM " + TABLE_NAME + " b " +
                     "LEFT JOIN users u ON b.user_id = u.id " +
                     "LEFT JOIN tours t ON b.tour_id = t.id " +
                     "WHERE b.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new BookingDAOException("findById", "Error finding booking by ID", e);
        }
        
        return null;
    }
    
    /**
     * Update booking status
     */
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE " + TABLE_NAME + " SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.toUpperCase());
            stmt.setInt(2, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new BookingDAOException("updateStatus", "Error updating booking status", e);
        }
    }
    
    /**
     * Confirm a booking
     */
    public boolean confirmBooking(int id) {
        return updateStatus(id, "CONFIRMED");
    }
    
    /**
     * Cancel a booking
     */
    public boolean cancelBooking(int id) {
        return updateStatus(id, "CANCELLED");
    }
    
    /**
     * Complete a booking
     */
    public boolean completeBooking(int id) {
        return updateStatus(id, "COMPLETED");
    }
    
    /**
     * Delete a booking
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new BookingDAOException("delete", "Error deleting booking", e);
        }
    }
    
    /**
     * Get booking count by status
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
            throw new BookingDAOException("countByStatus", "Error counting bookings", e);
        }
        
        return 0;
    }
    
    /**
     * Get total booking count
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
            throw new BookingDAOException("getTotalCount", "Error counting bookings", e);
        }
        
        return 0;
    }
    
    /**
     * Get total revenue from confirmed/completed bookings
     */
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM " + TABLE_NAME + 
                     " WHERE status IN ('CONFIRMED', 'COMPLETED')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                BigDecimal revenue = rs.getBigDecimal(1);
                return revenue != null ? revenue : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            throw new BookingDAOException("getTotalRevenue", "Error calculating total revenue", e);
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Get revenue by date range
     */
    public BigDecimal getRevenueByDateRange(Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM " + TABLE_NAME + 
                     " WHERE status IN ('CONFIRMED', 'COMPLETED') " +
                     " AND booking_date BETWEEN ? AND ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, startDate);
            stmt.setTimestamp(2, endDate);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal revenue = rs.getBigDecimal(1);
                    return revenue != null ? revenue : BigDecimal.ZERO;
                }
            }
            
        } catch (SQLException e) {
            throw new BookingDAOException("getRevenueByDateRange", "Error calculating revenue", e);
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Get recent bookings
     */
    public List<Booking> getRecentBookings(int limit) {
        String sql = "SELECT b.*, u.full_name as user_name, u.email as user_email, " +
                     "t.name as tour_name, t.destination as tour_destination " +
                     "FROM " + TABLE_NAME + " b " +
                     "LEFT JOIN users u ON b.user_id = u.id " +
                     "LEFT JOIN tours t ON b.tour_id = t.id " +
                     "ORDER BY b.id DESC";
        
        // For MS Access, use TOP clause
        if (limit > 0) {
            sql = "SELECT TOP " + limit + " b.*, u.full_name as user_name, u.email as user_email, " +
                  "t.name as tour_name, t.destination as tour_destination " +
                  "FROM " + TABLE_NAME + " b " +
                  "LEFT JOIN users u ON b.user_id = u.id " +
                  "LEFT JOIN tours t ON b.tour_id = t.id " +
                  "ORDER BY b.id DESC";
        }
        
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            throw new BookingDAOException("getRecentBookings", "Error fetching recent bookings", e);
        }
        
        return bookings;
    }
    
    /**
     * Map ResultSet to Booking object
     */
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setTourId(rs.getInt("tour_id"));
        
        Timestamp bookingDate = rs.getTimestamp("booking_date");
        booking.setBookingDate(bookingDate);
        
        booking.setStatus(rs.getString("status"));
        booking.setNumParticipants(rs.getInt("num_participants"));
        
        BigDecimal totalPrice = rs.getBigDecimal("total_price");
        booking.setTotalPrice(totalPrice);
        
        booking.setNotes(rs.getString("notes"));
        
        // Additional display fields
        booking.setUserName(rs.getString("user_name"));
        booking.setUserEmail(rs.getString("user_email"));
        booking.setTourName(rs.getString("tour_name"));
        booking.setTourDestination(rs.getString("tour_destination"));
        
        return booking;
    }
}