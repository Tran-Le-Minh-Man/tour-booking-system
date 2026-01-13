package Util;

import java.sql.SQLException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Servlet Context Listener for database initialization
 * Ensures DBConnection is properly initialized with servlet context
 */
@WebListener
public class DBContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize DBConnection with servlet context
        ServletContext context = sce.getServletContext();
        DBConnection.init(context);
        
        // Print database URL for debugging
        System.out.println("Database URL: " + DBConnection.getDatabaseUrl());
        
        // Test connection
        if (DBConnection.testConnection()) {
            System.out.println("Database connection established successfully!");
        } else {
            System.err.println("WARNING: Could not establish database connection!");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Force commit all pending transactions before closing
        try {
			DBConnection.forceCommit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Close all connections when context is destroyed
        DBConnection.closePool();
        System.out.println("Database pool closed.");
    }
}
