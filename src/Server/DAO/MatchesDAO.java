package Server.DAO;

import Server.model.Matches;
import common.DatabaseConnection;
import java.sql.*;

public class MatchesDAO {

    public Matches findById(int matchId) {
        String sql = "SELECT * FROM matches WHERE match_id = ?";
        Matches m = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    m = new Matches();
                    m.setMatchId(rs.getInt("match_id"));
                    m.setPlayer1Id(rs.getInt("player1_id"));
                    m.setPlayer2Id(rs.getInt("player2_id"));
                    m.setMatchStatus(rs.getString("match_status"));
                    m.setTotalRounds(rs.getInt("total_rounds"));
                    m.setPlayer1RoundsWon(rs.getInt("player1_rounds_won"));
                    m.setPlayer2RoundsWon(rs.getInt("player2_rounds_won"));
                    m.setWinnerId(rs.getInt("winner_id"));
                    m.setResult(rs.getString("result"));
                    m.setDraw(rs.getInt("draw"));
                    Timestamp s = rs.getTimestamp("started_at");
                    if (s != null) m.setStartedAt(s.toLocalDateTime());
                    Timestamp e = rs.getTimestamp("ended_at");
                    if (e != null) m.setEndedAt(e.toLocalDateTime());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return m;
    }

    // Thêm match mới và trả về matchId
    public int insert(Matches match) {
        String sql = "INSERT INTO matches (player1_id, player2_id, match_status, total_rounds, started_at) VALUES (?, ?, ?, ?, NOW())";
        System.out.println("[MatchesDAO] Inserting match - P1: " + match.getPlayer1Id() + ", P2: " + match.getPlayer2Id());
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, match.getPlayer1Id());
            stmt.setInt(2, match.getPlayer2Id());
            stmt.setString(3, match.getMatchStatus());
            stmt.setInt(4, match.getTotalRounds());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("[MatchesDAO] Insert SUCCESS - Generated matchId: " + id);
                    return id;
                }
            }
        } catch (SQLException ex) {
            System.err.println("[MatchesDAO] Insert FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    // Cập nhật kết quả match
    public void updateResult(int matchId, int winnerId, String result, int player1RoundsWon, int player2RoundsWon, int draw) {
        String sql = "UPDATE matches SET winner_id = ?, result = ?, player1_rounds_won = ?, player2_rounds_won = ?, draw = ?, match_status = 'completed', ended_at = NOW() WHERE match_id = ?";
        System.out.println("[MatchesDAO] Updating match - matchId: " + matchId + ", winnerId: " + winnerId + ", draw: " + draw);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Nếu winnerId = -1 (hòa), lưu NULL vào DB
            if (winnerId == -1) {
                stmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(1, winnerId);
            }
            stmt.setString(2, result);
            stmt.setInt(3, player1RoundsWon);
            stmt.setInt(4, player2RoundsWon);
            stmt.setInt(5, draw);
            stmt.setInt(6, matchId);
            int rows = stmt.executeUpdate();
            System.out.println("[MatchesDAO] Update SUCCESS - Rows affected: " + rows);
        } catch (SQLException ex) {
            System.err.println("[MatchesDAO] Update FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
