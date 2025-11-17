package Server;
import Server.service.Gameroom;
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

    private Gameroom currentGameroom; 
    
    private UserDAO userDAO = new UserDAO();
    private int nguoiChoiID = -1;  // ID của user đang kết nối
    private String tenNguoiChoi = null;  // Username của user đang kết nối
    private MatchHistoryService matchHistoryService = new MatchHistoryService();

    public ClientHandler(Socket socket) {
        this.socket = socket;
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
            
            // Kiểm tra nếu đang trong game → xử lý forfeit
            if (currentGameroom != null && nguoiChoiID != -1) {
                System.out.println("⚠️ Client " + nguoiChoiID + " ngắt kết nối trong khi đang chơi game!");
                currentGameroom.handleForfeit(nguoiChoiID);
                currentGameroom = null; // Clear reference
            }
            
            // Xóa khỏi Map
            if (nguoiChoiID != -1) {
                Server.getUserHandlers().remove(nguoiChoiID);
            }
            
            // Nếu đã biết username, cập nhật status về offline
            if (username != null) {
                userDAO.updateUserStatus(username, "offline");
                System.out.println("Đã cập nhật " + username + " về offline do mất kết nối.");
            } else if (tenNguoiChoi != null) {
                userDAO.updateUserStatus(tenNguoiChoi, "offline");
                System.out.println("Đã cập nhật " + tenNguoiChoi + " về offline do mất kết nối.");
            }
        }
    }
    // Method để gửi message cho client KHÁC (catch exception)
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.out.println("Lỗi khi gửi message: " + e.getMessage());
        }
    }
    
    // Method để gửi message cho CHÍNH client này (throw exception)
    private void sendToThisClient(Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
    
    // Method để clear gameroom reference (gọi khi game kết thúc)
    public void clearGameroom() {
        this.currentGameroom = null;
        System.out.println("🗑️ Đã clear gameroom reference cho userId: " + nguoiChoiID);
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
                    // Cập nhật trạng thái thành "online"
                    userDAO.updateUserStatus(username, "online");
                    // Lấy lại thông tin user đã cập nhật
                    user = userDAO.getUserByUsername(username);                   
                    // Lưu userId và username
                    nguoiChoiID = user.getUserId();
                    tenNguoiChoi = user.getUsername();
                    // Thêm vào Map để các ClientHandler khác có thể tìm thấy
                    Server.getUserHandlers().put(nguoiChoiID, this);
                    System.out.println(username + " đăng nhập thành công! UserID: " + nguoiChoiID);
                    
                    // GỬI OBJECT USER về Client
                    sendToThisClient(new Message(Protocol.LOGIN_SUCCESS, user));
                } else {
                    System.out.println("Sai tài khoản hoặc mật khẩu: " + username);
                    sendToThisClient(new Message(Protocol.LOGIN_FAILURE, "Sai tên hoặc mật khẩu!"));
                }
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
                    sendToThisClient(new Message(Protocol.REGISTER_FAILURE, "Username đã tồn tại!"));
                } else {
                    // Tạo user mới
                    Users newUser = new Users(regUsername, regPassword, fullName);                  
                    // Lưu vào database
                    boolean success = userDAO.insertUser(newUser);
                    
                    if (success) {
                        userDAO.updateUserStatus(regUsername, "online"); 
                        // Lấy thông tin user vừa tạo
                        Users registeredUser = userDAO.getUserByUsername(regUsername);
                        
                        // Lưu userId và username
                        nguoiChoiID = registeredUser.getUserId();
                        tenNguoiChoi = registeredUser.getUsername();
                        
                        // Thêm vào Map
                        Server.getUserHandlers().put(nguoiChoiID, this);
                        
                        System.out.println("Đăng ký thành công: " + regUsername + " UserID: " + nguoiChoiID);        
                        // GỬI OBJECT USER về Client
                        sendToThisClient(new Message(Protocol.REGISTER_SUCCESS, registeredUser));
                    } else {
                        System.out.println("Đăng ký thất bại: Lỗi database - " + regUsername);
                        sendToThisClient(new Message(Protocol.REGISTER_FAILURE, "Lỗi server, vui lòng thử lại!"));
                    }
                }
                break;
            
            case Protocol.LOGOUT:
                String usernameLogout = (String) msg.getContent();
                userDAO.updateUserStatus(usernameLogout, "offline");
                // Xóa khỏi Map
                if (nguoiChoiID != -1) {
                    Server.getUserHandlers().remove(nguoiChoiID);
                }
                System.out.println(usernameLogout + " đã đăng xuất!");
                socket.close(); // Đóng kết nối với client này
                break;
                
            case Protocol.GET_PLAYER_LIST:
                System.out.println("Client yêu cầu danh sách người chơi");
                List<Users> onlinePlayers = userDAO.getOnlinePlayersFromView();
                sendToThisClient(new Message(Protocol.PLAYER_LIST, onlinePlayers));
                System.out.println("Đã gửi danh sách: " + onlinePlayers.size() + " người chơi");
                break;
            case Protocol.GET_LEADERBOARD_POINTS:
                System.out.println("Client yêu cầu leaderboard theo điểm");
                List<Users> lbPoints = userDAO.getLeaderboardByPoints();
                sendToThisClient(new Message(Protocol.LEADERBOARD_DATA, lbPoints));
                System.out.println("Đã gửi leaderboard (points): " + (lbPoints != null ? lbPoints.size() : 0));
                break;
            case Protocol.GET_LEADERBOARD_WINS:
                System.out.println("Client yêu cầu leaderboard theo thắng");
                List<Users> lbWins = userDAO.getLeaderboardByWins();
                sendToThisClient(new Message(Protocol.LEADERBOARD_DATA, lbWins));
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
                sendToThisClient(new Message(Protocol.SEARCH_RESULT_LOBBY, foundListLobby));
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
                sendToThisClient(new Message(Protocol.SEARCH_RESULT_LEADERBOARD, foundListLeaderboard));
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
                sendToThisClient(new Message(Protocol.LEADERBOARD_DATA, foundList));
                break;
                
            case Protocol.CHALLENGE_REQUEST:
                // Client A mời client B đấu
                int doiThuID = Integer.parseInt((String) msg.getContent());
                System.out.println("User " + nguoiChoiID + " mời user " + doiThuID + " đấu");
                
                // Tìm ClientHandler của opponent
                ClientHandler doiThuHandler = Server.getUserHandlers().get(doiThuID);
                
                // Kiểm tra opponent có online không
                if (doiThuHandler == null) {
                    System.out.println("User " + doiThuID + " không online!");
                    sendToThisClient(new Message(Protocol.CHALLENGE_FAILED, "Người chơi không online!"));
                } else {
                    // Gửi lời mời cho opponent (kèm userId và username của người mời)
                    String nguoiMoiInfo = nguoiChoiID + ":" + tenNguoiChoi;
                    doiThuHandler.sendMessage(new Message(Protocol.CHALLENGE_INVITATION, nguoiMoiInfo));
                    System.out.println("Đã gửi lời mời đấu đến user " + doiThuID);
                }
                break;
                
            case Protocol.CHALLENGE_ACCEPT:
                // Người nhận chấp nhận lời mời
                int nguoiMoiID = Integer.parseInt((String) msg.getContent());
                System.out.println("User " + nguoiChoiID + " chấp nhận lời mời từ user " + nguoiMoiID);  
                // Tìm ClientHandler của inviter
                ClientHandler nguoiMoiHandler = Server.getUserHandlers().get(nguoiMoiID);
                
                if (nguoiMoiHandler != null) {
                    // CẬP NHẬT TRẠNG THÁI 'PLAYING' CHO CẢ 2 NGƯỜI
                    userDAO.updateUserStatus(nguoiMoiHandler.tenNguoiChoi, "playing");
                    userDAO.updateUserStatus(tenNguoiChoi, "playing");
                    System.out.println("✅ Đã update status 'playing' cho user " + nguoiMoiID + " và " + nguoiChoiID);
                    
                    // Tạo Gameroom
                    Gameroom gameroom = new Gameroom(nguoiMoiID, nguoiChoiID);
                    Server.getGamerooms().put(gameroom.getMatchId(), gameroom);
                    nguoiMoiHandler.currentGameroom = gameroom;
                    this.currentGameroom = gameroom;
                    
                    // Gửi thông báo chấp nhận cho cả 2
                    String nguoiChapNhanInfo = nguoiChoiID + ":" + tenNguoiChoi;
                    nguoiMoiHandler.sendMessage(new Message(Protocol.CHALLENGE_ACCEPTED, nguoiChapNhanInfo));
                    System.out.println("Đã thông báo chấp nhận đến user " + nguoiMoiID);

                    String nguoiMoiInfo = nguoiMoiID + ":" + nguoiMoiHandler.tenNguoiChoi;
                    sendToThisClient(new Message(Protocol.CHALLENGE_ACCEPTED, nguoiMoiInfo));
                    System.out.println("Đã thông báo chấp nhận đến user " + nguoiChoiID + " (accepter)");
                    
                    // Bắt đầu round 1
                    gameroom.startRound();
                }
                break;
            // Người nhận từ chối lời mời    
            case Protocol.CHALLENGE_REJECT:
                int nguoiMoiIDReject = Integer.parseInt((String) msg.getContent());
                System.out.println("User " + nguoiChoiID + " từ chối lời mời từ user " + nguoiMoiIDReject);
                // Tìm ClientHandler của inviter
                ClientHandler nguoiMoiHandlerReject = Server.getUserHandlers().get(nguoiMoiIDReject);
                
                if (nguoiMoiHandlerReject != null) {
                    // Gửi thông báo từ chối cho inviter
                    String nguoiTuChoiInfo = nguoiChoiID + ":" + tenNguoiChoi;
                    nguoiMoiHandlerReject.sendMessage(new Message(Protocol.CHALLENGE_REJECTED, nguoiTuChoiInfo));
                    System.out.println("Đã thông báo từ chối đến user " + nguoiMoiIDReject);
                }
                break;
            // Người mời hủy lời mời    
            case Protocol.CHALLENGE_CANCEL:
                int doiThuIDCancel = Integer.parseInt((String) msg.getContent());
                System.out.println("User " + nguoiChoiID + " hủy lời mời đấu với user " + doiThuIDCancel);
                
                // Tìm ClientHandler của opponent
                ClientHandler doiThuHandlerCancel = Server.getUserHandlers().get(doiThuIDCancel);
                
                if (doiThuHandlerCancel != null) {
                    // Gửi thông báo hủy cho opponent
                    String nguoiHuyInfo = nguoiChoiID + ":" + tenNguoiChoi;
                    doiThuHandlerCancel.sendMessage(new Message(Protocol.CHALLENGE_CANCELLED, nguoiHuyInfo));
                    System.out.println("Đã thông báo hủy đến user " + doiThuIDCancel);
                }
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
                
            case Protocol.SUBMIT_WORD:
                if (currentGameroom != null) {
                    String[] submitParts = ((String) msg.getContent()).split(":");
                    int playerId = Integer.parseInt(submitParts[0]);
                    String word = submitParts[1];
                    currentGameroom.submitWord(playerId, word);
                }
                break;
                
            case Protocol.PLAYER_READY:
                if (currentGameroom != null) {
                    currentGameroom.playerReady(nguoiChoiID);
                }
                break;
                
            case Protocol.FORFEIT_GAME:
                System.out.println("🏳️ Nhận FORFEIT_GAME từ userId: " + nguoiChoiID);
                if (currentGameroom != null) {
                    currentGameroom.handleForfeit(nguoiChoiID);
                }
                break;
                
            case Protocol.SEND_EMOTE:
                System.out.println("😊 Nhận SEND_EMOTE từ userId: " + nguoiChoiID + ", icon: " + msg.getContent());
                if (currentGameroom != null) {
                    // Broadcast emote to opponent
                    currentGameroom.broadcastEmote(nguoiChoiID, (String) msg.getContent());
                }
                break;
        }
    }
    

}

