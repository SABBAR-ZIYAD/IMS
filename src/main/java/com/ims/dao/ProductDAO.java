package com.ims.dao;

import com.ims.model.Product;
import com.ims.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {

    // 1. FETCH ALL
    public static ObservableList<Product> getAllProducts() {
        ObservableList<Product> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM products";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("sku"),
                        rs.getString("name"),
                        rs.getDouble("sell_price"),
                        rs.getInt("stock_qty"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. ARCHIVE (Soft Delete)
    public static void deleteProduct(int id) {
        String sql = "UPDATE products SET status = 'ARCHIVED' WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // 3. RESTORE
    public static void restoreProduct(int id) {
        String sql = "UPDATE products SET status = 'ACTIVE' WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // 4. UPDATE (Edit Name, Price, SKU)
    public static void updateProduct(int id, String sku, String name, double price) {
        String sql = "UPDATE products SET sku = ?, name = ?, sell_price = ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sku);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Add this method to ProductDAO.java
    public static int countActiveProducts() {
        String sql = "SELECT COUNT(*) FROM products WHERE status = 'active'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static double getTotalInventoryValue() {
        String sql = "SELECT SUM(sell_price * stock_qty) FROM products WHERE status = 'active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    // 6. DASHBOARD: Get Low Stock Count (Threshold < 5)
    public static int getLowStockCount() {
        String sql = "SELECT COUNT(*) FROM products WHERE stock_qty <= 5 AND status = 'active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}