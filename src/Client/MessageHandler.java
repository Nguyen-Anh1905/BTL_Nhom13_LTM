package Client;

import Client.controller.*;
import common.*;
import javafx.application.Platform;
import Server.model.*;
import java.util.List;
import javafx.stage.Stage;

public class MessageHandler {
    
    private Message msg;
    private LoginController loginController;
    private RegisterController registerController;
    private LobbyController lobbyController;
    private LeaderboardController leaderboardController;
    private Client client;

    // Khởi tạo
    public MessageHandler(){}

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }

    public void setRegisterController(RegisterController registerController) {
        this.registerController = registerController;
    }
        public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }

    public void handleMessage(Message msg) {
        System.out.println("Received message: " + msg.getType() + " - " + msg.getContent());
        if (msg == null) {
            return;
        }
        switch (msg.getType()) {
            case Protocol.LOGIN_SUCCESS:
                if (loginController != null) {
                    Users user = (Users) msg.getContent();
                    loginController.handleServerResponse(msg);
                    Platform.runLater(() -> {
                        try {
                            // Use primary stage stored in client to avoid null Scene/Window
                            client.showLobbyUI(client.getPrimaryStage(), user);
                            // SAU KHI CHUYỂN SANG LOBBY, YÊU CẦU DANH SÁCH NGƯỜI CHƠI
                            System.out.println("Gửi yêu cầu lấy danh sách người chơi...");
                            client.sendMessage(new Message(Protocol.GET_PLAYER_LIST, null));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
                break;
            case Protocol.LOGIN_FAILURE:
                if (loginController != null) {
                    loginController.handleServerResponse(msg);
                }
                break;
            case Protocol.REGISTER_SUCCESS:
                if (registerController != null) {
                    Users user = (Users) msg.getContent();
                    registerController.handleServerResponse(msg);
                    Platform.runLater(() -> {
                        try {
                            client.showLobbyUI(client.getPrimaryStage(), user);
                            // SAU KHI ĐĂNG KÝ VÀ CHUYỂN SANG LOBBY, YÊU CẦU DANH SÁCH NGƯỜI CHƠI
                            System.out.println("Gửi yêu cầu lấy danh sách người chơi...");
                            client.sendMessage(new Message(Protocol.GET_PLAYER_LIST, null));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
                break;
            case Protocol.REGISTER_FAILURE:
                if (registerController != null) {
                    registerController.handleServerResponse(msg);
                }
                break;
            case Protocol.PLAYER_LIST:
                System.out.println("Nhận được PLAYER_LIST");
                System.out.println("lobbyController = " + lobbyController);
                if (lobbyController != null) {
                    List<Users> onlinePlayers = (List<Users>) msg.getContent();
                    for (Users u : onlinePlayers) {
                        System.out.println(" - " + u.getUsername() + " (" + u.getStatus() + ")");
                    }
                    System.out.println("Số người chơi: " + (onlinePlayers != null ? onlinePlayers.size() : 0));
                    Platform.runLater(() -> {
                        lobbyController.updatePlayerList(onlinePlayers);
                    });
                } else {
                    System.out.println("lobbyController is NULL!");
                }
                break;
            case Protocol.LEADERBOARD_DATA:
                System.out.println("Nhận được LEADERBOARD_DATA");
                List<Users> list = (List<Users>) msg.getContent();
                // Nếu Lobby đang mở, ưu tiên gửi kết quả tới LobbyController (bảng xếp hạng trong Lobby)
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.updateLeaderboard(list);
                    });
                } else if (leaderboardController != null) {
                    Platform.runLater(() -> {
                        leaderboardController.updateLeaderboard(list);
                    });
                } else {
                    System.out.println("Không có controller nào để nhận LEADERBOARD_DATA");
                }
                break;
            // Xử lý các loại message khác nếu cần
            default:
                System.out.println("Unhandled message type: " + msg.getType());
                System.out.println("ww");
                break;
                
        }
    }
    

}

