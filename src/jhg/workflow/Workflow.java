package jhg.workflow;

import java.util.*;

public class Workflow {

	private WorkflowDefinition definition;
	private Case workCase;
	private List<Task> tasks;
	private Boolean isActive;
	private Date startDate;
	private Date endDate;
	
	
	public Workflow(WorkflowDefinition definition, Case workCase){
		super();
		tasks = new ArrayList<Task>();
		/*
		 * loop through definition's tasks
		 * create a task for each definition
		 * register them to each other.
		 * 
		 */
		this.definition = definition;
		this.workCase = workCase;
	}
	
	public void addListener(WorkflowListener wfl){
		
	}
	
	public void notifyListeners(WorkflowEvent wfe){
		
	}
	
	public void start(User firstTaskUser){
		
	}

	public WorkflowDefinition getDefinition() {
		return definition;
	}

	public Case getWorkCase() {
		return workCase;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}


	public WorkflowDashboard start() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAutoAssign(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void addUsers(User[] users) {
		// TODO Auto-generated method stub
		
	}

	public boolean hasTasks() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Task> nextTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public User findUserForTask(Task t) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTotalTasks() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCompletedTaskCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInProgressCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public User getNextUserInRoleForTask(Task t) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getAllTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getInProgressTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getRemainingTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getIncompleteTasks(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	public Task nextTask(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getNotStartedTasks(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getInProgressTasks(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getCompleteTasks(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addTimeSlot(Date start, Date stop) {
		// TODO Auto-generated method stub
		
	}

	public int getScheduleHours() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Date isNextAvailable(User u0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
