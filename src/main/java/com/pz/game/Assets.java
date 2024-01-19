package com.pz.game;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Assets {

    private static final Map<String, Image> images = new HashMap<>();

    public static Image getImage(String name) {
        return images.get(name);
    }

    public static void load() {
        try {
            loadDirectory(new File(Objects.requireNonNull(Assets.class.getResource("/images/")).getPath()));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Assets not found");
            alert.setContentText("Failed to load assets.");
            alert.showAndWait();
        }
    }

    private static void loadDirectory(File directory) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                loadDirectory(file);
            } else {
                loadFile(file);
            }
        }
    }

    private static void loadFile(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        final Image image = new Image(file.toURI().toString());
        images.put(name, image);
    }
}