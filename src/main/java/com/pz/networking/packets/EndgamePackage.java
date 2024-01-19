package com.pz.networking.packets;

import java.io.Serializable;

public class EndgamePackage implements Serializable {

    public int collectedItems;

    public EndgamePackage(int collectedItems) {
        this.collectedItems = collectedItems;
    }
}