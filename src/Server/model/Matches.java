package Server.model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng MATCHES trong database
 */
public class Matches {
    private int matchId;
    private int player1Id;
    private int player2Id;
    private String matchStatus;
    private int totalRounds;
    private int player1RoundsWon;
    private int player2RoundsWon;
    private Integer winnerId;
    private String result;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    // Constructor mặc định
    public Matches() {
    }

    // Constructor đầy đủ
    public Matches(int matchId, int player1Id, int player2Id, String matchStatus,
                   int totalRounds, int player1RoundsWon, int player2RoundsWon,
                   Integer winnerId, String result, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.matchId = matchId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.matchStatus = matchStatus;
        this.totalRounds = totalRounds;
        this.player1RoundsWon = player1RoundsWon;
        this.player2RoundsWon = player2RoundsWon;
        this.winnerId = winnerId;
        this.result = result;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    // Constructor không có ID (dùng khi insert mới)
    public Matches(int player1Id, int player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.matchStatus = "playing";
        this.totalRounds = 0;
        this.player1RoundsWon = 0;
        this.player2RoundsWon = 0;
    }

    // Getters
    public int getMatchId() {
        return matchId;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public int getPlayer1RoundsWon() {
        return player1RoundsWon;
    }

    public int getPlayer2RoundsWon() {
        return player2RoundsWon;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    // Setters
    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public void setPlayer1RoundsWon(int player1RoundsWon) {
        this.player1RoundsWon = player1RoundsWon;
    }

    public void setPlayer2RoundsWon(int player2RoundsWon) {
        this.player2RoundsWon = player2RoundsWon;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    @Override
    public String toString() {
        return "Matches{" +
                "matchId=" + matchId +
                ", player1Id=" + player1Id +
                ", player2Id=" + player2Id +
                ", matchStatus='" + matchStatus + '\'' +
                ", totalRounds=" + totalRounds +
                ", player1RoundsWon=" + player1RoundsWon +
                ", player2RoundsWon=" + player2RoundsWon +
                ", winnerId=" + winnerId +
                ", result='" + result + '\'' +
                '}';
    }
}
