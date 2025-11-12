package Server.model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng dictionary trong database
 */
public class Dictionary {
    private int wordId;
    private String word;
    private LocalDateTime createdAt;
    private Integer letterId;      // FK tới bảng letters (nullable)
    private String meaning;

    // Constructor mặc định
    public Dictionary() {
    }

    // Constructor đầy đủ
    public Dictionary(int wordId, String word, LocalDateTime createdAt, Integer letterId, String meaning) {
        this.wordId = wordId;
        this.word = word;
        this.createdAt = createdAt;
        this.letterId = letterId;
        this.meaning = meaning;
    }

    // Constructor không có ID (dùng khi insert mới)
    public Dictionary(String word, Integer letterId, String meaning) {
        this.word = word;
        this.letterId = letterId;
        this.meaning = meaning;
    }

    // Getters
    public int getWordId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getLetterId() {
        return letterId;
    }

    public String getMeaning() {
        return meaning;
    }

    // Setters
    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setLetterId(Integer letterId) {
        this.letterId = letterId;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "wordId=" + wordId +
                ", word='" + word + '\'' +
                ", letterId=" + letterId +
                ", meaning='" + meaning + '\'' +
                '}';
    }
}
