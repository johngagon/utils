package jhg.workflow;

import java.util.Date;

@SuppressWarnings("unused")
public class Task {

	
	private TaskDefinition definition;
	private User assignee;
	public void complete() {
		// TODO Auto-generated method stub
		/*
		 * trigger 
		 * default now
		 */		
	}

	public void assignTask(User user) {
		// TODO Auto-generated method stub
		
	}
	public boolean isStarted(){
		return false; //TODO impl
	}
	public Date getTimeStarted() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getTimeCompleted() {
		// TODO Auto-generated method stub
		return null;
	}

	public User getAssignee() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean assignUser(User u0) {
		// TODO Auto-generated method stub, must check role. returns true if could assign
		return false;
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void start(Date taskStarted) {
		// TODO Auto-generated method stub
		
	}

	public void complete(Date taskCompleted) {
		// TODO Auto-generated method stub
		/*
		 * trigger 
		 */
	}

	public boolean canStart() {
		// TODO Auto-generated method stub
		/*
		 * based on having assigned user and if schedule on wf, is user available.
		 */
		return false;
	}
	
	
	
}
