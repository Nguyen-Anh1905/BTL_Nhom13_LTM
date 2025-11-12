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
}
