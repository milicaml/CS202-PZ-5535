package com.pz.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * A class representing a Banana entity in the game.
 */
public class Banana extends  Entity{

    private Animation animation;
    public static  final double  RESPAWN_TIME = 3.0;
    private  long collectedTime;

    /**
     * Indicates whether the banana has been collected.
     *
     * @return true if the banana has been collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Sets the collected state of the banana.
     *
     * @param collected the state to set
     */
    public void setCollected(boolean collected) {
        if (collected == this.collected) return;
        this.collected = collected;
        if (!this.collected) {
            this.setCollisionType(Physics.CollisionType.STATIC);
            this.setVisible(true);

        }else{
            this.setCollisionType(Physics.CollisionType.NONE);
            this.setVisible(false);
            collectedTime = System.currentTimeMillis();
        }
    }

    private boolean collected = false;
    /**
     * Constructs a new Banana entity at the specified position.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    public Banana(int x, int y) {
        super(Physics.CollisionType.STATIC, Assets.getImage("Bananas"), x, y, 50, 50, 1.0);
        animation = new Animation(this,
                Duration.seconds(0.1),
                17, 4,
                0, 0,
                32, 32);
        final Timeline frameTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), animation::update)
        );
        frameTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        frameTimeline.playFromStart();
    }

    /**
     * Updates the banana entity.
     */
    @Override
    public void update() {
        super.update();
        if (isCollected()) {
            if (System.currentTimeMillis() - collectedTime > RESPAWN_TIME * 1000) {
                setCollected(false);
            }
        }
    }
}
