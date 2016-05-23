package jhg.workflow;

import java.util.*;

public class TestWorkflow {

	
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		/*
		 * Define a workflow descriptively using tasks and registering them to each other as listeners.
		 * 	Define tasks and conditions.
		 * 
		 * Start a workflow.
		 * 
		 * 
		 * Progress through tasks using several users in roles.
		 * 
		 * See each person's work queue.
		 * 
		 * See workflow group from high level. 
		 * 	Be able to "poke"
		 *  Be able to reassign.
		 *  Administer roles.
		 *  
		 *  See progress.
		 *  Estimate completion.
		 *  Do metrics.
		 *  
		 * 
		 * 1. define
		 * 2. initiate
		 * 3. execute.
		 * 4. report.
		 * 
		 */
		Role r1 = new Role("Oven user");
		Role r2 = new Role("Counter user");
		Role r3 = new Role("Buyer");
		Role r4 = new Role("Server");
		User john = new User("John");
		User frank = new User("Frank");
		User joe = new User("Joe");
		User chet = new User("Chet");
		frank.getRoles().add(r1);
		joe.getRoles().add(r2);
		john.getRoles().add(r3);
		chet.getRoles().add(r4);
		User superMan = new User("Super Man");
		Role r0 = new Role("Manager.");
		superMan.getRoles().add(r0);
		User wonderWoman = new User("Wonder Woman");
		wonderWoman.getRoles().add(r0);
		wonderWoman.getRoles().add(r1);
		wonderWoman.getRoles().add(r2);
		wonderWoman.getRoles().add(r3);
		
		WorkflowDefinition wd = new WorkflowDefinition("Bake and serve Cookies");
		TaskDefinition td  = new TaskDefinition("Start",r0);
		TaskDefinition td0 = new TaskDefinition("Shop supplies.",r3);
		TaskDefinition td1 = new TaskDefinition("Preheat Oven",r1);
		TaskDefinition td2 = new TaskDefinition("Gather and measure Ingredients",r2);
		TaskDefinition td3 = new TaskDefinition("Mix",r2);
		TaskDefinition td4 = new TaskDefinition("Drop",r2);
		TaskDefinition td5 = new TaskDefinition("Place in Oven",r1);
		TaskDefinition td6 = new TaskDefinition("Wait 30 minutes.",r1);
		TaskDefinition td7 = new TaskDefinition("Take out.",r1);//warning! reminder setup
		TaskDefinition td8 = new TaskDefinition("Let Cool 15 minutes.",r2);
		TaskDefinition td9 = new TaskDefinition("Serve.",r4);
		TaskDefinition td10 = new TaskDefinition("Approve",r0);
		/*
		 * add estimation
		 * add timeslots for all
		 * add timeslots for individuals
		 * add priorities
		 */
		td0.setPriority(1);
		td0.setHours(8);
		int priority = td0.getPriority();
		int estimateHours = td0.getHours();
		td10.setIsLast(true);
		td10.dependsOn(td9);
		td9.dependsOn(td8);
		td8.dependsOn(td7);
		td7.dependsOn(td6);
		td6.dependsOn(td5);
		td5.dependsOn(td4);
		td4.dependsOn(td3);
		td3.dependsOn(td2);
		td2.dependsOn(new TaskDefinition[] {td1,td0});
		Case chocolateChipCase = new Case("chocolate chip cookies",john);
		
		Workflow wf = new Workflow(wd, chocolateChipCase);
		Date start = null;
		Date stop = null;
		wf.addTimeSlot(start,stop);
		int hours = wf.getScheduleHours();
		frank.setAvailability(start,stop);
		boolean isAvailable = frank.isAvailable(start);
		User[] users = new User[]{superMan,frank,joe,john,chet};
		wf.addUsers(users);
		wf.setAutoAssign(true);
		WorkflowDashboard wdb = wf.start();
		
		wf.start();
		
		wdb.report(superMan);
		
		while(wf.hasTasks()){
			List<Task> tasks = wf.nextTasks();
			for(Task t:tasks){
				boolean canStart = t.canStart();
				User u0 = t.getAssignee();
				Date d = wf.isNextAvailable(u0);
				//User user = wf.findUserForTask(t);
				//t.assignTask(user);
				Date taskStarted = t.getTimeStarted();
				Date taskCompleted = t.getTimeCompleted();				
				t.start(taskStarted);
				t.complete(taskCompleted);
				wdb.report();

				
				t.assignUser(u0);
				/*
				 * Progress on work.
				 * Task View: Tasks Completed/Who did them, Current tasks in progress and who is assigned, Remaining Tasks and who is assigned.
				 * User View: each user and what tasks they have done, are doing and will be doing.
				 */
				//int allTaskCount = wf.getTotalTasks();
				//int completedTaskCount = wf.getCompletedTaskCount();
				//int inProgressCount = wf.getInProgressCount();
				//User user = wf.getNextUserInRoleForTask(t);
				List<Task> taskList = wf.getAllTasks();
				List<Task> inProgressTasks = wf.getInProgressTasks();
				List<Task> remainingTasks = wf.getRemainingTasks();
				User u = wonderWoman;
				List<Task> inBoxWonderWoman = wf.getIncompleteTasks(u);
				List<Task> notStartedWonderWoman = wf.getNotStartedTasks(u);
				List<Task> ipBoxWonderWoman = wf.getInProgressTasks(u);
				List<Task> outBoxWonderWoman = wf.getCompleteTasks(u);
				Task task = wf.nextTask(u);
			}
		}
		
	}

}
