package application.db;

import java.time.LocalDate;
import java.util.List;

public class OrderDAO {
	/****************************For Customer*******************************/
	
	public static List<String[]> getOrderByUser(int userId){
		String query = "SELECT id, country, shippingFee, totalPrice, status, orderDate "
					 + "FROM orders WHERE userId = ? "
					 + "ORDER BY id DESC";
		return DatabaseHelper.query(query, userId);
	}
	
	/****************************For Admin*******************************/
	public static List<String[]> getAllOrderByAdmin() {
		String query = "SELECT o.id, u.username, o.country, o.shippingFee, o.totalPrice, o.status, o.orderDate "
					 + "FROM orders o JOIN users u "
					 + "ON o.userId = u.id "
					 + "ORDER BY o.id DESC";
		return DatabaseHelper.query(query);
	}
	
	public static boolean updateOrderStatusByAdmin(int orderId, String status) {
		String query = "UPDATE orders SET status = ? WHERE id = ?";
		
		return DatabaseHelper.update(query, status, orderId) > 0;
	}
	
	/****************************For CheckoutService*******************************/
	public static boolean addOrderItem(int orderId, int productId, int quantity, double price) {
		String query = "INSERT INTO orderItems (orderId, productId, quantity, price) "
					 + "VALUES(?, ?, ?, ?)";
		return DatabaseHelper.update(query, orderId, productId, quantity, price) > 0;
	}
	public static int createOrder(int userId, String country, double shippingFee, double totalPrice) {
		String query = "INSERT INTO orders (userId, country, shippingFee, totalPrice, status, orderDate) "
					 + "VALUES(?, ?, ?, ?, 'Pending', ?)";
		return DatabaseHelper.insertAndGetId(query, userId, country, shippingFee, totalPrice, LocalDate.now().toString());
	}

}
