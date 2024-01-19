package com.pz.game;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Animation {
    private final Entity parent;
    private int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    private int lastIndex;

    public Animation(
            Entity parent,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height) {
        this.parent = parent;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    public final Map<String, Pair<Image, Integer>> maps = new HashMap<>();

    private String currentAnim = "";

    public void play(String name) {
        if (currentAnim.equals(name)) return;
        count = maps.get(name).getValue();
        parent.setViewport(new Rectangle2D(offsetX + (lastIndex % columns) * width, offsetY + (lastIndex / columns) * height, width, height));
        parent.setImage(maps.get(name).getKey());
        lastIndex = (lastIndex + 1) % count;
        currentAnim = name;
    }

    public void update(ActionEvent actionEvent) {
        lastIndex = (lastIndex + 1) % count;
        parent.setViewport(new Rectangle2D(lastIndex * width, 0, width, height));
    }
}
