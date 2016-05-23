package jhg.workflow;

import java.util.*;

public class User {

	private String name;
	private List<Role> roles;
	
	public User(String name){
		super();
		this.name = name;
		this.roles = new ArrayList<Role>();
	}
	public String getName(){
		return this.name;
	}
	public List<Role> getRoles(){
		return this.roles;
	}
	public void setAvailability(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}
	public void setAvailability(Date start, Date stop) {
		// TODO Auto-generated method stub
		
	}
	public boolean isAvailable(Date start) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
