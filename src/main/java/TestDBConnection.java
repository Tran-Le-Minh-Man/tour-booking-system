import Util.DBConnection;

public class TestDBConnection {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        if (DBConnection.testConnection()) {
            System.out.println("✓ Kết nối database thành công!");
        } else {
            System.out.println("✗ Kết nối database thất bại!");
            System.out.println("Kiểm tra lại đường dẫn file .accdb");
        }
    }
}