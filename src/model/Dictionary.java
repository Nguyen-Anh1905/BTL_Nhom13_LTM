package model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng DICTIONARY trong database
 */
public class Dictionary {
    private int wordId;
    private String word;
    private int wordLength;
    private String difficulty;
    private LocalDateTime createdAt;

    // Constructor mặc định
    public Dictionary() {
    }

    // Constructor đầy đủ
    public Dictionary(int wordId, String word, int wordLength, String difficulty, LocalDateTime createdAt) {
        this.wordId = wordId;
        this.word = word;
        this.wordLength = wordLength;
        this.difficulty = difficulty;
        this.createdAt = createdAt;
    }

    // Constructor không có ID (dùng khi insert mới)
    public Dictionary(String word, int wordLength, String difficulty) {
        this.word = word;
        this.wordLength = wordLength;
        this.difficulty = difficulty;
    }

    // Getters
    public int getWordId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }

    public int getWordLength() {
        return wordLength;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "wordId=" + wordId +
                ", word='" + word + '\'' +
                ", wordLength=" + wordLength +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
