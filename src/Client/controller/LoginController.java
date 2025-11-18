package Client.controller;

import Client.Client;
import Client.util.SoundManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;  
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import common.*;
import Server.model.*;
import javafx.application.Platform;


public class LoginController implements Initializable {

    private Client client;

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnLogin;

    public void setClient(Client client) {
        this.client = client;
    }
    public Scene getScene() {
        return txtUsername.getScene();
    }

    @FXML
    private void handleLogin(ActionEvent event){
        SoundManager.getInstance().playSound(SoundManager.BUTTON_CLICK, 0.4);
        String username = txtUsername.getText().trim();        
        String password = txtPassword.getText().trim();
        
        if(username.isEmpty() || password.isEmpty()){
            lblMessage.setText("Vui lòng điền đầy đủ thông tin");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }
        
        if(username.length() < 4){
            lblMessage.setText("Tên đăng nhập phải có ít nhất 4 kí tự");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }
        
         if (password.length() < 6) {
            lblMessage.setText("Mật khẩu phải có ít nhất 6 ký tự!");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }
         
        // Hiển thị đang gửi
        lblMessage.setText("⏳ Đang đăng nhập...");
        lblMessage.setStyle("-fx-text-fill: blue;");
         
        // Gửi lên server
        String data = username + ":" + password;
        client.sendMessage(new Message(Protocol.LOGIN, data));
    }
    
    public void handleServerResponse(Message msg) {
        Platform.runLater(() -> {
            switch (msg.getType()) {
                case Protocol.LOGIN_SUCCESS:
                    Users user = (Users) msg.getContent();
                    lblMessage.setText("Xin chào, " + user.getUsername());
                    lblMessage.setStyle("-fx-text-fill: green;");
                    break;
                case Protocol.LOGIN_FAILURE:
                    lblMessage.setText(msg.getContent().toString());
                    lblMessage.setStyle("-fx-text-fill: red;");
                    break;
            }
        });
    }
    
    @FXML
    private void handleBackToRegister(ActionEvent event) {
        SoundManager.getInstance().playSound(SoundManager.BUTTON_CLICK, 0.4);
        Platform.runLater(() -> {
            client.showRegisterUI((Stage) txtUsername.getScene().getWindow());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
