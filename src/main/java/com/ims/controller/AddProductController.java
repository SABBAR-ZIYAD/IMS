package com.ims.controller;

import com.ims.dao.CategoryDAO;
import com.ims.dao.SupplierDAO;
import com.ims.model.Category;
import com.ims.model.Supplier;
import com.ims.util.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddProductController {

    @FXML private TextField skuField;
    @FXML private TextField nameField;
    @FXML private TextField priceField; // Sell Price
    @FXML private TextField costField;  // Cost Price
    @FXML private ComboBox<Category> categoryBox;
    @FXML private ComboBox<Supplier> supplierBox;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        // Load data into dropdowns immediately
        categoryBox.setItems(CategoryDAO.getAllCategories());
        supplierBox.setItems(SupplierDAO.getAllSuppliers());
    }

    @FXML
    public void handleSave() {
        try {
            // 1. Validate Input
            if (skuField.getText().isEmpty() || nameField.getText().isEmpty() || priceField.getText().isEmpty()) {
                errorLabel.setText("Please fill all required fields.");
                return;
            }

            // 2. Prepare Data
            String sku = skuField.getText();
            String name = nameField.getText();
            double sellPrice = Double.parseDouble(priceField.getText());
            double costPrice = Double.parseDouble(costField.getText());
            int catId = categoryBox.getValue().getId();
            int supId = supplierBox.getValue().getId();

            // 3. Save to DB
            saveProductToDB(sku, name, costPrice, sellPrice, catId, supId);

            // 4. Close Window
            ((Stage) skuField.getScene().getWindow()).close();
            System.out.println("✅ Product Added!");

        } catch (NumberFormatException e) {
            errorLabel.setText("Price must be a number.");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error saving product.");
        }
    }

    private void saveProductToDB(String sku, String name, double cost, double sell, int catId, int supId) {
        String sql = "INSERT INTO products (sku, name, cost_price, sell_price, category_id, supplier_id, stock_qty, status) VALUES (?, ?, ?, ?, ?, ?, 0, 'ACTIVE')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sku);
            stmt.setString(2, name);
            stmt.setDouble(3, cost);
            stmt.setDouble(4, sell);
            stmt.setInt(5, catId);
            stmt.setInt(6, supId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}