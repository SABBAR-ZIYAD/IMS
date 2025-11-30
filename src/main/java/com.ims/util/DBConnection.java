package com.ims.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ims_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found in library path", e);
        }
    }
    public static void main() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ SUCCESS: Connected to the DATA BASE");
            }
        } catch (SQLException e) {
            System.err.println("❌ FAILED: Could not connect to IMS_DB.");
            e.printStackTrace();
        }

    }
}
