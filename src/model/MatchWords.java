package model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng MATCH_WORDS trong database
 */
public class MatchWords {
    private int wordId;
    private int matchId;
    private int detailId;
    private int userId;
    private String word;
    private boolean isValid;
    private LocalDateTime submittedAt;

    // Constructor mặc định
    public MatchWords() {
    }

    // Constructor đầy đủ
    public MatchWords(int wordId, int matchId, int detailId, int userId,
                      String word, boolean isValid, LocalDateTime submittedAt) {
        this.wordId = wordId;
        this.matchId = matchId;
        this.detailId = detailId;
        this.userId = userId;
        this.word = word;
        this.isValid = isValid;
        this.submittedAt = submittedAt;
    }

    // Constructor không có ID (dùng khi insert mới)
    public MatchWords(int matchId, int detailId, int userId, String word, boolean isValid) {
        this.matchId = matchId;
        this.detailId = detailId;
        this.userId = userId;
        this.word = word;
        this.isValid = isValid;
    }

    // Getters
    public int getWordId() {
        return wordId;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getDetailId() {
        return detailId;
    }

    public int getUserId() {
        return userId;
    }

    public String getWord() {
        return word;
    }

    public boolean isValid() {
        return isValid;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    // Setters
    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Override
    public String toString() {
        return "MatchWords{" +
                "wordId=" + wordId +
                ", matchId=" + matchId +
                ", detailId=" + detailId +
                ", userId=" + userId +
                ", word='" + word + '\'' +
                ", isValid=" + isValid +
                '}';
    }
}
