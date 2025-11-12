package Server.service;

import Server.DAO.DictionaryDAO;
import Server.DAO.LetterDAO;
import Server.DAO.MatchDetailDAO;
import Server.dto.MatchDetailResponse;
import Server.model.Dictionary;
import Server.model.Letters;
import Server.model.MatchDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatchDetailService {
    private DictionaryDAO dictionaryDAO;
    private MatchDetailDAO matchDetailDAO;
    private LetterDAO letterDAO;
    public List<MatchDetailResponse> getMatchDetails(int match_id, String you, String opponent) {
        // initialize DAOs if null
        if (matchDetailDAO == null) matchDetailDAO = new MatchDetailDAO();
        if (dictionaryDAO == null) dictionaryDAO = new DictionaryDAO();
        if (letterDAO == null) letterDAO = new LetterDAO();

        List<MatchDetails> details = matchDetailDAO.searchMatchDetailByMatchId(match_id);
        List<MatchDetailResponse> matchDetailResponses = new ArrayList<>();

        if (details == null || details.isEmpty()) return matchDetailResponses;

        for (MatchDetails detail : details) {
            MatchDetailResponse detailResponse = new MatchDetailResponse();
            detailResponse.setMatchId(detail.getMatchId());
            detailResponse.setDetialId(detail.getDetailId());
            detailResponse.setLetterId(detail.getLetterId());
            detailResponse.setRound(detail.getRoundNumber());

            boolean youIsPlayer1 = "player1".equalsIgnoreCase(you);
            if (youIsPlayer1) {
                detailResponse.setYourWordOk(detail.getPlayer1WordsCount());
                detailResponse.setOpponentWordOk(detail.getPlayer2WordsCount());
                detailResponse.setYourDic(detail.getPlayer1Dic());
                detailResponse.setOpponentDic(detail.getPlayer2Dic());
            } else {
                detailResponse.setYourWordOk(detail.getPlayer2WordsCount());
                detailResponse.setOpponentWordOk(detail.getPlayer1WordsCount());
                detailResponse.setYourDic(detail.getPlayer2Dic());
                detailResponse.setOpponentDic(detail.getPlayer1Dic());
            }

            Letters letters = letterDAO.findById(detail.getLetterId());
            detailResponse.setLetter(letters != null ? letters.getLetterDetail() : "");

            // The DAO returns CSV lists of dictionary IDs in yourDic/opponentDic (e.g. "1,2,3").
            List<Dictionary> yourDicWord = new ArrayList<>();
            List<Dictionary> opponentDicWord = new ArrayList<>();
            if (detailResponse.getYourDic() != null && !detailResponse.getYourDic().trim().isEmpty()) {
                yourDicWord = dictionaryDAO.findDictionaryFromListDicId(detailResponse.getYourDic());
            }
            if (detailResponse.getOpponentDic() != null && !detailResponse.getOpponentDic().trim().isEmpty()) {
                opponentDicWord = dictionaryDAO.findDictionaryFromListDicId(detailResponse.getOpponentDic());
            }

            for (Dictionary dictionary : yourDicWord) {
                detailResponse.getYourWord().add(dictionary.getMeaning());
            }

            for (Dictionary dictionary : opponentDicWord) {
                detailResponse.getOpponentWord().add(dictionary.getMeaning());
            }

            if (detailResponse.getYourWordOk() > detailResponse.getOpponentWordOk()) detailResponse.setResult("Thắng");
            else if (detailResponse.getYourWordOk() < detailResponse.getOpponentWordOk()) detailResponse.setResult("Thua");
            else detailResponse.setResult("Hòa");

            matchDetailResponses.add(detailResponse);
        }

        return matchDetailResponses;
    }
}
