package com.ims.controller;

import com.ims.dao.UserDAO;
import com.ims.model.User;
import com.ims.util.PasswordUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    // This method is called when the Login Button is clicked
    @FXML
    public void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 1. Basic Validation
        if (username.isEmpty() || password.isEmpty()) {
            showError("Fields cannot be empty.");
            return;
        }

        // 2. Database Check
        User user = UserDAO.getUserByUsername(username);

        // 3. Password Check & Transition
        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            // ✅ SUCCESS
            System.out.println("✅ LOGIN SUCCESS! User: " + user.getUsername());
            errorLabel.setVisible(false);

            // --- NEW: TRANSITION TO DASHBOARD ---
            try {
                // Load the Dashboard FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashboardView.fxml"));
                Parent root = loader.load();

                // Pass data to the Dashboard Controller
                DashboardController dashboardController = loader.getController();
                dashboardController.setUsername(user.getUsername());

                // Get the current window (Stage) and set the new Scene
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("IMS - Admin Dashboard");
                stage.centerOnScreen();

            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading dashboard.");
            }

        } else {
            // ❌ FAILED
            showError("Invalid Credentials.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}