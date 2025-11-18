package Server.DAO;

import Server.model.MatchDetails;
import Server.model.Matches;
import common.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDetailDAO {


    // lấy thông tin chi tiết trận đấu theo matchId
    public List<MatchDetails> searchMatchDetailByMatchId(int matchId) {
        List<MatchDetails> results = new ArrayList<>();
        // Correct table name and columns according to database schema
    // include player1_dic/player2_dic if available in schema — project expects these CSV fields
    String sql = "SELECT detail_id, match_id, round_number, letter_id, player1_words_count, player2_words_count, player1_dic, player2_dic, round_status, started_at, ended_at, created_at "
        + "FROM match_details WHERE match_id = ? ORDER BY round_number ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MatchDetails details = new MatchDetails();
                details.setMatchId(rs.getInt("match_id"));
                details.setDetailId(rs.getInt("detail_id"));
                details.setRoundNumber(rs.getInt("round_number"));
                details.setLetterId(rs.getInt("letter_id"));
                details.setPlayer1WordsCount(rs.getInt("player1_words_count"));
                details.setPlayer2WordsCount(rs.getInt("player2_words_count"));
                try {
                    details.setPlayer1Dic(rs.getString("player1_dic"));
                    details.setPlayer2Dic(rs.getString("player2_dic"));
                } catch (SQLException ex) {
                    // column not present in this database export — swallow and continue with nulls
                    details.setPlayer1Dic(null);
                    details.setPlayer2Dic(null);
                }
                results.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Thêm chi tiết round với started_at khi bắt đầu
    public void insert(MatchDetails detail) {
        String sql = "INSERT INTO match_details (match_id, round_number, letter_id, player1_words_count, player2_words_count, player1_dic, player2_dic, round_status, started_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        System.out.println("[MatchDetailDAO] Inserting - matchId: " + detail.getMatchId() + ", round: " + detail.getRoundNumber());
        System.out.println("[MatchDetailDAO]   player1_dic: '" + detail.getPlayer1Dic() + "'");
        System.out.println("[MatchDetailDAO]   player2_dic: '" + detail.getPlayer2Dic() + "'");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getMatchId());
            stmt.setInt(2, detail.getRoundNumber());
            stmt.setInt(3, detail.getLetterId());
            stmt.setInt(4, detail.getPlayer1WordsCount());
            stmt.setInt(5, detail.getPlayer2WordsCount());
            stmt.setString(6, detail.getPlayer1Dic());
            stmt.setString(7, detail.getPlayer2Dic());
            stmt.setString(8, detail.getRoundStatus());
            int rows = stmt.executeUpdate();
            System.out.println("[MatchDetailDAO] Insert SUCCESS - Rows affected: " + rows);
        } catch (SQLException e) {
            System.err.println("[MatchDetailDAO] Insert FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Cập nhật kết quả round khi kết thúc (thay vì insert rồi update riêng)
    public void updateRoundResult(MatchDetails detail) {
        String sql = "UPDATE match_details SET player1_words_count = ?, player2_words_count = ?, " +
                     "player1_dic = ?, player2_dic = ?, round_status = 'completed', ended_at = NOW() " +
                     "WHERE match_id = ? AND round_number = ?";
        System.out.println("[MatchDetailDAO] Updating round result - matchId: " + detail.getMatchId() + ", round: " + detail.getRoundNumber());
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getPlayer1WordsCount());
            stmt.setInt(2, detail.getPlayer2WordsCount());
            stmt.setString(3, detail.getPlayer1Dic());
            stmt.setString(4, detail.getPlayer2Dic());
            stmt.setInt(5, detail.getMatchId());
            stmt.setInt(6, detail.getRoundNumber());
            int rows = stmt.executeUpdate();
            System.out.println("[MatchDetailDAO] Update round result SUCCESS - Rows affected: " + rows);
        } catch (SQLException e) {
            System.err.println("[MatchDetailDAO] Update round result FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
