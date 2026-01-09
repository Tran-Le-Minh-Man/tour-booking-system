package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class for Microsoft Access Database (ACDB)
 * Author: MiniMax Agent
 */
public class DBConnection {
    // Access JDBC Driver (UCanAccess)
    private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    
    // Access database file path - THAY ĐỔI ĐƯỜNG DẪN NÀY CHO ĐÚNG
    private static final String DATABASE_URL = "jdbc:ucanaccess://D:/File word pdf/tour_booking.accdb";

    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            // Load driver
            Class.forName(DRIVER);
            
            // Create connection
            connection = DriverManager.getConnection(DATABASE_URL);
            
            // Enable auto-commit for safety
            connection.setAutoCommit(true);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Access Driver not found: " + e.getMessage());
            System.err.println("Vui lòng thêm thư viện UCanAccess vào WEB-INF/lib/");
            throw new SQLException("Access Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database Connection failed: " + e.getMessage());
            System.err.println("Kiểm tra đường dẫn file Access (.accdb hoặc .mdb)");
            throw e;
        }
        return connection;
    }

    /**
     * Close connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}