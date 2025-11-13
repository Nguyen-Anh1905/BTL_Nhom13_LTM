package Server.DAO;

import Server.model.Letters;
import Server.model.MatchDetails;
import Server.model.Matches;
import common.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.stream.Collectors;

public class LetterDAO {

    public List<Letters> findAllByIds(List<Integer> ids)  {
        List<Letters> results = new ArrayList<>();
        if (ids == null || ids.isEmpty()) return results;
        String placeholders = ids.stream().map(x -> "?").collect(Collectors.joining(","));
        String sql = "SELECT letter_id, letter_detail, length_word, time_round FROM letters WHERE letter_id IN (" + placeholders + ")";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            for (Integer id : ids) stmt.setInt(i++, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Letters l = new Letters();
                    l.setLetterId(rs.getInt("letter_id"));
                    l.setLetterDetail(rs.getString("letter_detail"));
                    l.setLengthWord(rs.getInt("length_word"));
                    l.setTimeRound(rs.getInt("time_round"));
                    results.add(l);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Letters findById(int id) {
        String sql = "SELECT letter_id, letter_detail, length_word, time_round FROM letters WHERE letter_id = ?";
        Letters l = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    l = new Letters();
                    l.setLetterId(rs.getInt("letter_id"));
                    l.setLetterDetail(rs.getString("letter_detail"));
                    l.setLengthWord(rs.getInt("length_word"));
                    l.setTimeRound(rs.getInt("time_round"));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }
}
