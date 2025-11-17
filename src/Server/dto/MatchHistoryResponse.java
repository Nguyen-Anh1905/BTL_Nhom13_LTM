package Server.dto;

import java.io.Serializable;

public class MatchHistoryResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private int matchId;
    private String playerName; // tên đối thủ
    private String matchResult;
    private String matchScore;
    private String matchStart; // thời gian bắt đầu
    private String matchDuration; // thời gian kết thúc

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public String getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(String matchScore) {
        this.matchScore = matchScore;
    }

    public String getMatchStart() {
        return matchStart;
    }

    public void setMatchStart(String matchStart) {
        this.matchStart = matchStart;
    }

    public String getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(String matchDuration) {
        this.matchDuration = matchDuration;
    }
}
