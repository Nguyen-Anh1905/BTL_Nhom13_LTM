package Server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MatchDetailResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private int matchId;
    private int detialId; // id round đấu
    private int letterId;
    private int round; // số thứ tự round đấu 1-3
    private String letter; // dãy ký tự của round đấu
    private int yourWordOk; // số từ bản thân tạo đúng
    private int opponentWordOk; // số từ đối thủ tạo đúng
    private String yourDic;
    private String opponentDic;
    private List<String> yourWord = new ArrayList<>();
    private List<String> opponentWord = new ArrayList<>();
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getYourDic() {
        return yourDic;
    }

    public void setYourDic(String yourDic) {
        this.yourDic = yourDic;
    }

    public String getOpponentDic() {
        return opponentDic;
    }

    public void setOpponentDic(String opponentDic) {
        this.opponentDic = opponentDic;
    }

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getDetialId() {
        return detialId;
    }

    public void setDetialId(int detialId) {
        this.detialId = detialId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getYourWordOk() {
        return yourWordOk;
    }

    public void setYourWordOk(int yourWordOk) {
        this.yourWordOk = yourWordOk;
    }

    public int getOpponentWordOk() {
        return opponentWordOk;
    }

    public void setOpponentWordOk(int opponentWordOk) {
        this.opponentWordOk = opponentWordOk;
    }

    public List<String> getYourWord() {
        return yourWord;
    }

    public void setYourWord(List<String> yourWord) {
        this.yourWord = yourWord;
    }

    public List<String> getOpponentWord() {
        return opponentWord;
    }

    public void setOpponentWord(List<String> opponentWord) {
        this.opponentWord = opponentWord;
    }

    @Override
    public String toString() {
        return "MatchDetailResponse{" +
                "detialId=" + detialId +
                ", round=" + round +
                ", letter='" + letter + '\'' +
                ", yourWordOk=" + yourWordOk +
                ", opponentWordOk=" + opponentWordOk +
                ", yourWord=" + yourWord +
                ", opponentWord=" + opponentWord +
                '}';
    }
}
