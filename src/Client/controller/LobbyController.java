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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
    @FXML
    private TextField txtSearchLeaderboard;
    @FXML
    private Button btnSearchLeaderboard;
    @FXML
    private TableColumn<Users, Integer> colRank;
    @FXML
    private TableColumn<Users, String> colPlayerName;
    @FXML
    private TableColumn<Users, Integer> colPoints;
    @FXML
    private TableColumn<Users, Integer> colTotalWins;
    @FXML
    private TableColumn<Users, Integer> colTotalDraws;
    @FXML
    private TableColumn<Users, Integer> colTotalLosses;
    @FXML
    private Label lblSearchResult;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Lobby Controller đã khởi tạo!");
        
    // Set up cell value factories cho TableView người chơi (Lobby - online players)
    // Use explicit cell value factories to avoid reflection issues
    if (colUsername != null) colUsername.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
    if (colTotalPoints != null) colTotalPoints.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotalPoints()).asObject());
    if (colWins != null) colWins.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotalWins()).asObject());
    if (colDraws != null) colDraws.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotalDraws()).asObject());
    if (colLosses != null) colLosses.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotalLosses()).asObject());
    if (colStatus != null) colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

    // Set up cell value factories cho bảng xếp hạng (nếu cần)
    if (colPlayerName != null) colPlayerName.setCellValueFactory(new PropertyValueFactory<>("username"));
    if (colPoints != null) colPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
    if (colTotalWins != null) colTotalWins.setCellValueFactory(new PropertyValueFactory<>("totalWins"));
    if (colTotalDraws != null) colTotalDraws.setCellValueFactory(new PropertyValueFactory<>("totalDraws"));
    if (colTotalLosses != null) colTotalLosses.setCellValueFactory(new PropertyValueFactory<>("totalLosses"));

        // Rank column: show current index + 1
        if (colRank != null) {
            colRank.setCellFactory(col -> new javafx.scene.control.TableCell<Users, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        int rowIndex = getIndex() + 1;
                        setText(String.valueOf(rowIndex));
                    }
                }
            });
        }
        
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
        try {
            client.showLeaderboardUI((Stage) lblWelcome.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            // Debug: print each user being added
            for (Users u : onlinePlayers) {
                System.out.println("updatePlayerList - user: " + u.getUsername() + ", points=" + u.getTotalPoints() + ", wins=" + u.getTotalWins() + ", draws=" + u.getTotalDraws() + ", losses=" + u.getTotalLosses() + ", status=" + u.getStatus());
            }
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

    @FXML
    private void handleSearchLeaderboard(ActionEvent event) {
        if (client == null) return;
        String q = txtSearchLeaderboard.getText();
        if (q == null || q.trim().isEmpty()) {
            // Optionally show a message to user (label or dialog). For now, print
            System.out.println("Vui lòng nhập username để tìm kiếm");
            return;
        }
        System.out.println("Gửi yêu cầu tìm user: " + q);
        client.sendMessage(new common.Message(common.Protocol.SEARCH_PLAYER, q.trim()));
    }
    
    // Cập nhật bảng xếp hạng
    public void updateLeaderboard(List<Users> leaderboard) {
        if (tblLeaderboard != null) {
            System.out.println("LobbyController.updateLeaderboard called, list size = " + (leaderboard == null ? "null" : leaderboard.size()));
            tblLeaderboard.getItems().clear();
            if (leaderboard != null) {
                tblLeaderboard.getItems().addAll(leaderboard);
                System.out.println("Added " + leaderboard.size() + " users to tblLeaderboard");
                if (leaderboard.isEmpty()) {
                    if (lblSearchResult != null) lblSearchResult.setText("Không tìm thấy người chơi");
                } else {
                    if (lblSearchResult != null) lblSearchResult.setText("");
                }
            }
        }
    }
}
