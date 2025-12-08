package com.ims.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class ViewFactory {

    private static ViewFactory instance;

    private ViewFactory() {}

    public static ViewFactory getInstance() {
        if (instance == null) {
            instance = new ViewFactory();
        }
        return instance;
    }

    // Helper method to load a view and return it
    public Parent loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}