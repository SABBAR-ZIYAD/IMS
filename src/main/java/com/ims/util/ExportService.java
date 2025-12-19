package com.ims.util;

import com.ims.model.Transaction;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

public class ExportService {

    public static void exportTransactions(Stage stage, ObservableList<Transaction> transactions) {
        // 1. Create the FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Transaction Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("Transaction_Report_" + java.time.LocalDate.now() + ".csv");

        // 2. Show Save Dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveFile(transactions, file);
        }
    }

    private static void saveFile(ObservableList<Transaction> list, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            // 3. Write Header
            writer.println("Transaction ID,Date,Product,Type,Quantity,Reason,User,Performed By");

            // 4. Write Data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Transaction t : list) {
                // Escape logic: If a string contains a comma, wrap it in quotes to prevent CSV breaking
                String productName = escapeSpecialCharacters(t.getProductName());
                String reason = escapeSpecialCharacters(t.getReason());
                String user = escapeSpecialCharacters(t.getUsername());
                String date = t.getTimestamp().toLocalDateTime().format(formatter);

                writer.printf("%d,%s,%s,%s,%d,%s,%s\n",
                        t.getId(),
                        date,
                        productName,
                        t.getType(),
                        t.getQty(),
                        reason,
                        user
                );
            }
            System.out.println("Report saved successfully: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility to handle commas in data (e.g., product name "Chair, Red")
    private static String escapeSpecialCharacters(String data) {
        if (data == null) return "";
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}