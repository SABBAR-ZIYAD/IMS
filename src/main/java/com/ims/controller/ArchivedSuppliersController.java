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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.stream.Collectors;

public class ArchivedSuppliersController {

    @FXML private TableView<Supplier> archivedTable;
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
        // Filter ONLY Archived
        ObservableList<Supplier> all = SupplierDAO.getAllSuppliers();
        ObservableList<Supplier> archived = all.stream()
                .filter(s -> "ARCHIVED".equals(s.getStatus()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        archivedTable.setItems(archived);
    }

    @FXML
    public void handleRestore() {
        Supplier selected = archivedTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SupplierDAO.restoreSupplier(selected.getId());
            loadData(); // Refresh list to remove the restored item
        }
    }

    @FXML
    public void handleBack() {
        // Navigate back to the Main Supplier View
        Parent view = ViewFactory.getInstance().loadView("/fxml/SuppliersView.fxml");
        BorderPane dashboard = (BorderPane) archivedTable.getScene().getRoot();
        dashboard.setCenter(view);
    }
}