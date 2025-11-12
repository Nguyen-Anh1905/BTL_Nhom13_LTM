package Server.service;

import Server.DAO.UserDAO;
import Server.dto.MatchHistoryResponse;
import Server.model.Matches;
import Server.model.Users;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MatchHistoryService {

	private UserDAO userDAO = new UserDAO();

	// Trả về danh sách MatchHistoryResponse cho username được truyền vào
	public List<MatchHistoryResponse> getMatchHistoryForUser(String username) {
		List<MatchHistoryResponse> result = new ArrayList<>();

		if (username == null || username.isEmpty()) return result;

		Users user = userDAO.getUserByUsername(username);
		if (user == null) return result;

		int userId = user.getUserId();
		List<Matches> matches = userDAO.searchMatchesByUserId(userId);
		if (matches == null || matches.isEmpty()) return result;

		for (Matches m : matches) {
			MatchHistoryResponse r = new MatchHistoryResponse();
			r.setMatchId(m.getMatchId());

			// tìm opponent
			int opponentId = (m.getPlayer1Id() == userId) ? m.getPlayer2Id() : m.getPlayer1Id();
			Users opp = userDAO.getUserById(opponentId);
			r.setPlayerName(opp != null ? opp.getUsername() : "(unknown)");

			// Kết quả trận nếu winner_id = currentUser => win
			if(m.getWinnerId() == userId) r.setMatchResult("Win");
			else r.setMatchResult("Lose");

			// Tạo score dạng "x - y" (số round của user - đối thủ)
			int userRounds = (m.getPlayer1Id() == userId) ? m.getPlayer1RoundsWon() : m.getPlayer2RoundsWon();
			int oppRounds = (m.getPlayer1Id() == userId) ? m.getPlayer2RoundsWon() : m.getPlayer1RoundsWon();
			r.setMatchScore(userRounds + " - " + oppRounds);

			// Thời lượng trận, nếu có startedAt/endedAt
			if (m.getStartedAt() != null && m.getEndedAt() != null) {
				Duration d = Duration.between(m.getStartedAt(), m.getEndedAt());
				long seconds = d.getSeconds();
				long mins = seconds / 60;
				long secs = seconds % 60;
				r.setMatchDuration(mins + "m " + secs + "s");
			} else {
				r.setMatchDuration("");
			}

			result.add(r);
		}

		return result;
	}

	// Trả về danh sách match history cho username, có thể lọc theo opponent username (partial match)
	public List<MatchHistoryResponse> getMatchHistoryForUser(String username, String opponentUsername) {
		List<MatchHistoryResponse> all = getMatchHistoryForUser(username);
		if (opponentUsername == null || opponentUsername.trim().isEmpty()) return all;
		String q = opponentUsername.trim().toLowerCase();
		List<MatchHistoryResponse> filtered = new ArrayList<>();
		for (MatchHistoryResponse r : all) {
			if (r.getPlayerName() != null && r.getPlayerName().toLowerCase().contains(q)) {
				filtered.add(r);
			}
		}
		return filtered;
	}

}
