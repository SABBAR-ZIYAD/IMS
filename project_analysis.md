# IMS Project Analysis & Handover Guide

> **Project Name:** IMS (Sentinel Inventory)  
> **Type:** Desktop Application (EPOS / Inventory Management)  
> **Stack:** JavaFX, MySQL, JDBC, MVC Pattern  
> **Status:** Active Development (MVP Phase)

This document provides a comprehensive analysis of the code, features, and structure of the IMS project. It is written for a developer who needs to pick up the project without prior knowledge.

---

## 🏗️ Architecture & Technology

The application is built using a standard **Model-View-Controller (MVC)** architecture, ensuring a clean separation of concerns.

*   **Language:** Java 21 (Modular)
*   **UI Framework:** JavaFX (using FXML for layouts).
*   **Database:** MySQL 8.
*   **Data Access:** Raw JDBC with a DAO (Data Access Object) pattern. *Note: No ORM like Hibernate is used; SQL queries are written manually.*
*   **Build System:** Maven (configured in `pom.xml`, but no wrapper script included).
*   **Dependencies:**
    *   `javafx-controls`, `javafx-fxml`: Core UI.
    *   `mysql-connector-j`: Database connectivity.
    *   `jbcrypt`: Password hashing.

### Package Structure (`com.ims`)
*   `controller`: Handles UI events (button clicks, form submission). Bridges the View (FXML) and the Model/DAO.
*   `dao`: Contains all SQL logic (`INSERT`, `UPDATE`, `SELECT`). This is where the business logic for data integrity lives.
*   `model`: Plain Java Objects (POJOs) representing DB tables (Product, User, etc.).
*   `util`: Helper classes for Database Connection and Password Security.
*   `Main.java`: The JavaFX Application entry point.

---

## 🚀 Key Features Breakdown

### 1. Authentication & Security
*   **Login Screen:** The app starts with a login view (`LoginView.fxml`).
*   **Logic:** `LoginController` validates credentials against the `users` table.
*   **Security:** Passwords are **not** stored in plain text. The app uses **BCrypt** (via `PasswordUtil`) to hash and verify passwords.
*   **User Roles:** The system supports roles (Admin/Staff), though the current hardcoded logic in some places (like `RestockController`) might default to ID 1.

### 2. Inventory Control (The Core Logic)
This is the most critical part of the system. It does not just "edit" numbers; it tracks movement.
*   **Stock In (Restook):** Managed by `RestockController` & `TransactionDAO`.
    *   Creates a `TRANSACTION` record of type 'IN'.
    *   Updates the `products.stock_qty` atomically.
*   **Stock Out (Sale):** Managed by `SaleController` & `TransactionDAO`.
    *   Creates a `TRANSACTION` record of type 'OUT'.
    *   **Validation:** Prevents selling more stock than available (SQL check `stock_qty >= ?`).
*   **Transaction Integrity:** The code explicitly uses `conn.setAutoCommit(false)`, `commit()`, and `rollback()` to ensure that the log and the stock update happen together or not at all.

### 3. Application Modules
*   **Dashboard:** Displays KPIs and a list of recent transactions.
*   **Product Management:**
    *   **CRUD:** Create, Read, Update products.
    *   **Archiving:** Instead of permanent deletion, products can be "archived" (Soft Delete), viewable in `ArchivedProductsView`.
    *   **Categories:** Manage product categories.
    *   **Suppliers:** Manage supplier details.
    *   **Images:** Supports linking images to products.

---

## 🛠️ Setup & Testing Instructions

To continue working on this project, you need to set up the environment as follows:

### 1. Database Setup
The application expects a local MySQL instance.

*   **Database Name:** `ims_db`
*   **Credentials:** Username `root`, Password `root` (Hardcoded in `src/main/java/com/ims/util/DBConnection.java`).
*   **Schema:** You must create the tables (`users`, `products`, `transactions`, etc.) matching the models. *Note: There is no migration script file explicitly visible in the source tree, so you may need to reverse-engineer the schema from the DAO SQL statements or the README ER diagram.*

### 2. Build & Run
The project uses Maven but is missing the `mvnw` wrapper. You must have Maven installed globally.

*   **Compile:** `mvn clean compile`
*   **Run:** `mvn javafx:run`

### 3. Current State & "Gotchas"
*   **Hardcoded ID:** `RestockController.java` has a comment `CURRENT_USER_ID = 1; // Hardcoded 'admin' for now`. This needs to be updated to use the logged-in user's session.
*   **No Auto-Tests:** The `src/test` directory is empty. There are no unit tests.
*   **Launcher:** There is a `Launcher.java` class which includes debug logic to help find FXML files. Use this if you encounter "Location not set" errors.
