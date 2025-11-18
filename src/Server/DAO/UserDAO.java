
package Server.DAO;

import Server.model.Matches;
import Server.model.Users;
import common.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class UserDAO {
    
    // Lấy connection
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
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
    
    // Cập nhật điểm và thống kê sau trận đấu
    // result: "win", "lose", "draw"
    public boolean updateUserStats(int userId, String result) {
        String sql;
        if ("win".equals(result)) {
            sql = "UPDATE users SET total_points = total_points + 2, total_wins = total_wins + 1, updated_at = NOW() WHERE user_id = ?";
        } else if ("draw".equals(result)) {
            sql = "UPDATE users SET total_points = total_points + 1, total_draws = total_draws + 1, updated_at = NOW() WHERE user_id = ?";
        } else if ("lose".equals(result)) {
            sql = "UPDATE users SET total_losses = total_losses + 1, updated_at = NOW() WHERE user_id = ?";
        } else {
            return false;
        }
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            System.out.println("[UserDAO] Updated stats for userId " + userId + " - Result: " + result + " - Rows: " + rows);
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("[UserDAO] Update stats FAILED for userId " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy username từ userId
    public String getUsernameById(int userId) {
        String sql = "SELECT username FROM users WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("[UserDAO] Get username FAILED for userId " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy danh sách người chơi online từ view online_players
    public List<Users> getOnlinePlayersFromView() {
        List<Users> onlinePlayers = new ArrayList<>();
        String sql = "SELECT * FROM online_players";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setStatus(rs.getString("status"));
                user.setTotalPoints(rs.getInt("total_points"));
                user.setTotalWins(rs.getInt("total_wins"));
                user.setTotalDraws(rs.getInt("total_draws"));
                user.setTotalLosses(rs.getInt("total_losses"));
                onlinePlayers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return onlinePlayers;
    }
    // Lấy bảng xếp hạng theo điểm
    public List<Users> getLeaderboardByPoints() {
        List<Users> leaderboard = new ArrayList<>();
        String sql = "SELECT * FROM leaderboard_by_points";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setTotalPoints(rs.getInt("total_points"));
                user.setTotalWins(rs.getInt("total_wins"));
                user.setTotalDraws(rs.getInt("total_draws"));
                user.setTotalLosses(rs.getInt("total_losses"));
                leaderboard.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }

    // Lấy bảng xếp hạng theo số trận thắng
    public List<Users> getLeaderboardByWins() {
        List<Users> leaderboard = new ArrayList<>();
        String sql = "SELECT * FROM leaderboard_by_wins";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setTotalWins(rs.getInt("total_wins"));
                user.setTotalPoints(rs.getInt("total_points"));
                user.setTotalDraws(rs.getInt("total_draws"));
                user.setTotalLosses(rs.getInt("total_losses"));
                leaderboard.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }

    // Tìm người dùng theo username (partial match) - trả về danh sách
    public List<Users> searchUsersByUsername(String pattern) {
        List<Users> results = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + pattern + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setStatus(rs.getString("status"));
                user.setTotalPoints(rs.getInt("total_points"));
                user.setTotalWins(rs.getInt("total_wins"));
                user.setTotalDraws(rs.getInt("total_draws"));
                user.setTotalLosses(rs.getInt("total_losses"));
                results.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Lấy thông tin lịch sử đấu của User đang đăng nhập
    public List<Matches> searchMatchesByUserId(int userId) {
        List<Matches> results = new ArrayList<>();
        String sql = "SELECT * FROM matches WHERE player1_id = ? OR player2_id = ? ORDER BY started_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Matches match = new Matches();
                    match.setMatchId(rs.getInt("match_id"));
                    match.setPlayer1Id(rs.getInt("player1_id"));
                    match.setPlayer2Id(rs.getInt("player2_id"));
                    match.setMatchStatus(rs.getString("match_status"));
                    match.setTotalRounds(rs.getInt("total_rounds"));
                    match.setPlayer1RoundsWon(rs.getInt("player1_rounds_won"));
                    match.setPlayer2RoundsWon(rs.getInt("player2_rounds_won"));
                    match.setWinnerId(rs.getInt("winner_id"));
                    match.setResult(rs.getString("result"));
                    Timestamp st = rs.getTimestamp("started_at");
                    if (st != null) match.setStartedAt(st.toLocalDateTime());
                    Timestamp et = rs.getTimestamp("ended_at");
                    if (et != null) match.setEndedAt(et.toLocalDateTime());
                    match.setDraw(rs.getInt("draw"));
                    results.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Lấy user theo user_id
    public Users getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    // nếu có thêm cột khác (full_name, status, ...) thì set ở đây
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm người dùng đang online theo username (dùng cho Lobby) - tìm trong view online_players
    public List<Users> searchOnlineUsersByUsername(String pattern) {
        List<Users> results = new ArrayList<>();
        String sql = "SELECT * FROM online_players WHERE username LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + pattern + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    results.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

}
