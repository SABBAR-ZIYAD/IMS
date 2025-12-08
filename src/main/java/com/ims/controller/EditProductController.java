package com.ims.controller;

import com.ims.dao.ProductDAO;
import com.ims.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProductController {

    @FXML private TextField skuField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;

    private int productId;

    // Receive data from the main table
    public void setProductData(Product product) {
        this.productId = product.getId();
        skuField.setText(product.getSku());
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getSellPrice()));
    }

    @FXML
    public void handleSave() {
        try {
            String sku = skuField.getText();
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());

            ProductDAO.updateProduct(productId, sku, name, price);

            // Close window
            ((Stage) skuField.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            System.out.println("Invalid Price");
        }
    }
}