package com.pz.networking.packets;

import java.io.Serializable;

/**
 * A serializable class representing an information package containing username and player ID.
 */
public class InfoPackage implements Serializable {
    public String username;
    public int playerID;

    /**
     * Constructs a new information package with the given username and player ID.
     *
     * @param username the username
     * @param playerID the player ID
     */
    public InfoPackage(String username, int playerID) {
        this.username = username;
        this.playerID = playerID;
    }
}
