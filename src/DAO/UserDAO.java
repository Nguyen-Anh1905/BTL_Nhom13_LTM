
package DAO;

import model.Users;
import java.sql.*;
import java.time.LocalDateTime;

public class UserDAO {
    
    // Thông tin kết nối database
    private static final String URL = "jdbc:mysql://localhost:3306/gamevtv";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    
    // Lấy connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    // Kiểm tra username đã tồn tại chưa
    public boolean checkUsernameExists(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Trả về true nếu có kết quả
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Thêm user mới (đăng ký)
    public boolean insertUser(Users user) {
        String sql = "INSERT INTO users (username, password, full_name, status, total_points, total_wins, total_draws, total_losses, created_at, updated_at) " +
                     "VALUES (?, ?, ?, 'offline', 0, 0, 0, 0, NOW(), NOW())";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            
            int rows = stmt.executeUpdate();
            return rows > 0; // Trả về true nếu insert thành công
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // Lấy user theo username (dùng cho đăng nhập)
    public Users getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                
                
                Users user = new Users();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setStatus(rs.getString("status"));
                user.setTotalPoints(rs.getInt("total_points"));
                user.setTotalWins(rs.getInt("total_wins"));
                user.setTotalDraws(rs.getInt("total_draws"));
                user.setTotalLosses(rs.getInt("total_losses"));
                
                // Chuyển đổi Timestamp sang LocalDateTime (kiểm tra null)
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    user.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    user.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                
                return user;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null; // Không tìm thấy user
    }
    
    // Cập nhật trạng thái user (online/offline/playing)
    public boolean updateUserStatus(String username, String status) {
        String sql = "UPDATE users SET status = ?, updated_at = NOW() WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setString(2, username);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
