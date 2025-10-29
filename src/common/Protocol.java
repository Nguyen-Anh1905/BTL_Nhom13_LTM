package common;

public class Protocol {
    // Client → Server
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";  // ← THÊM MỚI
    public static final String LOGOUT = "logout";  // ← THÊM MỚI
    
    // Server → Client
    public static final String LOGIN_SUCCESS = "login success";
    public static final String LOGIN_FAILURE = "login failure";
    public static final String REGISTER_SUCCESS = "register success";  // ← THÊM MỚI
    public static final String REGISTER_FAILURE = "register failure";  // ← THÊM MỚI
}
