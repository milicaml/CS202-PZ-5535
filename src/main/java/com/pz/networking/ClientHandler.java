package com.pz.networking;

import com.pz.App;
import com.pz.database.Database;
import com.pz.game.Player;
import com.pz.networking.packets.EndgamePackage;
import com.pz.networking.packets.InfoPackage;
import com.pz.networking.packets.PositionPackage;
import com.pz.networking.packets.StartPackage;
import com.pz.scenes.GameScene;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    private final boolean onServer;

    private String username;

    public String getUsername() {
        return username;
    }

    private int playerID;

    public int getPlayerID() {
        return playerID;
    }

    private int score = -1;

    public int getScore() {
        return score;
    }

    private int gameID = 0;

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public ClientHandler(Socket socket) {
        try {
            onServer = true;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientHandler(Socket socket, boolean onServer) {
        try {
            this.onServer = onServer;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Object object) {
        try {
            outputStream.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object receive() {
        try {
            return inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            Object object = receive();
            if (onServer) handleServerPackage(object);
            else handleClientPackage(object);
            if (object == null)
                break;
        }
    }

    private void handleServerPackage(Object object) {
// When server receives package from client
        if (object instanceof PositionPackage positionPackage) {
            if (Server.player1 == this)
                Server.player2.send(positionPackage);
            else Server.player1.send(positionPackage);
        } else if (object instanceof InfoPackage infoPackage) {
            this.username = infoPackage.username;
            this.playerID = infoPackage.playerID;
        } else if (object instanceof EndgamePackage endgamePackage) {
            this.score = endgamePackage.collectedItems;

            ClientHandler otherPlayer = Server.player1 == this ? Server.player2 : Server.player1;
            if (otherPlayer.score != -1) {
                Database.endGame(
                        Server.player1.getScore(),
                        Server.player2.getScore(),
                        Server.player1.getScore() > Server.player2.getScore() ?
                                Server.player1.getPlayerID() : Server.player2.getPlayerID(),
                        getGameID()
                );
            }
        }
    }

    private void handleClientPackage(Object object) {
// When client receives package from server
        if (object instanceof StartPackage startPackage) {
            Player.leftSpawn = startPackage.leftSpawn;
            Platform.runLater(() -> {
                App.changeScene(GameScene.INSTANCE);
            });
        } else if (object instanceof PositionPackage positionPackage) {
            Platform.runLater(() -> {
                Player.player2.setTranslateX(positionPackage.x);
                Player.player2.setTranslateY(positionPackage.y);
            });
        }
    }
}