package Server.DAO;

import Server.model.Matches;
import Server.model.MatchDetails;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho bảng matches và match_details
 */
public class MatchDAO {
    // Thông tin kết nối database
    private static final String URL = "jdbc:mysql://localhost:3306/gamevtv";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Tạo trận đấu mới - Gọi stored procedure CreateMatch
     * @param player1Id ID người chơi 1
     * @param player2Id ID người chơi 2
     * @return match_id của trận vừa tạo, hoặc -1 nếu thất bại
     */
    public int createMatch(int player1Id, int player2Id) {
        String sql = "{CALL CreateMatch(?, ?, ?)}";
        
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, player1Id);
            stmt.setInt(2, player2Id);
            stmt.registerOutParameter(3, Types.INTEGER);
            
            stmt.execute();
            
            return stmt.getInt(3);
            
        } catch (SQLException e) {
            System.err.println("Error createMatch: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Tạo vòng đấu - Gọi stored procedure CreateRound
     * @param matchId ID trận đấu
     * @param roundNumber Số vòng (1, 2, 3)
     * @param letterId ID của letter set
     * @return detail_id của vòng vừa tạo, hoặc -1 nếu thất bại
     */
    public int createRound(int matchId, int roundNumber, int letterId) {
        String sql = "{CALL CreateRound(?, ?, ?, ?)}";
        
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, matchId);
            stmt.setInt(2, roundNumber);
            stmt.setInt(3, letterId);
            stmt.registerOutParameter(4, Types.INTEGER);
            
            stmt.execute();
            
            return stmt.getInt(4);
            
        } catch (SQLException e) {
            System.err.println("Error createRound: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Kết thúc vòng đấu - Gọi stored procedure FinishRound
     * @param detailId ID của match_details
     */
    public void finishRound(int detailId) {
        String sql = "{CALL FinishRound(?)}";
        
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, detailId);
            stmt.execute();
            
        } catch (SQLException e) {
            System.err.println("Error finishRound: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Kết thúc trận đấu - Gọi stored procedure FinishMatch
     * @param matchId ID trận đấu
     */
    public void finishMatch(int matchId) {
        String sql = "{CALL FinishMatch(?)}";
        
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, matchId);
            stmt.execute();
            
        } catch (SQLException e) {
            System.err.println("Error finishMatch: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy thông tin trận đấu theo match_id
     */
    public Matches getMatchById(int matchId) {
        String sql = "SELECT * FROM matches WHERE match_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, matchId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Matches(
                    rs.getInt("match_id"),
                    rs.getInt("player1_id"),
                    rs.getInt("player2_id"),
                    rs.getString("match_status"),
                    rs.getInt("total_rounds"),
                    rs.getInt("player1_rounds_won"),
                    rs.getInt("player2_rounds_won"),
                    (Integer) rs.getObject("winner_id"),
                    rs.getString("result"),
                    rs.getTimestamp("started_at") != null ? rs.getTimestamp("started_at").toLocalDateTime() : null,
                    rs.getTimestamp("ended_at") != null ? rs.getTimestamp("ended_at").toLocalDateTime() : null,
                    rs.getInt("draw")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getMatchById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Lấy thông tin vòng đấu theo detail_id
     */
    public MatchDetails getRoundById(int detailId) {
        String sql = "SELECT * FROM match_details WHERE detail_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detailId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                MatchDetails detail = new MatchDetails(
                    rs.getInt("detail_id"),
                    rs.getInt("match_id"),
                    rs.getInt("round_number"),
                    (Integer) rs.getObject("letter_id"),
                    rs.getInt("player1_words_count"),
                    rs.getInt("player2_words_count"),
                    (Integer) rs.getObject("winner_id"),
                    rs.getString("round_status"),
                    rs.getTimestamp("started_at") != null ? rs.getTimestamp("started_at").toLocalDateTime() : null,
                    rs.getTimestamp("ended_at") != null ? rs.getTimestamp("ended_at").toLocalDateTime() : null,
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                return detail;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getRoundById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Cập nhật trạng thái vòng đấu thành 'playing'
     */
    public void updateRoundToPlaying(int detailId) {
        String sql = "UPDATE match_details SET round_status = 'playing', started_at = NOW() WHERE detail_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detailId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error updateRoundToPlaying: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
