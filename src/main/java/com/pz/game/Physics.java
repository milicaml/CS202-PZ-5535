package com.pz.game;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

import java.util.ArrayList;
import java.util.List;

public abstract class Physics {
    public static final double GRAVITY = 9.81 * 0.3;

    public enum CollisionType {
        NONE(0), DISCRETE(1), CONTINUOUS(2);

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
                case 1 -> DISCRETE;
                case 2 -> CONTINUOUS;
                default -> throw new IllegalArgumentException("Invalid value: " + value);
            };
        }
    }

    private static final List<Entity> entities = new ArrayList<>();

    public static void addEntity(Entity entity) {
        entities.add(entity);
    }

    public static void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public static void handleCollision() {
        for (int i = 0; i < entities.size() - 1; i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                if (checkCollision(entities.get(i), entities.get(j))) {
                    final Entity a = entities.get(i);
                    final Entity b = entities.get(j);

                    a.collide(b);
                    if(a.getCollisionType() == CollisionType.NONE)
                        continue;
                    b.collide(a);
                    if(b.getCollisionType() == CollisionType.NONE)
                        continue;
                    Entity temp = a.getCollisionType() == CollisionType.CONTINUOUS ? a : b;
                    resolveCollision(temp, temp == a ? b : a);
                }
            }
        }
    }

    private static boolean checkCollision(final Entity a, final Entity b) {
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

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