
package Server.service;

import Server.DAO.DictionaryDAO;
import Server.DAO.LetterDAO;
import Server.DAO.MatchDetailDAO;
import Server.DAO.MatchesDAO;
import Server.DAO.UserDAO;
import Server.ClientHandler;
import Server.Server;
import Server.model.*;
import Server.model.Dictionary;
import common.Message;
import common.Protocol;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Gameroom {
    private int matchId;
    private int player1Id;
    private int player2Id;
    private List<Letters> letters; // 3 letters random
    private List<String> currentDictionary; // dictionary cho round hiện tại
    private Map<Integer, Integer> correctWordsCount; // playerId -> count in current round
    private Map<Integer, Integer> roundWins; // playerId -> rounds won
    private int drawRounds = 0; // Số round hòa
    private Map<Integer, Set<Integer>> usedWordIdsByPlayer; // playerId -> word_ids đã dùng trong round hiện tại
    private Set<Integer> readyPlayers; // Theo dõi người chơi sẵn sàng cho round tiếp theo
    private boolean pendingRoundStart; // Đang chờ người chơi ready
    private int currentRound = 1;
    private boolean gameEnded = false; // Flag để ngăn các hành động sau khi game kết thúc
    private ScheduledExecutorService timerExecutor;
    private MatchesDAO matchesDAO = new MatchesDAO();
    private MatchDetailDAO matchDetailDAO = new MatchDetailDAO();
    private DictionaryDAO dictionaryDAO = new DictionaryDAO();
    private LetterDAO letterDAO = new LetterDAO();
    private UserDAO userDAO = new UserDAO();

    public Gameroom(int player1Id, int player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.correctWordsCount = new HashMap<>();
        this.roundWins = new HashMap<>();
        this.usedWordIdsByPlayer = new HashMap<>();
        this.readyPlayers = new HashSet<>();
        this.pendingRoundStart = false;
        this.gameEnded = false;
        this.timerExecutor = Executors.newScheduledThreadPool(1);
        initializeGame();
    }

    // khởi tạo trận đấuu
    private void initializeGame() {
        // Tạo match trong DB
        Matches match = new Matches();
        match.setPlayer1Id(player1Id);
        match.setPlayer2Id(player2Id);
        match.setMatchStatus("playing");
        match.setTotalRounds(3);
        this.matchId = matchesDAO.insert(match);
        System.out.println("💾 [DB] Đã tầo Matches - matchId: " + matchId + ", status: playing");
        
        if (this.matchId == -1) {
            System.err.println("❌ [ERROR] Không thể tạo match trong DB! Game sẽ không lưu được.");
            // Vẫn tiếp tục game nhưng không lưu DB
        }

        // Random 3 letters từ DB
        List<Letters> allLetters = letterDAO.findAll();
        if (allLetters.isEmpty()) {
            System.err.println("CẢNH BÁO: Không có letters trong DB!");
            return;
        }
        Collections.shuffle(allLetters);
        int count = Math.min(3, allLetters.size());
        this.letters = new ArrayList<>(allLetters.subList(0, count));
        
        System.out.println("✅ Đã random " + letters.size() + " letters cho match " + matchId);
        for (Letters l : letters) {
            System.out.println("  - Letter ID " + l.getLetterId() + ": " + l.getLetterDetail());
        }

        // Init counts
        correctWordsCount.put(player1Id, 0);
        correctWordsCount.put(player2Id, 0);
        roundWins.put(player1Id, 0);
        roundWins.put(player2Id, 0);
    }

    public void startRound() {
        if (currentRound > 3) return;
        Letters currentLetter = letters.get(currentRound - 1);
        
        // Load dictionary MỚI cho round này từ DB
        List<String> words = dictionaryDAO.getWordsByLetterId(currentLetter.getLetterId());
        currentDictionary = new ArrayList<>(words);
        for(String word : currentDictionary) {
            System.out.println("từ thứ nhất: " + word);

        }
        System.out.println("📚 Loaded " + currentDictionary.size() + " words cho round " + currentRound);
        
        // Reset used word IDs cho MỖI NGƯỜI CHƠI
        usedWordIdsByPlayer.put(player1Id, new HashSet<>());
        usedWordIdsByPlayer.put(player2Id, new HashSet<>());
        
        // Gửi ROUND_START đến cả hai client
        String payload = currentLetter.getLetterDetail() + ":" + currentLetter.getLengthWord() + ":" + currentLetter.getTimeRound();
        System.out.println("🎮 ROUND_START Round " + currentRound + " - Payload: " + payload);
        
        sendToPlayer(player1Id, new Message(Protocol.ROUND_START, payload));
        sendToPlayer(player2Id, new Message(Protocol.ROUND_START, payload));
        
        System.out.println("✅ Đã gửi ROUND_START đến player " + player1Id + " và " + player2Id);

        // Start timer
        timerExecutor.schedule(this::endRound, currentLetter.getTimeRound(), TimeUnit.SECONDS);
    }

    // so sánh word vừa nhập
    public void submitWord(int playerId, String word) {
        if (gameEnded) {
            System.out.println("⚠️ Game đã kết thúc, bỏ qua submitWord");
            return;
        }
        
        // Chuyển về chữ thường để so sánh
        String wordLower = word.toLowerCase();
        
        // Lấy word_id của từ này
        Integer wordId = dictionaryDAO.getWordIdByWord(wordLower);
        
        // Kiểm tra từ có trong dictionary HIỆN TẠI không
        // VÀ kiểm tra người chơi này đã dùng từ đó chưa (kiểm tra bằng word_id)
        boolean isValid = wordId != null && 
                          currentDictionary.contains(wordLower) && 
                          !usedWordIdsByPlayer.get(playerId).contains(wordId);
        
        String meaning = "";
        if (isValid) {
            correctWordsCount.put(playerId, correctWordsCount.get(playerId) + 1);
            // Lưu word_id thay vì word
            usedWordIdsByPlayer.get(playerId).add(wordId);
            
            // Lấy meaning từ DB
            meaning = dictionaryDAO.getMeaningByWord(wordLower);
            if (meaning == null) meaning = "";
            
            System.out.println("✅ Player " + playerId + " - Từ đúng: " + wordLower + " (ID: " + wordId + ", " + meaning + ")");
        } else {
            if (wordId == null || !currentDictionary.contains(wordLower)) {
                System.out.println("❌ Player " + playerId + " - Từ không hợp lệ: " + wordLower);
            } else {
                System.out.println("❌ Player " + playerId + " - Từ đã dùng: " + wordLower + " (ID: " + wordId + ")");
            }
        }
        
        // Gửi ROUND_RESULT: playerId:correctCount:isValid:meaning
        String payload = playerId + ":" + correctWordsCount.get(playerId) + ":" + isValid + ":" + meaning;
        sendToPlayer(player1Id, new Message(Protocol.ROUND_RESULT, payload));
        sendToPlayer(player2Id, new Message(Protocol.ROUND_RESULT, payload));
    }

    private void endRound() {
        if (gameEnded) {
            System.out.println("⚠️ Game đã kết thúc, bỏ qua endRound");
            return;
        }
        
        int p1Count = correctWordsCount.get(player1Id);
        int p2Count = correctWordsCount.get(player2Id);
        int winnerId = -1;
        if (p1Count > p2Count) {
            roundWins.put(player1Id, roundWins.get(player1Id) + 1);
            winnerId = player1Id;
        } else if (p2Count > p1Count) {
            roundWins.put(player2Id, roundWins.get(player2Id) + 1);
            winnerId = player2Id;
        } else {
            // Hòa - cộng drawRounds, winnerId = -1 (sẽ chuyển thành NULL trong DB)
            drawRounds++;
            winnerId = -1; // -1 để biết là hòa, nhưng sẽ lưu NULL vào DB
        }
        // Nếu hòa, không cộng roundWins cho ai

        // Lưu MatchDetail
        MatchDetails detail = new MatchDetails();
        detail.setMatchId(matchId);
        detail.setRoundNumber(currentRound);
        detail.setLetterId(letters.get(currentRound - 1).getLetterId());
        detail.setPlayer1WordsCount(p1Count);
        detail.setPlayer2WordsCount(p2Count);
        
        // Lưu word_id của player1 (dạng "1,2,3")
        String p1WordIds = usedWordIdsByPlayer.get(player1Id).stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
        detail.setPlayer1Dic(p1WordIds);
        System.out.println("🔍 Debug - Player1Id: " + player1Id + ", WordIds: " + p1WordIds);
        
        // Lưu word_id của player2 (dạng "1,2,3")
        String p2WordIds = usedWordIdsByPlayer.get(player2Id).stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
        detail.setPlayer2Dic(p2WordIds);
        System.out.println("🔍 Debug - Player2Id: " + player2Id + ", WordIds: " + p2WordIds);
        
        detail.setWinnerId(winnerId);
        detail.setRoundStatus("completed");
        
        if (matchId > 0) {
            System.out.println("💾 [DB] Đang lưu MatchDetail - Round: " + currentRound + ", MatchId: " + matchId);
            matchDetailDAO.insert(detail);
            System.out.println("💾 [DB] Đã lưu MatchDetail - Player1 word_ids: " + p1WordIds + ", Player2 word_ids: " + p2WordIds);
        } else {
            System.err.println("❌ [ERROR] Không thể lưu MatchDetail vì matchId không hợp lệ: " + matchId);
        }

        // Gửi ROUND_END với danh sách từ đúng của mỗi người (gửi WORD|MEANING)
        // Lấy words với meanings từ word_ids
        String p1WordsWithMeanings = getWordsWithMeanings(usedWordIdsByPlayer.get(player1Id));
        String p2WordsWithMeanings = getWordsWithMeanings(usedWordIdsByPlayer.get(player2Id));
        
        // Gửi payload riêng cho mỗi client: winnerId:myCount:oppCount:myWordsWithMeanings:oppWordsWithMeanings
        // Format của words: word1|meaning1,word2|meaning2,...
        // Với player1: p1Count:p2Count:p1Words:p2Words
        String payloadP1 = winnerId + ":" + p1Count + ":" + p2Count + ":" + p1WordsWithMeanings + ":" + p2WordsWithMeanings;
        sendToPlayer(player1Id, new Message(Protocol.ROUND_END, payloadP1));
        
        // Với player2: p2Count:p1Count:p2Words:p1Words (swap để p2 luôn thấy mình ở vị trí đầu)
        String payloadP2 = winnerId + ":" + p2Count + ":" + p1Count + ":" + p2WordsWithMeanings + ":" + p1WordsWithMeanings;
        sendToPlayer(player2Id, new Message(Protocol.ROUND_END, payloadP2));
        
        System.out.println("✅ Đã gửi ROUND_END cho round " + currentRound);
        System.out.println("   RoundWins - P1: " + roundWins.get(player1Id) + ", P2: " + roundWins.get(player2Id));

        // Reset counts
        correctWordsCount.put(player1Id, 0);
        correctWordsCount.put(player2Id, 0);

        // Tăng currentRound
        currentRound++;
        System.out.println("📈 Chuyển sang round: " + currentRound);

        // Check thắng sớm: 1 người đạt 2 điểm và người kia 0 điểm
        int p1Wins = roundWins.get(player1Id);
        int p2Wins = roundWins.get(player2Id);
        
        if ((p1Wins == 2 && p2Wins == 0) || (p2Wins == 2 && p1Wins == 0)) {
            System.out.println("🏆 Thắng sớm 2-0! Kết thúc game.");
            endGame();
        } else if (currentRound <= 3) {
            // Còn round, tiếp tục - Chờ cả 2 người ready
            pendingRoundStart = true;
            readyPlayers.clear();
            System.out.println("⏳ Chờ cả 2 người chơi sẵn sàng cho round " + currentRound);
            
            // Timeout 10 giây: nếu không cả 2 ready thì tự động bắt đầu
            timerExecutor.schedule(() -> {
                if (pendingRoundStart && currentRound <= 3) {
                    System.out.println("⏰ Timeout! Bắt đầu round " + currentRound + " dù chưa đủ người ready");
                    pendingRoundStart = false;
                    startRound();
                }
            }, 10, TimeUnit.SECONDS);
        } else {
            // Hết 3 round
            System.out.println("🏁 Hết 3 round, kết thúc game.");
            endGame();
        }
    }

    private void endGame() {
        int winnerId = -1;
        String result = "draw";
        
        // Tính điểm: thắng = 1 điểm, hòa = 1 điểm
        int player1Points = roundWins.get(player1Id) + drawRounds;
        int player2Points = roundWins.get(player2Id) + drawRounds;
        
        if (roundWins.get(player1Id) > roundWins.get(player2Id)) {
            winnerId = player1Id;
            result = "player1_win";
        } else if (roundWins.get(player2Id) > roundWins.get(player1Id)) {
            winnerId = player2Id;
            result = "player2_win";
        }
        // Nếu hòa (cùng số round thắng), winnerId = -1, result = "draw"
        
        System.out.println("💾 [DB] Đang update Matches result - matchId: " + matchId + ", winnerId: " + winnerId + ", result: " + result);
        System.out.println("💾 [DB] Points - P1: " + player1Points + " (" + roundWins.get(player1Id) + " wins + " + drawRounds + " draws), P2: " + player2Points + " (" + roundWins.get(player2Id) + " wins + " + drawRounds + " draws)");
        
        // Lưu điểm số (player1Points, player2Points) và số trận hòa
        if (matchId > 0) {
            matchesDAO.updateResult(matchId, winnerId, result, player1Points, player2Points, drawRounds);
            System.out.println("💾 [DB] Đã update Matches - status: completed, P1 points: " + player1Points + ", P2 points: " + player2Points + ", draws: " + drawRounds);
        } else {
            System.err.println("❌ [ERROR] Không thể update match vì matchId không hợp lệ: " + matchId);
        }

        // Gửi GAME_END
        String payload = winnerId + ":" + roundWins.get(player1Id) + ":" + roundWins.get(player2Id);
        sendToPlayer(player1Id, new Message(Protocol.GAME_END, payload));
        sendToPlayer(player2Id, new Message(Protocol.GAME_END, payload));
        
        // Update thống kê và điểm cho cả 2 người chơi
        String p1Result, p2Result;
        if (winnerId == player1Id) {
            p1Result = "win";
            p2Result = "lose";
        } else if (winnerId == player2Id) {
            p1Result = "lose";
            p2Result = "win";
        } else {
            // Hòa
            p1Result = "draw";
            p2Result = "draw";
        }
        
        userDAO.updateUserStats(player1Id, p1Result);
        userDAO.updateUserStats(player2Id, p2Result);
        System.out.println("📊 Đã cập nhật stats - P1: " + p1Result + ", P2: " + p2Result);
        
        // Update trạng thái người chơi về 'online'
        Users user1 = userDAO.getUserById(player1Id);
        Users user2 = userDAO.getUserById(player2Id);
        if (user1 != null) {
            userDAO.updateUserStatus(user1.getUsername(), "online");
            System.out.println("✅ Đã update status 'online' cho user " + player1Id + " (" + user1.getUsername() + ")");
        }
        if (user2 != null) {
            userDAO.updateUserStatus(user2.getUsername(), "online");
            System.out.println("✅ Đã update status 'online' cho user " + player2Id + " (" + user2.getUsername() + ")");
        }

        gameEnded = true; // Đánh dấu game đã kết thúc
        timerExecutor.shutdown();
        
        // Clear gameroom reference trong ClientHandler của cả 2 người chơi
        clearGameroomReference(player1Id);
        clearGameroomReference(player2Id);
    }

    public void playerDisconnected(int playerId) {
        int opponentId = playerId == player1Id ? player2Id : player1Id;
        roundWins.put(opponentId, 3); // Đối thủ thắng tất cả
        endGame();
    }
    
    // Xử lý khi người chơi sẵn sàng cho round tiếp theo
    public void playerReady(int playerId) {
        if (gameEnded) {
            System.out.println("⚠️ Game đã kết thúc, bỏ qua playerReady");
            return;
        }
        
        if (!pendingRoundStart) {
            System.out.println("⚠️ Player " + playerId + " ready nhưng không đang chờ round mới");
            return;
        }
        
        readyPlayers.add(playerId);
        System.out.println("✅ Player " + playerId + " đã ready (" + readyPlayers.size() + "/2)");
        
        // Nếu cả 2 đã ready, bắt đầu round ngay
        if (readyPlayers.size() >= 2) {
            System.out.println("🚀 Cả 2 người chơi ready! Bắt đầu round " + currentRound);
            pendingRoundStart = false;
            startRound();
        }
    }
    
    // Helper: Chuyển Set<Integer> word_ids sang String words (phân cách bằng dấu phẩy)
    private String getWordsFromIds(Set<Integer> wordIds) {
        if (wordIds == null || wordIds.isEmpty()) {
            return "";
        }
        
        List<String> words = new ArrayList<>();
        for (Integer wordId : wordIds) {
            // Lấy word từ word_id
            String wordIdsStr = String.valueOf(wordId);
            List<Dictionary> dictionaries = dictionaryDAO.findDictionaryFromListDicId(wordIdsStr);
            if (!dictionaries.isEmpty()) {
                words.add(dictionaries.get(0).getWord());
            }
        }
        
        return String.join(",", words);
    }
    
    private String getWordsWithMeanings(Set<Integer> wordIds) {
        if (wordIds == null || wordIds.isEmpty()) {
            return "";
        }
        
        List<String> wordMeaningPairs = new ArrayList<>();
        for (Integer wordId : wordIds) {
            // Lấy word và meaning từ word_id
            String wordIdsStr = String.valueOf(wordId);
            List<Dictionary> dictionaries = dictionaryDAO.findDictionaryFromListDicId(wordIdsStr);
            if (!dictionaries.isEmpty()) {
                Dictionary dict = dictionaries.get(0);
                // Format: word|meaning
                wordMeaningPairs.add(dict.getWord() + "|" + dict.getMeaning());
            }
        }
        
        return String.join(",", wordMeaningPairs);
    }

    private void sendToPlayer(int playerId, Message msg) {
        // Gửi qua ClientHandler
        ClientHandler handler = Server.getUserHandlers().get(playerId);
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }
    
    private void clearGameroomReference(int playerId) {
        // Clear gameroom reference trong ClientHandler
        ClientHandler handler = Server.getUserHandlers().get(playerId);
        if (handler != null) {
            handler.clearGameroom();
        }
    }

    public int getMatchId() {
        return matchId;
    }
    
    // Xử lý khi người chơi forfeit (đầu hàng/thoát trận)
    public void handleForfeit(int forfeiterId) {
        System.out.println("🏳️ [Gameroom] Player " + forfeiterId + " đã forfeit!");
        
        // Đánh dấu game đã kết thúc ngay lập tức
        gameEnded = true;
        
        // DỬNG TẤT CẢ TIMER NGAY LẬP TỨC
        if (timerExecutor != null && !timerExecutor.isShutdown()) {
            timerExecutor.shutdownNow();
            System.out.println("⏹️ Đã dừng tất cả timer");
        }
        
        // Xác định người thắng (người còn lại)
        int winnerId = (forfeiterId == player1Id) ? player2Id : player1Id;
        String result = (winnerId == player1Id) ? "player1_win" : "player2_win";
        
        System.out.println("   → Người thắng: " + winnerId + ", Result: " + result);
        
        // Forfeit = tự động thua 0-3, người thắng được 3 rounds, draw = 0
        int p1Wins = (winnerId == player1Id) ? 3 : 0;
        int p2Wins = (winnerId == player2Id) ? 3 : 0;
        int draws = 0;
        
        System.out.println("   → Tỉ số forfeit: P1=" + p1Wins + ", P2=" + p2Wins + ", Draw=" + draws);
        
        // Lưu vào database
        if (matchId > 0) {
            matchesDAO.updateResult(matchId, winnerId, result, p1Wins, p2Wins, draws);
            System.out.println("💾 [DB] Đã cập nhật match result - Forfeit 3-0");
            
            // Cập nhật stats cho người chơi
            String p1Result = (winnerId == player1Id) ? "win" : "lose";
            String p2Result = (winnerId == player2Id) ? "win" : "lose";
            
            userDAO.updateUserStats(player1Id, p1Result);
            userDAO.updateUserStats(player2Id, p2Result);
            System.out.println("📊 Đã cập nhật stats - Forfeit: P1=" + p1Result + ", P2=" + p2Result);
        }
        
        // Cập nhật trạng thái người chơi về online
        String player1Username = userDAO.getUsernameById(player1Id);
        String player2Username = userDAO.getUsernameById(player2Id);
        
        if (player1Username != null) {
            userDAO.updateUserStatus(player1Username, "online");
        }
        if (player2Username != null) {
            userDAO.updateUserStatus(player2Username, "online");
        }
        
        // Gửi thông báo cho người còn lại (người thắng)
        int opponentId = (forfeiterId == player1Id) ? player2Id : player1Id;
        sendToPlayer(opponentId, new Message(Protocol.OPPONENT_FORFEITED, ""));
        
        System.out.println("✅ Đã xử lý forfeit - Gửi OPPONENT_FORFEITED cho player " + opponentId);
        
        // Clear gameroom reference trong ClientHandler của cả 2 người chơi
        clearGameroomReference(player1Id);
        clearGameroomReference(player2Id);
        
        // Xóa game room khỏi Server
        Server.getGamerooms().remove(matchId);
        System.out.println("🗑️ Đã xóa gameroom với matchId: " + matchId);
    }
    
    /**
     * Broadcast emote from sender to opponent
     */
    public void broadcastEmote(int senderId, String iconFileName) {
        System.out.println("😊 [Gameroom] Broadcasting emote '" + iconFileName + "' from player " + senderId);
        
        // Determine opponent ID
        int opponentId = (senderId == player1Id) ? player2Id : player1Id;
        
        // Send emote to opponent
        sendToPlayer(opponentId, new Message(Protocol.RECEIVE_EMOTE, iconFileName));
        
        System.out.println("📤 Sent emote to player " + opponentId);
    }
}

