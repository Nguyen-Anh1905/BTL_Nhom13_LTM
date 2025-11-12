package Server;

import common.*;
import java.io.*;
import java.net.*;
import java.util.*;
import Server.DAO.*;
import Server.model.*;
import Server.service.MatchHistoryService;
import Server.dto.MatchHistoryResponse;
import Server.service.MatchDetailService;
import Server.DAO.MatchesDAO;
import Server.dto.MatchDetailResponse;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private List<ClientHandler> clients;
    
    private UserDAO userDAO = new UserDAO();
    private MatchHistoryService matchHistoryService = new MatchHistoryService();

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        String username = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client đã kết nối: " + socket.getInetAddress());

            while (true) {
                Message msg = (Message) in.readObject();
                // Lưu lại username khi login thành công
                if (msg.getType().equals(Protocol.LOGIN)) {
                    String[] parts = ((String) msg.getContent()).split(":");
                    username = parts[0];
                }
                handleMessage(msg);
            }
        } catch (Exception e) {
            System.out.println("Client ngắt kết nối: " + socket.getInetAddress());
            // Nếu đã biết username, cập nhật status về offline
            if (username != null) {
                userDAO.updateUserStatus(username, "offline");
                System.out.println("Đã cập nhật " + username + " về offline do mất kết nối.");
            }
        }
    }

    private void handleMessage(Message msg) throws IOException {
        if (msg == null) return;

        switch (msg.getType()) {
            case Protocol.LOGIN:
                // Parse dữ liệu login
                String[] parts = ((String) msg.getContent()).split(":");
                String username = parts[0];
                String password = parts[1];

                // Lấy user từ database
                Users user = userDAO.getUserByUsername(username);   
                // Kiểm tra user tồn tại và mật khẩu đúng
                if (user != null && user.getPassword().equals(password)) {
                    // ĐĂNG NHẬP THÀNH CÔNG                  
                    // Cập nhật trạng thái thành "online"
                    userDAO.updateUserStatus(username, "online");
                    // Lấy lại thông tin user đã cập nhật
                    user = userDAO.getUserByUsername(username);
                    System.out.println(username + " đăng nhập thành công!");
                    
                    // GỬI OBJECT USER về Client
                    out.writeObject(new Message(Protocol.LOGIN_SUCCESS, user));
                    // KHÔNG GỬI PLAYER_LIST NGAY - để client tự yêu cầu sau
                    
                } else {
                    System.out.println("Sai tài khoản hoặc mật khẩu: " + username);
                    out.writeObject(new Message(Protocol.LOGIN_FAILURE, "Sai tên hoặc mật khẩu!"));
                }
                out.flush();
                break;            
            case Protocol.REGISTER:  // ← THÊM case mới
                // Parse dữ liệu: "fullName:username:password"
                String[] regData = ((String) msg.getContent()).split(":");
                String fullName = regData[0];
                String regUsername = regData[1];
                String regPassword = regData[2];
                
                // Kiểm tra username đã tồn tại chưa
                if (userDAO.checkUsernameExists(regUsername)) {
                    System.out.println("Đăng ký thất bại: Username đã tồn tại - " + regUsername);
                    out.writeObject(new Message(Protocol.REGISTER_FAILURE, "Username đã tồn tại!"));
                } else {
                    // Tạo user mới
                    Users newUser = new Users(regUsername, regPassword, fullName);                  
                    // Lưu vào database
                    boolean success = userDAO.insertUser(newUser);
                    
                    if (success) {
                        // ĐĂNG KÝ THÀNH CÔNG                 
                        // Cập nhật trạng thái thành "online"
                        userDAO.updateUserStatus(regUsername, "online"); 
                        // Lấy thông tin user vừa tạo
                        Users registeredUser = userDAO.getUserByUsername(regUsername);
                        System.out.println("Đăng ký thành công: " + regUsername);        
                        // GỬI OBJECT USER về Client
                        out.writeObject(new Message(Protocol.REGISTER_SUCCESS, registeredUser));
                    } else {
                        System.out.println("Đăng ký thất bại: Lỗi database - " + regUsername);
                        out.writeObject(new Message(Protocol.REGISTER_FAILURE, "Lỗi server, vui lòng thử lại!"));
                    }
                }
                out.flush();
                break;
            
            case Protocol.LOGOUT:
                String usernameLogout = (String) msg.getContent();
                userDAO.updateUserStatus(usernameLogout, "offline");
                System.out.println(usernameLogout + " đã đăng xuất!");
                socket.close(); // Đóng kết nối với client này
                break;
                
            case Protocol.GET_PLAYER_LIST:
                System.out.println("Client yêu cầu danh sách người chơi");
                List<Users> onlinePlayers = userDAO.getOnlinePlayersFromView();
                out.writeObject(new Message(Protocol.PLAYER_LIST, onlinePlayers));
                out.flush();
                System.out.println("Đã gửi danh sách: " + onlinePlayers.size() + " người chơi");
                break;
            case Protocol.GET_LEADERBOARD_POINTS:
                System.out.println("Client yêu cầu leaderboard theo điểm");
                List<Users> lbPoints = userDAO.getLeaderboardByPoints();
                out.writeObject(new Message(Protocol.LEADERBOARD_DATA, lbPoints));
                out.flush();
                System.out.println("Đã gửi leaderboard (points): " + (lbPoints != null ? lbPoints.size() : 0));
                break;
            case Protocol.GET_LEADERBOARD_WINS:
                System.out.println("Client yêu cầu leaderboard theo thắng");
                List<Users> lbWins = userDAO.getLeaderboardByWins();
                out.writeObject(new Message(Protocol.LEADERBOARD_DATA, lbWins));
                out.flush();
                System.out.println("Đã gửi leaderboard (wins): " + (lbWins != null ? lbWins.size() : 0));
                break;
            case Protocol.SEARCH_PLAYER_IN_LOBBY:
                System.out.println("Client yêu cầu tìm user (Tab 1 - Lobby): " + msg.getContent());
                String findUsernameLobby = (String) msg.getContent();
                List<Users> foundListLobby = userDAO.searchUsersByUsername(findUsernameLobby);
                if (foundListLobby == null || foundListLobby.isEmpty()) {
                    System.out.println("Không tìm thấy user: " + findUsernameLobby);
                } else {
                    System.out.println("Tìm thấy " + foundListLobby.size() + " người chơi khớp với: " + findUsernameLobby);
                }
                out.writeObject(new Message(Protocol.SEARCH_RESULT_LOBBY, foundListLobby));
                out.flush();
                break;
            case Protocol.SEARCH_PLAYER_IN_LEADERBOARD:
                System.out.println("Client yêu cầu tìm user (Tab 3 - Leaderboard): " + msg.getContent());
                String findUsernameLeaderboard = (String) msg.getContent();
                List<Users> foundListLeaderboard = userDAO.searchUsersByUsername(findUsernameLeaderboard);
                if (foundListLeaderboard == null || foundListLeaderboard.isEmpty()) {
                    System.out.println("Không tìm thấy user: " + findUsernameLeaderboard);
                } else {
                    System.out.println("Tìm thấy " + foundListLeaderboard.size() + " người chơi khớp với: " + findUsernameLeaderboard);
                }
                out.writeObject(new Message(Protocol.SEARCH_RESULT_LEADERBOARD, foundListLeaderboard));
                out.flush();
                break;
            case Protocol.SEARCH_PLAYER:
                System.out.println("Client yêu cầu tìm user: " + msg.getContent());
                String findUsername = (String) msg.getContent();
                // Dùng phương thức tìm kiếm partial trong DAO để trả về nhiều kết quả
                List<Users> foundList = userDAO.searchUsersByUsername(findUsername);
                if (foundList == null || foundList.isEmpty()) {
                    System.out.println("Không tìm thấy user: " + findUsername);
                } else {
                    System.out.println("Tìm thấy " + foundList.size() + " người chơi khớp với: " + findUsername);
                }
                out.writeObject(new Message(Protocol.LEADERBOARD_DATA, foundList));
                out.flush();
                break;
            case Protocol.GET_MATCH_HISTORY:
                System.out.println("Client yêu cầu lịch sử trận đấu: " + msg.getContent());
                String raw = (String) msg.getContent();
                String reqUser = null;
                String opponentFilter = null;
                if (raw != null && raw.contains(":")) {
                    String[] p = raw.split(":", 2);
                    reqUser = p[0];
                    opponentFilter = p[1];
                } else {
                    reqUser = raw;
                }
                List<MatchHistoryResponse> hist;
                if (opponentFilter != null && !opponentFilter.trim().isEmpty()) {
                    hist = matchHistoryService.getMatchHistoryForUser(reqUser, opponentFilter);
                } else {
                    hist = matchHistoryService.getMatchHistoryForUser(reqUser);
                }
                if (hist == null || hist.isEmpty()) {
                    System.out.println("Không có lịch sử trận đấu cho: " + reqUser + (opponentFilter != null ? (" với " + opponentFilter) : ""));
                } else {
                    System.out.println("Trả về " + hist.size() + " bản ghi lịch sử cho: " + reqUser + (opponentFilter != null ? (" với " + opponentFilter) : ""));
                }
                out.writeObject(new Message(Protocol.MATCH_HISTORY_DATA, hist));
                out.flush();
                break;
            case Protocol.GET_MATCH_DETAIL:
                System.out.println("Client yêu cầu chi tiết trận: " + msg.getContent());
                String rawDetail = (String) msg.getContent();
                // expected payload: "matchId:username"
                if (rawDetail != null && rawDetail.contains(":")) {
                    String[] partsDetail = rawDetail.split(":", 2);
                    try {
                        int matchId = Integer.parseInt(partsDetail[0]);
                        String usernameReq = partsDetail[1];

                        // determine whether username is player1 or player2
                        MatchesDAO matchesDAO = new MatchesDAO();
                        Matches match = matchesDAO.findById(matchId);
                        String youSide = "player1"; // default
                        if (match != null) {
                            UserDAO udao = new UserDAO();
                            try {
                                int uid = udao.getUserByUsername(usernameReq).getUserId();
                                if (uid == match.getPlayer2Id()) youSide = "player2";
                            } catch (Exception ex) {
                                // cannot resolve user, default to player1
                            }
                        }

                        MatchDetailService detailService = new MatchDetailService();
                        List<MatchDetailResponse> detailsResp = detailService.getMatchDetails(matchId, youSide, null);
                        out.writeObject(new Message(Protocol.MATCH_DETAIL_DATA, detailsResp));
                        out.flush();
                    } catch (NumberFormatException nfe) {
                        out.writeObject(new Message(Protocol.MATCH_DETAIL_DATA, new ArrayList<>()));
                        out.flush();
                    }
                } else {
                    out.writeObject(new Message(Protocol.MATCH_DETAIL_DATA, new ArrayList<>()));
                    out.flush();
                }
                break;
        }
    }
}
