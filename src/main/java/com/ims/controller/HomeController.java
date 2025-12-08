package com.ims.controller;

import com.ims.dao.ProductDAO;
import com.ims.dao.SupplierDAO;
import com.ims.model.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
// 1. Add these imports
import com.ims.model.Transaction;
import com.ims.dao.TransactionDAO;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Timestamp;

public class HomeController {

    @FXML private Label totalProductsLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label activeSuppliersLabel;
    @FXML private Label inventoryValueLabel;


    @FXML
    public void initialize() {
        loadDashboardStats();
        setupTransactionTable();
    }

    private void loadDashboardStats() {
        // 1. Get exact counts directly from DB (FAST & CORRECT)
        int totalActiveProducts = ProductDAO.countActiveProducts();
        int totalActiveSuppliers = SupplierDAO.countActiveSuppliers();

        // 2. Update the Count Labels
        totalProductsLabel.setText(String.valueOf(totalActiveProducts));
        activeSuppliersLabel.setText(String.valueOf(totalActiveSuppliers)); // No longer hardcoded "4"

        // --- HANDLING THE LIST DATA ---
        // Critical Observation: If getAllProducts() includes archived items,
        // your Value and Low Stock alerts are currently WRONG.
        // You should ideally filter this list too.

        ObservableList<Product> allProducts = ProductDAO.getAllProducts();

        // Calculate stats ONLY on active products
        double totalValue = allProducts.stream()
                .filter(p -> "active".equalsIgnoreCase(p.getStatus())) // Ensure your Product model has getStatus()
                .mapToDouble(p -> p.getSellPrice() * p.getStockQty())
                .sum();

        long lowStockCount = allProducts.stream()
                .filter(p -> "active".equalsIgnoreCase(p.getStatus()))
                .filter(p -> p.getStockQty() < 10)
                .count();

        lowStockLabel.setText(String.valueOf(lowStockCount));
        inventoryValueLabel.setText(String.format("%.2f dh", totalValue));
    }


    // 2. Add these FXML injections (Make sure these IDs match your FXML file!)
    @FXML private TableView<Transaction> recentTransactionsTable;
    @FXML private TableColumn<Transaction, Timestamp> colDate;
    @FXML private TableColumn<Transaction, String> colProduct;
    @FXML private TableColumn<Transaction, String> colType; // IN or OUT
    @FXML private TableColumn<Transaction, Integer> colQty;
    @FXML private TableColumn<Transaction, String> colReason;
    @FXML private TableColumn<Transaction, String> colUser;

    private void setupTransactionTable() {
        // Link columns to Model properties
        // These strings must match the getters in Transaction.java (e.g. "productName" -> getProductName())
        colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Load data
        recentTransactionsTable.setItems(TransactionDAO.getRecentTransactions());
    }
}