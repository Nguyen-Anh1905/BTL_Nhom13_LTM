package Server.model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng MATCH_DETAILS trong database
 */
public class MatchDetails {
    private int detailId;
    private int matchId;
    private int roundNumber;
    private int letterId;
    private int player1WordsCount;
    private int player2WordsCount;
    private String roundStatus;
    private String player1Dic;
    private String player2Dic;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;

    // Constructor mặc định
    public MatchDetails() {
    }

    // Constructor đầy đủ
    public MatchDetails(int detailId, int matchId, int roundNumber, int letterId,
                        int player1WordsCount,
                        int player2WordsCount, String roundStatus,
                        LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime createdAt) {
        this.detailId = detailId;
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.letterId = letterId;
        this.player1WordsCount = player1WordsCount;
        this.player2WordsCount = player2WordsCount;
        this.roundStatus = roundStatus;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.createdAt = createdAt;
    }

    // Constructor không có ID (dùng khi insert mới)
    public MatchDetails(int matchId, int roundNumber, int letterId, int wordLength) {
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.letterId = letterId;
        this.player1WordsCount = 0;
        this.player2WordsCount = 0;
        this.roundStatus = "waiting";
    }

    // Getters
    public int getDetailId() {
        return detailId;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getLetterId() {
        return letterId;
    }

    public String getPlayer2Dic() {
        return player2Dic;
    }

    public String getPlayer1Dic() {
        return player1Dic;
    }

    public int getPlayer1WordsCount() {
        return player1WordsCount;
    }

    public int getPlayer2WordsCount() {
        return player2WordsCount;
    }

    public String getRoundStatus() {
        return roundStatus;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public void setPlayer1Dic(String player1Dic) {
        this.player1Dic = player1Dic;
    }

    public void setPlayer2Dic(String player2Dic) {
        this.player2Dic = player2Dic;
    }

    public void setPlayer1WordsCount(int player1WordsCount) {
        this.player1WordsCount = player1WordsCount;
    }

    public void setPlayer2WordsCount(int player2WordsCount) {
        this.player2WordsCount = player2WordsCount;
    }

    public void setRoundStatus(String roundStatus) {
        this.roundStatus = roundStatus;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "MatchDetails{" +
                "detailId=" + detailId +
                ", matchId=" + matchId +
                ", roundNumber=" + roundNumber +
                ", letterId=" + letterId +
                ", player1WordsCount=" + player1WordsCount +
                ", player2WordsCount=" + player2WordsCount +
                ", roundStatus='" + roundStatus + '\'' +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}