package com.ims.model;

public class Product {
    private int id;
    private String sku;
    private String name;
    private double sellPrice; // We only show Sell Price in the main table
    private int stockQty;
    private String status;

    public Product(int id, String sku, String name, double sellPrice, int stockQty, String status) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.sellPrice = sellPrice;
        this.stockQty = stockQty;
        this.status = status;
    }

    // Getters are REQUIRED for JavaFX TableView
    public int getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public double getSellPrice() { return sellPrice; }
    public int getStockQty() { return stockQty; }
    public String getStatus() { return status; }
}
