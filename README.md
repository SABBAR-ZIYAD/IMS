# IMS
# 🛡️ Sentinel Inventory Management System (2025)

> **Status:** Active Development
>
> **Stack:** JavaFX | MySQL | MVC Architecture

## 📋 Project Overview

**Sentinel Inventory** is a desktop-based inventory management solution designed to bridge the gap between basic data entry and professional enterprise resource planning (ERP).

Unlike simple CRUD applications, Sentinel focuses on **Data Integrity** and **Transactional Logic**. Stock levels are not manually editable numbers; they are the calculated result of strict "Stock In" and "Stock Out" movements. This project also features a modern UI with dynamic image handling.

## 🎯 Objectives

1. **Backend Logic Mastery:** Implement robust SQL handling and MVC separation.
2. **Data Integrity:** Prevent logical errors in stock calculations through transactional history.
3. **Modern UX:** Move away from "Gray Swing" interfaces using JavaFX and CSS styling.
4. **Media Handling:** Implement efficient file I/O for product image management.

## 🛠️ Technical Stack

* **Language:** Java (JDK 21+)
* **UI Framework:** JavaFX (Modular SDK)
* **Database:** MySQL 8.0
* **ORM/DAO:** Custom DAO Pattern (JDBC)
* **Build Tool:** Maven/Gradle
* **Design Pattern:** Model-View-Controller (MVC)

## ⚙️ Functional Requirements

### 1. User Management & Security
* **Authentication:** Secure Login screen.
* **Role-Based Access Control (RBAC):**
  * `ADMIN`: Full access to Users, Settings, and Master Data deletion.
  * `STAFF`: Limited access to Sales, Restocking, and View-Only reports.
* **Security:** Passwords are hashed before storage (no plain text).

### 2. Master Data Management (Products & Suppliers)
* **Suppliers (New):**
  * Simple CRUD: Create/Read/Update/Delete Supplier details.
  * Fields: Name, Contact Info (Phone/Email).
* **Products:**
  * **Cataloging:** Add, Edit, and Soft-Delete products.
  * **Attributes:** SKU (Barcode), Name, Category, **Supplier (FK)**.
  * **Pricing:** Cost Price (Buy) vs. Selling Price (Retail).
  * **Image Handling:** Store file path in DB, render image in UI.

### 3. Inventory Control (The Logic)
* **Transactional Updates:** Users cannot manually type a new stock number.
  * **Stock In:** Increases inventory.
  * **Stock Out:** Decreases inventory.
* **Calculated State:** `Current Quantity = Initial + Total In - Total Out`.
* **Low Stock Alerts:** Visual warning when `Quantity < Reorder Point`.

### 4. Dashboard & Analytics
* **KPI Cards:** Real-time metrics for *Total Inventory Value (Cost)*, *Projected Revenue*, *Low Stock Alerts*.
* **Visualization:** Charts displaying inventory distribution by Category.

### 5. Audit Trail
* **History Log:** A read-only record of every transaction (Who, What, When, Why).

## 📂 Project Structure (MVC)

```text
src/
├── main/
│   ├── java/
│   │   ├── com.sentinel/
│   │   │   ├── model/          # POJOs (Product, User, Supplier)
│   │   │   ├── dao/            # Database Access Logic (SQL)
│   │   │   ├── controller/     # UI Logic & Event Handling
│   │   │   ├── util/           # Helpers (DBConnection, PasswordHash)
│   │   │   └── Main.java       # App Entry Point
│   └── resources/
│       ├── fxml/               # UI Layout Files
│       ├── css/                # Stylesheets
│       └── images/             # Icons & Assets

## 💾 Database Schema (ER Diagram)

The system relies on a relational MySQL database designed in 3NF.

```mermaid
erDiagram
    USERS {
        int user_id PK
        string username
        string password_hash
        enum role "ADMIN, STAFF"
    }
    SUPPLIERS {
        int supplier_id PK
        string name
        string contact_info
    }
    CATEGORIES {
        int category_id PK
        string name
    }
    PRODUCTS {
        int product_id PK
        string sku
        string name
        decimal cost_price
        decimal sell_price
        int stock_qty
        string image_path
        int category_id FK
        int supplier_id FK
    }
    TRANSACTIONS {
        int trans_id PK
        int product_id FK
        int user_id FK
        enum type "IN, OUT"
        int qty
        datetime timestamp
    }

    CATEGORIES ||--|{ PRODUCTS : "categorizes"
    SUPPLIERS ||--|{ PRODUCTS : "supplies"
    PRODUCTS ||--|{ TRANSACTIONS : "logs"
    USERS ||--|{ TRANSACTIONS : "performs"
