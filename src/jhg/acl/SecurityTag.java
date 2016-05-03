package jhg.acl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SecurityTag {

	private Map<String,List<User>> permissions;
	private User owner;
	private AccessLog accessLog;
	private Secure secured;


	
	public SecurityTag(User o,Secure s){
		this.owner = o;
		this.secured = s;
		permissions = new Hashtable<String,List<User>>();
		SecurityPolicy policy = secured.getPolicy();
		String[] actions = policy.getActions();
		for(String action:actions){
			permissions.put(action,new ArrayList<User>());
		}
		this.accessLog = new AccessLog();
	}
	void setPermission(String permission,User user){
		SecurityPolicy policy = secured.getPolicy();
		if(policy.contains(permission)){
			/*
			if(policy.hasAllowedUsers()){
				if(policy.contains(user)){
					permissions.set(permission,permissions.get(permission).add(user));
				}else{
					accessLog.logBadSetAttempt(user,secured);
				}
			}else{
			*/
				List<User> pUsers = permissions.get(permission);
				pUsers.add(user);
				permissions.put(permission,pUsers);
			/*
			}
			*/
		}else{
			accessLog.logBadSetAttempt(permission,secured);
		}
	}
	
	
	public User getOwner(){
		return owner;
	}
	
	//could use predefined state or permission.
	public boolean check(User user, String perm) {
		boolean rv = false;
		if(owner.isSameIdentityAs(user)){
			rv = true;
		}else if(permissions.containsKey(perm)){
			List<User> validUsrs = permissions.get(perm);
			if(validUsrs.contains(user)){
				User valid = validUsrs.get(validUsrs.indexOf(user));
				rv = (valid.isSameIdentityAs(user) || valid.hasMember(user));
			}
			
		}
		accessLog.log(secured,user,perm,rv);
		return rv;
	}
	
	public void forcedCheck(User user, String perm) throws IllegalAccessException{
		if(!check(user,perm)) throw new IllegalAccessException("User "+user.getIdentity()+" cannot perform "+perm);
	}
	public void print(){
		accessLog.print();
	}
	public void clear() {
		accessLog.clear();
		
	}
	public void printAclTable(){
		for(String p:permissions.keySet()){
			List<User> users = permissions.get(p);
			String userList = listToCsv(users);
			System.out.println(p+":"+userList);
		}
	}
	public static final String listToCsv(List<User> users){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(User u:users){
			sb.append(first?"":", ");
			first=(first)?false:first;
			sb.append(u.getIdentity());
		}
		return sb.toString();
	}
}
