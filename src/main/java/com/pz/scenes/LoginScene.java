package com.pz.scenes;

import com.pz.App;
import com.pz.database.Database;
import com.pz.networking.Client;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents the login scene of the application.
 */
public class LoginScene extends CustomScene<VBox> {
    public static final LoginScene INSTANCE = new LoginScene();

    public LoginScene() {
        super(new VBox());
    }

    @Override
    void initialize() {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            Database.connect();

            if (Database.isConnected()) {
                final String username = usernameField.getText();
                final String password = passwordField.getText();
                onLogin(username, password);
            }
        });

        Button registerButton = new Button("Register");
        registerButton.setOnAction(event -> {
            Database.connect();

            if (Database.isConnected()) {
                final String username = usernameField.getText();
                final String password = passwordField.getText();
                onRegister(username, password);
            }
        });

        HBox logReg = new HBox(30, registerButton, loginButton);
        logReg.getStyleClass().add("logReg");
        root.getChildren().addAll(
                new Label("Login") {{
                    setStyle("-fx-font-size: 50px");
                }},
                usernameField, passwordField,
                logReg
        );
    }

    private void onLogin(final String username, final String password) {
        final int playerID = Database.validateLogin(username, password);
        if (playerID == Database.INVALID_USER) {
            return;
        }
        Database.setLocalPlayerID(playerID);
        Client.setLocalUsername(username);
        App.changeScene(MenuScene.INSTANCE);
    }

    private void onRegister(final String username, final String password) {
        final int playerID = Database.registerUser(username, password);
        if (playerID == Database.INVALID_USER) {
            return;
        }
        Database.setLocalPlayerID(playerID);
        Client.setLocalUsername(username);
        App.changeScene(MenuScene.INSTANCE);
    }

    @Override
    public void reset() {
    }
}