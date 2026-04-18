package application.db;

import java.util.List;

public class ProductDAO {

    // CRUD - C
    public static boolean createProduct(String name, String description, double price, int stock) {
        String query = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?)";
        return DatabaseHelper.update(query, name, description, price, stock) > 0;
    }

    // CRUD - R
    public static List<String[]> getAllProducts() {
        return DatabaseHelper.query("SELECT * FROM products");
    }

    // CRUD - U
    public static boolean updateProduct(int id, String name, String description, double price, int stock) {
        String query = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE id = ?";
        return DatabaseHelper.update(query, name, description, price, stock, id) > 0;
    }

    // CRUD - D
    public static boolean deleteProduct(int id) {
        return DatabaseHelper.update("DELETE FROM products WHERE id = ?", id) > 0;
    }

    // Returns live stock from DB — used at Add-to-Cart time to avoid stale UI values
    public static int getLiveStock(int productId) {
        String query = "SELECT stock FROM products WHERE id = ?";
        List<String[]> result = DatabaseHelper.query(query, productId);
        return result.isEmpty() ? 0 : Integer.parseInt(result.get(0)[0]);
    }

    // Atomically reduces stock only if enough is available (AND stock >= ? prevents overselling)
    public static boolean reduceStock(int productId, int quantity) {
        String query = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        return DatabaseHelper.update(query, quantity, productId, quantity) > 0;
    }
}