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
                        client.showLobbyUI((Stage) loginController.getScene().getWindow(), user);
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
                        client.showLobbyUI((Stage) registerController.getScene().getWindow(), user);
                    });
                }
                break;
            case Protocol.REGISTER_FAILURE:
                if (registerController != null) {
                    registerController.handleServerResponse(msg);
                }
                break;
            case Protocol.PLAYER_LIST:
                if (lobbyController != null) {
                    List<Users> onlinePlayers = (List<Users>) msg.getContent();
                    lobbyController.updatePlayerList(onlinePlayers);
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

