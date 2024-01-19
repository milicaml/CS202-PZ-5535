package com.pz.networking;

import com.pz.database.Database;
import com.pz.networking.packets.InfoPackage;

import java.io.IOException;
import java.net.Socket;

public abstract class Client {
    private static String localUsername;

    public static String getLocalUsername() {
        return localUsername;
    }

    public static void setLocalUsername(String username) {
        localUsername = username;
    }

    private static boolean connected = false;

    public static boolean isConnected() {
        return connected;
    }

    private static Socket socket;
    private static ClientHandler handler;

    public static ClientHandler getHandler() {
        return handler;
    }

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

    public static void disconnect() {
        connected = false;
    }
}
