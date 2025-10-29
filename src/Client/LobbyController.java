package Client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import model.Users;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.List;

public class LobbyController implements Initializable {
    
    private Client client;
    private Users currentUser;
    
    @FXML
    private Label lblWelcome;  // "Xin chào, [username]"
    @FXML
    private Label lblUserStats;  // "Thắng: X | Hòa: Y | Thua: Z"
    @FXML
    private Label lblUserPoints;  // "Điểm: X"
    @FXML
    private Label lblUserStatus;  // "Trạng thái: Rảnh"
    @FXML
    private TableView tblPlayers;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("✅ Lobby Controller đã khởi tạo!");
    }
    
    // Nhận thông tin user từ LoginController/RegisterController
    public void setCurrentUser(Users user) {
        this.currentUser = user;
        
        // Hiển thị thông tin lên UI
        if (lblWelcome != null) {
            lblWelcome.setText("Xin chào, " + user.getUsername());
        }
        
        if (lblUserStats != null) {
            lblUserStats.setText(user.getFullName() + 
                               " | Thắng: " + user.getTotalWins() + 
                               " | Hòa: " + user.getTotalDraws() + 
                               " | Thua: " + user.getTotalLosses());
        }
        
        if (lblUserPoints != null) {
            lblUserPoints.setText("Điểm: " + user.getTotalPoints());
        }
        
        if (lblUserStatus != null) {
            lblUserStatus.setText("Trạng thái: Rảnh");
        }
        
        System.out.println("Thông tin user đã được cập nhật: " + user.getUsername());
    }
    
    @FXML
    private void handleViewLeaderboard(ActionEvent event) {
        System.out.println("🏆 Xem bảng xếp hạng");
        // TODO: Chuyển sang màn hình Leaderboard
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        System.out.println("Đăng xuất");
    // Gửi message LOGOUT lên server
    client.sendMessage(new common.Message(common.Protocol.LOGOUT, currentUser.getUsername()));

    // Đóng socket client (nếu muốn)
    // client.close(); // Nếu có method close()

    // Quay về màn hình Login
    try {
        Stage stage = (Stage) lblWelcome.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Đăng Nhập");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
    // Method để nhận client từ LoginController/RegisterController
    public void setClient(Client client) {
        this.client = client;
    }

    public void setPlayerList(List<Users> onlinePlayers) {
        if (onlinePlayers == null) {
            tblPlayers.getItems().clear(); // hoặc setAll(Collections.emptyList());
        } else {
            tblPlayers.getItems().setAll(onlinePlayers);
        }
    }
}
