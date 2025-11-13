package Server.model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng DICTIONARY trong database
 */
public class Dictionary {
    private int wordId;
    private int letterId;
    private String word;
    private String meaning;
    private LocalDateTime createdAt;


    public int getWordId() {
        return wordId;
    }

    public void setWordId(int word_id) {
        this.wordId = word_id;
    }

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "word_id=" + wordId+
                ", letterId=" + letterId +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}