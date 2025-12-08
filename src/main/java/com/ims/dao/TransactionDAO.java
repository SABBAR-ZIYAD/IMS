package com.ims.dao;

import com.ims.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.ims.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;

public class TransactionDAO {

    // This method handles the "Stock In" logic safely
    public static boolean handleStockIn(int productId, int userId, int qty, String reason) {
        String insertTransSql = "INSERT INTO transactions (product_id, user_id, type, reason, qty) VALUES (?, ?, 'IN', ?, ?)";
        String updateStockSql = "UPDATE products SET stock_qty = stock_qty + ? WHERE product_id = ?";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();

            // 🛑 CRITICAL: Turn off Auto-Commit to start a Transaction
            conn.setAutoCommit(false);

            // 1. Log the Transaction
            try (PreparedStatement transStmt = conn.prepareStatement(insertTransSql)) {
                transStmt.setInt(1, productId);
                transStmt.setInt(2, userId);
                transStmt.setString(3, reason);
                transStmt.setInt(4, qty);
                transStmt.executeUpdate();
            }

            // 2. Update the Product Stock
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
                stockStmt.setInt(1, qty);
                stockStmt.setInt(2, productId);
                stockStmt.executeUpdate();
            }

            // ✅ COMMIT: If we get here, both steps worked. Save everything.
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // ❌ ROLLBACK: If anything failed, undo everything!
            if (conn != null) {
                try {
                    System.err.println("Transaction Failed! Rolling back...");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // Always reset connection to default state
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean handleStockOut(int productId, int userId, int qty, String reason) {
        // 1. Logic Check: Ensure we don't go negative
        // (Ideally, we check this in Java before DB, but DB constraint is the final guard)

        String insertTransSql = "INSERT INTO transactions (product_id, user_id, type, reason, qty) VALUES (?, ?, 'OUT', ?, ?)";
        // Note the MINUS sign below: stock_qty - ?
        String updateStockSql = "UPDATE products SET stock_qty = stock_qty - ? WHERE product_id = ? AND stock_qty >= ?";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Update Stock First (Optimistic Locking approach)
            // We verify 'stock_qty >= qty' in the WHERE clause to prevent negative stock
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
                stockStmt.setInt(1, qty);
                stockStmt.setInt(2, productId);
                stockStmt.setInt(3, qty); // Check if we have enough!

                int rowsAffected = stockStmt.executeUpdate();

                if (rowsAffected == 0) {
                    System.err.println("❌ Validation Failed: Not enough stock!");
                    conn.rollback();
                    return false;
                }
            }

            // 2. Log Transaction
            try (PreparedStatement transStmt = conn.prepareStatement(insertTransSql)) {
                transStmt.setInt(1, productId);
                transStmt.setInt(2, userId);
                transStmt.setString(3, reason);
                transStmt.setInt(4, qty);
                transStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    public static ObservableList<Transaction> getRecentTransactions() {
        ObservableList<Transaction> list = FXCollections.observableArrayList();

        // We JOIN 'products' to get the name, and 'users' to get the username
        String sql = "SELECT t.trans_id, t.product_id, t.user_id, t.type, t.reason, t.qty, t.timestamp, " +
                "p.name AS product_name, u.username " +
                "FROM transactions t " +
                "JOIN products p ON t.product_id = p.product_id " +
                "JOIN users u ON t.user_id = u.user_id " +
                "ORDER BY t.timestamp DESC LIMIT 10";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // 1. Create the object using your existing constructor
                Transaction t = new Transaction(
                        rs.getInt("trans_id"),
                        rs.getInt("product_id"),
                        rs.getInt("user_id"),
                        rs.getString("type"),
                        rs.getString("reason"),
                        rs.getInt("qty"),
                        rs.getTimestamp("timestamp")
                );

                // 2. Inject the "Extended" data (the names)
                t.setProductName(rs.getString("product_name"));
                t.setUsername(rs.getString("username"));

                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}