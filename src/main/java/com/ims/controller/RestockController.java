package com.ims.controller;

import com.ims.dao.TransactionDAO;
import com.ims.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RestockController {

    @FXML private Label productLabel;
    @FXML private TextField qtyField;
    @FXML private ComboBox<String> reasonBox;
    @FXML private Label errorLabel;

    private Product selectedProduct;
    private final int CURRENT_USER_ID = 1; // Hardcoded 'admin' for now. Later we pass the real user ID.

    @FXML
    public void initialize() {
        reasonBox.getItems().addAll("RESTOCK", "ADJUSTMENT", "RETURN");
    }

    // Called by ProductsController to pass data
    public void setProduct(Product product) {
        this.selectedProduct = product;
        productLabel.setText("Product: " + product.getName() + " (SKU: " + product.getSku() + ")");
    }

    @FXML
    public void handleConfirm() {
        try {
            int qty = Integer.parseInt(qtyField.getText());
            String reason = reasonBox.getValue();

            if (qty <= 0 || reason == null) {
                errorLabel.setText("Invalid quantity or reason.");
                return;
            }

            // CALL THE TRANSACTION DAO
            boolean success = TransactionDAO.handleStockIn(selectedProduct.getId(), CURRENT_USER_ID, qty, reason);

            if (success) {
                System.out.println("✅ Restock Successful!");
                ((Stage) qtyField.getScene().getWindow()).close();
            } else {
                errorLabel.setText("Database Error. Transaction Rolled Back.");
            }

        } catch (NumberFormatException e) {
            errorLabel.setText("Quantity must be a number.");
        }
    }
}