package com.pz.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.security.PublicKey;

public class Entity extends ImageView {
    private static final int ENTITY_WIDTH = 32;
    private static final int ENTITY_HEIGHT = 32;

    private Physics.CollisionType collisionType = Physics.CollisionType.NONE;

    public Entity() {
        super();
        setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        setX(0);
        setY(0);
    }

    public Entity(final Image image) {
        super(image);
        setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        setX(0);
        setY(0);
    }

    public Entity(final Image image, final double x, final double y) {
        super(image);
        setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        setX(0);
        setY(0);
        setPosition(x, y);
    }

    public Entity(final Image image, final double x, final double y, final double width, final double height) {
        this(image, x, y);
        setSize(width, height);
    }

    public Entity(final Image image, final double x, final double y, final double width, final double height, final double opacity) {
        this(image, x, y, width, height);
        setOpacity(opacity);
    }

    public Entity(final Physics.CollisionType collisionType, final Image image, final double x, final double y, final double width, final double height, final double opacity) {
        this(image, x, y, width, height, opacity);
        setCollisionType(collisionType);
        setSize(width, height);
    }

    public void setSize(final double width, final double height) {
        setFitWidth(width);
        setFitHeight(height);
    }

    public void setPosition(final double x, final double y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    public void setCollisionType(final Physics.CollisionType collisionType) {
        this.collisionType = collisionType;
        if (this.collisionType != Physics.CollisionType.NONE)
            Physics.addEntity(this);
        else
            Physics.removeEntity(this);
    }

    public Physics.CollisionType getCollisionType() {
        return collisionType;
    }

    public void update() {
    }

    public void collide(Entity b) {
    }
}