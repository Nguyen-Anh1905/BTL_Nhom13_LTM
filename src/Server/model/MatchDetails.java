package Server.model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng match_details trong database
 */
public class MatchDetails {
    private int detailId;
    private int matchId;
    private int roundNumber;
    private Integer letterId;           // FK tới bảng letters (nullable)
    private int player1WordsCount;
    private int player2WordsCount;
    private Integer winnerId;           // FK tới bảng users (nullable)
    private String roundStatus;         // enum: 'waiting', 'playing', 'completed'
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;

    // Constructor mặc định
    public MatchDetails() {
    }

    // Constructor đầy đủ
    public MatchDetails(int detailId, int matchId, int roundNumber, Integer letterId,
                        int player1WordsCount, int player2WordsCount, Integer winnerId,
                        String roundStatus, LocalDateTime startedAt, LocalDateTime endedAt,
                        LocalDateTime createdAt) {
        this.detailId = detailId;
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.letterId = letterId;
        this.player1WordsCount = player1WordsCount;
        this.player2WordsCount = player2WordsCount;
        this.winnerId = winnerId;
        this.roundStatus = roundStatus;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.createdAt = createdAt;
    }

    // Constructor không có ID (dùng khi insert mới)
    public MatchDetails(int matchId, int roundNumber, Integer letterId) {
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

    public Integer getLetterId() {
        return letterId;
    }

    public int getPlayer1WordsCount() {
        return player1WordsCount;
    }

    public int getPlayer2WordsCount() {
        return player2WordsCount;
    }

    public Integer getWinnerId() {
        return winnerId;
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

    public void setLetterId(Integer letterId) {
        this.letterId = letterId;
    }

    public void setPlayer1WordsCount(int player1WordsCount) {
        this.player1WordsCount = player1WordsCount;
    }

    public void setPlayer2WordsCount(int player2WordsCount) {
        this.player2WordsCount = player2WordsCount;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
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
                '}';
    }
}
