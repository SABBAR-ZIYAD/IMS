package com.ims.controller;

import com.ims.util.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private BorderPane mainLayout;

    // NOTE: We removed 'welcomeLabel' and 'dashboardHomeView' because
    // we are now using the new HomeView.fxml architecture.

    // This method is automatically called by JavaFX when the view loads
    @FXML
    public void initialize() {
        // Automatically load the Dashboard Home (Stats Cards) on startup
        handleDashboardBtn(null);
    }

    // We keep this method so LoginController doesn't crash,
    // but for now, it just logs to console until we add a user label to the new HomeView.
    public void setUsername(String username) {
        System.out.println("Logged in user: " + username);
    }

    // --- NAVIGATION ACTIONS ---

    @FXML
    public void handleDashboardBtn(ActionEvent event) {
        System.out.println("Navigating to Dashboard Home...");
        // Load the new Stats Cards view
        Parent view = ViewFactory.getInstance().loadView("/fxml/HomeView.fxml");
        if (view != null) {
            mainLayout.setCenter(view);
        }
    }

    @FXML
    public void handleProductsBtn(ActionEvent event) {
        System.out.println("Navigating to Products...");
        Parent productsView = ViewFactory.getInstance().loadView("/fxml/ProductsView.fxml");
        if (productsView != null) {
            mainLayout.setCenter(productsView);
        }
    }

    @FXML
    public void handleSuppliersBtn(ActionEvent event) {
        System.out.println("Navigating to Suppliers...");
        Parent view = ViewFactory.getInstance().loadView("/fxml/SuppliersView.fxml");
        if (view != null) {
            mainLayout.setCenter(view);
        }
    }

    @FXML
    public void handleCategoriesBtn(ActionEvent event) {
        System.out.println("Navigating to Categories...");
        Parent view = ViewFactory.getInstance().loadView("/fxml/CategoriesView.fxml");
        if (view != null) {
            mainLayout.setCenter(view);
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.out.println("User logged out.");
    }
}