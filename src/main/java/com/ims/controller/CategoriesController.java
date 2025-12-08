package com.ims.controller;

import com.ims.dao.CategoryDAO;
import com.ims.model.Category;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CategoriesController {

    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, Integer> colId;
    @FXML private TableColumn<Category, String> colName;
    @FXML private TextField nameField;

    private Category selectedCategory = null; // Track selection for Edit Mode

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Listen for row selection
        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedCategory = newVal;
                nameField.setText(newVal.getName());
            }
        });

        loadData();
    }

    private void loadData() {
        categoryTable.setItems(CategoryDAO.getAllCategories());
    }

    @FXML
    public void handleSave() {
        String name = nameField.getText();
        if (name.isEmpty()) return;

        if (selectedCategory == null) {
            // CREATE
            CategoryDAO.addCategory(name);
        } else {
            // UPDATE
            CategoryDAO.updateCategory(selectedCategory.getId(), name);
        }
        handleClear(); // Reset form
        loadData();    // Refresh table
    }

    @FXML
    public void handleDelete() {
        if (selectedCategory != null) {
            CategoryDAO.deleteCategory(selectedCategory.getId());
            handleClear();
            loadData();
        }
    }

    @FXML
    public void handleClear() {
        selectedCategory = null;
        nameField.clear();
        categoryTable.getSelectionModel().clearSelection();
    }
}