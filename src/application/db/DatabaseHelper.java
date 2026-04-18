package application.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper{
	// READ
	public static List<String[]> query(String sql, Object ...params){
		List<String[]> results = new ArrayList<>();
		
		try (
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)
		){
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}
			
			ResultSet rs = stmt.executeQuery();
			int columnCount = rs.getMetaData().getColumnCount();
			
			while (rs.next()) {
				String[] row = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					row[i] = rs.getString(i + 1);
				}
				results.add(row);
			}
		
		} catch (Exception e) { e.printStackTrace(); }
		
			return results;
	}
	// Create & UPDATE & DELETE
	public static int update(String sql, Object ...params) {
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)
		){
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}
			return stmt.executeUpdate();
		} catch (Exception e) { e.printStackTrace(); }

		return  0;
	}
	// To Create Order for Checkout Service 
	public static int insertAndGetId(String sql, Object ...params) {
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
		){
			for(int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}
			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			return keys.next() ? keys.getInt(1) : -1;
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
}