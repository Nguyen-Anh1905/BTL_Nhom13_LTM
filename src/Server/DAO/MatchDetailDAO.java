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
    String sql = "SELECT detail_id, match_id, round_number, letter_id, player1_words_count, player2_words_count, player1_dic, player2_dic, winner_id, round_status, started_at, ended_at, created_at "
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
}
