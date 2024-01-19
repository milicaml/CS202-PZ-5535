package com.pz.database;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LeaderboardItem {
    private SimpleStringProperty username;
    private SimpleIntegerProperty score;

    public LeaderboardItem(String username, int score) {
        this.username = new SimpleStringProperty(username);
        this.score = new SimpleIntegerProperty(score);
    }

    public SimpleStringProperty getUsername() {
        return username;
    }

    public SimpleIntegerProperty getScore() {
        return score;
    }
}