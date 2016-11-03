package cmpe.boun.Learner.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cmpe.boun.Learner.Model.User;


public class Extractors {
	
	public Extractors(){}
	
	public static List<User> extractUser(ResultSet rs) throws SQLException{
		List<User> result = new ArrayList<User>();
		
		while(rs.next()){
			User user = new User();
			
			user.setUserID(rs.getInt("userId"));
			user.setName(rs.getString("name"));
			user.setSurName(rs.getString("surName"));

			result.add(user);
		}
		
		return result;
	}
	
	
}
