package com.pz.game;

import com.pz.networking.packets.PositionPackage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;

public class Player extends Entity {
    public static boolean leftSpawn = true;
    public static final Player player1 = new Player();
    public static final Player player2 = new Player();

    public static enum GameType {
        SINGLE_PLAYER, MULTI_PLAYER
    }

    public static GameType GAME_TYPE = GameType.SINGLE_PLAYER;

    public Player() {
        super(Physics.CollisionType.CONTINUOUS, Assets.getImage("Run (32x32)"), 0, 0, 50, 50, 1.0);
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
                new KeyFrame(Duration.millis(100), animation::update)
        );
        frameTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        frameTimeline.playFromStart();
    }

    private Animation animation;
    public static final double SPEED = 5;
    public static final double JUMP_STRENGTH = 30;
    private final double[] velocity = new double[]{0, 0};

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(final double x, final double y) {
        velocity[0] = x;
        setScaleX(x < 0 ? -1 : 1);
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
        super.collide(b);
    }

    @Override
    public void update() {
        super.update();

        if (Player.player2 == this) return;

        if (velocity[1] > 0) {
            setTranslateY(getTranslateY() - Physics.GRAVITY);
            velocity[1] -= Physics.GRAVITY;
        } else {
            setTranslateY(getTranslateY() + Physics.GRAVITY);
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
        positionPackage.set(this);
        //Client.getHandler().send(positionPackage);
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
