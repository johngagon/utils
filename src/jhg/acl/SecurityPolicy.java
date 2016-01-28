package jhg.acl;

import java.util.List;

public class SecurityPolicy {

	public static SecurityPolicy createPolicy(User owner, String[] actions) {
		return new SecurityPolicy(owner,actions);
	}
	
	private User owner;
	private String[] actions;
	private List<User> allowedUsers;
	private SecurityPolicy(User o,String[] a){
		this.owner = o;
		this.actions = a;
	}
	private SecurityPolicy(User o,String[] a,List<User> allAllowed){
		this(o,a);
		this.allowedUsers = allAllowed;
	}
	public boolean hasAllowedUsers(){
		return this.allowedUsers!=null;
	}
	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}
	/**
	 * @return the actions
	 */
	public String[] getActions() {
		return actions;
	}
	public boolean allowsOwner(User user) {
		return this.owner.isSameIdentityAs(user);
	}
	public boolean contains(String permission) {
		for(String s:actions){
			if(s.equals(permission)){
				return true;
			}
		}
		return false;
	}
	public boolean contains(User user) {
		for(User u:allowedUsers){
			if(u.isSameIdentityAs(user)){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Declare a list of actions.
	 * 
	 */
}
