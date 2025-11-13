package Client.controller;

import Client.Client;
import common.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WaitingDialogController {
    @FXML private Label lblOpponentName;
    @FXML private Label lblCountdown;
    @FXML private ProgressBar progressTime;
    @FXML private Button btnCancel;
    
    private Client client;
    private String opponentUsername;
    private int opponentUserId;
    private Timeline timeline;
    private int countdown = 30;
    private Stage dialogStage;
    
    public void initialize() {
        // Khởi tạo progress bar
        progressTime.setProgress(1.0);
        startCountdown();
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setOpponent(String username, int userId) {
        this.opponentUsername = username;
        this.opponentUserId = userId;
        lblOpponentName.setText(username);
    }
    
    public void startCountdown() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            countdown--;
            lblCountdown.setText("⏱ " + countdown + " giây");
            progressTime.setProgress((double) countdown / 30);
            
            if (countdown <= 0) {
                timeline.stop();
                handleTimeout();
            }
        }));
        timeline.setCycleCount(30);
        timeline.play();
    }
    
    @FXML
    private void handleCancel() {
        // Dừng countdown
        if (timeline != null) {
            timeline.stop();
        }
        
        // Gửi thông báo hủy đến server
        client.sendMessage(new Message(Protocol.CHALLENGE_CANCEL, String.valueOf(opponentUserId)));
        
        // Đóng dialog
        closeDialog();
    }
    
    private void handleTimeout() {
        // Tự động hủy khi hết thời gian
        client.sendMessage(new Message(Protocol.CHALLENGE_CANCEL, String.valueOf(opponentUserId)));
        
        Platform.runLater(() -> {
            // Hiển thị thông báo timeout
            System.out.println("Hết thời gian chờ phản hồi từ " + opponentUsername);
            closeDialog();
        });
    }
    
    public void closeDialog() {
        dialogStage.close();
    }
}
