package Server.DAO;

import Server.model.Letters;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho bảng letters
 */
public class LetterDAO {
    // Thông tin kết nối database
    private static final String URL = "jdbc:mysql://localhost:3306/gamevtv";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Lấy ngẫu nhiên 3 letter_id khác nhau từ bảng letters
     */
    public List<Integer> randomThreeLetterIds() {
        List<Integer> letterIds = new ArrayList<>();
        String sql = "SELECT letter_id FROM letters ORDER BY RAND() LIMIT 3";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                letterIds.add(rs.getInt("letter_id"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error randomThreeLetterIds: " + e.getMessage());
            e.printStackTrace();
        }
        
        return letterIds;
    }
    

    public Letters getLetterById(int letterId) {
        String sql = "SELECT * FROM letters WHERE letter_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, letterId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Letters(
                    rs.getInt("letter_id"),
                    rs.getString("letter_detail"),
                    rs.getInt("length_word"),
                    rs.getInt("time_round")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getLetterById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
