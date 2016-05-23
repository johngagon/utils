package jhg.workflow;

import java.util.*;

public class TaskDefinition {

	private String name;
	private Role role;
	public TaskDefinition(String name, Role r){
		super();
		this.name = name;
	}	
	
	
	public String getName(){
		return this.name;
	}

	public Role getRole(){
		return this.role;
	}


	public void setIsLast(boolean b) {
		// TODO Auto-generated method stub
		
	}


	public void dependsOn(TaskDefinition td) {
		// TODO Auto-generated method stub
		/*
		 * add to a list of what this task depends on.
		 * for the task depended on, add this to it's list of tasks depended on by.
		 */
		
	}
	public List<Task> dependsOn(){
		return null;//TODO impl
	}


	public List<Task> dependedOnBy(){
		return null;//TODO impl
	}

	public void dependsOn(TaskDefinition[] taskDefinitions) {
		// TODO Auto-generated method stub
		
	}


	public void setPriority(int i) {
		// TODO Auto-generated method stub
		
	}


	public void setHours(int i) {
		// TODO Auto-generated method stub
		
	}


	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getHours() {
		// TODO Auto-generated method stub
		return 0;
	}


}
