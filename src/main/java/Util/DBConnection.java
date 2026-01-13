package Util;

import java.io.File;
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
     * SMART SEARCH: Find DomesticTourHub project in any location, then follow fixed path structure
     * Works on ANY machine - only the part before "DomesticTourHub" varies
     */
    private static String findDatabaseUrl() {
        String userName = System.getProperty("user.name");
        String userDir = System.getProperty("user.dir");
        String os = System.getProperty("os.name").toLowerCase();
        
        System.out.println("=== Finding Database (Smart Search) ===");
        System.out.println("User: " + userName);
        System.out.println("UserDir: " + userDir);
        System.out.println("OS: " + os);
        
        // SMART STRATEGY: Search for DomesticTourHub in common parent directories
        String[] parentDirs = {
            // Windows common locations
            "C:/Users/" + userName + "/eclipse-workspace",
            "C:/Users/" + userName + "/Documents/workspace",
            "D:/eclipse-workspace",
            "D:/Users/" + userName + "/eclipse-workspace",
            userDir,
            userDir + "/..",
            System.getProperty("user.home") + "/workspace",
        };
        
        String dbFileName = DB_FILE_NAME;
        String dbRelativePath = "src/main/webapp/WEB-INF/db/" + dbFileName;
        String dbAlternativePath = "WebContent/WEB-INF/db/" + dbFileName; // For older projects
        
        // Search for database file
        for (String parent : parentDirs) {
            parent = parent.replace("/", java.io.File.separator).replace("\\", java.io.File.separator);
            
            // Try path 1: src/main/webapp/WEB-INF/db/
            String fullPath1 = parent + java.io.File.separator + "DomesticTourHub" + java.io.File.separator + dbRelativePath;
            Path path1 = Paths.get(fullPath1);
            if (Files.exists(path1)) {
                String absolutePath = path1.toAbsolutePath().toString();
                System.out.println("✓ FOUND: " + absolutePath);
                return "jdbc:ucanaccess://" + absolutePath;
            }
            
            // Try path 2: WebContent/WEB-INF/db/ (older project structure)
            String fullPath2 = parent + java.io.File.separator + "DomesticTourHub" + java.io.File.separator + dbAlternativePath;
            Path path2 = Paths.get(fullPath2);
            if (Files.exists(path2)) {
                String absolutePath = path2.toAbsolutePath().toString();
                System.out.println("✓ FOUND: " + absolutePath);
                return "jdbc:ucanaccess://" + absolutePath;
            }
        }
        
        // FALLBACK: Use servletContext real path (temporary deployed files)
        // This will show a WARNING because data will be lost on restart
        if (servletContext != null) {
            String realPath = servletContext.getRealPath("/WEB-INF/db/" + dbFileName);
            if (realPath != null && Files.exists(Paths.get(realPath))) {
                System.err.println("⚠ WARNING: Project database not found!");
                System.err.println("⚠ Expected structure: [any-path]/DomesticTourHub/src/main/webapp/WEB-INF/db/" + dbFileName);
                System.err.println("⚠ Using TEMPORARY deployed database: " + realPath);
                System.err.println("⚠ DATA WILL BE LOST when server restarts!");
                return "jdbc:ucanaccess://" + realPath;
            }
        }
        
        // Last resort - return default path
        String defaultPath = userDir + java.io.File.separator + ".." + java.io.File.separator + 
                           "DomesticTourHub" + java.io.File.separator + dbRelativePath;
        defaultPath = new File(defaultPath).getAbsolutePath();
        
        System.err.println("✗ ERROR: Database file not found!");
        System.err.println("✗ Please ensure database exists at:");
        System.err.println("✗   [your-workspace]/DomesticTourHub/src/main/webapp/WEB-INF/db/" + dbFileName);
        System.err.println("✗ Falling back to: " + defaultPath);
        
        return "jdbc:ucanaccess://" + defaultPath;
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
            
            // Force commit immediately
            connection.setAutoCommit(true);
            
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
     * Force commit all pending transactions and sync to disk
     * @throws SQLException 
     */
    public static synchronized void forceCommit() throws SQLException {
        System.out.println("=== DBConnection FORCE COMMIT ===");
        for (int i = 0; i < poolCount; i++) {
            if (pool[i] != null && !pool[i].isClosed()) {
                try {
                    if (!pool[i].getAutoCommit()) {
                        pool[i].commit();
                        System.out.println("Committed connection " + i);
                    }
                } catch (SQLException e) {
                    System.err.println("Error committing connection " + i + ": " + e.getMessage());
                }
            }
        }
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
