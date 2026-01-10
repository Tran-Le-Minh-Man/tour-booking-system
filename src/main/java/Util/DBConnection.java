package Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection utility class for Microsoft Access Database (ACDB)
 * Supports configuration file loading and connection pooling
 */
public class DBConnection {
    // Access JDBC Driver (UCanAccess)
    private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    
    // Configuration file path
    private static final String CONFIG_FILE = "/WEB-INF/config/database.properties";
    
    // Connection pool settings
    private static final int MAX_CONNECTIONS = 10;
    private static final int CONNECTION_TIMEOUT = 30000; // 30 seconds
    
    // Database configuration
    private static String databaseUrl;
    private static volatile Connection pool[] = new Connection[MAX_CONNECTIONS];
    private static volatile int poolCount = 0;
    
    /**
     * Load configuration from properties file
     */
    static {
        loadConfiguration();
    }
    
    /**
     * Load database configuration from properties file
     */
    private static void loadConfiguration() {
        Properties props = new Properties();
        
        // Try to load from properties file
        try (FileInputStream fis = new FileInputStream(
                System.getProperty("catalina.base") + CONFIG_FILE)) {
            props.load(fis);
            databaseUrl = props.getProperty("db.url");
            if (databaseUrl == null || databaseUrl.isEmpty()) {
                databaseUrl = getDefaultDatabasePath();
            }
        } catch (IOException e) {
            // Config file not found, use default path
            databaseUrl = getDefaultDatabasePath();
            System.err.println("Config file not found, using default path: " + e.getMessage());
        }
    }
    
    /**
     * Get default database path - works for both development and production
     */
    private static String getDefaultDatabasePath() {
        String catalinaBase = System.getProperty("catalina.base", "");
        
        // If running in Tomcat server
        if (!catalinaBase.isEmpty()) {
            return "jdbc:ucanaccess://" + catalinaBase + "/WEB-INF/db/tour_booking.accdb";
        }
        
        // If running as standalone Java app (development)
        // Try to find the database file in common locations
        String[] possiblePaths = {
            "src/main/webapp/WEB-INF/db/tour_booking.accdb",
            "../src/main/webapp/WEB-INF/db/tour_booking.accdb",
            "../../src/main/webapp/WEB-INF/db/tour_booking.accdb",
            "tour_booking.accdb",
            "../tour_booking.accdb"
        };
        
        for (String path : possiblePaths) {
            Path dbPath = Paths.get(path).toAbsolutePath();
            if (Files.exists(dbPath)) {
                System.out.println("Found database at: " + dbPath);
                return "jdbc:ucanaccess://" + dbPath.toString();
            }
        }
        
        // Last resort: use user home directory
        String userHome = System.getProperty("user.home");
        String defaultPath = userHome + "/tour_booking.accdb";
        System.err.println("Database not found in project, expecting at: " + defaultPath);
        return "jdbc:ucanaccess://" + defaultPath;
    }
    
    /**
     * Get database connection from pool
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static synchronized Connection getConnection() throws SQLException {
        // Try to get connection from pool first
        for (int i = 0; i < poolCount; i++) {
            Connection conn = pool[i];
            if (conn != null && !conn.isClosed() && isConnectionValid(conn)) {
                return conn;
            }
        }
        
        // Create new connection if pool is not full
        if (poolCount < MAX_CONNECTIONS) {
            Connection newConn = createConnection();
            if (newConn != null) {
                pool[poolCount++] = newConn;
                return newConn;
            }
        }
        
        // If pool is full, create direct connection (failover)
        return createConnection();
    }
    
    /**
     * Create a new database connection
     * @return Connection object or null if failed
     */
    private static Connection createConnection() throws SQLException {
        Connection connection = null;
        try {
            // Load driver
            Class.forName(DRIVER);
            
            // Create connection with timeout
            connection = DriverManager.getConnection(databaseUrl);
            connection.setAutoCommit(true);
            
            // Set login timeout
            DriverManager.setLoginTimeout(CONNECTION_TIMEOUT / 1000);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Access Driver not found: " + e.getMessage());
            System.err.println("Vui lòng thêm thư viện UCanAccess vào WEB-INF/lib/");
            throw new SQLException("Access Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database Connection failed: " + e.getMessage());
            System.err.println("Kiểm tra đường dẫn file Access (.accdb hoặc .mdb)");
            System.err.println("Current URL: " + databaseUrl);
            throw e;
        }
        return connection;
    }
    
    /**
     * Check if connection is still valid
     * @param connection Connection to check
     * @return true if connection is valid
     */
    private static boolean isConnectionValid(Connection conn) {
        try {
            return conn.isValid(5); // 5 seconds timeout
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Release connection back to pool
     * @param connection Connection to release
     */
    public static void releaseConnection(Connection connection) {
        if (connection == null) return;
        
        // Check if connection is from pool
        boolean inPool = false;
        for (int i = 0; i < poolCount; i++) {
            if (pool[i] == connection) {
                inPool = true;
                break;
            }
        }
        
        // Only close if not from pool
        if (!inPool) {
            closeConnection(connection);
        }
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
    
    /**
     * Close all connections in pool
     */
    public static synchronized void closePool() {
        for (int i = 0; i < poolCount; i++) {
            closeConnection(pool[i]);
            pool[i] = null;
        }
        poolCount = 0;
    }
    
    /**
     * Get current database URL (for debugging)
     * @return database URL string
     */
    public static String getDatabaseUrl() {
        return databaseUrl;
    }
}
