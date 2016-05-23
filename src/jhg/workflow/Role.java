package jhg.workflow;

import java.util.*;

public class Role {

	private String name;
	public Role(String name){
		super();
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public List<User> users(){
		return null;//TODO impl
	}
	
}
