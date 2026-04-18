package application.db;
import java.util.List;

public class CartDAO {
	public static List<String[]> getAllCartItem(int userId) {
		String query = "SELECT c.id, p.name, p.price, c.quantity " 
					 + "FROM cart c JOIN products P " 
					 + "ON c.productId = p.id " 
					 + "WHERE c.userId = ? ";
		
		return DatabaseHelper.query(query, userId); 
	}
	
    public static int getCartIdByProduct(int userId, int productId) {
        String query = "SELECT id FROM cart WHERE userId = ? AND productId = ?";
        List<String[]> result = DatabaseHelper.query(query, userId, productId);
        return result.isEmpty() ? -1 : Integer.parseInt(result.get(0)[0]);
    }
    
    public static int getCartQuantity(int cartId) {
        String query = "SELECT quantity FROM cart WHERE id = ?";
        List<String[]> result = DatabaseHelper.query(query, cartId);
        return result.isEmpty() ? 0 : Integer.parseInt(result.get(0)[0]);
    }
    
	public static boolean itemExists(int userId, int productId){
		String query = "SELECT id FROM cart WHERE userId = ? AND productId = ?"; 
		return !DatabaseHelper.query(query, userId, productId).isEmpty();
	}
	
	public static boolean insertCartItem(int userId, int productId, int quantity) {
		String query = "INSERT INTO cart(userId, productId, quantity) VALUES (?, ?, ?)";
		return DatabaseHelper.update(query, userId, productId, quantity) > 0;
	}
	
	public static boolean updateCartItem(int id, int quantity) {
		String query = "UPDATE cart SET quantity = ? WHERE id = ?";
		return DatabaseHelper.update(query, quantity, id) > 0;
	}
	
	public static boolean removeCartItem(int id) {
		String query = "DELETE FROM cart WHERE id = ?";
		return DatabaseHelper.update(query, id) > 0;
	}

	/****************************For CheckoutService*******************************/
	public static boolean removeEntireCart(int userId) {
		String query = "DELETE FROM cart WHERE userId= ?";
		return DatabaseHelper.update(query, userId) > 0;
	}
	
	
}
