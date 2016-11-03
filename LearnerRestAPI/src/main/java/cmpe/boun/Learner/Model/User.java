package cmpe.boun.Learner.Model;

import java.util.List;

public class User {
	private int userID;
	private String name;
	private String surName;
	
	public User(){
		//words = new ArrayList<Character>();
	}


	public int getUserID() {
		return userID;
	}

	public void setUserID(int userId){
		this.userID = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName){
		this.surName = surName;
	}
}
