package com.cmpe352group6.util;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Utility class for wrapping database functionality.
 * Retrieves or adds the river data from/to database.
 * @author Erhan Cagirici
 *
 */
public class DBManager {
	private Connection connection = null;

	/**
	 * Construct the database
	 * 
	 */
	public DBManager(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Retrieves the user's saved data from the DB
	 * @return user-saved resultSet
	 */
	public ResultSet retrieveUserSaves(){
		PreparedStatement stmt = null;
		String sql = "SELECT * FROM erhanrecords";
		try {
			System.out.println("SQLing");
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Adds a given river string into the database
	 * @param rivername The rivername to be saved into database
	 * @return operation result , 1 if successful
	 */
	public int addDataUrl(String rivername){
		PreparedStatement stmt = null;
		String sql = "INSERT INTO erhanrecords VALUES ('name', '" + rivername + "')";
		try {
			stmt = connection.prepareStatement(sql);
			System.out.println(sql);
			int rs = stmt.executeUpdate();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
