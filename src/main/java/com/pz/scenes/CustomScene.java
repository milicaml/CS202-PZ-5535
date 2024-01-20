package com.pz.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

/**
 * The CustomScene class represents a custom JavaFX scene with a specific root element and styling.
 *
 * @param <T> The type of the root element of the scene
 */
public abstract class CustomScene<T extends Parent> extends Scene {             // parent-root
    public static final int SCENE_WIDTH = 800;
    public static final int SCENE_HEIGHT = 600;
    protected final T root;

    public CustomScene(T root) {
        super(root, SCENE_WIDTH, SCENE_HEIGHT);
        this.root = root;
        initialize();
        String style = Objects.requireNonNull(getClass().getResource("/styles/CustomSceneStyle.css")).toExternalForm();
        getStylesheets().add(style);
    }

    abstract void initialize();

    public abstract void reset();
}