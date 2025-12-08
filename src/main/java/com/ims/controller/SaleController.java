package com.ims.controller;

import com.ims.dao.TransactionDAO;
import com.ims.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaleController {

    @FXML private Label productLabel;
    @FXML private Label stockLabel;
    @FXML private TextField qtyField;
    @FXML private Label errorLabel;

    private Product selectedProduct;
    private final int CURRENT_USER_ID = 1; // Admin

    public void setProduct(Product product) {
        this.selectedProduct = product;
        productLabel.setText(product.getName());
        stockLabel.setText("Available Stock: " + product.getStockQty());
    }

    @FXML
    public void handleConfirm() {
        try {
            int qty = Integer.parseInt(qtyField.getText());

            if (qty <= 0) {
                errorLabel.setText("Quantity must be positive.");
                return;
            }

            if (qty > selectedProduct.getStockQty()) {
                errorLabel.setText("Not enough stock! Max: " + selectedProduct.getStockQty());
                return;
            }

            // Perform Sale (Reason is hardcoded to 'SALE' for simplicity)
            boolean success = TransactionDAO.handleStockOut(selectedProduct.getId(), CURRENT_USER_ID, qty, "SALE");

            if (success) {
                System.out.println("✅ Sale Successful!");
                ((Stage) qtyField.getScene().getWindow()).close();
            } else {
                errorLabel.setText("Transaction Failed.");
            }

        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid quantity.");
        }
    }
}