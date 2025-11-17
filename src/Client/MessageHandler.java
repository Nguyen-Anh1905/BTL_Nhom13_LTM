package Client;

import Client.controller.*;
import common.*;
import javafx.application.Platform;
import Server.model.*;
import Server.dto.MatchHistoryResponse;
import java.util.List;

public class MessageHandler {

    private LoginController loginController;
    private RegisterController registerController;
    private LobbyController lobbyController;
    private Client client;

    public MessageHandler() {}

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }

    public void setRegisterController(RegisterController registerController) {
        this.registerController = registerController;
    }

    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public void handleMessage(Message msg) {
        if (msg == null) return;
        System.out.println("Received message: " + msg.getType() + " - " + msg.getContent());

        switch (msg.getType()) {
            case Protocol.LOGIN_SUCCESS:
                if (loginController != null) {
                    Users user = (Users) msg.getContent();
                    loginController.handleServerResponse(msg);
                    Platform.runLater(() -> {
                        try {
                            client.showLobbyUI(client.getPrimaryStage(), user);
                            // SAU KHI CHUYỂN SANG LOBBY, GỬI YÊU CẦU DỮ LIỆU
                            client.sendMessage(new Message(Protocol.GET_PLAYER_LIST, null));
                            System.out.println("Gửi yêu cầu lấy bảng xếp hạng...");
                            client.sendMessage(new Message(Protocol.GET_LEADERBOARD_POINTS, null));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
                break;

            case Protocol.LOGIN_FAILURE:
                if (loginController != null) loginController.handleServerResponse(msg);
                break;

            case Protocol.REGISTER_SUCCESS:
                if (registerController != null) {
                    Users user = (Users) msg.getContent();
                    registerController.handleServerResponse(msg);
                    Platform.runLater(() -> {
                        try {
                            client.showLobbyUI(client.getPrimaryStage(), user);
                            // SAU KHI ĐĂNG KÝ VÀ CHUYỂN SANG LOBBY, GỬI YÊU CẦU DỮ LIỆU
                            System.out.println("Gửi yêu cầu lấy danh sách người chơi...");
                            client.sendMessage(new Message(Protocol.GET_PLAYER_LIST, null));
                            System.out.println("Gửi yêu cầu lấy bảng xếp hạng...");
                            client.sendMessage(new Message(Protocol.GET_LEADERBOARD_POINTS, null));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
                break;

            case Protocol.REGISTER_FAILURE:
                if (registerController != null) registerController.handleServerResponse(msg);
                break;

            case Protocol.PLAYER_LIST:
                System.out.println("Nhận được PLAYER_LIST");
                if (lobbyController != null) {
                    List<Users> onlinePlayers = (List<Users>) msg.getContent();
                    Platform.runLater(() -> lobbyController.updatePlayerList(onlinePlayers));
                }
                break;

            case Protocol.LEADERBOARD_DATA:
                System.out.println("Nhận được LEADERBOARD_DATA");
                List<Users> list = (List<Users>) msg.getContent();
                if (lobbyController != null) {
                    Platform.runLater(() -> lobbyController.updateLeaderboard(list));
                } else {
                    System.out.println("Không có controller nào để nhận LEADERBOARD_DATA");
                }
                break;

            case Protocol.SEARCH_RESULT_LOBBY:
                System.out.println("Nhận được SEARCH_RESULT_LOBBY (Tab 1)");
                List<Users> searchResultLobby = (List<Users>) msg.getContent();
                if (lobbyController != null) Platform.runLater(() -> lobbyController.updatePlayerList(searchResultLobby));
                break;

            case Protocol.SEARCH_RESULT_LEADERBOARD:
                System.out.println("Nhận được SEARCH_RESULT_LEADERBOARD (Tab 3)");
                List<Users> searchResultLeaderboard = (List<Users>) msg.getContent();
                if (lobbyController != null) Platform.runLater(() -> lobbyController.updateLeaderboard(searchResultLeaderboard));
                break;

            case Protocol.MATCH_HISTORY_DATA:
                System.out.println("Nhận được MATCH_HISTORY_DATA");
                List<MatchHistoryResponse> history = (List<MatchHistoryResponse>) msg.getContent();
                if (lobbyController != null) Platform.runLater(() -> lobbyController.updateMatchHistory(history));
                break;

            case Protocol.MATCH_DETAIL_DATA:
                System.out.println("Nhận được MATCH_DETAIL_DATA");
                List<Server.dto.MatchDetailResponse> details = (List<Server.dto.MatchDetailResponse>) msg.getContent();
                if (lobbyController != null) Platform.runLater(() -> lobbyController.showMatchDetail(details));
                break;
                
            case Protocol.CHALLENGE_INVITATION:
                // Nhận lời mời đấu từ user khác
                System.out.println("Nhận được CHALLENGE_INVITATION");
                String inviterInfo = (String) msg.getContent();
                String[] inviterParts = inviterInfo.split(":");
                int inviterUserId = Integer.parseInt(inviterParts[0]);
                String inviterUsername = inviterParts[1];
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.showInviteDialog(inviterUserId, inviterUsername);
                    });
                }
                break;
                
            case Protocol.CHALLENGE_ACCEPTED:
                // Người nhận chấp nhận lời mời
                System.out.println("Nhận được CHALLENGE_ACCEPTED");
                String accepterInfo = (String) msg.getContent();
                String[] accepterParts = accepterInfo.split(":");
                int accepterUserId = Integer.parseInt(accepterParts[0]);
                String accepterUsername = accepterParts[1];
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.onChallengeAccepted(accepterUserId, accepterUsername);
                    });
                }
                break;
                
            case Protocol.CHALLENGE_REJECTED:
                // Người nhận từ chối lời mời
                System.out.println("Nhận được CHALLENGE_REJECTED");
                String rejecterInfo = (String) msg.getContent();
                String[] rejecterParts = rejecterInfo.split(":");
                String rejecterUsername = rejecterParts[1];
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.onChallengeRejected(rejecterUsername);
                    });
                }
                break;
                
            case Protocol.CHALLENGE_CANCELLED:
                // Người mời hủy lời mời
                System.out.println("Nhận được CHALLENGE_CANCELLED");
                String cancellerInfo = (String) msg.getContent();
                String[] cancellerParts = cancellerInfo.split(":");
                String cancellerUsername = cancellerParts[1];
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.onChallengeCancelled(cancellerUsername);
                    });
                }
                break;
                
            case Protocol.CHALLENGE_FAILED:
                // Lời mời thất bại (user offline, etc.)
                System.out.println("Nhận được CHALLENGE_FAILED");
                String errorMessage = (String) msg.getContent();
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.onChallengeFailed(errorMessage);
                    });
                }
                break;
                
            case Protocol.ROUND_START:
                System.out.println("Nhận được ROUND_START: " + msg.getContent());
                // Payload: letterDetail:lengthWord:timeRound
                String[] roundStartParts = ((String) msg.getContent()).split(":");
                String letterDetail = roundStartParts[0];
                int lengthWord = Integer.parseInt(roundStartParts[1]);
                int timeRound = Integer.parseInt(roundStartParts[2]);
                
                System.out.println("  - Letters: " + letterDetail);
                System.out.println("  - Length: " + lengthWord);
                System.out.println("  - Time: " + timeRound);
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        // Chuyển đến GameRoom và setup round
                        lobbyController.startGameRound(letterDetail, lengthWord, timeRound);
                    });
                } else {
                    System.err.println("❌ LobbyController is null!");
                }
                break;
                
            case Protocol.ROUND_RESULT:
                System.out.println("Nhận được ROUND_RESULT");
                // Payload: playerId:correctCount:isValid:meaning
                String[] resultParts = ((String) msg.getContent()).split(":", 4);
                int resPlayerId = Integer.parseInt(resultParts[0]);
                int correctCount = Integer.parseInt(resultParts[1]);
                boolean isValid = Boolean.parseBoolean(resultParts[2]);
                String meaning = resultParts.length > 3 ? resultParts[3] : "";
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.updateRoundResult(resPlayerId, correctCount, isValid, meaning);
                    });
                }
                break;
                
            case Protocol.ROUND_END:
                System.out.println("Nhận được ROUND_END");
                // Payload từ server: winnerId:myCount:oppCount:myWords:oppWords
                // (Server đã swap để mỗi client nhận info của mình ở vị trí đầu)
                String[] endParts = ((String) msg.getContent()).split(":", 5);
                int roundWinnerId = Integer.parseInt(endParts[0]);
                int myCount = Integer.parseInt(endParts[1]);
                int oppCount = Integer.parseInt(endParts[2]);
                String myWords = endParts.length > 3 ? endParts[3] : "";
                String oppWords = endParts.length > 4 ? endParts[4] : "";
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.endRound(roundWinnerId, myCount, oppCount, myWords, oppWords);
                    });
                }
                break;
                
            case Protocol.GAME_END:
                System.out.println("Nhận được GAME_END");
                // Payload: winnerId:roundWinsP1:roundWinsP2
                String[] gameEndParts = ((String) msg.getContent()).split(":");
                int gameWinnerId = Integer.parseInt(gameEndParts[0]);
                int roundWinsP1 = Integer.parseInt(gameEndParts[1]);
                int roundWinsP2 = Integer.parseInt(gameEndParts[2]);
                
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.endGame(gameWinnerId, roundWinsP1, roundWinsP2);
                    });
                }
                break;
                
            case Protocol.OPPONENT_FORFEITED:
                System.out.println("Nhận được OPPONENT_FORFEITED - Đối thủ đã thoát trận");
                if (lobbyController != null) {
                    Platform.runLater(() -> {
                        lobbyController.onOpponentForfeited();
                    });
                }
                break;
                
            case Protocol.RECEIVE_EMOTE:
                System.out.println("📥 Nhận emote từ đối thủ: " + msg.getContent());
                if (lobbyController != null) {
                    lobbyController.handleOpponentEmote((String) msg.getContent());
                }
                break;
                
            // Xử lý các loại message khác nếu cần
            default:
                System.out.println("Unhandled message type: " + msg.getType());
                break;
        }
    }
}

