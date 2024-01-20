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

/**
 * A class representing a client handler for networking communication.
 */
public class ClientHandler implements Runnable {
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    private final boolean onServer;     //da li obradjuje operacije na serveru ili klijentu

    private String username;

    /**
     * Gets the username associated with the client handler.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    private int playerID;

    /**
     * Gets the player ID associated with the client handler.
     *
     * @return the player ID
     */
    public int getPlayerID() {
        return playerID;
    }

    private int score = -1;

    /**
     * Gets the score associated with the client handler.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    private int gameID = 0;

    /**
     * Gets the game ID associated with the client handler.
     *
     * @return the game ID
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Sets the game ID associated with the client handler.
     *
     * @param gameID the game ID to set
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Constructs a client handler with the specified socket.
     *
     * @param socket the socket to use
     */
    public ClientHandler(Socket socket) {
        try {
            onServer = true;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a client handler with the specified socket and server flag.
     *
     * @param socket   the socket to use
     * @param onServer true if the client is on the server, false otherwise
     */
    public ClientHandler(Socket socket, boolean onServer) {
        try {
            this.onServer = onServer;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an object through the client handler's output stream.
     *
     * @param object the object to send
     */
    public void send(Object object) {
        try {
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Receives an object from the client handler's input stream.
     *
     * @return the received object
     */
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
            System.out.println("Updated player info");
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