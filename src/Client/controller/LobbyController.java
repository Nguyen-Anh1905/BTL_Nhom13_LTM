package Client.controller;

import Client.Client;
import Client.MessageHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import Server.model.Users;
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
    private Label lblOnlineCount;  // "Online: X người chơi"
    
    // Tab 1: Người chơi Online - TableView và các Columns
    @FXML
    private TableView<Users> tblPlayers;
    @FXML
    private TableColumn<Users, String> colUsername;
    @FXML
    private TableColumn<Users, Integer> colTotalPoints;
    @FXML
    private TableColumn<Users, Integer> colWins;
    @FXML
    private TableColumn<Users, Integer> colDraws;
    @FXML
    private TableColumn<Users, Integer> colLosses;
    @FXML
    private TableColumn<Users, String> colStatus;
    
    // Tab 2: Lịch sử đấu
    @FXML
    private TableView tblMatchHistory;
    
    // Tab 3: Bảng xếp hạng
    @FXML
    private TableView<Users> tblLeaderboard;
    @FXML
    private Button btnSortByPoints;
    @FXML
    private Button btnSortByWins;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Lobby Controller đã khởi tạo!");
        
        // Set up cell value factories cho TableView người chơi
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colTotalPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        colWins.setCellValueFactory(new PropertyValueFactory<>("totalWins"));
        colDraws.setCellValueFactory(new PropertyValueFactory<>("totalDraws"));
        colLosses.setCellValueFactory(new PropertyValueFactory<>("totalLosses"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        System.out.println("TableView columns đã được setup!");
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
        System.out.println("Xem bảng xếp hạng");
        // TODO: Chuyển sang màn hình Leaderboard
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        System.out.println("Đăng xuất");
        
        // Gửi message LOGOUT lên server
        client.sendMessage(new common.Message(common.Protocol.LOGOUT, currentUser.getUsername()));

        // Quay về màn hình Login và TẠO LẠI CLIENT
        try {
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            
            // Tạo lại MessageHandler và Client mới
            MessageHandler newHandler = new MessageHandler();
            Client newClient = new Client("localhost", 9999, newHandler);
            newHandler.setClient(newClient);
            
            // Gọi method showLoginUI có sẵn trong Client
            newClient.showLoginUI(stage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Method để nhận client từ LoginController/RegisterController
    public void setClient(Client client) {
        this.client = client;
    }

    // Cập nhật danh sách người chơi (gọi từ MessageHandler)
    public void updatePlayerList(List<Users> onlinePlayers) {
        if (onlinePlayers == null) {
            tblPlayers.getItems().clear();
            if (lblOnlineCount != null) {
                lblOnlineCount.setText("Online: 0 người chơi");
            }
        } else {
            tblPlayers.getItems().clear();
            tblPlayers.getItems().addAll(onlinePlayers);
            if (lblOnlineCount != null) {
                lblOnlineCount.setText("Online: " + onlinePlayers.size() + " người chơi");
            }
        }
        System.out.println("Đã cập nhật danh sách người chơi: " + (onlinePlayers != null ? onlinePlayers.size() : 0) + " người");
    }
    
    // Xử lý sắp xếp bảng xếp hạng
    @FXML
    private void handleSortByPoints(ActionEvent event) {
        System.out.println("Sắp xếp theo điểm");
        // Gửi yêu cầu lấy bảng xếp hạng theo điểm
        client.sendMessage(new common.Message(common.Protocol.GET_LEADERBOARD_POINTS, null));
    }
    
    @FXML
    private void handleSortByWins(ActionEvent event) {
        System.out.println("Sắp xếp theo thắng");
        // Gửi yêu cầu lấy bảng xếp hạng theo số trận thắng
        client.sendMessage(new common.Message(common.Protocol.GET_LEADERBOARD_WINS, null));
    }
    
    // Cập nhật bảng xếp hạng
    public void updateLeaderboard(List<Users> leaderboard) {
        if (tblLeaderboard != null) {
            tblLeaderboard.getItems().clear();
            if (leaderboard != null) {
                tblLeaderboard.getItems().addAll(leaderboard);
            }
        }
    }
}
