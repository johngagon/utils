package jhg.security;

import java.util.*;


public class RoleAssignment {
	
	private Role role;
	private List<Action> actions;
	public RoleAssignment(Role role){
		this.role = role;
		this.actions = new ArrayList<Action>();
	}

	public void addAction(Action a){
		actions.add(a);
	}
	public List<Action> getActions(){
		return this.actions;
	}
	public Role getRole(){
		return this.role;
	}
}
