package com.cmpe352group6.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Utility class for retrieving a connection from the database
 * @author Erhan
 *
 */
public class DBConnectionPool {
	private static Connection conn = null;
	
	/**
	 * Obtains a connection from the database with the specified credentials
	 * @return A connection from the database
	 */
	public static Connection getConnection() {
		try {
			//Load DB driver
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			//Connection string to the database
			String constring = "jdbc:mysql://localhost:3306/cmpe352test?user=javauser&password=javadude" ;
			//Request connection from the driver
			conn = DriverManager.getConnection(constring);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
}
