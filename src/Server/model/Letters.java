package Server.model;

public class Letters {
    private int letterId;
    private String letterDetail;
    private int lengthWord;
    private int timeRound;

    public Letters(int letterId, String letterDetail, int lengthWord, int timeRound) {
        this.letterId = letterId;
        this.letterDetail = letterDetail;
        this.lengthWord = lengthWord;
        this.timeRound = timeRound;
    }

    
    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public String getLetterDetail() {
        return letterDetail;
    }

    public void setLetterDetail(String letterDetail) {
        this.letterDetail = letterDetail;
    }

    public int getLengthWord() {
        return lengthWord;
    }

    public void setLengthWord(int lengthWord) {
        this.lengthWord = lengthWord;
    }

    public int getTimeRound() {
        return timeRound;
    }

    public void setTimeRound(int timeRound) {
        this.timeRound = timeRound;
    }
}
