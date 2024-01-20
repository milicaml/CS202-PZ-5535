package com.pz.database;

import java.sql.*;
import java.util.List;

import com.pz.game.Assets;
import com.pz.game.Entity;
import com.pz.game.Physics;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.util.Pair;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Represent connection to the database.
 */
public abstract class Database {
    private static int localPlayerID = -1; // id iz baze trenutnog player

    /**
     * Get the local player ID.
     *
     * @return the local player ID
     */
    public static int getLocalPlayerID() {
        return localPlayerID;
    }

    /**
     * Set the local player ID.
     *
     * @param id the local player ID to set
     */
    public static void setLocalPlayerID(int id) {
        localPlayerID = id;
    }

    public static final String DB_URL = "jdbc:mysql://localhost:3306/cs202pz";
    public static final String DB_USER = "admin";
    public static final String DB_PASSWORD = "admin";
    private static boolean connected = false;

    /**
     * Check if the database is connected.
     *
     * @return true if the database is connected, false otherwise
     */
    public static boolean isConnected() {
        return connected;
    }

    private static Connection connection;

    /**
     * Connect to the database.
     */
    public static void connect() {
        if (isConnected()) return;

        // Load MySQL driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database driver not found");
            alert.setContentText("Failed to load MySQL driver.");
            alert.showAndWait();
            return;
        }

        // Connect to the database
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            // Failed to connect to database
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database connection failed");
            alert.setContentText("Failed to connect to database.");
            alert.showAndWait();
            return;
        }
        // Database connected
        connected = true;
    }

    /**
     * Disconnect from the database.
     */
    public static void disconnect() {
        // Check if the database is connected
        if (!isConnected()) return;
        try {
            // Disconnect from the database
            connection.close();
        } catch (SQLException e) {
            // Failed to disconnect from database
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database disconnection failed");
            alert.setContentText("Failed to disconnect from database.");
            alert.showAndWait();
            return;
        }
        // Database disconnected
        connected = false;
    }

    public static final int INVALID_USER = -1;

    /**
     * Validate the login credentials.
     *
     * @param username the username
     * @param password the password
     * @return the player ID if the login is successful, otherwise -1
     */
    public static int validateLogin(String username, String password) {
        String query = "SELECT * FROM player WHERE player.username = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String dbPassword = resultSet.getString("password");
                    if (BCrypt.checkpw(password, dbPassword)) {     //BCrypt se koristi za enkripciju podataka (proverava originalni sa hesovanim stringom)
                        System.out.println("Login successful");
                        return resultSet.getInt("playerID");
                    } else {
                        System.out.println("Invalid password");
                        return INVALID_USER;
                    }
                } else {
                    System.out.println("Username not found");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return INVALID_USER;
    }

    /**
     * Register a new user.
     *
     * @param username the username
     * @param password the password
     * @return the player ID if registration is successful, otherwise -1
     */
    public static int registerUser(String username, String password) {
        final int playerID = validateLogin(username, password);
        if (playerID != INVALID_USER) {
            return playerID;
        }

        String query = "INSERT INTO player (username, password) VALUES (?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));  // orginalni string stavi u hesovan

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return INVALID_USER;
    }

    /**
     * Get a list of maps.
     *
     * @return a list of map name and ID pairs
     */
    public static List<Pair<String, Integer>> getMaps() {
        String query = "SELECT * FROM map;";

        final List<Pair<String, Integer>> results = new java.util.ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(new Pair<>(resultSet.getString("name"), resultSet.getInt("mapID")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    /**
     * Get a list of entities for a specific map.
     *
     * @param mapID the ID of the map
     * @return a list of map entities
     */
    public static List<Entity> getMapEntities(int mapID) {
        String query = "SELECT * FROM entity WHERE entity.mapID = ?;";

        final List<Entity> results = new java.util.ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, mapID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Image image = Assets.getImage(resultSet.getString("imageName"));
                    Entity entity = new Entity(
                            Physics.CollisionType.fromValue(resultSet.getInt("collisionType")),
                            image,
                            resultSet.getDouble("xPosition"),
                            resultSet.getDouble("yPosition"),
                            resultSet.getDouble("width"),
                            resultSet.getDouble("height"),
                            resultSet.getDouble("opacity"));
                    results.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    /**
     * Create a new game.
     *
     * @param playerID1 the ID of player 1
     * @param playerID2 the ID of player 2
     * @return the ID of the new game
     */
    public static int createGame(int playerID1, int playerID2) {
        String query = "INSERT INTO game (player1, player2) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, playerID1);
            preparedStatement.setInt(2, playerID2);
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {      //vraca sve generisane primarne kljuceve iz table
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    /**
     * End a game and update the scores and winner ID.
     *
     * @param score1   the score of player 1
     * @param score2   the score of player 2
     * @param winnerID the ID of the winner
     * @param gameID   the ID of the game
     */
    public static void endGame(int score1, int score2, int winnerID, int gameID) {
        String query = "UPDATE game SET score1 = ?, score2 = ?, winner = ? WHERE gameId = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, score1);
            preparedStatement.setInt(2, score2);
            preparedStatement.setInt(3, winnerID);
            preparedStatement.setInt(4, gameID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update the leaderboard in a TableView.
     *
     * @param tableView the TableView to update
     */
    public static void updateLeaderboard(final TableView<LeaderboardItem> tableView) {
        String query = "SELECT u.username, SUM(CASE WHEN g.winner = u.playerID THEN 1 ELSE 0 END) AS WIN_COUNT\n" +
                "FROM player u LEFT JOIN game g ON u.playerID IN (g.player1,g.player2)\n" +
                "GROUP BY u.playerID\n" +
                "ORDER BY WIN_COUNT DESC;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            tableView.getItems().clear();
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tableView.getItems().add(new LeaderboardItem(resultSet.getString("username"), resultSet.getInt("WIN_COUNT")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Push a server event description to the database.
     *
     * @param description the description of the server event
     */
    public static void pushServerEvent(String description) {
        String query = "INSERT INTO serverEvent (description) VALUES (?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, description);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}