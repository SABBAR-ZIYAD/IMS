package com.ims;

import com.ims.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✅ SUCCESS: Connected to IMS_DB!");
            }
        } catch (SQLException e) {
            System.err.println("❌ FAILED: Could not connect to IMS_DB.");
            e.printStackTrace();
        }
    }
}
