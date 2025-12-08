package com.ims.model;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int productId;
    private int userId;
    private String type;   // "IN" or "OUT"
    private String reason; // "RESTOCK", "SALE", etc.
    private int qty;
    private Timestamp timestamp;

    // Extended fields (for display purposes in the UI later)
    private String productName;
    private String username;

    public Transaction(int id, int productId, int userId, String type, String reason, int qty, Timestamp timestamp) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.type = type;
        this.reason = reason;
        this.qty = qty;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() { return id; }
    public int getProductId() { return productId; }
    public int getUserId() { return userId; }
    public String getType() { return type; }
    public String getReason() { return reason; }
    public int getQty() { return qty; }
    public Timestamp getTimestamp() { return timestamp; }

    // Setters for the "Extended" fields (we will use these when we join tables later)
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductName() { return productName; }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
}