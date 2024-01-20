package com.pz.networking.packets;

import java.io.Serializable;

/**
 * A serializable class representing the endgame package containing collected items information.
 */
public class EndgamePackage implements Serializable {

    public int collectedItems;

    /**
     * Constructs a new endgame package with the given collected items count.
     *
     * @param collectedItems the number of collected items
     */
    public EndgamePackage(int collectedItems) {
        this.collectedItems = collectedItems;
    }
}