package com.ims.model;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String role; // "ADMIN" or "STAFF"

    // Constructor
    public User(int id, String username, String passwordHash, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters (We don't need Setters for this strictly yet)
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
}
