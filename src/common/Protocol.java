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

    
    // Server → Client
    public static final String LOGIN_SUCCESS = "login success";
    public static final String LOGIN_FAILURE = "login failure";
    public static final String REGISTER_SUCCESS = "register success";
    public static final String REGISTER_FAILURE = "register failure"; 
    public static final String PLAYER_LIST = "player list";
    public static final String LEADERBOARD_DATA = "leaderboard data";
    // Có thể dùng LEADERBOARD_DATA để trả về kết quả tìm kiếm (danh sách user hoặc rỗng)
}
