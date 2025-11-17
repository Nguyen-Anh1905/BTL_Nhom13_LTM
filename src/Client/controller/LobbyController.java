package Client.controller;

import Client.Client;
import Client.MessageHandler;
import common.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import Server.dto.MatchHistoryResponse;
import Server.model.Users;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import java.io.IOException;
import java.util.List;

public class LobbyController implements Initializable {
    
    private Client client;
    private Users currentUser; // currentUser
    private WaitingDialogController waitingDialogController;
    private Stage waitingDialogStage;
    private InviteDialogController inviteDialogController;
    private Stage inviteDialogStage;
    private GameRoomController gameRoomController; // Lưu reference
    
    // Lưu thông tin đối thủ khi challenge
    private String opponentName;
    private int opponentId;


    
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
    private TableView<MatchHistoryResponse> tblMatchHistory;
    @FXML
    private TableColumn<MatchHistoryResponse, String> colMatchStart;
    @FXML
    private TableColumn<MatchHistoryResponse, String> colOpponent;
    @FXML
    private TableColumn<MatchHistoryResponse, String> colResult;
    @FXML
    private TableColumn<MatchHistoryResponse, String> colScore;
    @FXML
    private TableColumn<MatchHistoryResponse, String> colMatchDate;
    @FXML
    private TableColumn<MatchHistoryResponse, String> colMatchDetails;
    @FXML
    private TextField txtSearchHistory;
    @FXML
    private Button btnReloadHistory;
    @FXML
    private Button btnSearchHistory;
    @FXML
    private TabPane tabPane;
    
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
    
