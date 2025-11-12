package Server.model;

import java.io.Serializable;

public class Letter implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int letterId;           // letter_id - Primary Key
    private String letterDetail;    // letter_detail - Các chữ cái cách nhau bởi dấu phẩy (VD: "C,H,Ả,O,M,Ừ,N,G,I,Ê,U")
    private int lengthWord;         // length_word - Độ dài từ yêu cầu
    private int timeRound;          // time_round - Thời gian cho vòng đấu (giây)
    
    // Constructor mặc định
    public Letter() {
    }
    
    // Constructor đầy đủ
    public Letter(int letterId, String letterDetail, int lengthWord, int timeRound) {
        this.letterId = letterId;
        this.letterDetail = letterDetail;
        this.lengthWord = lengthWord;
        this.timeRound = timeRound;
    }
    
    // Constructor không có letterId (dùng khi insert mới)
    public Letter(String letterDetail, int lengthWord, int timeRound) {
        this.letterDetail = letterDetail;
        this.lengthWord = lengthWord;
        this.timeRound = timeRound;
    }
    
    // Getters
    public int getLetterId() {
        return letterId;
    }
    
    public String getLetterDetail() {
        return letterDetail;
    }
    
    public int getLengthWord() {
        return lengthWord;
    }
    
    public int getTimeRound() {
        return timeRound;
    }
    
    // Setters
    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }
    
    public void setLetterDetail(String letterDetail) {
        this.letterDetail = letterDetail;
    }
    
    public void setLengthWord(int lengthWord) {
        this.lengthWord = lengthWord;
    }
    
    public void setTimeRound(int timeRound) {
        this.timeRound = timeRound;
    }
    
    @Override
    public String toString() {
        return "Letter{" +
                "letterId=" + letterId +
                ", letterDetail='" + letterDetail + '\'' +
                ", lengthWord=" + lengthWord +
                ", timeRound=" + timeRound +
                '}';
    }
}
