package com.pz.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.security.PublicKey;

/**
 * A base class for game entities, representing objects in the game world.
 */
public class Entity extends ImageView {
    private static final int ENTITY_WIDTH = 32;
    private static final int ENTITY_HEIGHT = 32;

    private Physics.CollisionType collisionType = Physics.CollisionType.NONE;

    /**
     * Constructs a new entity with default settings.
     */
    public Entity() {
        super();
        setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        setX(0);
        setY(0);
    }

    /**
     * Constructs a new entity with the specified image.
     *
     * @param image the image to set for the entity
     */
    public Entity(final Image image) {
        super(image);
        setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        setX(0);
        setY(0);
    }

    /**
     * Constructs a new entity with the specified image and position.
     *
     * @param image the image to set for the entity
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    public Entity(final Image image, final double x, final double y) {
        super(image);
        setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        setX(0);
        setY(0);
        setPosition(x, y);
    }

    /**
     * Constructs a new entity with the specified image and position.
     *
     * @param image the image to set for the entity
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    public Entity(final Image image, final double x, final double y, final double width, final double height) {
        this(image, x, y);
        setSize(width, height);
    }

    /**
     * Constructs a new entity with the specified image and position.
     *
     * @param image the image to set for the entity
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    public Entity(final Image image, final double x, final double y, final double width, final double height, final double opacity) {
        this(image, x, y, width, height);
        setOpacity(opacity);
    }

    /**
     * Constructs a new entity with the specified image and position.
     *
     * @param image the image to set for the entity
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    public Entity(final Physics.CollisionType collisionType, final Image image, final double x, final double y, final double width, final double height, final double opacity) {
        this(image, x, y, width, height, opacity);
        setCollisionType(collisionType);
        setSize(width, height);
    }

    /**
     *  Sets the size of the entity.
     * @param width parameter
     * @param height parameter
     */
    public void setSize(final double width, final double height) {
        setFitWidth(width);
        setFitHeight(height);
    }

    /**
     *  Sets the position of the entity.
     * @param x parameter
     * @param y parameter
     */
    public void setPosition(final double x, final double y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Sets the collision type for the entity.
     *
     * @param collisionType the collision type to set
     */
    public void setCollisionType(final Physics.CollisionType collisionType) {
        this.collisionType = collisionType;
        if (this.collisionType != Physics.CollisionType.NONE)
            Physics.addEntity(this);
        else
            Physics.removeEntity(this);
    }

    /**
     * Retrieves the collision type of the entity.
     *
     * @return the collision type of the entity
     */
    public Physics.CollisionType getCollisionType() {
        return collisionType;
    }

    /**
     * Updates the entity.
     */
    public void update() {
    }

    /**
     * Handles collision with another entity.
     *
     * @param b the entity to collide with
     */
    public void collide(Entity b) {
    }
}