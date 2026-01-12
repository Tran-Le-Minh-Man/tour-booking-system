package Util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jakarta.servlet.ServletContext;

/**
 * Database connection utility class for Microsoft Access Database (ACDB)
 * Uses ServletContext to get real path - works on any machine
 */
public class DBConnection {
    // Access JDBC Driver (UCanAccess)
    private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    
    // Database file name
    private static final String DB_FILE_NAME = "tour_booking.accdb";
    
    // Connection pool settings
    private static final int MAX_CONNECTIONS = 10;
    private static final int CONNECTION_TIMEOUT = 30000;
    
    // Database URL
    private static String databaseUrl;
    
    private static volatile Connection pool[] = new Connection[MAX_CONNECTIONS];
    private static volatile int poolCount = 0;
    
    // Store ServletContext for getting real path
    private static ServletContext servletContext;
    
    /**
     * Initialize with servlet context (should be called from a ServletContextListener)
     */
    public static void init(ServletContext context) {
        servletContext = context;
        databaseUrl = findDatabaseUrl();
        System.out.println("Database initialized: " + databaseUrl);
    }
    
    /**
     * Get database URL - lazy initialization
     */
    private static String findDatabaseUrl() {
        // Try 1: Use ServletContext to get real path (best for web apps)
        if (servletContext != null) {
            String realPath = servletContext.getRealPath("/WEB-INF/db/" + DB_FILE_NAME);
            if (realPath != null && Files.exists(Paths.get(realPath))) {
                System.out.println("Using database from ServletContext: " + realPath);
                return "jdbc:ucanaccess://" + realPath;
            }
        }
        
        // Try 2: Common workspace locations
        String userName = System.getProperty("user.name");
        String[][] possiblePaths = {
            // Standard Eclipse workspace
            {"C:/Users", userName + "/eclipse-workspace/DomesticTourHub/src/main/webapp/WEB-INF/db/" + DB_FILE_NAME},
            {"C:/Users", userName + "/eclipse-workspace/tour-booking-system/src/main/webapp/WEB-INF/db/" + DB_FILE_NAME},
            // D drive
            {"D:/eclipse-workspace", "/DomesticTourHub/src/main/webapp/WEB-INF/db/" + DB_FILE_NAME},
            {"D:/Users", userName + "/eclipse-workspace/DomesticTourHub/src/main/webapp/WEB-INF/db/" + DB_FILE_NAME},
            // Current directory based
            {System.getProperty("user.dir"), "/src/main/webapp/WEB-INF/db/" + DB_FILE_NAME},
            {System.getProperty("user.dir"), "/../src/main/webapp/WEB-INF/db/" + DB_FILE_NAME},
        };
        
        for (String[] pathInfo : possiblePaths) {
            String baseDir = pathInfo[0];
            String relativePath = pathInfo[1];
            String fullPath = baseDir + relativePath;
            
            // Handle both Windows and Unix paths
            fullPath = fullPath.replace("/", java.io.File.separator).replace("\\", java.io.File.separator);
            
            Path dbPath = Paths.get(fullPath);
            if (Files.exists(dbPath)) {
                String absolutePath = dbPath.toAbsolutePath().toString();
                System.out.println("Found database at: " + absolutePath);
                return "jdbc:ucanaccess://" + absolutePath;
            }
        }
        
        // Last resort
        System.err.println("WARNING: Database file not found! Using default path.");
        return "jdbc:ucanaccess://" + System.getProperty("user.dir") + "/WEB-INF/db/" + DB_FILE_NAME;
    }
    
    /**
     * Get database connection from pool
     */
    public static synchronized Connection getConnection() throws SQLException {
        // Initialize on first use if not already done
        if (databaseUrl == null) {
            databaseUrl = findDatabaseUrl();
        }
        
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
        
        return createConnection();
    }
    
    /**
     * Create a new database connection
     */
    private static Connection createConnection() throws SQLException {
        // Initialize if needed
        if (databaseUrl == null) {
            databaseUrl = findDatabaseUrl();
        }
        
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(databaseUrl);
            connection.setAutoCommit(true);
            DriverManager.setLoginTimeout(CONNECTION_TIMEOUT / 1000);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Access Driver not found: " + e.getMessage());
            throw new SQLException("Access Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database Connection failed: " + e.getMessage());
            System.err.println("Database URL: " + databaseUrl);
            throw e;
        }
        return connection;
    }
    
    /**
     * Check if connection is still valid
     */
    private static boolean isConnectionValid(Connection conn) {
        try {
            return conn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Release connection back to pool
     */
    public static void releaseConnection(Connection connection) {
        if (connection == null) return;
        
        boolean inPool = false;
        for (int i = 0; i < poolCount; i++) {
            if (pool[i] == connection) {
                inPool = true;
                break;
            }
        }
        
        if (!inPool) {
            closeConnection(connection);
        }
    }
    
    /**
     * Close connection safely
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
     */
    public static String getDatabaseUrl() {
        if (databaseUrl == null) {
            databaseUrl = findDatabaseUrl();
        }
        return databaseUrl;
    }
}
