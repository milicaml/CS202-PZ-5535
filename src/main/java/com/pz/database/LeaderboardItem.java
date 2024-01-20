package com.pz.database;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents an item in the leaderboard.
 */
public class LeaderboardItem {
    private SimpleStringProperty username;      //SimpleStringProperty prati promene vrednosti
    private SimpleIntegerProperty score;

    /**
     * Constructs a LeaderboardItem with the specified username and score.
     * @param username the username of the player
     * @param score the score of the player
     */
    public LeaderboardItem(String username, int score) {
        this.username = new SimpleStringProperty(username);
        this.score = new SimpleIntegerProperty(score);
    }


    /**
     * Gets the username property of the leaderboard item.
     * @return the username property
     */
    public SimpleStringProperty getUsername() {
        return username;
    }

    /**
     * Gets the score property of the leaderboard item.
     * @return the score property
     */
    public SimpleIntegerProperty getScore() {
        return score;
    }
}