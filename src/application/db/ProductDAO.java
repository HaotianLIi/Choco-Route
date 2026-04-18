package application.db;
import java.util.List;

public class ProductDAO {
	//CRUD - C 
	public static boolean createProduct(String name, String description, double price, int stock){
		String query = "INSERT INTO products (name, description, price, stock) VALUES (?,?,?,?)";
		
		return DatabaseHelper.update(query, name, description, price, stock) > 0;
	}
	//CRUD - R
	public static List<String[]> getAllProducts() {
		String query = "SELECT * FROM products";

		List<String[]> products = DatabaseHelper.query(query);		

		return products; 
	}
	//CRUD - U
	public static boolean updateProduct(int id, String name, String description, double price, int stock) {
		String query = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE id = ? ";
		
		return DatabaseHelper.update(query, name, description, price, stock, id) > 0;
	}
	//CRUD - D
	public static boolean deleteProduct(int id) {
		String query = "DELETE FROM products WHERE id = ?";
		
		return DatabaseHelper.update(query, id) > 0;
	}
	

	/****************************For CheckoutService*******************************/
    public static int getLiveStock(int productId) {
        String query = "SELECT stock FROM products WHERE id = ?";
        List<String[]> result = DatabaseHelper.query(query, productId);
        return result.isEmpty() ? 0 : Integer.parseInt(result.get(0)[0]);
    }
    
	public static boolean reduceStock(int productId, int quantity) {
		String query = "UPDATE products SET stock = stock - ?"
					 + "WHERE productId = ?";
		return DatabaseHelper.update(query, quantity, productId) > 0;
	}
}
