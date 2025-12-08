package com.ims.controller;

import com.ims.dao.ProductDAO;
import com.ims.model.Product;
import com.ims.util.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // <--- NEW IMPORT
import javafx.collections.transformation.SortedList;   // <--- NEW IMPORT
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; // <--- NEW IMPORT
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.stream.Collectors;

public class ProductsController {

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colSku;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, String> colStatus;

    // 👇 NEW: Link to the FXML Search Bar
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQty"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadData();
    }

    private void loadData() {
        // 1. Get ALL data
        ObservableList<Product> all = ProductDAO.getAllProducts();

        // 2. Filter for 'ACTIVE' only
        ObservableList<Product> activeList = all.stream()
                .filter(p -> "ACTIVE".equals(p.getStatus()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // 3. Wrap the Active List in a FilteredList (Initially displays all data)
        FilteredList<Product> filteredData = new FilteredList<>(activeList, p -> true);

        // 4. Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                // If filter text is empty, display all products.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches Name
                } else if (product.getSku().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches SKU
                }
                return false; // Does not match.
            });
        });

        // 5. Wrap the FilteredList in a SortedList.
        // This lets us sort the table even when filtered.
        SortedList<Product> sortedData = new SortedList<>(filteredData);

        // 6. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(productsTable.comparatorProperty());

        // 7. Add sorted (and filtered) data to the table.
        productsTable.setItems(sortedData);
    }

    // --- BUTTON ACTIONS (Unchanged) ---

    @FXML
    public void handleAddProductBtn() {
        openModal("/fxml/AddProductView.fxml", "Add New Product");
    }

    @FXML
    public void handleRestockBtn() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RestockView.fxml"));
            Parent root = loader.load();
            RestockController controller = loader.getController();
            controller.setProduct(selected);
            showModal(root, "Restock Product");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleSaleBtn() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SaleView.fxml"));
            Parent root = loader.load();
            SaleController controller = loader.getController();
            controller.setProduct(selected);
            showModal(root, "Sell Product");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleEditBtn() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditProductView.fxml"));
            Parent root = loader.load();
            EditProductController controller = loader.getController();
            controller.setProductData(selected);
            showModal(root, "Edit Product");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleDeleteBtn() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ProductDAO.deleteProduct(selected.getId());
            loadData();
        }
    }

    @FXML
    public void handleViewArchived() {
        Parent view = ViewFactory.getInstance().loadView("/fxml/ArchivedProductsView.fxml");
        BorderPane dashboard = (BorderPane) productsTable.getScene().getRoot();
        dashboard.setCenter(view);
    }

    private void openModal(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            showModal(root, title);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showModal(Parent root, String title) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        loadData();
    }
}