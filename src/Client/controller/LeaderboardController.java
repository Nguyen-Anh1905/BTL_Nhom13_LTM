package Client.controller;

import Client.Client;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import Server.model.Users;

public class LeaderboardController implements Initializable {

    private Client client;

    @FXML
    private ComboBox<String> cmbSortBy;
    @FXML
    private Label lblLastUpdate;
    @FXML
    private Label lblYourRank;
    @FXML
    private Label lblYourStats;
    
    @FXML
    private TextField txtSearch;
    

    @FXML
    private TableView<Users> tblLeaderboard;
    @FXML
    private TableColumn<Users, Integer> colRank;
    @FXML
    private TableColumn<Users, String> colUsername;
    @FXML
    private TableColumn<Users, Integer> colTotalPoints;
    @FXML
    private TableColumn<Users, Integer> colWins;
    @FXML
    private TableColumn<Users, Integer> colDraws;
    @FXML
    private TableColumn<Users, Integer> colLosses;
    @FXML
    private TableColumn<Users, String> colWinRate;

    private final ObservableList<Users> masterData = FXCollections.observableArrayList();
    private FilteredList<Users> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup columns
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colTotalPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        colWins.setCellValueFactory(new PropertyValueFactory<>("totalWins"));
        colDraws.setCellValueFactory(new PropertyValueFactory<>("totalDraws"));
        colLosses.setCellValueFactory(new PropertyValueFactory<>("totalLosses"));

        // Rank column: show current index + 1
        colRank.setCellFactory(col -> new TableCell<Users, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    int rowIndex = getIndex() + 1;
                    setText(String.valueOf(rowIndex));
                }
            }
        });

        // Win rate column: compute from wins/draws/losses
        colWinRate.setCellFactory(col -> new TableCell<Users, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Users u = getTableView().getItems().get(getIndex());
                    int wins = u.getTotalWins();
                    int draws = u.getTotalDraws();
                    int losses = u.getTotalLosses();
                    int total = wins + draws + losses;
                    String rate = total == 0 ? "0%" : String.format("%.0f%%", (wins * 100.0) / total);
                    setText(rate);
                }
            }
        });

        // Setup filtered + sorted list
        filteredData = new FilteredList<>(masterData, p -> true);
        SortedList<Users> sorted = new SortedList<>(filteredData);
        sorted.comparatorProperty().bind(tblLeaderboard.comparatorProperty());
        tblLeaderboard.setItems(sorted);
    }

    // Called by FXML onKeyReleased for live search
    @FXML
    private void handleSearch(KeyEvent event) {
        String q = txtSearch.getText();
        if (q == null || q.isEmpty()) {
            filteredData.setPredicate(u -> true);
        } else {
            String lower = q.toLowerCase();
            filteredData.setPredicate(u -> u.getUsername() != null && u.getUsername().toLowerCase().contains(lower));
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        // Ask server for default leaderboard (by points)
        if (client != null) {
            client.sendMessage(new common.Message(common.Protocol.GET_LEADERBOARD_POINTS, null));
        }
    }

    @FXML
    private void handleSearchButton(ActionEvent event) {
        // (search handled in Lobby) - placeholder
    }

    @FXML
    private void handleBack(ActionEvent event) {
        // Close or navigate back - left as placeholder
        System.out.println("Quay lại từ Leaderboard (chưa triển khai điều hướng)");
    }

    @FXML
    private void handleSortByPoints(ActionEvent event) {
        if (client != null) {
            client.sendMessage(new common.Message(common.Protocol.GET_LEADERBOARD_POINTS, null));
        }
    }

    @FXML
    private void handleSortByWins(ActionEvent event) {
        if (client != null) {
            client.sendMessage(new common.Message(common.Protocol.GET_LEADERBOARD_WINS, null));
        }
    }

    // Allow lobby/message handler to set client so controller can request data
    public void setClient(Client client) {
        this.client = client;
    }

    // Update the leaderboard from outside (e.g., MessageHandler)
    public void updateLeaderboard(List<Users> list) {
        masterData.clear();
        if (list != null) {
            masterData.addAll(list);
            if (lblLastUpdate != null) {
                lblLastUpdate.setText("Cập nhật: " + java.time.LocalTime.now().withNano(0).toString());
            }
        }
    }
}
