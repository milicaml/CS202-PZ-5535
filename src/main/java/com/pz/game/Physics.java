package com.pz.game;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the physics engine for the game.
 */
public abstract class Physics {
    public static final double GRAVITY = 9.81 * 0.3;

    public enum CollisionType {
        NONE(0), STATIC(1), DYNAMIC(2);

        private final int value;

        CollisionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static CollisionType fromValue(int value) {
            return switch (value) {
                case 0 -> NONE;
                case 1 -> STATIC;
                case 2 -> DYNAMIC;
                default -> throw new IllegalArgumentException("Invalid value: " + value);
            };
        }
    }

    private static final List<Entity> entities = new ArrayList<>();

    /**
     * Adds an entity to the list of entities for collision detection.
     *
     * @param entity the entity to add
     */
    public static void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Removes an entity from the list of entities for collision detection.
     *
     * @param entity the entity to remove
     */
    public static void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Handles collision between entities.
     */
    public static void handleCollision() {
        for (int i = 0; i < entities.size() - 1; i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                if (checkCollision(entities.get(i), entities.get(j))) {
                    final Entity a = entities.get(i);
                    final Entity b = entities.get(j);

                    a.collide(b);
                    if(a.getCollisionType() == CollisionType.NONE)      // provera da li je entitt (bananica) obrisan
                        continue;
                    b.collide(a);
                    if(b.getCollisionType() == CollisionType.NONE)
                        continue;
                    Entity temp = a.getCollisionType() == CollisionType.DYNAMIC ? a : b;        // uzimamo dynamic entity
                    resolveCollision(temp, temp == a ? b : a);          // dodeljujemo prvi argument dynamic drugi static
                }
            }
        }
    }

    private static boolean checkCollision(final Entity a, final Entity b) {
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

    /**
     * Resolves the collision between two entities.
     *
     * @param a the first entity involved in the collision
     * @param b the second entity involved in the collision
     */
    protected static void resolveCollision(final Entity a, final Entity b) {
        double overlapX = Math.min(a.getBoundsInParent().getMaxX() - b.getBoundsInParent().getMinX(),
                b.getBoundsInParent().getMaxX() - a.getBoundsInParent().getMinX());
        double overlapY = Math.min(a.getBoundsInParent().getMaxY() - b.getBoundsInParent().getMinY(),
                b.getBoundsInParent().getMaxY() - a.getBoundsInParent().getMinY());

        if (Math.abs(overlapX) < Math.abs(overlapY)) {
            double moveX = overlapX / 2;
            a.setTranslateX(a.getTranslateX() - moveX);
        } else {
            double moveY = overlapY / 2;
            a.setTranslateY(a.getTranslateY() - moveY);
        }
    }
}