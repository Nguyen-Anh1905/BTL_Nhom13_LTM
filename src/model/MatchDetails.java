package model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng MATCH_DETAILS trong database
 */
public class MatchDetails {
    private int detailId;
    private int matchId;
    private int roundNumber;
    private String letters;
    private int wordLength;
    private int durationSeconds;
    private int player1WordsCount;
    private int player2WordsCount;
    private Integer winnerId;
    private String roundStatus;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;

    // Constructor mặc định
    public MatchDetails() {
    }

    // Constructor đầy đủ
    public MatchDetails(int detailId, int matchId, int roundNumber, String letters,
                        int wordLength, int durationSeconds, int player1WordsCount,
                        int player2WordsCount, Integer winnerId, String roundStatus,
                        LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime createdAt) {
        this.detailId = detailId;
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.letters = letters;
        this.wordLength = wordLength;
        this.durationSeconds = durationSeconds;
        this.player1WordsCount = player1WordsCount;
        this.player2WordsCount = player2WordsCount;
        this.winnerId = winnerId;
        this.roundStatus = roundStatus;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.createdAt = createdAt;
    }

    // Constructor không có ID (dùng khi insert mới)
    public MatchDetails(int matchId, int roundNumber, String letters, int wordLength) {
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.letters = letters;
        this.wordLength = wordLength;
        this.durationSeconds = 60;
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

    public String getLetters() {
        return letters;
    }

    public int getWordLength() {
        return wordLength;
    }

    public int getDurationSeconds() {
        return durationSeconds;
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

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
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
                ", letters='" + letters + '\'' +
                ", wordLength=" + wordLength +
                ", player1WordsCount=" + player1WordsCount +
                ", player2WordsCount=" + player2WordsCount +
                ", roundStatus='" + roundStatus + '\'' +
                '}';
    }
}
