package application.db;

import java.time.LocalDate;
import java.util.List;

public class OrderDAO {

    // For Customer: returns [0]=id, [1]=country, [2]=shippingFee, [3]=totalPrice, [4]=status, [5]=orderDate
    public static List<String[]> getOrderByUser(int userId) {
        String query = "SELECT id, country, shippingFee, totalPrice, status, orderDate "
                     + "FROM orders WHERE userId = ? ORDER BY id DESC";
        return DatabaseHelper.query(query, userId);
    }

    // For Admin: returns [0]=id, [1]=username, [2]=country, [3]=shippingFee, [4]=totalPrice, [5]=status, [6]=orderDate
    public static List<String[]> getAllOrderByAdmin() {
        String query = "SELECT o.id, u.username, o.country, o.shippingFee, o.totalPrice, o.status, o.orderDate "
                     + "FROM orders o JOIN users u ON o.userId = u.id ORDER BY o.id DESC";
        return DatabaseHelper.query(query);
    }

    public static boolean updateOrderStatusByAdmin(int orderId, String status) {
        return DatabaseHelper.update("UPDATE orders SET status = ? WHERE id = ?", status, orderId) > 0;
    }

    // Checkout: creates order header and returns generated ID
    public static int createOrder(int userId, String country, double shippingFee, double totalPrice) {
        String query = "INSERT INTO orders (userId, country, shippingFee, totalPrice, status, orderDate) "
                     + "VALUES(?, ?, ?, ?, 'Pending', ?)";
        return DatabaseHelper.insertAndGetId(query, userId, country, shippingFee, totalPrice, LocalDate.now().toString());
    }

    public static boolean addOrderItem(int orderId, int productId, int quantity, double price) {
        String query = "INSERT INTO orderItems (orderId, productId, quantity, price) VALUES(?, ?, ?, ?)";
        return DatabaseHelper.update(query, orderId, productId, quantity, price) > 0;
    }

    // Rollback: called if stock reduction fails mid-checkout to clean up the partial order
    public static boolean deleteOrder(int orderId) {
        DatabaseHelper.update("DELETE FROM orderItems WHERE orderId = ?", orderId);
        return DatabaseHelper.update("DELETE FROM orders WHERE id = ?", orderId) > 0;
    }
}