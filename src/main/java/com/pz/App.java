package com.pz;

import com.pz.database.Database;
import com.pz.game.Assets;
import com.pz.scenes.CustomScene;
import com.pz.scenes.LoginScene;
import javafx.application.Application;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Random;

public class App extends Application {
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return App.primaryStage;
    }

    /**
     * Change the scene to the specified CustomScene object.
     *
     * @param scene the CustomScene object to change to
     * @throws IllegalArgumentException if the scene is null
     */
    public static void changeScene(CustomScene<?> scene) {
        if (scene == null)
            throw new IllegalArgumentException("Scene cannot be null");
        scene.reset();
        App.primaryStage.setScene(scene);
    }

    /**
     * Start the application by setting up the primary stage,
     * loading assets, changing the scene to the login scene,
     * and showing the stage.
     *
     * @param stage the primary stage of the application
     * @throws IOException if there is an IO error
     */
    @Override
    public void start(Stage stage) throws IOException {
        App.primaryStage = stage;
        stage.setTitle("Hello!");

        Assets.load();

        App.changeScene(LoginScene.INSTANCE);

        stage.show();
    }

    /**
     * Stops the execution of the program.
     *
     * @throws Exception if an error occurs during the stopping process
     */
    @Override
    public void stop() throws Exception {
        Database.disconnect();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}