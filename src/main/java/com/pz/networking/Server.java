package com.pz.networking;

import com.pz.database.Database;
import com.pz.networking.packets.StartPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Server class represents the game server that handles connections and game logic.
 */
public abstract class Server {
    /**
     * The main method of the Server class. It starts the server.
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        try {
            Server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int PORT = 8080;
    public static final String HOST = "localhost";

    private static boolean running = false;

    /**
     * Returns whether the server is currently running.
     *
     * @return true if the server is running, false otherwise
     */
    public static boolean isRunning() {
        return running;
    }

    private static ServerSocket serverSocket;
    protected static ClientHandler player1;
    protected static ClientHandler player2;

    private static ExecutorService executor;        // servis koji obradjuje operacije na serveru

    /**
     * Starts the server, connecting to the database and accepting player connections.
     *
     * @throws IOException if the server fails to start
     */
    public static void start() throws IOException {
        if (isRunning()) return;

        Database.connect();
        System.out.println("Starting server on port " + PORT);
        Database.pushServerEvent("Starting server on port " + PORT);
        serverSocket = new ServerSocket(PORT);

        if (serverSocket.isClosed()) {
            System.err.println("Could not start server");
            Database.pushServerEvent("Could not start server");
            throw new IOException("Could not start server");
        }
        System.out.println("Server started");
        Database.pushServerEvent("Server started");
        running = true;

        System.out.println("Waiting for players...");
        Database.pushServerEvent("Waiting for players...");
        executor = Executors.newFixedThreadPool(2);

        Socket player1 = serverSocket.accept();
        System.out.println("Player 1 connected");
        Database.pushServerEvent("Player 1 connected");
        executor.submit(Server.player1 = new ClientHandler(player1));

        Socket player2 = serverSocket.accept();
        System.out.println("Player 2 connected");
        Database.pushServerEvent("Player 2 connected");
        executor.submit(Server.player2 = new ClientHandler(player2));

        System.out.println("Starting game");
        Database.pushServerEvent("Starting game");
        final StartPackage startPackage = new StartPackage();
        startPackage.leftSpawn = true;
        Server.player1.send(startPackage);
        System.out.println("Package sent to player 1");
        Database.pushServerEvent("Package sent to player 1");
        startPackage.leftSpawn = false;
        Server.player2.send(startPackage);
        System.out.println("Package sent to player 2");
        Database.pushServerEvent("Package sent to player 2");

        if (Database.isConnected()) {
            System.out.println("Creating game...");
            Database.pushServerEvent("Creating game...");
            int gameID = Database.createGame(Server.player1.getPlayerID(), Server.player2.getPlayerID());
            Server.player1.setGameID(gameID);
            Server.player2.setGameID(gameID);
            System.out.println("Game created");
            Database.pushServerEvent("Game created");
        }
    }

    /**
     * Stops the server, shutting down the executor and disconnecting from the database.
     */
    public static void stop() {
        if (!isRunning()) return;
        if (executor != null) executor.shutdownNow();
        Database.disconnect();
        running = false;
    }
}
