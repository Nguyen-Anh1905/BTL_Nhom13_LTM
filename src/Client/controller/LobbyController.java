package Client.controller;

import Client.Client;
import Client.MessageHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
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
    @FXML
    private TableColumn<Users, Void> colActions;
    @FXML
    private TextField txtSearchPlayers;
    @FXML
    private Button btnReloadPlayers;
    @FXML
    private Button btnSearchPlayers;
    
    // Tab 2: Lịch sử đấu
    @FXML
    private TableView tblMatchHistory;
    @FXML
    private TextField txtSearchHistory;
    @FXML
    private Button btnReloadHistory;
    @FXML
    private Button btnSearchHistory;
    
    // Tab 3: Bảng xếp hạng
    @FXML
    private TableView<Users> tblLeaderboard;
    @FXML
    private TextField txtSearchLeaderboard;
    @FXML
    private Button btnReloadLeaderboard;
    @FXML
    private Button btnSearchLeaderboard;
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
        
        // Set up cột Hành động với 2 nút: Mời đấu và Chat
        colActions.setCellFactory(param -> new TableCell<Users, Void>() {
            private final Button btnChallenge = new Button("Mời đấu");
            private final Button btnChat = new Button("Chat");
            private final HBox hbox = new HBox(10, btnChallenge, btnChat);
            
            {
                hbox.setAlignment(Pos.CENTER);
                btnChallenge.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnChat.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                
                btnChallenge.setOnAction(event -> {
                    Users selectedUser = getTableView().getItems().get(getIndex());
                    handleChallenge(selectedUser);
                });
                
                btnChat.setOnAction(event -> {
                    Users selectedUser = getTableView().getItems().get(getIndex());
                    handleChat(selectedUser);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Users user = getTableView().getItems().get(getIndex());
                    // Không hiển thị nút cho chính mình
                    if (currentUser != null && user.getUserId() == currentUser.getUserId()) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbox);
                    }
                }
            }
        });
        
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
    
    // Xử lý khi click nút "Mời đấu"
    private void handleChallenge(Users opponent) {
        System.out.println("Mời đấu với: " + opponent.getUsername());
        // TODO: Gửi lời mời đấu lên server
        // client.sendMessage(new Message(Protocol.CHALLENGE_REQUEST, opponent.getUserId()));
    }
    
    // Xử lý khi click nút "Chat"
    private void handleChat(Users user) {
        System.out.println("Mở chat với: " + user.getUsername());
        // TODO: Mở cửa sổ chat với user này
    }
    
    // ========== XỬ LÝ RELOAD VÀ SEARCH ==========
    
    // Tab 1: Người chơi Online
    @FXML
    private void handleReloadPlayers(ActionEvent event) {
        System.out.println("Reload danh sách người chơi");
        client.sendMessage(new common.Message(common.Protocol.GET_PLAYER_LIST, null));
    }
    
    @FXML
    private void handleSearchPlayers(ActionEvent event) {
        String keyword = txtSearchPlayers.getText().trim();
        System.out.println("Tìm kiếm người chơi: " + keyword);
        // TODO: Lọc danh sách người chơi theo keyword
        if (keyword.isEmpty()) {
            return;
        }
        // Filter local table
        tblPlayers.getItems().filtered(user -> 
            user.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
            user.getFullName().toLowerCase().contains(keyword.toLowerCase())
        );
    }
    
    // Tab 2: Lịch sử đấu
    @FXML
    private void handleReloadHistory(ActionEvent event) {
        System.out.println("Reload lịch sử đấu");
        // TODO: Gửi yêu cầu lấy lịch sử đấu
        // client.sendMessage(new Message(Protocol.GET_MATCH_HISTORY, currentUser.getUserId()));
    }
    
    @FXML
    private void handleSearchHistory(ActionEvent event) {
        String keyword = txtSearchHistory.getText().trim();
        System.out.println("Tìm kiếm lịch sử: " + keyword);
        // TODO: Lọc lịch sử đấu theo keyword
    }
    
    // Tab 3: Bảng xếp hạng
    @FXML
    private void handleReloadLeaderboard(ActionEvent event) {
        System.out.println("Reload bảng xếp hạng");
        // Reload theo bộ lọc hiện tại (mặc định theo điểm)
        client.sendMessage(new common.Message(common.Protocol.GET_LEADERBOARD_POINTS, null));
    }
    
    @FXML
    private void handleSearchLeaderboard(ActionEvent event) {
        String keyword = txtSearchLeaderboard.getText().trim();
        System.out.println("Tìm kiếm bảng xếp hạng: " + keyword);
        // TODO: Lọc bảng xếp hạng theo keyword
    }
}
