package com.pz.game;

import com.pz.networking.Client;
import com.pz.networking.packets.PositionPackage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * A class representing the player in the game.
 */

public class Player extends Entity {
    public static boolean leftSpawn = true;
    public static final Player player1 = new Player();
    public static final Player player2 = new Player();


    /**
     * Constructs a new player with default settings.
     */
    public Player() {
        super(Physics.CollisionType.DYNAMIC, Assets.getImage("Run (32x32)"), 0, 0, 50, 50, 1.0);
        animation = new Animation(this,
                Duration.seconds(0.1),
                12, 4,
                0, 0,
                32, 32);
        animation.maps.put("run", new Pair<>(Assets.getImage("Run (32x32)"), 12));
        animation.maps.put("idle", new Pair<>(Assets.getImage("Idle (32x32)"), 11));
        animation.maps.put("jump", new Pair<>(Assets.getImage("Jump (32x32)"), 1));
        animation.play("idle");
        final Timeline frameTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), (ActionEvent actionEvent) -> animation.update(actionEvent))
        );
        frameTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        frameTimeline.playFromStart();
    }

    private Animation animation;
    public static final double SPEED = 5;
    public static final double JUMP_STRENGTH = 30;
    private final double[] velocity = new double[]{0, 0};

    /**
     * Gets the current velocity of the player.
     *
     * @return an array containing the x and y velocity components
     */

    public double[] getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity of the player.
     *
     * @param x the x velocity component
     * @param y the y velocity component
     */
    public void setVelocity(final double x, final double y) {
        velocity[0] = x;
        setScaleX(x < 0 ? -1 : 1);              // -1 flipuje sliku na drugu stranu
    }

    public final boolean inAir() {
        return velocity[1] > 0;
    }

    public void jump() {
        velocity[1] = Physics.GRAVITY * JUMP_STRENGTH;
    }

    private int collectedItems = 0;

    public int getCollectedItems() {
        return collectedItems;
    }

    @Override
    public void collide(Entity b) {
        if (b instanceof Banana banana) {
            banana.setCollected(true);
            collectedItems += 1;
        }
    }

    /**
     * Updates the player's state and handles movement and animation.
     */
    @Override
    public void update() {
        if (Player.player2 == this) return;         // ignorisi update za network igraca

        if (velocity[1] > 0) {                                  // skok
            setTranslateY(getTranslateY() - Physics.GRAVITY);
            velocity[1] -= Physics.GRAVITY;                     // ide u suprotnom smeru gravitacije dok je velocity y > 0
        } else {
            setTranslateY(getTranslateY() + Physics.GRAVITY);       // pada
        }

        if (velocity[0] != 0) {
            setTranslateX(getTranslateX() + velocity[0] * SPEED);
            animation.play("run");
        }

        if (inAir() && velocity[0] == 0) {
            animation.play("jump");
        } else {
            if (velocity[0] != 0) {
                animation.play("run");
            } else animation.play("idle");
        }
        boundsRestrict();

        final PositionPackage positionPackage = new PositionPackage();
        positionPackage.set(this);          // pisanje pozicije u paket
        Client.getHandler().send(positionPackage);      // slanje paketa serveru
    }

    private void boundsRestrict() {
        if (getTranslateX() < 0) setTranslateX(0);
        if (getTranslateY() < 0) setTranslateY(0);

        if (getTranslateX() > 800 - getFitWidth())
            setTranslateX(800 - getFitWidth());
        if (getTranslateY() > 600 - getFitHeight()) {
            setTranslateY(50);
            setTranslateX(Player.leftSpawn ? 50 : 800 - (50 + Player.player1.getFitWidth()));
        }
    }
}
