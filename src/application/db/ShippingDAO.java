package application.db;
import java.util.List;
import java.util.ArrayList;

public class ShippingDAO {
	
	public static double getShippingFee(String country) {
		String query = "SELECT fee FROM shippingFees WHERE country = ?";
		List<String[]> result = DatabaseHelper.query(query, country);
		
		return result.isEmpty() ? -1 : Double.parseDouble(result.get(0)[0]);
	}
	
	public static List<String> getAllCountries(){
		List<String> countries = new ArrayList<>();
		String query = "SELECT country FROM shippingFees ORDER BY country";
		for (String[] row : DatabaseHelper.query(query)) {
			countries.add(row[0]);
		}
		return countries;
	}
	
}
