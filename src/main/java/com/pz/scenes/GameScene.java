package com.pz.scenes;

import com.pz.App;
import com.pz.database.Database;
import com.pz.game.Banana;
import com.pz.game.Entity;
import com.pz.game.Physics;
import com.pz.game.Player;
import com.pz.networking.Client;
import com.pz.networking.packets.EndgamePackage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameScene extends CustomScene<Pane> {
    public static final GameScene INSTANCE = new GameScene();

    public GameScene() {
        super(new Pane());
    }

    Timeline frameTimeline;

    @Override
    void initialize() {
        frameTimeline = new Timeline(
                new KeyFrame(Duration.millis(10), this::update)
        );

        super.setOnKeyPressed(keyEvent -> handleKeyPress(keyEvent.getCode()));
        super.setOnKeyReleased(keyEvent -> handleKeyRelease(keyEvent.getCode()));

        root.getChildren().addAll(
                new Label("Game")
        );

        frameTimeline.setCycleCount(Animation.INDEFINITE);
        frameTimeline.playFromStart();
        timeStarted = System.currentTimeMillis();
    }

    private long timeStarted;
    private long timeElapsed;
    public static final double GAME_TIME = 10; // TODO : PODESITI REALNU VREDNOST

    private void update(ActionEvent actionEvent) {
        timeElapsed = System.currentTimeMillis() - timeStarted;
        if (timeElapsed > GAME_TIME * 1000) {
            frameTimeline.stop();
            Platform.runLater(() -> {
                Client.getHandler().send(new EndgamePackage(Player.player1.getCollectedItems()));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("Time's up!");
                alert.setContentText("You collected " + Player.player1.getCollectedItems() + " bananas.");
                alert.showAndWait();

                App.changeScene(MenuScene.INSTANCE);
            });
            return;
        }

        Physics.handleCollision();

        for (Entity entity : entities)
            entity.update();
    }

    @Override
    public void reset() {
        loadScene();
    }

    private int mapID = 1;

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public int getMapID() {
        return mapID;
    }

    private final List<Entity> entities = new ArrayList<>();

    private void loadScene() {
        entities.clear();

        entities.addAll(Database.getMapEntities(getMapID()));

        for (Entity entity : entities)
            root.getChildren().add(entity);

        root.getChildren().remove(Player.player1);
        root.getChildren().remove(Player.player2);

        Player.player1.setTranslateX(Player.leftSpawn ? 50 : 800 - (50 + Player.player1.getFitWidth()));
        Player.player2.setTranslateX(!Player.leftSpawn ? 50 : 800 - (50 + Player.player2.getFitWidth()));

        entities.add(Player.player1);
        entities.add(Player.player2);

        root.getChildren().addAll(Player.player1, Player.player2);

        int[][] bananas = new int[][]{
                {293, 300},
                {578,287-60},
                {429+71,341-60},
                {-26+40,275-60},
                {155+18,303-60},
                {715+50,338-60},
                {-7+50,371-60},
                {147+85,455-60},
                {697+60,402-60},
                {605+53,446-60},
                {39+22,133-60},
                {596+100,120-60},
                {243+65,206-60}
        };
        for (int[] banana : bananas) {
            Banana b = new Banana(banana[0], banana[1]);
            entities.add(b);
            root.getChildren().add(b);
        }
    }

    Input mainInput = new Input();

    static class Input {
        protected boolean w = false, a = false, s = false, d = false;
        protected boolean up = false, down = false, left = false, right = false;

        public boolean isMoving() {
            return w || a || s || d;
        }

        public double getVelocityX() {
            return a ? -1 : (d ? 1 : 0);
        }

        public double getAltVelocityX() {
            return left ? -1 : (right ? 1 : 0);
        }

        public double getVelocityY() {
            return w ? -1 : (s ? 1 : 0);
        }

        public double getAltVelocityY() {
            return up ? -1 : (down ? 1 : 0);
        }
    }

    private void handleKeyPress(KeyCode code) {
        switch (code) {
            case A -> mainInput.a = true;
            case S -> mainInput.s = true;
            case D -> mainInput.d = true;
            case W -> Player.player1.jump();
            case LEFT -> mainInput.left = true;
            case RIGHT -> mainInput.right = true;
            case DOWN -> mainInput.down = true;
            case UP -> {
                if (Player.GAME_TYPE == Player.GameType.SINGLE_PLAYER)
                    Player.player2.jump();
            }
        }
        Player.player1.setVelocity(mainInput.getVelocityX(), mainInput.getVelocityY());
        if (Player.GAME_TYPE == Player.GameType.SINGLE_PLAYER)
            Player.player2.setVelocity(mainInput.getAltVelocityX(), mainInput.getAltVelocityY());
    }

    private void handleKeyRelease(KeyCode code) {
        switch (code) {
            case A -> mainInput.a = false;
            case S -> mainInput.s = false;
            case D -> mainInput.d = false;
            case LEFT -> mainInput.left = false;
            case RIGHT -> mainInput.right = false;
            case DOWN -> mainInput.down = false;
        }
        Player.player1.setVelocity(mainInput.getVelocityX(), mainInput.getVelocityY());
        if (Player.GAME_TYPE == Player.GameType.SINGLE_PLAYER)
            Player.player2.setVelocity(mainInput.getAltVelocityX(), mainInput.getAltVelocityY());
    }
}