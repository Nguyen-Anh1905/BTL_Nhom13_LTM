/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.util.Properties;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        MessageHandler handler = new MessageHandler();
        
        // Đọc config từ file server.config
        String serverIP = "localhost"; // Giá trị mặc định
        int serverPort = 9999;
        
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("server.config"));
            serverIP = props.getProperty("SERVER_IP", "localhost");
            serverPort = Integer.parseInt(props.getProperty("SERVER_PORT", "9999"));
            System.out.println("📡 Kết nối đến Server: " + serverIP + ":" + serverPort);
        } catch (Exception e) {
            System.out.println("⚠️ Không đọc được file server.config, dùng localhost:9999");
        }
        
        Client client = new Client(serverIP, serverPort, handler);
        handler.setClient(client); 
        client.showLoginUI(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

