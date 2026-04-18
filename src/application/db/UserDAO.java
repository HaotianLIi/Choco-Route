package application.db;

import java.util.List;

public class UserDAO {

	public static String authenticate(String username, String password) {
		String query = "SELECT role FROM users WHERE username = ? AND password = ?";
		List<String[]> results = DatabaseHelper.query(query,username,password); 
		return results.isEmpty() ? null : results.get(0)[0];
	}

	public static int getUserId(String username) {
		String query = "SELECT id FROM users WHERE username = ?";
		List<String[]> results = DatabaseHelper.query(query, username);
		return results.isEmpty() ? -1 : Integer.parseInt(results.get(0)[0]);
	}
}