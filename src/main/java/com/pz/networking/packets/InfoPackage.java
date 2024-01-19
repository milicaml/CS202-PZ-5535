package com.pz.networking.packets;

import java.io.Serializable;

public class InfoPackage implements Serializable {
    public String username;
    public int playerID;

    public InfoPackage(String username, int playerID) {
        this.username = username;
        this.playerID = playerID;
    }
}
