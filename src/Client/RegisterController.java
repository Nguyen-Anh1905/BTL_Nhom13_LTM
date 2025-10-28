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
import javafx.application.Platform;

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
        try {
            // Lấy Stage hiện tại
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            
            // Load file login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/login.fxml"));
            
            // Tạo Scene mới
            Scene scene = new Scene(root);
            
            // Đổi scene
            stage.setScene(scene);
            stage.setTitle("Đăng Nhập");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setText("❌ Lỗi khi chuyển trang!");
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }

    private void handleServerResponse(common.Message msg) {
        switch (msg.getType()) {
            case common.Protocol.REGISTER_SUCCESS:
                // Nhận object Users từ Server
                model.Users user = (model.Users) msg.getContent();
                
                lblMessage.setText("✅ Xin chào, " + user.getUsername());
                lblMessage.setStyle("-fx-text-fill: green;");
                
                // Chuyển sang màn hình Lobby
                try {
                    Stage stage = (Stage) txtUsername.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Lobby.fxml"));
                    Parent root = loader.load();
                    
                    // Truyền client và user sang LobbyController
                    LobbyController lobbyController = loader.getController();
                    lobbyController.setClient(client);
                    lobbyController.setCurrentUser(user);  // ← TRUYỀN THÔNG TIN USER
                    
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Sảnh Chờ");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    lblMessage.setText("❌ Lỗi khi chuyển sang màn hình chính!");
                    lblMessage.setStyle("-fx-text-fill: red;");
                }
                break;
                
            case common.Protocol.REGISTER_FAILURE:
                lblMessage.setText("❌ " + msg.getContent());
                lblMessage.setStyle("-fx-text-fill: red;");
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Tạo MessageHandler với callback
            MessageHandler handler = new MessageHandler(msg -> {
                // Callback sẽ chạy trên thread của Client
                // Cần chuyển sang JavaFX thread để cập nhật UI
                Platform.runLater(() -> {
                    handleServerResponse(msg);
                });
            });
            
            // Kết nối tới server
            client = new Client("localhost", 9999, handler);
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("❌ Không thể kết nối tới server!");
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }    
    
}
