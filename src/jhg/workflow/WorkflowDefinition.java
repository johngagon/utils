package jhg.workflow;

import java.util.*;

public class WorkflowDefinition {

	private String name;
	private List<TaskDefinition> taskDefinitions;
	
	public WorkflowDefinition(String name){
		super();
		taskDefinitions = new ArrayList<TaskDefinition>();
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public List<TaskDefinition> getTaskDefinitions() {
		return taskDefinitions;
	}

	public void addRoles(List<Role> roles) {
		// TODO Auto-generated method stub
		
	}
	
	
}
