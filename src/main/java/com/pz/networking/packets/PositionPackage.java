package com.pz.networking.packets;

import javafx.scene.image.ImageView;

import java.io.Serializable;

/**
 * A serializable class representing a position package containing x and y coordinates.
 */
public class PositionPackage implements Serializable {
    public double x;
    public double y;

    /**
     * Sets the position package based on the provided ImageView's translation coordinates.
     *
     * @param image the ImageView from which to set the position
     */
    public void set(final ImageView image) {        // entity nasledjuje klasu ImageView
        this.x = image.getTranslateX();
        this.y = image.getTranslateY();
    }
}
