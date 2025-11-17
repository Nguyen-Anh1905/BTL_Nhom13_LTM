package Server.DAO;

import Server.model.Dictionary;
import common.DatabaseConnection;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;


public class DictionaryDAO {
    public List<Dictionary> findDictionaryFromListDicId(String listDic) {
        List<Dictionary> results = new ArrayList<>();

        // Tách chuỗi "1,2,3" thành list<Integer> chủ yếu tránh sql injection
        List<Integer> ids = Arrays.stream(listDic.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // Tạo câu SQL
        String placeholders = ids.stream().map(x -> "?").collect(Collectors.joining(","));
        String sql = "SELECT word_id, word, meaning FROM dictionary WHERE word_id IN (" + placeholders + ")";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán từng giá trị id vào dấu ? 
            int i = 1;
            for (Integer id : ids) stmt.setInt(i++, id);

            // Chạy truy vấn
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Dictionary d = new Dictionary();
                d.setWordId(rs.getInt("word_id"));
                d.setWord(rs.getString("word"));
                d.setMeaning(rs.getString("meaning"));
                results.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Lấy meaning của một word từ DB
    public String getMeaningByWord(String word) {
        String sql = "SELECT meaning FROM dictionary WHERE LOWER(word) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, word);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("meaning");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy word_id của một word từ DB
    public Integer getWordIdByWord(String word) {
        String sql = "SELECT word_id FROM dictionary WHERE LOWER(word) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, word);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("word_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy list từ hợp lệ cho một letter theo letter_id
    public List<String> getWordsByLetterId(int letterId) {
        List<String> words = new ArrayList<>();
        String sql = "SELECT word FROM dictionary WHERE letter_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, letterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                words.add(rs.getString("word"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }
}
