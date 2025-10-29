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

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        MessageHandler handler = new MessageHandler();
        Client client = new Client("localhost", 9999, handler);
        handler.setClient(client); 
        client.showLoginUI(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

