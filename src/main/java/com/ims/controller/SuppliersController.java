package com.ims.controller;

import com.ims.dao.SupplierDAO;
import com.ims.model.Supplier;
import com.ims.util.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.stream.Collectors;

public class SuppliersController {

    @FXML private TextField nameField;
    @FXML private TextField contactField;
    @FXML private TableView<Supplier> suppliersTable;
    @FXML private TableColumn<Supplier, Integer> colId;
    @FXML private TableColumn<Supplier, String> colName;
    @FXML private TableColumn<Supplier, String> colContact;
    @FXML private TableColumn<Supplier, String> colStatus;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        loadData();
    }

    private void loadData() {
        // Filter ONLY Active
        ObservableList<Supplier> all = SupplierDAO.getAllSuppliers();
        ObservableList<Supplier> active = all.stream()
                .filter(s -> "ACTIVE".equals(s.getStatus()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        suppliersTable.setItems(active);
    }

    @FXML
    public void handleAdd() {
        if (!nameField.getText().isEmpty()) {
            SupplierDAO.addSupplier(nameField.getText(), contactField.getText());
            nameField.clear();
            contactField.clear();
            loadData();
        }
    }

    @FXML
    public void handleDelete() {
        Supplier selected = suppliersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SupplierDAO.deleteSupplier(selected.getId());
            loadData();
        }
    }

    @FXML
    public void handleViewArchived() {
        // Switch center screen to Archived View
        Parent view = ViewFactory.getInstance().loadView("/fxml/ArchivedSuppliersView.fxml");
        // Trick: Get the Main Dashboard BorderPane from the current scene
        BorderPane dashboard = (BorderPane) suppliersTable.getScene().getRoot();
        dashboard.setCenter(view);
    }
}