    // Method để nhận client từ LoginController/RegisterController
    public void setClient(Client client) {
        this.client = client;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Lobby Controller đã khởi tạo!");
        // Listen to tab changes: auto request data when switching tabs
        try {
            if (tabPane != null) {
                tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                    if (newTab != null) {
                        if ("Người chơi Online".equals(newTab.getText())) {
                            System.out.println("Online players tab selected -> requesting player list...");
                            if (client != null) {
                                client.sendMessage(new common.Message(common.Protocol.GET_PLAYER_LIST, null));
                            }
                        } else if ("Lịch sử đấu".equals(newTab.getText())) {
                            System.out.println("History tab selected -> requesting match history...");
                            if (client != null && currentUser != null) {
                                client.sendMessage(new common.Message(common.Protocol.GET_MATCH_HISTORY, currentUser.getUsername()));
                            }
                        } else if ("Bảng xếp hạng".equals(newTab.getText())) {
                            System.out.println("Leaderboard tab selected -> requesting leaderboard...");
                            if (client != null) {
                                client.sendMessage(new common.Message(common.Protocol.GET_LEADERBOARD_POINTS, null));
                            }
                        }
                    }
                });
            }
        } catch (Exception ex) {
            // ignore if tabPane not injected yet
        }
        
    // Set up cell value factories cho TableView người chơi (Lobby - online players)
    // Use PropertyValueFactory for proper JavaFX binding
    if (colUsername != null) colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    if (colTotalPoints != null) colTotalPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
    if (colWins != null) colWins.setCellValueFactory(new PropertyValueFactory<>("totalWins"));
    if (colDraws != null) colDraws.setCellValueFactory(new PropertyValueFactory<>("totalDraws"));
    if (colLosses != null) colLosses.setCellValueFactory(new PropertyValueFactory<>("totalLosses"));
    if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

    // Set up cell value factories cho bảng xếp hạng
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
        
        // Set up cột Hành động với nút Mời đấu
        colActions.setCellFactory(param -> new TableCell<Users, Void>() {
            private final Button btnChallenge = new Button("Mời đấu");
            private final HBox hbox = new HBox(10, btnChallenge);
            
            {
                hbox.setAlignment(Pos.CENTER);
                btnChallenge.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                
                btnChallenge.setOnAction(event -> {
                    Users selectedUser = getTableView().getItems().get(getIndex());
                    try {
                        handleChallenge(selectedUser);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Lỗi khi mở dialog: " + e.getMessage());
                    }
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
                        String status = user.getStatus();
                        // Disable nút nếu user không online (offline hoặc playing)
                        if ("offline".equalsIgnoreCase(status) || status == null || status.trim().isEmpty()) {
                            btnChallenge.setDisable(true);
                            btnChallenge.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: #666666;");
                            btnChallenge.setText("Offline");
                        } else if ("playing".equalsIgnoreCase(status)) {
                            btnChallenge.setDisable(true);
                            btnChallenge.setStyle("-fx-background-color: #CCCCCC; -fx-text-fill: #666666;");
                            btnChallenge.setText("Đang chơi");
                        } else {
                            // online - có thể mời
                            btnChallenge.setDisable(false);
                            btnChallenge.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                            btnChallenge.setText("Mời đấu");
                        }
                        setGraphic(hbox);
                    }
                }
            }
        });
        
        System.out.println("TableView columns đã được setup!");
        // Setup match history columns
        if (colMatchStart != null) colMatchStart.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMatchStart()));
        if (colOpponent != null) colOpponent.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlayerName()));
        if (colResult != null) colResult.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMatchResult()));
        if (colScore != null) colScore.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMatchScore()));
        if (colMatchDate != null) colMatchDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMatchDuration()));
        if (colMatchDetails != null) {
            // create a button in each row to request and show match detail
            colMatchDetails.setCellFactory(col -> new TableCell<MatchHistoryResponse, String>() {
                private final Button btn = new Button("Chi tiết");

                {
                    btn.setOnAction(event -> {
                        MatchHistoryResponse r = getTableView().getItems().get(getIndex());
                        if (r != null && client != null && currentUser != null) {
                            String payload = r.getMatchId() + ":" + currentUser.getUsername();
                            client.sendMessage(new common.Message(common.Protocol.GET_MATCH_DETAIL, payload));
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) setGraphic(null);
                    else setGraphic(btn);
                }
            });
        }
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
        
        // Auto-request data sau khi user được set (controller đã sẵn sàng)
        if (client != null) {
            System.out.println("LobbyController: Auto-requesting initial data...");
            client.sendMessage(new Message(Protocol.GET_PLAYER_LIST, null));
            client.sendMessage(new Message(Protocol.GET_LEADERBOARD_POINTS, null));
        }
    }
    // Xử lý nút Đăng xuất
    @FXML
    private void handleLogout(ActionEvent event) {
        System.out.println("Đăng xuất");
        client.sendMessage(new Message(Protocol.LOGOUT, currentUser.getUsername()));
        // Quay về màn hình Login và TẠO LẠI CLIENT
        try {
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            // Tạo lại MessageHandler và Client mới
            MessageHandler newHandler = new MessageHandler();
            Client newClient = new Client("localhost", 9999, newHandler);
            newHandler.setClient(newClient); 
            newClient.showLoginUI(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            // Set inline style to ensure visibility
            tblPlayers.setStyle("-fx-text-fill: #000000;");
            tblPlayers.getItems().addAll(onlinePlayers);
            // Debug: print each user being added
            for (Users u : onlinePlayers) {
                System.out.println("updatePlayerList - user: " + u.getUsername() + 
                    ", userId=" + u.getUserId() + 
                    ", points=" + u.getTotalPoints() + 
                    ", wins=" + u.getTotalWins() + 
                    ", draws=" + u.getTotalDraws() + 
                    ", losses=" + u.getTotalLosses() + 
                    ", status=" + u.getStatus());
                // Check if this is current user
                if (currentUser != null && u.getUserId() == currentUser.getUserId()) {
                    System.out.println("  ^^^ This is the CURRENT USER! ^^^");
                }
            }
            if (lblOnlineCount != null) {
                lblOnlineCount.setText("Online: " + onlinePlayers.size() + " người chơi");
            }
            tblPlayers.refresh(); // Force refresh
        }
        System.out.println("Đã cập nhật danh sách người chơi: " + (onlinePlayers != null ? onlinePlayers.size() : 0) + " người");
        System.out.println("Current user ID in LobbyController: " + (currentUser != null ? currentUser.getUserId() : "NULL"));
    }
    
    // Xử lý sắp xếp bảng xếp hạng
    @FXML
    private void handleSortByPoints(ActionEvent event) {
        System.out.println("Sắp xếp theo điểm");
        // Gửi yêu cầu lấy bảng xếp hạng theo điểm
        client.sendMessage(new Message(Protocol.GET_LEADERBOARD_POINTS, null));
    }
    
    @FXML
    private void handleSortByWins(ActionEvent event) {
        System.out.println("Sắp xếp theo thắng");
        // Gửi yêu cầu lấy bảng xếp hạng theo số trận thắng
        client.sendMessage(new Message(Protocol.GET_LEADERBOARD_WINS, null));
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
        client.sendMessage(new Message(Protocol.SEARCH_PLAYER_IN_LEADERBOARD, q.trim()));
    }

    @FXML
    private void handleReloadPlayers(ActionEvent event) {
        System.out.println("Reload players list");
        if (client != null) client.sendMessage(new Message(Protocol.GET_PLAYER_LIST, null));
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
        client.sendMessage(new Message(Protocol.SEARCH_PLAYER_IN_LOBBY, q.trim()));
    }

    @FXML
    private void handleReloadHistory(ActionEvent event) {
        System.out.println("Reload match history");
        if (client != null && currentUser != null) {
            client.sendMessage(new common.Message(common.Protocol.GET_MATCH_HISTORY, currentUser.getUsername()));
        }
    }

    // Called by MessageHandler when MATCH_HISTORY_DATA is received
    public void updateMatchHistory(List<MatchHistoryResponse> history) {
        System.out.println("updateMatchHistory called, history list = " + (history == null ? "null" : history.size()));
        if (history != null) {
            for (MatchHistoryResponse r : history) {
                System.out.println(" - matchId=" + r.getMatchId() + ", opp=" + r.getPlayerName() + ", result=" + r.getMatchResult() + ", score=" + r.getMatchScore());
            }
        }
        if (tblMatchHistory == null) {
            System.out.println("tblMatchHistory is null");
            return;
        }
        tblMatchHistory.getItems().clear();
        if (history != null && !history.isEmpty()) {
            tblMatchHistory.getItems().addAll(history);
        }
    }

    // Hiển thị popup chi tiết trận - redesigned with minimal spacing
    public void showMatchDetail(List<Server.dto.MatchDetailResponse> details) {
        if (details == null || details.isEmpty()) return;
        Stage owner = null;
        try {
            owner = (Stage) lblWelcome.getScene().getWindow();
        } catch (Exception ex) {
            // ignore
        }

        Stage stage = new Stage();
        stage.setTitle("Chi tiết trận");
        stage.initModality(Modality.WINDOW_MODAL);
        if (owner != null) stage.initOwner(owner);

        // Create a custom VBox layout with GridPane rows for tight control over spacing
        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: white;");

        // Header row (column labels)
        GridPane headerRow = new GridPane();
        headerRow.setStyle("-fx-background-color: #4A90E2; -fx-padding: 4 8 4 8;");
        headerRow.setHgap(4);
        
        Label hVong = new Label("Vòng");
        hVong.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        hVong.setPrefWidth(50);
        
        Label hLetter = new Label("Letter");
        hLetter.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        hLetter.setPrefWidth(140);
        
        Label hBan = new Label("Bạn");
        hBan.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        hBan.setPrefWidth(220);
        
        Label hOpp = new Label("Đối thủ");
        hOpp.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        hOpp.setPrefWidth(220);
        
        Label hResult = new Label("Kết quả");
        hResult.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        hResult.setPrefWidth(100);
        
        headerRow.add(hVong, 0, 0);
        headerRow.add(hLetter, 1, 0);
        headerRow.add(hBan, 2, 0);
        headerRow.add(hOpp, 3, 0);
        headerRow.add(hResult, 4, 0);
        
        content.getChildren().add(headerRow);

        // Data rows - each row sized exactly to its content
        for (int i = 0; i < details.size(); i++) {
            Server.dto.MatchDetailResponse item = details.get(i);
            
            GridPane row = new GridPane();
            row.setHgap(4);
            row.setPadding(new Insets(4, 8, 4, 8));
            
            // Alternate row colors for readability
            if (i % 2 == 0) {
                row.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
            } else {
                row.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
            }
            
            // Vòng
            Label lblRound = new Label(String.valueOf(item.getRound()));
            lblRound.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
            lblRound.setPrefWidth(50);
            lblRound.setAlignment(Pos.CENTER);
            
            // Letter
            Label lblLetter = new Label(item.getLetter());
            lblLetter.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
            lblLetter.setPrefWidth(140);
            lblLetter.setWrapText(true);
            
            // Bạn (Your words)
            VBox boxYou = new VBox(2);
            boxYou.setPrefWidth(220);
            boxYou.setMaxWidth(220);
            Label lblYouCount = new Label(item.getYourWordOk() + " từ");
            lblYouCount.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2c5aa0;");
            String yourTxt = String.join(", ", item.getYourWord());
            Label lblYouList = new Label(yourTxt);
            lblYouList.setWrapText(true);
            lblYouList.setStyle("-fx-font-size: 11px; -fx-text-fill: #444; -fx-line-spacing: 0;");
            lblYouList.setMaxWidth(220);
            boxYou.getChildren().addAll(lblYouCount, lblYouList);
            
            // Click to see full list
            boxYou.setOnMouseClicked(evt -> {
                if (evt.getButton() == MouseButton.PRIMARY) {
                    showWordListDialog("Danh sách từ của bạn", yourTxt, stage);
                }
            });
            boxYou.setStyle("-fx-cursor: hand;");
            Tooltip.install(boxYou, new Tooltip("Click để xem đầy đủ"));
            
            // Đối thủ (Opponent words)
            VBox boxOpp = new VBox(2);
            boxOpp.setPrefWidth(220);
            boxOpp.setMaxWidth(220);
            Label lblOppCount = new Label(item.getOpponentWordOk() + " từ");
            lblOppCount.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #c44545;");
            String oppTxt = String.join(", ", item.getOpponentWord());
            Label lblOppList = new Label(oppTxt);
            lblOppList.setWrapText(true);
            lblOppList.setStyle("-fx-font-size: 11px; -fx-text-fill: #444; -fx-line-spacing: 0;");
            lblOppList.setMaxWidth(220);
            boxOpp.getChildren().addAll(lblOppCount, lblOppList);
            
            boxOpp.setOnMouseClicked(evt -> {
                if (evt.getButton() == MouseButton.PRIMARY) {
                    showWordListDialog("Danh sách từ đối thủ", oppTxt, stage);
                }
            });
            boxOpp.setStyle("-fx-cursor: hand;");
            Tooltip.install(boxOpp, new Tooltip("Click để xem đầy đủ"));
            
            // Kết quả
            Label lblResult = new Label(item.getResult());
            lblResult.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
            lblResult.setPrefWidth(100);
            lblResult.setAlignment(Pos.CENTER);
            
            // Color-code the result
            if (item.getResult().contains("Thắng")) {
                lblResult.setStyle(lblResult.getStyle() + " -fx-text-fill: #28a745;");
            } else if (item.getResult().contains("Thua")) {
                lblResult.setStyle(lblResult.getStyle() + " -fx-text-fill: #dc3545;");
            } else {
                lblResult.setStyle(lblResult.getStyle() + " -fx-text-fill: #ffc107;");
            }
            
            row.add(lblRound, 0, 0);
            row.add(lblLetter, 1, 0);
            row.add(boxYou, 2, 0);
            row.add(boxOpp, 3, 0);
            row.add(lblResult, 4, 0);
            
            content.getChildren().add(row);
        }
        
        // Wrap content in ScrollPane so it can scroll if many rounds
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-background-color: white;");

        
        // Title and close button
        Label title = new Label("Chi tiết trận đấu");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        Button btnClose = new Button("Đóng");
        btnClose.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 6 16 6 16; -fx-background-radius: 4;");
        btnClose.setOnAction(e -> stage.close());
        
        HBox titleBar = new HBox(10);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(8, 12, 8, 12));
        titleBar.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        titleBar.getChildren().addAll(title, spacer, btnClose);
        
        VBox root = new VBox(0, titleBar, scrollPane);
        root.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        
        // Size popup to history table bounds
        try {
            if (tblMatchHistory != null) {
                javafx.geometry.Bounds tb = tblMatchHistory.localToScreen(tblMatchHistory.getBoundsInLocal());
                if (tb != null) {
                    stage.setWidth(tb.getWidth());
                    stage.setHeight(tb.getHeight());
                    stage.setX(tb.getMinX());
                    stage.setY(tb.getMinY());
                } else {
                    stage.setWidth(760);
                    stage.setHeight(480);
                    stage.centerOnScreen();
                }
            } else {
                stage.setWidth(760);
                stage.setHeight(480);
                stage.centerOnScreen();
            }
        } catch (Exception ex) {
            stage.setWidth(760);
            stage.setHeight(480);
            stage.centerOnScreen();
        }

        stage.show();
    }
    
    // Helper method to show full word list dialog
    private void showWordListDialog(String title, String wordList, Stage owner) {
        Platform.runLater(() -> {
            Stage dlg = new Stage();
            dlg.setTitle(title);
            dlg.initModality(Modality.WINDOW_MODAL);
            if (owner != null) {
                try { dlg.initOwner(owner); } catch (Exception ex) {}
            }
            TextArea ta = new TextArea(wordList.replaceAll(", ", "\n"));
            ta.setEditable(false);
            ta.setWrapText(true);
            ta.setPrefSize(480, 360);
            ta.setStyle("-fx-font-size: 13px;");
            VBox vb = new VBox(8, ta);
            vb.setPadding(new Insets(12));
            vb.setStyle("-fx-background-color: white;");
            Scene s = new Scene(vb);
            dlg.setScene(s);
            dlg.setResizable(true);
            dlg.show();
        });
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
        // Send GET_MATCH_HISTORY with payload 'currentUser:opponent' to request filtered history
        if (currentUser != null) {
            String payload = currentUser.getUsername() + ":" + q.trim();
            client.sendMessage(new common.Message(common.Protocol.GET_MATCH_HISTORY, payload));
        } else {
            System.out.println("currentUser is null, cannot search history");
        }
    }

    @FXML
    private void handleReloadLeaderboard(ActionEvent event) {
        System.out.println("Reload leaderboard (by points)");
        if (client != null) client.sendMessage(new Message(Protocol.GET_LEADERBOARD_POINTS, null));
    }
    
    // Cập nhật bảng xếp hạng
    public void updateLeaderboard(List<Users> leaderboard) {
        if (tblLeaderboard != null) {
            System.out.println("LobbyController.updateLeaderboard called, list size = " + (leaderboard == null ? "null" : leaderboard.size()));
            tblLeaderboard.getItems().clear();
            // Set inline style to ensure visibility
            tblLeaderboard.setStyle("-fx-text-fill: #000000;");
            if (leaderboard != null) {
                tblLeaderboard.getItems().addAll(leaderboard);
                tblLeaderboard.refresh(); // Force refresh
                System.out.println("Added " + leaderboard.size() + " users to tblLeaderboard");
                if (leaderboard.isEmpty()) {
                    if (lblSearchResult != null) lblSearchResult.setText("Không tìm thấy người chơi");
                } else {
                    if (lblSearchResult != null) lblSearchResult.setText("");
                }
            }
        }
    }
//--------------------------------------------------------------------------------------------------------------------    
    // Xử lý khi click nút "Mời đấu"
    private void handleChallenge(Users opponent) throws IOException {
        // Lưu thông tin đối thủ
        opponentName = opponent.getUsername();
        opponentId = opponent.getUserId();
        
        // 1. Gửi CHALLENGE_REQUEST lên server
        System.out.println("Gửi CHALLENGE_REQUEST lên server...");
        client.sendMessage(new Message(Protocol.CHALLENGE_REQUEST, String.valueOf(opponent.getUserId())));
        
        // 2. Load WaitingDialog FXML
        System.out.println("Đang load WaitingDialog.fxml...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/GUI/fxml/WaitingDialog.fxml"));
        Parent root = loader.load();
        System.out.println("Load FXML thành công!");
        
        // 3. Lấy controller và setup
        waitingDialogController = loader.getController();
        System.out.println("Lấy controller: " + waitingDialogController);
        waitingDialogController.setClient(client);
        waitingDialogController.setOpponent(opponent.getUsername(), opponent.getUserId());
        
        // 4. Tạo stage cho dialog
        waitingDialogStage = new Stage();
        waitingDialogStage.initModality(Modality.APPLICATION_MODAL);
        waitingDialogStage.setTitle("Đang chờ phản hồi...");
        waitingDialogStage.setScene(new Scene(root));
        waitingDialogStage.setResizable(true);
        
        // 5. Set dialog stage vào controller
        waitingDialogController.setDialogStage(waitingDialogStage);
        
        // 6. Hiển thị dialog (countdown tự động chạy trong initialize)
        waitingDialogStage.show();
    }
    
    // ==================== CHALLENGE METHODS ====================
    
    // Hiển thị InviteDialog khi nhận lời mời
    public void showInviteDialog(int inviterUserId, String inviterUsername) {
        try {
            // Lưu thông tin người mời (đối thủ của người được mời)
            opponentName = inviterUsername;
            opponentId = inviterUserId;
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/GUI/fxml/InviteDialog.fxml"));
            Parent root = loader.load();
            
            inviteDialogController = loader.getController();
            inviteDialogController.setClient(client);
            inviteDialogController.setInviter(inviterUsername, inviterUserId);
            
            inviteDialogStage = new Stage();
            inviteDialogStage.initModality(Modality.APPLICATION_MODAL);
            inviteDialogStage.setTitle("Lời mời đấu");
            inviteDialogStage.setScene(new Scene(root));
            inviteDialogStage.setResizable(true);
            
            inviteDialogController.setDialogStage(inviteDialogStage);
            
            // Hiển thị dialog (countdown tự động chạy trong initialize)
            inviteDialogStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Đóng WaitingDialog và chuyển sang game (khi challenge accepted)
    public void onChallengeAccepted(int accepterUserId, String accepterUsername) {
        if (waitingDialogStage != null && waitingDialogStage.isShowing()) {
            waitingDialogStage.close();
        }
        if (inviteDialogStage != null && inviteDialogStage.isShowing()) {
            inviteDialogStage.close();
        }      
        System.out.println("🎉 " + accepterUsername + " đã chấp nhận! Bắt đầu game...");
        // Chuyển sang màn hình GameRoom với đối thủ đã lưu
        try {
            showGameRoom(opponentName);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi mở GameRoom: " + e.getMessage());
            // Hiện alert cho user
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Không thể mở màn hình game");
                alert.setContentText("Lỗi: " + e.getMessage());
                alert.showAndWait();
            });
        }
    }
    // Hiển thị GameRoom
    private void showGameRoom(String opponentUsername) throws IOException {
        System.out.println("🎮 showGameRoom() called for opponent: " + opponentUsername);
        // Load GameRoom FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/GUI/fxml/GameRoom.fxml"));
        System.out.println("📄 Loading FXML...");
        Parent root = loader.load();
        System.out.println("✅ FXML loaded successfully");
        
        // Lấy controller và setup
        gameRoomController = loader.getController();
        gameRoomController.setClient(client);
        gameRoomController.setCurrentUser(currentUser);
        gameRoomController.setOpponent(opponentUsername);
        
        // Lấy stage hiện tại (Lobby stage)
        Stage lobbyStage = (Stage) lblWelcome.getScene().getWindow();
        
        // Truyền lobby stage vào GameRoom để có thể quay lại
        gameRoomController.setLobbyStage(lobbyStage);
        
        // Lưu kích thước hiện tại của lobby
        double currentWidth = lobbyStage.getWidth();
        double currentHeight = lobbyStage.getHeight();
        double currentX = lobbyStage.getX();
        double currentY = lobbyStage.getY();
        
        // Đổi scene của stage hiện tại thay vì tạo stage mới
        Scene gameScene = new Scene(root);
        lobbyStage.setScene(gameScene);
        lobbyStage.setTitle("Game Room - VS " + opponentUsername);
        
        // Giữ nguyên kích thước và vị trí
        lobbyStage.setWidth(currentWidth);
        lobbyStage.setHeight(currentHeight);
        lobbyStage.setX(currentX);
        lobbyStage.setY(currentY);
        
        // Khi đóng GameRoom bằng nút X, gửi forfeit và ngắt kết nối
        lobbyStage.setOnCloseRequest(event -> {
            // Ngăn đóng trực tiếp
            event.consume();
            
            System.out.println("🏳️ Người chơi thoát và ngắt kết nối!");
            
            // Gửi forfeit lên server
            client.sendMessage(new Message(Protocol.FORFEIT_GAME, String.valueOf(currentUser.getUserId())));
            
            // Chờ một chút để message được gửi
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Đóng stage và ngắt kết nối
            lobbyStage.close();
            
            // Ngắt kết nối client
            if (client != null) {
                client.disconnect();
            }
            
            // Thoát ứng dụng
            Platform.exit();
            System.exit(0);
        });
        
        // Stage đã được cập nhật, không cần show() nữa
        System.out.println("✅ Đã chuyển sang GameRoom với đối thủ: " + opponentUsername);
        System.out.println("⏳ Đợi server gửi ROUND_START để bắt đầu game...");
    }
    
    // Đóng WaitingDialog và hiện thông báo (khi challenge rejected)
    public void onChallengeRejected(String rejecterUsername) {
        if (waitingDialogStage != null && waitingDialogStage.isShowing()) {
            waitingDialogStage.close();
        }       
        System.out.println(rejecterUsername + " đã từ chối!");
    }
    
    // Đóng InviteDialog (khi người mời cancel)
    public void onChallengeCancelled(String cancellerUsername) {
        // Đóng InviteDialog nếu đang mở
        if (inviteDialogStage != null && inviteDialogStage.isShowing()) {
            inviteDialogStage.close();
        }
        System.out.println(cancellerUsername + " đã hủy lời mời!");
    }
    
    // Xử lý khi challenge failed (user offline, etc.)
    public void onChallengeFailed(String errorMessage) {
        System.out.println("Challenge failed: " + errorMessage);
    }
    
    // Bắt đầu round game
    public void startGameRound(String letterDetail, int lengthWord, int timeRound) {
        System.out.println("🎮 LobbyController.startGameRound() called");
        System.out.println("  - gameRoomController: " + (gameRoomController != null ? "OK" : "NULL"));
        
        if (gameRoomController != null) {
            gameRoomController.setupRound(letterDetail, lengthWord, timeRound);
        } else {
            System.err.println("❌ gameRoomController is null! GameRoom chưa được mở.");
        }
    }
    
    // Cập nhật kết quả submit trong round
    public void updateRoundResult(int playerId, int correctCount, boolean isValid, String meaning) {
        if (gameRoomController != null) {
            // Chỉ hiển thị kết quả nếu là người chơi hiện tại submit
            if (playerId == currentUser.getUserId()) {
                gameRoomController.showSubmitResult(isValid, meaning, correctCount);
            }
            // Cập nhật điểm của cả 2 người chơi
            gameRoomController.updateScore(playerId, correctCount);
        }
    }
    
    // Kết thúc round
    public void endRound(int roundWinnerId, int myCount, int oppCount, String myWords, String oppWords) {
        if (gameRoomController != null) {
            gameRoomController.onRoundEnd(roundWinnerId, myCount, oppCount, myWords, oppWords);
        }
    }
    
    // Kết thúc game
    public void endGame(int gameWinnerId, int roundWinsP1, int roundWinsP2) {
        if (gameRoomController != null) {
            gameRoomController.onGameEnd(gameWinnerId, roundWinsP1, roundWinsP2);
        }
    }
    
    // Xử lý khi đối thủ forfeit
    public void onOpponentForfeited() {
        if (gameRoomController != null) {
            gameRoomController.onOpponentForfeited();
        }
    }
    
    public void handleOpponentEmote(String iconFileName) {
        if (gameRoomController != null) {
            gameRoomController.showOpponentEmote(iconFileName);
        }
    }

//----------------------------------------------------------------------------------------------------

}

