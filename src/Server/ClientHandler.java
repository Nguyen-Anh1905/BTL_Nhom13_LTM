package Server;

import common.*;
import java.io.*;
import java.net.*;
import java.util.*;
import Server.DAO.*;
import Server.model.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private List<ClientHandler> clients;
    
    private UserDAO userDAO = new UserDAO();

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
        }
    }
}
