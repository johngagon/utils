package jhg.security;

import java.util.ArrayList;
import java.util.List;

public class SecurityManager<T> {

	@SuppressWarnings("unused")
	private Class<T> clazz;
	private Role owningRole;
	private List<SecurityTag> tags;
	private List<Action> actions;
	private List<RoleAssignment> assignments;
	
	public SecurityManager(Role owning){
		super();
		owningRole = owning;
		tags = new ArrayList<SecurityTag>();
		actions = new ArrayList<Action>();
		assignments = new ArrayList<RoleAssignment>();
	}
	public Role getOwner(){
		return owningRole;
	}
	
	
	public void addSecurityTag(SecurityTag tag){
		tags.add(tag);
	}
	public void addAssignment(Role role, List<Action> actions){
		RoleAssignment assignment = new RoleAssignment(role);
		for(Action a:actions){
			assignment.addAction(a);
		}
		assignments.add(assignment);
	}
	public List<RoleAssignment> getAssignments(){
		return this.assignments;
	}
	public List<Action> getActions(){
		return this.actions;
	}
	
	public boolean canRoleDoActionObject(Role role, Action action, SecurityTag tag){
		boolean rv = false;
		if(owningRole.equals(role)){
			return true;
		}
		for(RoleAssignment assignment:assignments){
			Role assignmentRole = assignment.getRole();
			if(assignmentRole.equals(role)){
				List<Action> assignmentActions = assignment.getActions();
				if(assignmentActions.contains(action)){
					rv = tag.isAllowedAction(action);
				}
			}
		}
		return rv;
	}
	/*
	public void performAction(User user, Action action, SecurityTag tag){
		if(canRoleDoActionObject(user.getRole(),action,tag){
			tag.performAction(user,action);
		}
	} FIXME
	*/
}
