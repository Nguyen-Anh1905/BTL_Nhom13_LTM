  
package Client;

import common.*;
import java.io.*;
import java.net.*;

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
            }
        } catch (Exception e) {
            System.out.println("🔌 Mất kết nối với server.");
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


    public void showLobbyUI(Stage stage, model.Users user, List<model.Users> onlinePlayers) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Lobby.fxml"));
            Parent root = loader.load();

            LobbyController lobbyController = loader.getController();
            lobbyController.setClient(this);
            lobbyController.setCurrentUser(user);
            lobbyController.setPlayerList(onlinePlayers);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Lobby - Chào mừng " + user.getFullname());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

