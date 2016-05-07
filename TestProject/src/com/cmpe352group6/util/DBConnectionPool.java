package com.cmpe352group6.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionPool {
	private static Connection conn = null;

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			String constring = "jdbc:mysql://localhost:3306/cmpe352test?user=root&password=ajanGadget" ;
			conn = DriverManager.getConnection(constring);
		} catch (SQLException ex) {
			// handle any errors
			ex.printStackTrace();
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
}
