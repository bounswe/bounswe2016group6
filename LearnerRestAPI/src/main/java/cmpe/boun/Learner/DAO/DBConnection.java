package cmpe.boun.Learner.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class DBConnection {

	private Connection conn;
	private Statement stmt;
	
	public DataSource dataSource;
	
	public DBConnection(){
	}	
	
	public void setDataSource(DataSource dataSource) throws SQLException {
		this.dataSource = dataSource;
		conn = dataSource.getConnection();
	}

	public void closeConnection(){
		if (conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}	
	public Connection getConn() {
		return conn;
	}
	public Statement getStmt() {
		return stmt;
	}
}
	
	
