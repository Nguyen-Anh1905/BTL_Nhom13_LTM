package Client.controller;

import Client.Client;
import Client.MessageHandler;
import common.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import Server.model.Users;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameRoomController implements Initializable {
    
    @FXML private Label lblPlayer1Name;
    @FXML private Label lblPlayer1Score;
    @FXML private Label lblPlayer1RoundWins;
    @FXML private ImageView imgPlayer1Emote; // Emote display in header
    
    @FXML private Label lblPlayer2Name;
    @FXML private Label lblPlayer2Score;
    @FXML private Label lblPlayer2RoundWins;
    @FXML private ImageView imgPlayer2Emote; // Emote display in header
    
    @FXML private Label lblRoundNumber;
    @FXML private Label lblTimer;
    @FXML private ProgressBar progressTimer;
    
    @FXML private FlowPane flowAvailableLetters;
    @FXML private HBox hboxDropZones;
    
    @FXML private Button btnSubmitWord;
    @FXML private Button btnClearWord;
    
    @FXML private Label lblOpponentEmote;
    @FXML private Label lblSubmitResult;  // Label hiển thị kết quả submit
    
    // Emote system - inline display
    @FXML private HBox hboxEmoteIcons;
    @FXML private HBox hboxMyEmote;
    @FXML private Label lblMyName;
    @FXML private ImageView imgMyEmote;
    @FXML private HBox hboxOpponentEmote;
    @FXML private Label lblOpponentName;
    @FXML private ImageView imgOpponentEmote;
    
    // New containers for inline results
    @FXML private VBox vboxGameContainer;
    @FXML private VBox vboxGamePlayArea;
    @FXML private VBox vboxRoundResult;
    @FXML private VBox vboxGameEnd;
    
    private Client client;
    private Stage lobbyStage;
    private Users currentUser;
    private String opponentUsername;
    private int opponentUserId;
    
    private Timeline gameTimer;
    private Timeline autoReadyTimer; // Timer for auto-ready
    private int timeRemaining = 60; // Thời gian còn lại
    private int totalTime = 60; // Tổng thời gian của round (dùng cho progress bar)
    private int currentRound = 1;
    private int maxRounds = 3;
    private boolean isRoundActive = false; // Kiểm tra xem round có đang chạy không
    private boolean hasPlayerReadied = false; // Track if player already sent READY
    
    // Track tổng điểm (tổng số từ đúng) qua các round
    private int totalScoreP1 = 0;
    private int totalScoreP2 = 0;
    
    // Track điểm theo hệ thống: thắng = 1 điểm, hòa = 1 điểm (cả 2), thua = 0 điểm
    private int pointsP1 = 0;
    private int pointsP2 = 0;
    
    // Danh sách chữ cái có sẵn
    private List<String> availableLetters = new ArrayList<>();
    // Từ đang được ghép (các ô drop zone)
    private List<Label> dropZoneSlots = new ArrayList<>();
    // Số lượng ô trống (độ dài từ cần ghép)
    private int wordLength = 5; // Mặc định 5 chữ cái
    private String currentLetterDetail = ""; // Letters từ server
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("🎮 GameRoom Controller initialized");
        System.out.println("   - vboxGameContainer: " + (vboxGameContainer != null ? "OK" : "NULL"));
        System.out.println("   - vboxGamePlayArea: " + (vboxGamePlayArea != null ? "OK" : "NULL"));
        System.out.println("   - vboxRoundResult: " + (vboxRoundResult != null ? "OK" : "NULL"));
        System.out.println("   - vboxGameEnd: " + (vboxGameEnd != null ? "OK" : "NULL"));
        System.out.println("   - flowAvailableLetters: " + (flowAvailableLetters != null ? "OK" : "NULL"));
        System.out.println("⏳ Đợi server gửi ROUND_START...");
        
        // Load icons inline
        loadEmoteIcons();
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public void setLobbyStage(Stage lobbyStage) {
        this.lobbyStage = lobbyStage;
    }
    
    public void setCurrentUser(Users user) {
        this.currentUser = user;
        if (lblPlayer1Name != null) {
            lblPlayer1Name.setText(user.getUsername());
        }
        if (lblPlayer1Score != null) {
            lblPlayer1Score.setText("Điểm: 0");
        }
        if (lblPlayer1RoundWins != null) {
            lblPlayer1RoundWins.setText("Thắng: 0/3");
        }
    }
    
    public void setOpponent(String opponentUsername) {
        this.opponentUsername = opponentUsername;
        if (lblPlayer2Name != null) {
            lblPlayer2Name.setText(opponentUsername);
        }
        if (lblPlayer2Score != null) {
            lblPlayer2Score.setText("Điểm: 0");
        }
        if (lblPlayer2RoundWins != null) {
            lblPlayer2RoundWins.setText("Thắng: 0/3");
        }
    }
    
    private void khoiTaoVongMoi() {
        // Show game controls (hide round result if showing)
        showGameControls();
        
        // Reset timer
        if (lblTimer != null) {
            lblTimer.setText(String.format("⏱ %ds", timeRemaining));
        }
        if (progressTimer != null) {
            progressTimer.setProgress(1.0);
        }
        
        // Cập nhật số vòng
        if (lblRoundNumber != null) {
            lblRoundNumber.setText("VÒNG " + currentRound + "/" + maxRounds);
        }
        
        // Parse letterDetail từ server (ví dụ: "a,b,c,d,g,h,i")
        availableLetters.clear();
        if (currentLetterDetail != null && !currentLetterDetail.isEmpty()) {
            // Split bằng dấu phẩy
            String[] letters = currentLetterDetail.split(",");
            for (String letter : letters) {
                availableLetters.add(letter.trim().toUpperCase()); // Trim và uppercase
            }
        }
        
        // Hiển thị chữ cái
        hienThiCacChuCai();
        
        // Tạo các ô trống
        taoOTrong(wordLength);
        
        // Bắt đầu đếm ngược
        batDauDemNguoc();
    }
    
    private void hienThiCacChuCai() {
        flowAvailableLetters.getChildren().clear();
        
        for (String letter : availableLetters) {
            Button btnLetter = new Button(letter);
            btnLetter.setPrefSize(60, 60);
            btnLetter.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; " +
                             "-fx-background-color: #7E57C2; -fx-text-fill: white; " +
                             "-fx-background-radius: 10; -fx-cursor: hand;");
            
            // Khi click vào chữ cái
            btnLetter.setOnAction(e -> xuLyClickChuCai(letter, btnLetter));
            
            flowAvailableLetters.getChildren().add(btnLetter);
        }
    }
    
    private void taoOTrong(int soChuCai) {
        hboxDropZones.getChildren().clear();
        dropZoneSlots.clear();
        
        for (int i = 0; i < soChuCai; i++) {
            Label slot = new Label("_");
            slot.setPrefSize(60, 60);
            slot.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; " +
                        "-fx-alignment: center; -fx-border-color: #7E57C2; " +
                        "-fx-border-width: 2; -fx-border-radius: 5; " +
                        "-fx-background-color: white; -fx-cursor: hand;");
            
            final int viTri = i;
            // Khi click vào ô đã điền → trả lại chữ
            slot.setOnMouseClicked(e -> xuLyClickO(viTri));
            
            dropZoneSlots.add(slot);
            hboxDropZones.getChildren().add(slot);
        }
    }
    
    private void xuLyClickChuCai(String chuCai, Button btnChuCai) {
        // Tìm ô trống đầu tiên
        for (Label slot : dropZoneSlots) {
            if (slot.getText().equals("_")) {
                // Điền chữ vào ô
                slot.setText(chuCai);
                slot.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; " +
                            "-fx-alignment: center; -fx-border-color: #7E57C2; " +
                            "-fx-border-width: 2; -fx-border-radius: 5; " +
                            "-fx-background-color: #E1BEE7; -fx-cursor: hand;");
                
                // Ẩn button chữ cái
                btnChuCai.setVisible(false);
                btnChuCai.setManaged(false);
                return;
            }
        }
    }
    
    private void xuLyClickO(int viTri) {
        Label slot = dropZoneSlots.get(viTri);
        String chuCai = slot.getText();
        
        // Nếu ô không trống
        if (!chuCai.equals("_")) {
            // Trả lại chữ về danh sách
            slot.setText("_");
            slot.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; " +
                        "-fx-alignment: center; -fx-border-color: #7E57C2; " +
                        "-fx-border-width: 2; -fx-border-radius: 5; " +
                        "-fx-background-color: white; -fx-cursor: hand;");
            
            // Hiện lại button chữ cái
            for (javafx.scene.Node node : flowAvailableLetters.getChildren()) {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    if (btn.getText().equals(chuCai) && !btn.isVisible()) {
                        btn.setVisible(true);
                        btn.setManaged(true);
                        break;
                    }
                }
            }
        }
    }
    
    @FXML
    private void handleSubmitWord() {
        // Kiểm tra xem round có đang active không
        if (!isRoundActive) {
            System.out.println("⚠️ Round đã kết thúc, không thể submit!");
            return;
        }
        
        // Lấy từ đã ghép
        StringBuilder word = new StringBuilder();
        for (Label slot : dropZoneSlots) {
            if (slot.getText().equals("_")) {
                // Chưa điền đủ
                System.out.println("Chưa điền đủ chữ!");
                return;
            }
            word.append(slot.getText());
        }
        
        String submittedWord = word.toString();
        System.out.println("Từ đã ghép: " + submittedWord);
        
        // Gửi đến server
        String payload = currentUser.getUserId() + ":" + submittedWord;
        client.sendMessage(new Message(Protocol.SUBMIT_WORD, payload));
        
        // Tự động clear và reset về trạng thái ban đầu
        Platform.runLater(() -> {
            handleClearWord();
            // Reset lại tất cả letters về ban đầu
            hienThiCacChuCai();
        });
    }
    
    @FXML
    private void handleClearWord() {
        // Xóa tất cả chữ đã điền và reset về trạng thái ban đầu
        for (Label slot : dropZoneSlots) {
            slot.setText("_");
            slot.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; " +
                        "-fx-alignment: center; -fx-border-color: #7E57C2; " +
                        "-fx-border-width: 2; -fx-border-radius: 5; " +
                        "-fx-background-color: white; -fx-cursor: hand;");
        }
        
        // Trả lại tất cả chữ cái cho phía đề bài
        hienThiCacChuCai();
    }
    
    private void batDauDemNguoc() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            
            // Hiển thị số giây thuần túy (VD: 90, 89, 88...)
            lblTimer.setText(String.format("⏱ %ds", timeRemaining));
            
            // Tính progress bar dựa trên totalTime
            progressTimer.setProgress((double) timeRemaining / totalTime);
            
            if (timeRemaining <= 0) {
                gameTimer.stop();
                xuLyHetGio();
            }
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE); // Chạy vô hạn, dừng khi timeRemaining <= 0
        gameTimer.play();
    }
    
    private void xuLyHetGio() {
        System.out.println("⏰ Hết giờ vòng " + currentRound);
        isRoundActive = false; // Tắt flag khi hết giờ
        // Server sẽ tự động gửi ROUND_END
    }
    
    /**
     * Load icons inline into the emotion bar
     */
    private void loadEmoteIcons() {
        if (hboxEmoteIcons == null) {
            System.err.println("⚠️ hboxEmoteIcons is null!");
            return;
        }
        
        hboxEmoteIcons.getChildren().clear();
        
        try {
            java.io.File iconsDir = new java.io.File("src/Server/assets/icons");
            if (!iconsDir.exists() || !iconsDir.isDirectory()) {
                System.err.println("⚠️ Icons directory not found: " + iconsDir.getAbsolutePath());
                return;
            }
            
            java.io.File[] iconFiles = iconsDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".png") || 
                name.toLowerCase().endsWith(".jpg") || 
                name.toLowerCase().endsWith(".jpeg") ||
                name.toLowerCase().endsWith(".gif")
            );
            
            if (iconFiles == null || iconFiles.length == 0) {
                System.err.println("⚠️ No icon files found in " + iconsDir.getAbsolutePath());
                return;
            }
            
            for (java.io.File iconFile : iconFiles) {
                try {
                    javafx.scene.image.Image image = new javafx.scene.image.Image(iconFile.toURI().toString());
                    javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
                    imageView.setFitWidth(70);
                    imageView.setFitHeight(70);
                    imageView.setPreserveRatio(true);
                    
                    Button iconBtn = new Button();
                    iconBtn.setGraphic(imageView);
                    iconBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 5;");
                    iconBtn.setOnMouseEntered(e -> iconBtn.setStyle("-fx-background-color: #f5f5f5; -fx-cursor: hand; -fx-border-color: #667eea; -fx-border-width: 2; -fx-border-radius: 5; -fx-padding: 5;"));
                    iconBtn.setOnMouseExited(e -> iconBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 5;"));
                    
                    String fileName = iconFile.getName();
                    iconBtn.setOnAction(e -> sendEmote(fileName));
                    
                    hboxEmoteIcons.getChildren().add(iconBtn);
                    System.out.println("✅ Loaded icon: " + fileName);
                } catch (Exception ex) {
                    System.err.println("❌ Error loading icon: " + iconFile.getName());
                    ex.printStackTrace();
                }
            }
            
            System.out.println("✅ Loaded " + hboxEmoteIcons.getChildren().size() + " icons");
        } catch (Exception e) {
            System.err.println("❌ Error loading emote icons:");
            e.printStackTrace();
        }
    }
    
    /**
     * Send emote to server and show it locally with animation
     */
    private void sendEmote(String iconFileName) {
        System.out.println("📤 Sending emote: " + iconFileName);
        
        // Show emote on my side
        showMyEmote(iconFileName);
        
        // Send to server to broadcast to opponent
        if (client != null) {
            client.sendMessage(new Message(Protocol.SEND_EMOTE, iconFileName));
        }
    }
    
    /**
     * Display emote sent by current player (my emote)
     */
    private void showMyEmote(String iconFileName) {
        if (imgPlayer1Emote == null) {
            System.err.println("⚠️ imgPlayer1Emote is null!");
            return;
        }
        
        try {
            java.io.File iconFile = new java.io.File("src/Server/assets/icons/" + iconFileName);
            if (!iconFile.exists()) {
                System.err.println("⚠️ Icon file not found: " + iconFile.getAbsolutePath());
                return;
            }
            
            javafx.scene.image.Image image = new javafx.scene.image.Image(iconFile.toURI().toString());
            imgPlayer1Emote.setImage(image);
            
            // Play animation on header emote
            playEmoteAnimation(imgPlayer1Emote);
        } catch (Exception e) {
            System.err.println("❌ Error showing my emote:");
            e.printStackTrace();
        }
    }
    
    /**
     * Display emote received from opponent
     */
    public void showOpponentEmote(String iconFileName) {
        if (imgPlayer2Emote == null) {
            System.err.println("⚠️ imgPlayer2Emote is null!");
            return;
        }
        
        javafx.application.Platform.runLater(() -> {
            try {
                java.io.File iconFile = new java.io.File("src/Server/assets/icons/" + iconFileName);
                if (!iconFile.exists()) {
                    System.err.println("⚠️ Icon file not found: " + iconFile.getAbsolutePath());
                    return;
                }
                
                javafx.scene.image.Image image = new javafx.scene.image.Image(iconFile.toURI().toString());
                imgPlayer2Emote.setImage(image);
                
                // Play animation on opponent's header emote
                playEmoteAnimation(imgPlayer2Emote);
            } catch (Exception e) {
                System.err.println("❌ Error showing opponent emote:");
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Play fade-in, hold, fade-out animation for emote display
     */
    private void playEmoteAnimation(ImageView imageView) {
        // Initial state
        imageView.setVisible(true);
        imageView.setOpacity(0);
        
        // Fade in (300ms)
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), imageView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        // Fade out (500ms)
        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(500), imageView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> imageView.setVisible(false));
        
        // Sequential: fade in -> wait 3s -> fade out
        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition(
            fadeIn,
            new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3)),
            fadeOut
        );
        
        sequence.play();
    }
    
    @FXML
    private void handleExitGame() {
        triggerExitGame();
    }
    
    /**
     * Public method to trigger exit game (can be called from window close event)
     */
    public void triggerExitGame() {
        // Gửi thông báo forfeit cho server
        System.out.println("🏳️ Người chơi đầu hàng!");
        client.sendMessage(new Message(Protocol.FORFEIT_GAME, String.valueOf(currentUser.getUserId())));
        
        // Dừng timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Quay về Lobby với single-stage architecture
        returnToLobby();
    }
    
    // Về lobby sau khi game kết thúc (không cần xác nhận)
    private void returnToLobby() {
        // Dừng timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Lấy stage hiện tại
        Stage currentStage = (Stage) lblPlayer1Name.getScene().getWindow();
        
        // Load lại Lobby scene
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/Client/GUI/fxml/Lobby.fxml"));
            javafx.scene.Parent lobbyRoot = loader.load();
            
            // Setup lobby controller
            LobbyController lobbyController = loader.getController();
            lobbyController.setClient(client);
            lobbyController.setCurrentUser(currentUser);
            
            // Cập nhật MessageHandler để nhận message cho lobby
            if (client != null && client.getHandler() != null) {
                client.getHandler().setLobbyController(lobbyController);
            }
            
            // Lưu kích thước hiện tại
            double currentWidth = currentStage.getWidth();
            double currentHeight = currentStage.getHeight();
            double currentX = currentStage.getX();
            double currentY = currentStage.getY();
            
            // Đổi scene về lobby
            javafx.scene.Scene lobbyScene = new javafx.scene.Scene(lobbyRoot);
            currentStage.setScene(lobbyScene);
            currentStage.setTitle("Lobby - Chào mừng " + currentUser.getFullName());
            
            // Giữ nguyên kích thước
            currentStage.setWidth(currentWidth);
            currentStage.setHeight(currentHeight);
            currentStage.setX(currentX);
            currentStage.setY(currentY);
            
            // Gửi request lấy dữ liệu lobby
            client.sendMessage(new Message(common.Protocol.GET_PLAYER_LIST, null));
            client.sendMessage(new Message(common.Protocol.GET_MATCH_HISTORY, currentUser.getUsername()));
            client.sendMessage(new Message(common.Protocol.GET_LEADERBOARD_POINTS, null));
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi quay về lobby:");
            e.printStackTrace();
        }
    }
    
    // Setup round từ server
    public void setupRound(String letterDetail, int lengthWord, int timeRound) {
        System.out.println("🎮 GameRoomController.setupRound() called");
        System.out.println("  - letterDetail: " + letterDetail);
        System.out.println("  - lengthWord: " + lengthWord);
        System.out.println("  - timeRound: " + timeRound);
        
        Platform.runLater(() -> {
            // Hide round result if visible
            if (vboxRoundResult != null && vboxRoundResult.isVisible()) {
                vboxRoundResult.setVisible(false);
                vboxRoundResult.setManaged(false);
                System.out.println("🔄 Ẩn round result để bắt đầu round mới");
            }
            
            // Cancel auto-ready timer if running
            if (autoReadyTimer != null) {
                autoReadyTimer.stop();
                autoReadyTimer = null;
                System.out.println("⏹️ Hủy auto-ready timer");
            }
            
            // Show game play area
            if (vboxGamePlayArea != null) {
                vboxGamePlayArea.setVisible(true);
                vboxGamePlayArea.setManaged(true);
            }
        });
        
        // Reset points khi bắt đầu game mới (round 1)
        if (currentRound == 1) {
            totalScoreP1 = 0;
            totalScoreP2 = 0;
            pointsP1 = 0;
            pointsP2 = 0;
            System.out.println("🔄 Reset points cho game mới");
        }
        
        // Reset ready flag for new round
        hasPlayerReadied = false;
        
        this.currentLetterDetail = letterDetail;
        this.wordLength = lengthWord;
        this.timeRemaining = timeRound;
        this.totalTime = timeRound; // Lưu thời gian ban đầu
        this.isRoundActive = true; // Bật flag khi bắt đầu round
        khoiTaoVongMoi();
    }
    
    // Hiển thị kết quả submit từ server
    public void showSubmitResult(boolean isValid, String meaning, int correctCount) {
        Platform.runLater(() -> {
            if (lblSubmitResult == null) return;
            
            if (isValid) {
                System.out.println("✅ ĐÚNG! " + meaning + " (Tổng: " + correctCount + ")");
                lblSubmitResult.setText("✅ ĐÚNG! Nghĩa: " + meaning + "  |  Tổng từ đúng: " + correctCount);
                lblSubmitResult.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 8;");
            } else {
                System.out.println("❌ SAI!");
                lblSubmitResult.setText("❌ SAI! Từ không hợp lệ hoặc không có trong từ điển. Hãy thử lại!");
                lblSubmitResult.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 8;");
            }
            
            // Hiệu ứng fade in
            lblSubmitResult.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), lblSubmitResult);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            
            // Tự động fade out và xóa sau 3 giây
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), lblSubmitResult);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(event -> lblSubmitResult.setText(""));
                fadeOut.play();
            });
            pause.play();
        });
    }
    
    // Cập nhật điểm người chơi
    public void updateScore(int playerId, int score) {
        Platform.runLater(() -> {
            if (playerId == currentUser.getUserId()) {
                if (lblPlayer1Score != null) {
                    lblPlayer1Score.setText("Từ đúng: " + score);
                }
            } else {
                if (lblPlayer2Score != null) {
                    lblPlayer2Score.setText("Từ đúng: " + score);
                }
            }
        });
    }
    
    // Xử lý khi round kết thúc
    public void onRoundEnd(int roundWinnerId, int myCount, int oppCount, String myWords, String oppWords) {
        Platform.runLater(() -> {
            System.out.println("🏁 Round " + currentRound + " kết thúc!");
            System.out.println("  - Người thắng: " + roundWinnerId);
            System.out.println("  - Điểm My: " + myCount + ", Opp: " + oppCount);
            
            // Tắt round
            isRoundActive = false;
            if (gameTimer != null) {
                gameTimer.stop();
            }
            
            // Cập nhật số round thắng
            if (roundWinnerId == currentUser.getUserId()) {
                String currentWins = lblPlayer1RoundWins.getText().split("/")[0].split(": ")[1];
                int wins = Integer.parseInt(currentWins) + 1;
                lblPlayer1RoundWins.setText("Thắng: " + wins + "/3");
            } else if (roundWinnerId > 0) {
                String currentWins = lblPlayer2RoundWins.getText().split("/")[0].split(": ")[1];
                int wins = Integer.parseInt(currentWins) + 1;
                lblPlayer2RoundWins.setText("Thắng: " + wins + "/3");
            }
            
            // Cộng dồn tổng điểm
            totalScoreP1 += myCount;
            totalScoreP2 += oppCount;
            
            // Cộng điểm theo hệ thống: thắng = 1 điểm, hòa = 1 điểm (cả 2), thua = 0 điểm
            if (roundWinnerId == currentUser.getUserId()) {
                // Player1 thắng
                pointsP1 += 1;
            } else if (roundWinnerId > 0 && roundWinnerId != currentUser.getUserId()) {
                // Player2 thắng
                pointsP2 += 1;
            } else if (roundWinnerId == -1) {
                // Hòa - cả 2 đều được 1 điểm
                pointsP1 += 1;
                pointsP2 += 1;
            }
            System.out.println("📊 Tổng điểm hiện tại - My: " + totalScoreP1 + ", Opp: " + totalScoreP2);
            System.out.println("🏆 Points - My: " + pointsP1 + ", Opp: " + pointsP2);
            
            // Hiển thị kết quả round inline (thay thế khu vực chơi game)
            showInlineRoundResult(roundWinnerId, myCount, oppCount, myWords, oppWords);
            
            // Tăng currentRound để chuẩn bị cho round tiếp theo
            currentRound++;
            System.out.println("📈 Chuyển sang round: " + currentRound);
        });
    }
    
    // Hiển thị dialog kết quả round
    private void showRoundResultDialog(int winnerId, int myCount, int oppCount, String myWords, String oppWords) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Kết quả Round " + currentRound);
            
            javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(20);
            root.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
            
            // Tiêu đề
            javafx.scene.control.Label lblTitle = new javafx.scene.control.Label("Kết quả Round " + currentRound);
            lblTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
            
            // Xác định player1 và player2 info (player1 = currentUser, player2 = opponent)
            String player1Name = currentUser.getUsername();
            String player2Name = opponentUsername;
            String player1WordsList = myWords;  // Server đã swap để myWords là của currentUser
            String player2WordsList = oppWords;
            int player1Count = myCount;
            int player2Count = oppCount;
            
            // Kiểm tra hòa
            boolean isDraw = (myCount == oppCount);
            
            // Tạo HBox chứa 2 cột kết quả
            javafx.scene.layout.HBox resultBox = new javafx.scene.layout.HBox(40);
            resultBox.setStyle("-fx-alignment: center;");
            
            // Cột Player 1
            javafx.scene.layout.VBox player1Box = createPlayerResultBox(
                player1Name, 
                player1Count, 
                player1WordsList, 
                winnerId == currentUser.getUserId(),
                isDraw
            );
            
            // Cột Player 2
            javafx.scene.layout.VBox player2Box = createPlayerResultBox(
                player2Name, 
                player2Count, 
                player2WordsList, 
                winnerId != currentUser.getUserId() && winnerId > 0,
                isDraw
            );
            
            resultBox.getChildren().addAll(player1Box, player2Box);
            
            // Nút đóng
            javafx.scene.control.Button btnClose = new javafx.scene.control.Button("Tiếp tục");
            btnClose.setStyle("-fx-font-size: 16px; -fx-padding: 10 30; -fx-background-color: white; -fx-text-fill: #667eea; -fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 20;");
            btnClose.setOnAction(e -> {
                // Gửi thông báo cho server là đã sẵn sàng
                client.sendMessage(new Message(Protocol.PLAYER_READY, String.valueOf(currentUser.getUserId())));
                dialogStage.close();
            });
            
            root.getChildren().addAll(lblTitle, resultBox, btnClose);
            
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 650, 450);
            dialogStage.setScene(scene);
            dialogStage.setResizable(true);
            
            // Tự động đóng sau 10 giây và gửi ready
            Timeline autoClose = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
                if (dialogStage.isShowing()) {
                    client.sendMessage(new Message(Protocol.PLAYER_READY, String.valueOf(currentUser.getUserId())));
                    dialogStage.close();
                }
            }));
            autoClose.play();
            
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Tạo box hiển thị kết quả của 1 người chơi
    private javafx.scene.layout.VBox createPlayerResultBox(String playerName, int wordCount, String words, boolean isWinner, boolean isDraw) {
        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(15);
        String borderColor = isWinner ? "#4CAF50" : (isDraw ? "#FFC107" : "#E0E0E0");
        box.setStyle("-fx-padding: 20; -fx-border-color: " + borderColor + 
                     "; -fx-border-width: 3; -fx-border-radius: 10; -fx-background-color: white; -fx-background-radius: 10; -fx-alignment: center;");
        box.setPrefWidth(250);
        
        // Win/Lose/Draw label
        String resultText = isWinner ? "🏆 WIN" : (isDraw ? "🤝 DRAW" : "😢 LOSE");
        String resultColor = isWinner ? "#4CAF50" : (isDraw ? "#FFC107" : "#F44336");
        javafx.scene.control.Label lblResult = new javafx.scene.control.Label(resultText);
        lblResult.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + resultColor + ";");
        
        // Player name
        javafx.scene.control.Label lblName = new javafx.scene.control.Label(playerName);
        lblName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Word count
        javafx.scene.control.Label lblCount = new javafx.scene.control.Label("Số từ đúng: " + wordCount);
        lblCount.setStyle("-fx-font-size: 16px;");
        
        // Word list
        javafx.scene.control.Label lblWordsTitle = new javafx.scene.control.Label("Các từ đã nhập:");
        lblWordsTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        javafx.scene.control.TextArea txtWords = new javafx.scene.control.TextArea();
        txtWords.setEditable(false);
        txtWords.setWrapText(true);
        txtWords.setPrefHeight(120);
        txtWords.setStyle("-fx-font-size: 13px;");
        
        if (words != null && !words.isEmpty()) {
            String[] wordArray = words.split(",");
            txtWords.setText(String.join("\n", wordArray));
        } else {
            txtWords.setText("(Không có từ nào)");
        }
        
        box.getChildren().addAll(lblResult, lblName, lblCount, lblWordsTitle, txtWords);
        return box;
    }
    
    // Tạo box hiển thị kết quả với meaning
    private javafx.scene.layout.VBox createPlayerResultBoxWithMeaning(String playerName, int wordCount, String wordsWithMeanings, boolean isWinner, boolean isDraw) {
        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(8);
        String borderColor = isWinner ? "#4CAF50" : (isDraw ? "#FFC107" : "#F44336");
        box.setStyle("-fx-padding: 20; -fx-border-color: " + borderColor + 
                     "; -fx-border-width: 4; -fx-border-radius: 15; " +
                     "-fx-background-color: rgba(255,255,255,0.95); " +
                     "-fx-background-radius: 15; -fx-alignment: center; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        box.setPrefWidth(450);
        box.setMaxHeight(550);
        
        // Win/Lose/Draw label
        String resultText = isWinner ? "🏆 THẮNG" : (isDraw ? "🤝 HÒA" : "😢 THUA");
        String resultColor = isWinner ? "#4CAF50" : (isDraw ? "#FFC107" : "#F44336");
        javafx.scene.control.Label lblResult = new javafx.scene.control.Label(resultText);
        lblResult.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + resultColor + ";");
        
        // Player name
        javafx.scene.control.Label lblName = new javafx.scene.control.Label(playerName);
        lblName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        // Word count
        javafx.scene.control.Label lblCount = new javafx.scene.control.Label("Số từ đúng: " + wordCount);
        lblCount.setStyle("-fx-font-size: 16px; -fx-text-fill: #555; -fx-font-weight: bold;");
        
        // Meanings list (không cần ScrollPane, tất cả trên một dòng)
        javafx.scene.layout.VBox wordsContainer = new javafx.scene.layout.VBox(5);
        wordsContainer.setStyle("-fx-padding: 12; -fx-background-color: white;");
        
        if (wordsWithMeanings != null && !wordsWithMeanings.isEmpty()) {
            // Parse format: word1|meaning1,word2|meaning2,...
            String[] wordPairs = wordsWithMeanings.split(",");
            StringBuilder meaningsList = new StringBuilder();
            
            for (int i = 0; i < wordPairs.length; i++) {
                if (wordPairs[i].contains("|")) {
                    String[] parts = wordPairs[i].split("\\|", 2);
                    String meaning = parts.length > 1 ? parts[1] : "N/A";
                    
                    if (i > 0) {
                        meaningsList.append(", ");
                    }
                    meaningsList.append((i + 1) + ". " + meaning);
                }
            }
            
            // Hiển thị tất cả meanings trên một label với wrap
            javafx.scene.control.Label lblAllMeanings = new javafx.scene.control.Label(meaningsList.toString());
            lblAllMeanings.setWrapText(true);
            lblAllMeanings.setStyle("-fx-font-size: 14px; -fx-text-fill: #333; -fx-padding: 10; " +
                                   "-fx-background-color: #f8f9fa; -fx-border-color: #e9ecef; " +
                                   "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
            
            wordsContainer.getChildren().add(lblAllMeanings);
        } else {
            javafx.scene.control.Label lblEmpty = new javafx.scene.control.Label("(Không có từ nào)");
            lblEmpty.setStyle("-fx-font-size: 14px; -fx-text-fill: #999; -fx-font-style: italic;");
            wordsContainer.getChildren().add(lblEmpty);
        }
        
        box.getChildren().addAll(lblResult, lblName, lblCount, wordsContainer);
        return box;
    }
    
    // Xử lý khi game kết thúc
    public void onGameEnd(int gameWinnerId, int roundWinsP1, int roundWinsP2) {
        Platform.runLater(() -> {
            System.out.println("🎉 GAME KẾT THÚC!");
            System.out.println("  - Người thắng: " + gameWinnerId);
            System.out.println("  - Round wins P1: " + roundWinsP1 + ", P2: " + roundWinsP2);
            
            // Dừng timer
            isRoundActive = false;
            if (gameTimer != null) {
                gameTimer.stop();
            }
            
            // Hiển thị kết quả game inline (thay thế toàn bộ màn hình game)
            showInlineGameEnd(gameWinnerId, pointsP1, pointsP2, roundWinsP1, roundWinsP2);
        });
    }
    
    // Hiển thị dialog tổng kết game
    private void showGameResultDialog(int winnerId, int pointsP1, int pointsP2, int roundWinsP1, int roundWinsP2) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Kết quả trận đấu");
            
            javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(30);
            root.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom right, #1a2a6c, #b21f1f, #fdbb2d);");
            
            // Xác định kết quả
            boolean isWin = (winnerId == currentUser.getUserId());
            boolean isDraw = (winnerId == -1);
            
            // Icon và tiêu đề lớn
            javafx.scene.control.Label lblIcon = new javafx.scene.control.Label(
                isWin ? "🏆" : (isDraw ? "🤝" : "😢")
            );
            lblIcon.setStyle("-fx-font-size: 80px;");
            
            javafx.scene.control.Label lblResult = new javafx.scene.control.Label(
                isWin ? "CHIẾN THẮNG!" : (isDraw ? "HÒA!" : "THẤT BẠI!")
            );
            lblResult.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 5);");
            
            // Tỉ số
            javafx.scene.layout.HBox scoreBox = new javafx.scene.layout.HBox(20);
            scoreBox.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 15;");
            
            javafx.scene.control.Label lblPlayer1 = new javafx.scene.control.Label(currentUser.getUsername());
            lblPlayer1.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
            
            javafx.scene.control.Label lblScore = new javafx.scene.control.Label(pointsP1 + " - " + pointsP2);
            lblScore.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #FFD700; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 5, 0, 0, 2);");
            
            javafx.scene.control.Label lblPlayer2 = new javafx.scene.control.Label(opponentUsername);
            lblPlayer2.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
            
            scoreBox.getChildren().addAll(lblPlayer1, lblScore, lblPlayer2);
            
            // Thông tin chi tiết
            javafx.scene.layout.VBox detailBox = new javafx.scene.layout.VBox(10);
            detailBox.setStyle("-fx-alignment: center; -fx-padding: 15; -fx-background-color: rgba(255,255,255,0.15); -fx-background-radius: 10;");
            
            javafx.scene.control.Label lblDetail = new javafx.scene.control.Label(
                "Tổng số round: 3\n" +
                currentUser.getUsername() + " thắng: " + roundWinsP1 + " round\n" +
                opponentUsername + " thắng: " + roundWinsP2 + " round"
            );
            lblDetail.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-text-alignment: center;");
            detailBox.getChildren().add(lblDetail);
            
            // Nút action
            javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(20);
            buttonBox.setStyle("-fx-alignment: center;");
            
            javafx.scene.control.Button btnBackToLobby = new javafx.scene.control.Button("🏠 Về Lobby");
            btnBackToLobby.setStyle("-fx-font-size: 18px; -fx-padding: 12 40; -fx-background-color: white; -fx-text-fill: #1a2a6c; -fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 25;");
            btnBackToLobby.setOnAction(e -> {
                dialogStage.close();
                returnToLobby();
            });
            
            buttonBox.getChildren().add(btnBackToLobby);
            
            root.getChildren().addAll(lblIcon, lblResult, scoreBox, detailBox, buttonBox);
            
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 600, 500);
            dialogStage.setScene(scene);
            dialogStage.setResizable(true);
            dialogStage.setOnCloseRequest(e -> returnToLobby());
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Xử lý khi đối thủ forfeit (đầu hàng/thoát trận)
    public void onOpponentForfeited() {
        Platform.runLater(() -> {
            System.out.println("🏳️ Đối thủ đã thoát trận!");
            
            // Dừng timer
            if (gameTimer != null) {
                gameTimer.stop();
            }
            
            // Hiển thị thông báo chiến thắng
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Chiến thắng!");
            alert.setHeaderText("🎉 Chúc mừng! Bạn đã thắng!");
            alert.setContentText("Đối thủ " + opponentUsername + " đã thoát khỏi trận đấu.\n\n" +
                               "Bạn được tính là NGƯỜI THẮNG CUỘC!");
            
            alert.getDialogPane().setStyle("-fx-font-size: 14px;");
            alert.showAndWait();
            
            // Về lobby
            returnToLobby();
        });
    }
    
    // ==================== INLINE RESULT DISPLAY METHODS ====================
    
    /**
     * Show inline round result - replaces the game play area
     */
    private void showInlineRoundResult(int winnerId, int myCount, int oppCount, String myWords, String oppWords) {
        Platform.runLater(() -> {
            try {
                // Hide game play area
                vboxGamePlayArea.setVisible(false);
                vboxGamePlayArea.setManaged(false);
                
                // Clear previous round result content
                vboxRoundResult.getChildren().clear();
                
                // Build the content
                Label lblTitle = new Label("Kết quả Round " + (currentRound - 1));
                lblTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; " +
                                 "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 5);");
                
                // Xác định player1 và player2 info
                String player1Name = currentUser.getUsername();
                String player2Name = opponentUsername;
                String player1WordsList = myWords;
                String player2WordsList = oppWords;
                int player1Count = myCount;
                int player2Count = oppCount;
                boolean isDraw = (myCount == oppCount);
                
                // Create result boxes
                HBox resultBox = new HBox(40);
                resultBox.setStyle("-fx-alignment: center;");
                
                VBox player1Box = createPlayerResultBoxWithMeaning(
                    player1Name, 
                    player1Count, 
                    player1WordsList, 
                    winnerId == currentUser.getUserId(),
                    isDraw
                );
                
                VBox player2Box = createPlayerResultBoxWithMeaning(
                    player2Name, 
                    player2Count, 
                    player2WordsList, 
                    winnerId != currentUser.getUserId() && winnerId > 0,
                    isDraw
                );
                
                resultBox.getChildren().addAll(player1Box, player2Box);
                
                // Continue button
                Button btnContinue = new Button("Tiếp tục");
                btnContinue.setStyle("-fx-font-size: 18px; -fx-padding: 12 40; -fx-background-color: white; " +
                                    "-fx-text-fill: #667eea; -fx-cursor: hand; -fx-font-weight: bold; " +
                                    "-fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 3);");
                btnContinue.setOnMouseEntered(e -> btnContinue.setStyle(
                    "-fx-font-size: 18px; -fx-padding: 12 40; -fx-background-color: #FFD700; " +
                    "-fx-text-fill: #667eea; -fx-cursor: hand; -fx-font-weight: bold; " +
                    "-fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0, 0, 5);"
                ));
                btnContinue.setOnMouseExited(e -> btnContinue.setStyle(
                    "-fx-font-size: 18px; -fx-padding: 12 40; -fx-background-color: white; " +
                    "-fx-text-fill: #667eea; -fx-cursor: hand; -fx-font-weight: bold; " +
                    "-fx-background-radius: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 3);"
                ));
                btnContinue.setOnAction(e -> {
                    // Cancel auto-ready timer
                    if (autoReadyTimer != null) {
                        autoReadyTimer.stop();
                    }
                    
                    // Hide round result immediately
                    vboxRoundResult.setVisible(false);
                    vboxRoundResult.setManaged(false);
                    
                    // Send PLAYER_READY if not already sent
                    // Only stop if someone won 2-0 (early win)
                    boolean isEarlyWin = (pointsP1 == 2 && pointsP2 == 0) || (pointsP2 == 2 && pointsP1 == 0);
                    if (!hasPlayerReadied && currentRound <= maxRounds && !isEarlyWin) {
                        hasPlayerReadied = true;
                        client.sendMessage(new Message(Protocol.PLAYER_READY, String.valueOf(currentUser.getUserId())));
                        System.out.println("✅ Gửi PLAYER_READY cho round " + currentRound);
                    } else {
                        System.out.println("⏭️ Không gửi PLAYER_READY (currentRound=" + currentRound + ", maxRounds=" + maxRounds + ", points=" + pointsP1 + "-" + pointsP2 + ", earlyWin=" + isEarlyWin + ")");
                    }
                });
                
                vboxRoundResult.getChildren().addAll(lblTitle, resultBox, btnContinue);
                
                // Show round result
                vboxRoundResult.setVisible(true);
                vboxRoundResult.setManaged(true);
                
                // Reset ready flag
                hasPlayerReadied = false;
                
                // Auto-send ready after 10 seconds
                autoReadyTimer = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
                    boolean isEarlyWin = (pointsP1 == 2 && pointsP2 == 0) || (pointsP2 == 2 && pointsP1 == 0);
                    if (vboxRoundResult.isVisible() && !hasPlayerReadied && currentRound <= maxRounds && !isEarlyWin) {
                        hasPlayerReadied = true;
                        client.sendMessage(new Message(Protocol.PLAYER_READY, String.valueOf(currentUser.getUserId())));
                        System.out.println("⏰ Auto-ready sau 10 giây cho round " + currentRound);
                    }
                }));
                autoReadyTimer.play();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Show inline game end result - replaces entire screen
     */
    private void showInlineGameEnd(int winnerId, int pointsP1, int pointsP2, int roundWinsP1, int roundWinsP2) {
        Platform.runLater(() -> {
            try {
                // Hide game container
                vboxGameContainer.setVisible(false);
                vboxGameContainer.setManaged(false);
                
                // Set purple gradient background - JavaFX 8 syntax
                String purpleGradient = "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #667eea 0%, #764ba2 100%); -fx-alignment: center; -fx-spacing: 30;";
                vboxGameEnd.setStyle(purpleGradient);
                
                // Also set parent to ensure no white background
                if (vboxGameEnd.getParent() != null) {
                    vboxGameEnd.getParent().setStyle("-fx-background-color: transparent;");
                }
                
                System.out.println("DEBUG: Set vboxGameEnd style to: " + purpleGradient);
                System.out.println("DEBUG: vboxGameEnd current style: " + vboxGameEnd.getStyle());
                System.out.println("DEBUG: vboxGameEnd parent: " + vboxGameEnd.getParent());
                
                // Clear previous game end content
                vboxGameEnd.getChildren().clear();
                
                // Determine result
                boolean isWin = (winnerId == currentUser.getUserId());
                boolean isDraw = (winnerId == -1);
                
                // Icon and title
                Label lblIcon = new Label(isWin ? "🏆" : (isDraw ? "🤝" : "😢"));
                lblIcon.setStyle("-fx-font-size: 100px;");
                
                Label lblResult = new Label(isWin ? "CHIẾN THẮNG!" : (isDraw ? "HÒA!" : "THẤT BẠI!"));
                lblResult.setStyle("-fx-font-size: 56px; -fx-font-weight: bold; -fx-text-fill: #FFD700; " +
                                  "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 15, 0, 0, 8);");
                
                // Score box
                HBox scoreBox = new HBox(30);
                scoreBox.setStyle("-fx-alignment: center; -fx-padding: 30; " +
                                 "-fx-background-color: rgba(0,0,0,0.4); " +
                                 "-fx-background-radius: 20; -fx-border-color: #FFD700; " +
                                 "-fx-border-width: 3; -fx-border-radius: 20;");
                
                Label lblPlayer1 = new Label(currentUser.getUsername());
                lblPlayer1.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white; " +
                                   "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 5, 0, 0, 3);");
                
                Label lblScore = new Label(pointsP1 + " - " + pointsP2);
                lblScore.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #FFD700; " +
                                 "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 8, 0, 0, 4);");
                
                Label lblPlayer2 = new Label(opponentUsername);
                lblPlayer2.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white; " +
                                   "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 5, 0, 0, 3);");
                
                scoreBox.getChildren().addAll(lblPlayer1, lblScore, lblPlayer2);
                
                // Detail box
                VBox detailBox = new VBox(12);
                detailBox.setStyle("-fx-alignment: center; -fx-padding: 25; " +
                                  "-fx-background-color: rgba(0,0,0,0.3); " +
                                  "-fx-background-radius: 15; -fx-border-color: rgba(255,215,0,0.5); " +
                                  "-fx-border-width: 2; -fx-border-radius: 15;");
                
                Label lblDetailTitle = new Label("CHI TIẾT TRẬN ĐẤU");
                lblDetailTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");
                
                Label lblRoundDetail = new Label(
                    currentUser.getUsername() + " thắng: " + roundWinsP1 + " round\n" +
                    opponentUsername + " thắng: " + roundWinsP2 + " round"
                );
                lblRoundDetail.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-text-alignment: center;");
                detailBox.getChildren().addAll(lblDetailTitle, lblRoundDetail);
                
                // Button with hover effect
                Button btnBackToLobby = new Button("🏠 VỀ LOBBY");
                btnBackToLobby.setStyle(
                    "-fx-font-size: 20px; -fx-padding: 15 50; " +
                    "-fx-background-color: #FFD700; -fx-text-fill: #1a2a6c; " +
                    "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 30; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 10, 0, 0, 5);"
                );
                btnBackToLobby.setOnMouseEntered(e -> btnBackToLobby.setStyle(
                    "-fx-font-size: 20px; -fx-padding: 15 50; " +
                    "-fx-background-color: white; -fx-text-fill: #1a2a6c; " +
                    "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 30; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 15, 0, 0, 8); -fx-scale-x: 1.05; -fx-scale-y: 1.05;"
                ));
                btnBackToLobby.setOnMouseExited(e -> btnBackToLobby.setStyle(
                    "-fx-font-size: 20px; -fx-padding: 15 50; " +
                    "-fx-background-color: #FFD700; -fx-text-fill: #1a2a6c; " +
                    "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 30; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 10, 0, 0, 5);"
                ));
                btnBackToLobby.setOnAction(e -> returnToLobby());
                
                vboxGameEnd.getChildren().addAll(lblIcon, lblResult, scoreBox, detailBox, btnBackToLobby);
                
                // Show game end screen
                vboxGameEnd.setVisible(true);
                vboxGameEnd.setManaged(true);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Show game controls - for next round
     */
    private void showGameControls() {
        Platform.runLater(() -> {
            vboxRoundResult.setVisible(false);
            vboxRoundResult.setManaged(false);
            vboxGamePlayArea.setVisible(true);
            vboxGamePlayArea.setManaged(true);
        });
    }
}

