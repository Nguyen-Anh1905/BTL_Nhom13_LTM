package Client.controller;

import Client.Client;
import Client.util.SoundManager;
import common.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InviteDialogController {
    @FXML private Label lblInviterName;
    @FXML private Label lblCountdown;
    @FXML private Button btnAccept;
    @FXML private Button btnReject;
    
    private Client client;
    private String inviterUsername;
    private int inviterUserId;
    private Timeline timeline;
    private int countdown = 30;
    private Stage dialogStage;
    
    public void initialize() {
        // Tự động bắt đầu countdown khi dialog được load
        startCountdown();
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setInviter(String username, int userId) {
        this.inviterUsername = username;
        this.inviterUserId = userId;
        lblInviterName.setText(username);
    }
    
    public void startCountdown() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            countdown--;
            lblCountdown.setText("⏱ " + countdown + " giây");
            
            if (countdown <= 0) {
                timeline.stop();
                handleTimeout();
            }
        }));
        timeline.setCycleCount(30);
        timeline.play();
    }
    
    @FXML
    private void handleAccept() {
        SoundManager.getInstance().playSound(SoundManager.BUTTON_CLICK, 0.4);
        // Dừng countdown
        if (timeline != null) {
            timeline.stop();
        }
        // Gửi thông báo chấp nhận đến server
        client.sendMessage(new Message(Protocol.CHALLENGE_ACCEPT, String.valueOf(inviterUserId)));
        
        // Đóng dialog
        closeDialog();
    }
    
    @FXML
    private void handleReject() {
        SoundManager.getInstance().playSound(SoundManager.BUTTON_CLICK, 0.4);
        // Dừng countdown
        if (timeline != null) {
            timeline.stop();
        }
        
        // Gửi thông báo từ chối đến server
        client.sendMessage(new Message(Protocol.CHALLENGE_REJECT, String.valueOf(inviterUserId)));
        
        // Đóng dialog
        closeDialog();
    }
    
    private void handleTimeout() {
        // Hết thời gian → Tự động đóng dialog (không cần gửi message)
        Platform.runLater(() -> {
            System.out.println("Hết thời gian phản hồi lời mời từ " + inviterUsername);
            closeDialog();
        });
    }
    
    public void closeDialog() {
        dialogStage.close();
    }
}
