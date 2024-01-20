package com.pz.networking;

import com.pz.database.Database;
import com.pz.networking.packets.InfoPackage;

import java.io.IOException;
import java.net.Socket;

/**
 * The abstract base class for a networking client.
 */
public abstract class Client {
    private static String localUsername;

    /**
     * Gets the local username.
     *
     * @return the local username
     */
    public static String getLocalUsername() {
        return localUsername;
    }

    /**
     * Sets the local username.
     *
     * @param username the local username to set
     */
    public static void setLocalUsername(String username) {
        localUsername = username;
    }

    private static boolean connected = false;

    /**
     * Checks if the client is connected to the server.
     *
     * @return true if the client is connected, false otherwise
     */
    public static boolean isConnected() {
        return connected;
    }

    private static Socket socket;
    private static ClientHandler handler;

    /**
     * Gets the client handler.
     *
     * @return the client handler
     */
    public static ClientHandler getHandler() {
        return handler;
    }

    /**
     * Connects the client to the server.
     */
    public static void connect() {
        if (isConnected()) return;

        try {
            socket = new Socket(Server.HOST, Server.PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        connected = true;

        handler = new ClientHandler(socket, false);
        handler.send(new InfoPackage(localUsername, Database.getLocalPlayerID()));
        new Thread(handler).start();
    }

    /**
     * Disconnects the client from the server.
     */
    public static void disconnect() {
        connected = false;
    }
}
