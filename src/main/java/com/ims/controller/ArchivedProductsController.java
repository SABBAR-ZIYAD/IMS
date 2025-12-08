package com.ims.controller;

import com.ims.dao.ProductDAO;
import com.ims.model.Product;
import com.ims.util.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import java.util.stream.Collectors;

public class ArchivedProductsController {

    @FXML private TableView<Product> archivedTable;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colSku;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colStatus;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        loadData();
    }

    private void loadData() {
        ObservableList<Product> all = ProductDAO.getAllProducts();
        ObservableList<Product> archived = all.stream()
                .filter(p -> "ARCHIVED".equals(p.getStatus()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        archivedTable.setItems(archived);
    }

    @FXML
    public void handleRestore() {
        Product selected = archivedTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ProductDAO.restoreProduct(selected.getId());
            loadData();
        }
    }

    @FXML
    public void handleBack() {
        Parent view = ViewFactory.getInstance().loadView("/fxml/ProductsView.fxml");
        BorderPane dashboard = (BorderPane) archivedTable.getScene().getRoot();
        dashboard.setCenter(view);
    }
}