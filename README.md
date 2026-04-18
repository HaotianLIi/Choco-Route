# ChocoRoute — Chocolate E-Commerce Desktop App

A JavaFX desktop application for browsing and purchasing chocolate products. Built with SQLite for persistence and Flyway for automatic database migration.

---

## Prerequisites

| Requirement | Version |
|---|---|
| JDK | 17 or newer |
| Eclipse IDE | 2022-09 or newer |
| JavaFX SDK | 17 or newer |

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/HaotianLIi/Choco-Route.git
```

### 2. Import into Eclipse

1. **File → Import → General → Existing Projects into Workspace**
2. Browse to the cloned folder → **Finish**

> **"Marketplace solutions available" popup?** Click **Cancel** — the two missing natures (Xtext, DBeaver) are dev tools not needed to run the app.

### 3. Fix the JRE if needed

If the project shows **JRE System Library [JavaSE-1.8]**:

1. Right-click project → **Properties → Java Build Path → Libraries**
2. Select **JRE System Library** → **Edit** → pick **JDK 17+** → **Finish**
3. **Properties → Java Compiler** → set compliance level to **17**

Don't have JDK 17? Download from [https://adoptium.net](https://adoptium.net/temurin/releases/?version=17)

### 4. Add the JavaFX SDK

1. Download JavaFX SDK 17 from [https://gluonhq.com/products/javafx](https://gluonhq.com/products/javafx/)
2. Right-click project → **Properties → Java Build Path → Libraries → Add External JARs**
3. Select all `.jar` files from the JavaFX SDK `lib/` folder

### 5. Set VM arguments

1. Right-click `Main.java` → **Run As → Run Configurations → Arguments tab**
2. Paste into **VM arguments** (update the path to match your JavaFX SDK location):

**Linux / macOS:**
```
--module-path /path/to/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml
```

**Windows:**
```
--module-path "C:\path\to\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml
```

3. Under the **JRE tab**, make sure it shows your JDK 17 installation
4. Click **Apply → Run**

---

## Default Login Credentials

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | Admin |
| `Frankie` | `Frankie` | Customer |
| `Manzar` | `Manzar` | Customer |
| `Jamal` | `Jamal` | Customer |

The database (`chocoroute.db`) is auto-created and seeded on first launch via Flyway.

---

## Project Structure

```
src/application/
├── Main.java                            # App entry point
├── UIHelper.java                        # Shared UI utilities
├── admin/
│   ├── AdminDashboardController.java
│   ├── ManageOrdersController.java
│   └── ManageProductsController.java
├── customer/
│   ├── CustomerDashboardController.java # Browse products, add to cart
│   ├── CustomerCartController.java
│   ├── CustomerCheckoutController.java  # Places order, reduces stock
│   └── CustomerOrdersController.java
├── db/
│   ├── CartDAO.java
│   ├── OrderDAO.java
│   ├── ProductDAO.java
│   ├── ShippingDAO.java
│   ├── UserDAO.java
│   ├── DatabaseHelper.java
│   ├── DatabaseConnection.java
│   ├── DatabaseInitializer.java         # Flyway runner
│   └── migration/
│       ├── V1__Create_Tables.sql
│       └── V2__Seed_Tables.sql
└── login/
    ├── LoginController.java
    ├── SceneManager.java
    └── UserSession.java
lib/
├── sqlite-jdbc-3.36.0.3.jar
└── flyway-core-6.5.7.jar
```

---

## Troubleshooting

**"Marketplace solutions available" on import**
→ Click Cancel. Not needed.

**"Selection does not contain a main type"**
→ Project JRE is set to Java 8. Fix the JRE to JDK 17 (see step 3 above).

**"Module javafx.controls not found"**
→ VM arguments are missing or the JavaFX SDK path is wrong. Check step 5 above. Also make sure the **Dependencies tab** in Run Configurations doesn't have a duplicate `JavaFX SDK` under Modulepath — remove it if so.

**Blank tables after login**
→ Delete `chocoroute.db` from the project root and rerun — Flyway will recreate it.