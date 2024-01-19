package com.pz.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;

public class Banana extends  Entity{

    private Animation animation;
    public static  final double  RESPAWN_TIME = 3.0;
    private  long collectedTime;

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        if (collected == this.collected) return;
        this.collected = collected;
        if (!this.collected) {
            this.setCollisionType(Physics.CollisionType.DISCRETE);
            this.setVisible(true);

        }else{
            this.setCollisionType(Physics.CollisionType.NONE);
            this.setVisible(false);
            collectedTime = System.currentTimeMillis();
        }
    }

    private boolean collected = false;
    public Banana(int x, int y) {
        super(Physics.CollisionType.CONTINUOUS, Assets.getImage("Bananas"), x, y, 50, 50, 1.0);
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
