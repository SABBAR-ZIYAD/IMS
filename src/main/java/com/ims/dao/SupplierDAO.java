package com.ims.dao;

import com.ims.model.Product;
import com.ims.model.Supplier;
import com.ims.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class SupplierDAO {
    public static ObservableList<Supplier> getAllSuppliers() {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT supplier_id, name, contact_info, status FROM suppliers")) {

            while (rs.next()) {
                list.add(new Supplier(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("contact_info"), rs.getString("status")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    // 1. ADD NEW SUPPLIER
    public static void addSupplier(String name, String contactInfo) {
        String sql = "INSERT INTO suppliers (name, contact_info, status) VALUES (?, ?, 'ACTIVE')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, contactInfo);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. DELETE (SOFT DELETE)
    public static void deleteSupplier(int id) {
        String sql = "UPDATE suppliers SET status = 'ARCHIVED' WHERE supplier_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void restoreSupplier(int id) {
        // 1. SQL to update the status back to 'Active'
        String query = "UPDATE suppliers SET status = 'Active' WHERE supplier_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Add this method to SupplierDAO.java
    public static int countActiveSuppliers() {
        String sql = "SELECT COUNT(*) FROM suppliers WHERE status = 'active'";

        try (Connection conn = DBConnection.getConnection(); // Use your connection class
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
}