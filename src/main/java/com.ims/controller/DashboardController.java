package com.ims.controller;

import com.ims.util.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private BorderPane mainLayout; // Refers to the main layout in DashboardView.fxml

    @FXML
    private Label welcomeLabel;

    public void setUsername(String username) {
        welcomeLabel.setText("Logged in as: " + username);
    }

    // --- NAVIGATION ACTIONS ---

    @FXML
    public void handleDashboardBtn(ActionEvent event) {
        System.out.println("Navigating to Dashboard Home...");
    }

    @FXML
    public void handleProductsBtn(ActionEvent event) {
        System.out.println("Navigating to Products...");
        // We will enable the ViewFactory logic here in the next step
        // Parent productView = ViewFactory.getInstance().loadView("/fxml/ProductsView.fxml");
        // mainLayout.setCenter(productView);
    }

    @FXML
    public void handleSuppliersBtn(ActionEvent event) {
        System.out.println("Navigating to Suppliers...");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        // Close the dashboard window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.out.println("User logged out.");
    }
}