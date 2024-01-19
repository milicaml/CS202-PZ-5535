package com.pz.scenes;

import com.pz.App;
import com.pz.database.Database;
import com.pz.database.LeaderboardItem;
import com.pz.networking.Client;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class MenuScene extends CustomScene<VBox> {
    public static final MenuScene INSTANCE = new MenuScene();

    public MenuScene() {
        super(new VBox());
    }

    Label status;
    List<Button> buttons = new ArrayList<>();
    TableView<LeaderboardItem> leaderboardView;

    @Override
    void initialize() {
        status = new Label("Status");
        status.setVisible(false);
        leaderboardView = new TableView<>();

        TableColumn<LeaderboardItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().getUsername());

        TableColumn<LeaderboardItem, Integer> winsCol = new TableColumn<>("Wins");
        winsCol.setCellValueFactory(cellData -> cellData.getValue().getScore().asObject());

        leaderboardView.getColumns().addAll(nameCol, winsCol);
        leaderboardView.setItems(FXCollections.observableArrayList());
        leaderboardView.setTableMenuButtonVisible(false);
        root.getChildren().addAll(
                new Label("Menu") {{
                    setStyle("-fx-font-size: 50px");
                }},
                leaderboardView,
                status
        );
    }

    @Override
    public void reset() {
        for (Button button : buttons)
            root.getChildren().remove(button);

        buttons.clear();

        Database.updateLeaderboard(leaderboardView);

        List<Pair<String, Integer>> maps = Database.getMaps();
        for (Pair<String, Integer> pair : maps) {
            Button button = new Button();
            button.setText(pair.getKey());

            button.setOnAction(event -> {
                status.setVisible(true);
                status.setText("Waiting for opponent...");
                for (Button b : buttons)
                    b.setDisable(true);
                GameScene.INSTANCE.setMapID(pair.getValue());
                Client.connect();
            });
            buttons.add(button);
            root.getChildren().add(button);
        }
    }
}