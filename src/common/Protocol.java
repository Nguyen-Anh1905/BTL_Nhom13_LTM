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
    // Có thể dùng LEADERBOARD_DATA để trả về kết quả tìm kiếm (danh sách user hoặc rỗng)
}
