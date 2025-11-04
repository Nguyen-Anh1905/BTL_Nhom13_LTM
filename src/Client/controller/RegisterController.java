package Client.controller;

import Client.Client;
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
import javafx.application.Platform;
import common.*;
import Server.model.*;

public class RegisterController implements Initializable {
    
    private Client client;
    
    @FXML
    private TextField txtFullName;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Label lblMessage;

    public void setClient(Client client) {
        this.client = client;
    }
    public Scene getScene() {
        return txtFullName.getScene();
    }

    @FXML
    private void handleRegister(ActionEvent event){
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();        
        String password = txtPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();
        
        
        if(fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
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
         if (!password.equals(confirmPassword)) {
            lblMessage.setText("Mật khẩu không khớp!");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }
         
        // Hiển thị đang gửi
        lblMessage.setText("⏳ Đang gửi đăng ký...");
        lblMessage.setStyle("-fx-text-fill: blue;");
        // Tạo chuỗi dữ liệu: "fullName:username:password"
        String data = fullName + ":" + username + ":" + password;
        // Gửi lên server
        client.sendMessage(new common.Message(common.Protocol.REGISTER, data));
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        Platform.runLater(() -> {
            client.showLoginUI((Stage) txtFullName.getScene().getWindow());
        });
    }

    public void handleServerResponse(Message msg) {
        Platform.runLater(() -> {
            switch (msg.getType()) {
                case common.Protocol.REGISTER_SUCCESS:
                    Users user = (Users) msg.getContent();
                    lblMessage.setText("Xin chào, " + user.getUsername());
                    lblMessage.setStyle("-fx-text-fill: green;");
                    break;
                case common.Protocol.REGISTER_FAILURE:
                    lblMessage.setText(msg.getContent().toString());
                    lblMessage.setStyle("-fx-text-fill: red;");
                    break;
            }
        });
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
