  
package Client;

import Client.controller.*;
import Server.model.*;
import common.*;
import java.io.*;
import java.net.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.util.List;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private MessageHandler handler;

    public Client(String host, int port, MessageHandler handler) throws Exception{
        this.handler = handler;

        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        new Thread(this::listen).start();
        
    }

    private void listen() {
        try {
            while (true) {
                Message msg = (Message) in.readObject();
                handler.handleMessage(msg);
                System.out.println("Received message: " + msg.getType() + " - " + msg.getContent());
            }
        } catch (Exception e) {
            System.out.println("🔌 Mất kết nối với server.");
            e.printStackTrace(); 
        }
    }

    public void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showLobbyUI(Stage stage, Server.model.Users user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/GUI/fxml/Lobby.fxml"));
            Parent root = loader.load();

            LobbyController lobbyController = loader.getController();
            lobbyController.setClient(this);
            lobbyController.setCurrentUser(user);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Lobby - Chào mừng " + user.getFullName());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showLoginUI(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/GUI/fxml/login.fxml"));
            Parent root = loader.load();
            LoginController loginController = loader.getController();
            loginController.setClient(this);
            handler.setLoginController(loginController);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Đăng Nhập");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showRegisterUI(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/GUI/fxml/register.fxml"));
            Parent root = loader.load();
            RegisterController registerController = loader.getController();
            registerController.setClient(this);
            handler.setRegisterController(registerController);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Đăng Ký");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}

