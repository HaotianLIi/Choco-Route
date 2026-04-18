package application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {

	private static String url = "jdbc:sqlite:chocoroute.db";
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url);
	}
	
	public static void setUrl(String newUrl) {
		url = newUrl;
	}
	
	public static String getUrl() {
		return url;
	}
}

