package common;

public class Protocol {
    // Client → Server
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String LOGOUT = "logout";
    public static final String GET_PLAYER_LIST = "get player list";
    public static final String GET_LEADERBOARD_POINTS = "get leaderboard points";
    public static final String GET_LEADERBOARD_WINS = "get leaderboard wins";
    public static final String SEARCH_PLAYER = "search player";
    public static final String SEARCH_PLAYER_IN_LOBBY = "search player in lobby";  // Tab 1
    public static final String SEARCH_PLAYER_IN_LEADERBOARD = "search player in leaderboard";  // Tab 3
    
    // Challenge (Mời đấu)
    public static final String CHALLENGE_REQUEST = "challenge request";     // Client → Server: A mời B
    public static final String CHALLENGE_ACCEPT = "challenge accept";       // Client → Server: B chấp nhận
    public static final String CHALLENGE_REJECT = "challenge reject";       // Client → Server: B từ chối
    public static final String CHALLENGE_CANCEL = "challenge cancel";       // Client → Server: A hủy lời mời
    public static final String CHALLENGE_TIMEOUT = "challenge timeout";     // Client → Server: Hết thời gian
    public static final String GET_MATCH_HISTORY = "get match history";
    public static final String MATCH_HISTORY_DATA = "match history data";
    public static final String GET_MATCH_DETAIL = "get match detail";
    public static final String MATCH_DETAIL_DATA = "match detail data";

    
    // Server → Client
    public static final String LOGIN_SUCCESS = "login success";
    public static final String LOGIN_FAILURE = "login failure";
    public static final String REGISTER_SUCCESS = "register success";
    public static final String REGISTER_FAILURE = "register failure"; 
    public static final String PLAYER_LIST = "player list";
    public static final String LEADERBOARD_DATA = "leaderboard data";
    public static final String SEARCH_RESULT_LOBBY = "search result lobby";  // Kết quả tìm kiếm cho Tab 1
    public static final String SEARCH_RESULT_LEADERBOARD = "search result leaderboard";  // Kết quả tìm kiếm cho Tab 3
    
    // Challenge responses
    public static final String CHALLENGE_INVITATION = "challenge invitation";   // Server → Client B: Nhận lời mời từ A
    public static final String CHALLENGE_ACCEPTED = "challenge accepted";       // Server → Client A: B đã chấp nhận
    public static final String CHALLENGE_REJECTED = "challenge rejected";       // Server → Client A: B đã từ chối
    public static final String CHALLENGE_CANCELLED = "challenge cancelled";     // Server → Client B: A đã hủy
    public static final String CHALLENGE_TIMED_OUT = "challenge timed out";     // Server → Client: Hết thời gian
    public static final String CHALLENGE_FAILED = "challenge failed";           // Server → Client: Lỗi (user offline, đang bận...)
    
    // Game Room
    public static final String GAME_ROOM_CREATED = "game room created";         // Server → Client: Phòng game đã tạo
    public static final String ROUND_START = "round start";                     // Server → Client: Bắt đầu vòng đấu
    public static final String SUBMIT_WORD = "submit word";                     // Client → Server: Nộp từ
    public static final String ROUND_RESULT = "round result";                   // Server → Client: Kết quả submit trong round
    public static final String ROUND_END = "round end";                         // Server → Client: Kết thúc round
    public static final String PLAYER_READY = "player ready";                   // Client → Server: Người chơi sẵn sàng cho round tiếp theo
    public static final String GAME_END = "game end";                           // Server → Client: Kết thúc game
    public static final String FORFEIT_GAME = "forfeit game";                   // Client → Server: Đầu hàng/thoát trận
    public static final String OPPONENT_FORFEITED = "opponent forfeited";       // Server → Client: Đối thủ đã thoát trận
    
    // Emote system
    public static final String SEND_EMOTE = "send emote";                       // Client → Server: Gửi biểu tượng cảm xúc
    public static final String RECEIVE_EMOTE = "receive emote";                 // Server → Client: Nhận biểu tượng cảm xúc từ đối thủ
}
