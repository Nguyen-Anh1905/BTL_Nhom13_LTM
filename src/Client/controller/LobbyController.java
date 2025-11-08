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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
        client.sendMessage(new common.Message(common.Protocol.SEARCH_PLAYER_IN_LEADERBOARD, q.trim()));
    }

    @FXML
    private void handleReloadPlayers(ActionEvent event) {
        System.out.println("Reload players list");
        if (client != null) client.sendMessage(new common.Message(common.Protocol.GET_PLAYER_LIST, null));
    }

    @FXML
    private void handleSearchPlayers(ActionEvent event) {
        if (client == null) return;
        String q = txtSearchPlayers.getText();
        if (q == null || q.trim().isEmpty()) {
            System.out.println("Vui lòng nhập username để tìm kiếm");
            return;
        }
        System.out.println("Gửi yêu cầu tìm user (players tab): " + q);
        client.sendMessage(new common.Message(common.Protocol.SEARCH_PLAYER_IN_LOBBY, q.trim()));
    }

    @FXML
    private void handleReloadHistory(ActionEvent event) {
        System.out.println("Reload match history");
        // TODO: implement fetch match history from server if a protocol exists
    }

    @FXML
    private void handleSearchHistory(ActionEvent event) {
        if (client == null) return;
        String q = txtSearchHistory.getText();
        if (q == null || q.trim().isEmpty()) {
            System.out.println("Vui lòng nhập username để tìm kiếm trong lịch sử");
            return;
        }
        System.out.println("Gửi yêu cầu tìm user (history tab): " + q);
        // For now reuse SEARCH_PLAYER to request user info from server
        client.sendMessage(new common.Message(common.Protocol.SEARCH_PLAYER, q.trim()));
    }

    @FXML
    private void handleReloadLeaderboard(ActionEvent event) {
        System.out.println("Reload leaderboard (by points)");
        if (client != null) client.sendMessage(new common.Message(common.Protocol.GET_LEADERBOARD_POINTS, null));
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
}
