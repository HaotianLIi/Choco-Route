package application.db;

import java.util.List;

public class CartDAO {

    // Returns cart items for a user
    // Row layout: [0]=cartId, [1]=productId, [2]=productName, [3]=price, [4]=quantity
    public static List<String[]> getAllCartItem(int userId) {
        String query = "SELECT c.id, p.id, p.name, p.price, c.quantity "
                     + "FROM cart c JOIN products p ON c.productId = p.id "
                     + "WHERE c.userId = ?";
        return DatabaseHelper.query(query, userId);
    }

    public static boolean itemExists(int userId, int productId) {
        String query = "SELECT id FROM cart WHERE userId = ? AND productId = ?";
        return !DatabaseHelper.query(query, userId, productId).isEmpty();
    }

    // Get cart row ID by productId — used to update quantity without name matching
    public static int getCartIdByProduct(int userId, int productId) {
        String query = "SELECT id FROM cart WHERE userId = ? AND productId = ?";
        List<String[]> result = DatabaseHelper.query(query, userId, productId);
        return result.isEmpty() ? -1 : Integer.parseInt(result.get(0)[0]);
    }

    // Get current quantity in a cart row
    public static int getCartQuantity(int cartId) {
        String query = "SELECT quantity FROM cart WHERE id = ?";
        List<String[]> result = DatabaseHelper.query(query, cartId);
        return result.isEmpty() ? 0 : Integer.parseInt(result.get(0)[0]);
    }

    public static boolean insertCartItem(int userId, int productId, int quantity) {
        String query = "INSERT INTO cart(userId, productId, quantity) VALUES (?, ?, ?)";
        return DatabaseHelper.update(query, userId, productId, quantity) > 0;
    }

    public static boolean updateCartItem(int cartId, int quantity) {
        String query = "UPDATE cart SET quantity = ? WHERE id = ?";
        return DatabaseHelper.update(query, quantity, cartId) > 0;
    }

    public static boolean removeCartItem(int cartId) {
        return DatabaseHelper.update("DELETE FROM cart WHERE id = ?", cartId) > 0;
    }

    // Clears all cart items for a user after checkout
    public static boolean removeEntireCart(int userId) {
        return DatabaseHelper.update("DELETE FROM cart WHERE userId = ?", userId) > 0;
    }
}