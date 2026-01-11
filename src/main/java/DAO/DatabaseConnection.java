package DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String driverClass = "net.ucanaccess.jdbc.UcanaccessDriver";
    private static String url = "jdbc:ucanaccess://C:/path/to/your/database.accdb";
    private static String username = "";
    private static String password = "";

    static {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}