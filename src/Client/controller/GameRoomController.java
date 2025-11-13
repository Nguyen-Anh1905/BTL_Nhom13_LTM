package Client.controller;

import Client.Client;
import common.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
    
    @FXML private Label lblPlayer2Name;
    @FXML private Label lblPlayer2Score;
    @FXML private Label lblPlayer2RoundWins;
    
    @FXML private Label lblRoundNumber;
    @FXML private Label lblTimer;
    @FXML private ProgressBar progressTimer;
    
    @FXML private FlowPane flowAvailableLetters;
    @FXML private HBox hboxDropZones;
    
    @FXML private Button btnSubmitWord;
    @FXML private Button btnClearWord;
    
    @FXML private Label lblOpponentEmote;
    
    private Client client;
    private Stage lobbyStage;
    private Users currentUser;
    private String opponentUsername;
    private int opponentUserId;
    
    private Timeline gameTimer;
    private int timeRemaining = 60; // 60 giây mỗi vòng
    private int currentRound = 1;
    private int maxRounds = 3;
    
    // Danh sách chữ cái có sẵn
    private List<String> availableLetters = new ArrayList<>();
    // Từ đang được ghép (các ô drop zone)
    private List<Label> dropZoneSlots = new ArrayList<>();
    // Số lượng ô trống (độ dài từ cần ghép)
    private int wordLength = 5; // Mặc định 5 chữ cái
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("GameRoom Controller initialized");
        
        // Khởi tạo game mới
        khoiTaoVongMoi();
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
        // Reset timer
        timeRemaining = 60;
        lblTimer.setText("⏱ 01:00");
        progressTimer.setProgress(1.0);
        
        // Cập nhật số vòng
        lblRoundNumber.setText("VÒNG " + currentRound + "/" + maxRounds);
        
        // Tạo danh sách chữ cái ngẫu nhiên (ví dụ)
        availableLetters.clear();
        String[] letters = {"C", "H", "Ả", "O", "M", "Ừ", "N", "G", "I", "Ê", "U"};
        for (String letter : letters) {
            availableLetters.add(letter);
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
        
        // TODO: Gửi từ lên server để kiểm tra
        // client.sendMessage(new Message(Protocol.SUBMIT_WORD, submittedWord));
    }
    
    @FXML
    private void handleClearWord() {
        // Xóa tất cả chữ đã điền
        for (int i = 0; i < dropZoneSlots.size(); i++) {
            xuLyClickO(i);
        }
    }
    
    private void batDauDemNguoc() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            
            int minutes = timeRemaining / 60;
            int seconds = timeRemaining % 60;
            lblTimer.setText(String.format("⏱ %02d:%02d", minutes, seconds));
            progressTimer.setProgress((double) timeRemaining / 60);
            
            if (timeRemaining <= 0) {
                gameTimer.stop();
                xuLyHetGio();
            }
        }));
        gameTimer.setCycleCount(60);
        gameTimer.play();
    }
    
    private void xuLyHetGio() {
        System.out.println("Hết giờ vòng " + currentRound);
        // TODO: Xử lý khi hết giờ
    }
    
    @FXML
    private void handleEmoteLike() {
        // TODO: Gửi emote lên server
        System.out.println("Sent: 👍");
    }
    
    @FXML
    private void handleEmoteHeart() {
        System.out.println("Sent: ❤️");
    }
    
    @FXML
    private void handleEmoteDislike() {
        System.out.println("Sent: 👎");
    }
    
    @FXML
    private void handleEmoteSurprise() {
        System.out.println("Sent: 😲");
    }
    
    @FXML
    private void handleExitGame() {
        // Dừng timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Đóng game room
        Stage gameStage = (Stage) lblPlayer1Name.getScene().getWindow();
        gameStage.close();
        
        // Hiện lại Lobby
        if (lobbyStage != null) {
            lobbyStage.show();
        }
    }
}
