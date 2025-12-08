package com.ims.model;

public class Supplier {
    private int id;
    private String name;
    private String contactInfo; // NEW FIELD
    private String status;
    public Supplier(int id, String name, String contactInfo, String status) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.status = status;
    }

    // Add this new Constructor for the old code (Dropdowns) to keep working
    public Supplier(int id, String name) {
        this.id = id;
        this.name = name;
        this.contactInfo = "";
        this.status = "ACTIVE";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getContactInfo() { return contactInfo; } // Getter for Table
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    @Override
    public String toString() { return name; }
}