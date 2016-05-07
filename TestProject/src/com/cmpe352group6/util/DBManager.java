package com.cmpe352group6.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DBManager {
	private Connection connection = null;

	/**
	 * Constructor
	 * 
	 * @throws ManagerException
	 */
	public DBManager(Connection connection) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("DB hatasý!");
		}
		if(connection == null){
			System.out.println("BOS");
		}
		
		this.connection = connection;
	}
	
	public ResultSet callSQLFunc(){
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
	
}
