package com.ims;

import java.net.URL;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("--- 🕵️ DEBUGGING RESOURCE PATH ---");

        // 1. Check if we can find it at the root
        URL urlRoot = Launcher.class.getResource("/LoginView.fxml");
        System.out.println("Checking /LoginView.fxml: " + (urlRoot != null ? "FOUND ✅" : "NOT FOUND ❌"));

        // 2. Check if we can find it in the folder
        URL urlFolder = Launcher.class.getResource("/fxml/LoginView.fxml");
        System.out.println("Checking /fxml/LoginView.fxml: " + (urlFolder != null ? "FOUND ✅" : "NOT FOUND ❌"));

        // 3. Print where Java is actually running from
        System.out.println("Running from: " + Launcher.class.getProtectionDomain().getCodeSource().getLocation());

        if (urlFolder != null) {
            System.out.println("🚀 FILE FOUND! Launching App...");
            Main.main(args);
        } else {
            System.err.println("🛑 CRITICAL: Java cannot see the FXML file.");
            System.err.println("Please check your src/main/resources folder.");
        }
    }
}