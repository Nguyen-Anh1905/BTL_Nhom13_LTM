
package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class TestMySql {
    
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/LapTrinhMang"; // tên database bạn đã tạo
        String user = "root";        //Dùng MySQL94
        String password = "123456";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Connect successfully !");
            } else {
                System.out.println("Error connect !");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to MySQL:");
            e.printStackTrace();
        }
        
    }
    
}
