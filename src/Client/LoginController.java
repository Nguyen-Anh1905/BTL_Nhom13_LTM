package Client;

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

    @FXML
    private void handleLogin(ActionEvent event){
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
    
    private void handleServerResponse(Message msg) {
        switch (msg.getType()) {
            case Protocol.LOGIN_SUCCESS:
                // Nhận object Users từ Server
                model.Users user = (model.Users) msg.getContent();
                
                lblMessage.setText("✅ Xin chào, " + user.getUsername());
                lblMessage.setStyle("-fx-text-fill: green;");
                
                break;
                
            case Protocol.LOGIN_FAILURE:
                lblMessage.setText("❌ " + msg.getContent());
                lblMessage.setStyle("-fx-text-fill: red;");
                break;
        }
    }
    
    @FXML
    private void handleBackToRegister(ActionEvent event) {
        try {
            // Lấy Stage hiện tại
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            
            // Load file register.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/register.fxml"));
            
            // Tạo Scene mới
            Scene scene = new Scene(root);
            
            // Đổi scene
            stage.setScene(scene);
            stage.setTitle("Đăng Ký");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setText("❌ Lỗi khi chuyển trang!");
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            MessageHandler handler = new MessageHandler(msg -> {
                Platform.runLater(() -> {
                    handleServerResponse(msg);
                });
            });
            // Kết nối tới server
            client = new Client("localhost", 9999, handler);
        }catch(Exception e){
            e.printStackTrace();
            lblMessage.setText("❌ Không thể kết nối tới server!");
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }    
    
}
