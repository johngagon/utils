package jhg.workflow;

import java.util.List;

public class Case {

	
	private User currentOwner;
	private String name;
	
	public Case(String name, User owner){
		this.currentOwner = owner;
		this.name = name;
	}

	public User getCurrentOwner() {
		return currentOwner;
	}

	public String getName() {
		return name;
	}
	
	public Task getTask(){
		return null;//TODO impl
	}
	public void setTask(Task task){
		//TODO impl
	}	
	public boolean isWaiting(){
		return false;//TODO impl
	}
	public List<User> getCurrentlyAssigned(){
		return null;//TODO impl
	}
	
}

