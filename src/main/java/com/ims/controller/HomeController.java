package com.ims.controller;

import com.ims.dao.CategoryDAO;
import com.ims.dao.ProductDAO;
import com.ims.dao.SupplierDAO;
import com.ims.dao.TransactionDAO;
import com.ims.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import com.ims.util.ExportService;
import javafx.stage.Stage;

public class HomeController implements Initializable {

    // --- KPI CARDS ---
    @FXML
    private Label totalProductsLabel;
    @FXML
    private Label lowStockLabel;
    @FXML
    private Label activeSuppliersLabel;
    @FXML
    private Label inventoryValueLabel;

    // --- CHARTS ---
    @FXML
    private PieChart categoryPieChart;
    @FXML
    private BarChart<String, Number> topProductsBarChart;
    @FXML
    private AreaChart<String, Number> trendLineChart;

    // --- TABLE ---
    @FXML
    private TableView<Transaction> recentTransactionsTable;
    @FXML
    private TableColumn<Transaction, String> colDate;
    @FXML
    private TableColumn<Transaction, String> colProduct;
    @FXML
    private TableColumn<Transaction, String> colType;
    @FXML
    private TableColumn<Transaction, Integer> colQty;
    @FXML
    private TableColumn<Transaction, String> colReason;
    @FXML
    private TableColumn<Transaction, String> colUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadDashboardData();
    }

    private void setupTableColumns() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    public void loadDashboardData() {
        // 1. Load KPI Cards
        totalProductsLabel.setText(String.valueOf(ProductDAO.countActiveProducts()));
        lowStockLabel.setText(String.valueOf(ProductDAO.getLowStockCount()));
        activeSuppliersLabel.setText(String.valueOf(SupplierDAO.countActiveSuppliers()));
        inventoryValueLabel.setText(String.format("%.2f Dh", ProductDAO.getTotalInventoryValue()));

        // 2. Load Charts
        loadPieChart();
        loadBarChart();
        loadLineChart();

        // 3. Load Table
        recentTransactionsTable.setItems(TransactionDAO.getRecentTransactions());
    }

    // --- CHART LOGIC ---

    private void loadPieChart() {
        // Fetch data from DAO: Map<CategoryName, Count>
        Map<String, Integer> data = CategoryDAO.getCategoryProductCounts();
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            chartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        categoryPieChart.setData(chartData);
    }

    private void loadBarChart() {
        // Fetch data from DAO: Map<ProductName, SoldQty>
        Map<String, Integer> data = TransactionDAO.getTopSellingProducts();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Units Sold");

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        topProductsBarChart.getData().clear();
        topProductsBarChart.getData().add(series);
    }

    private void loadLineChart() {
        // Fetch data from DAO: Map<DateString, TransactionCount>
        Map<String, Integer> data = TransactionDAO.getTransactionTrend();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Activity");

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        trendLineChart.getData().clear();
        trendLineChart.getData().add(series);
    }

    @FXML
    private void handleExportReport() {
        // Get the current window (Stage) to show the Save Dialog
        Stage stage = (Stage) recentTransactionsTable.getScene().getWindow();

        // Pass the items currently in the table
        ExportService.exportTransactions(stage, recentTransactionsTable.getItems());
    }
}