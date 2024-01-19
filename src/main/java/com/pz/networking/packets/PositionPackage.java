package com.pz.networking.packets;

import javafx.scene.image.ImageView;

import java.io.Serializable;

public class PositionPackage implements Serializable {
    public double x;
    public double y;

    public void set(final ImageView image) {
        this.x = image.getTranslateX();
        this.y = image.getTranslateY();
    }
}
