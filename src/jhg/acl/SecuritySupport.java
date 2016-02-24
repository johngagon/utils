package jhg.acl;

import java.util.List;

public abstract class SecuritySupport implements Secure {

	protected SecurityTag tag;	
	protected SecurityPolicy policy; 
	
	public SecuritySupport(User user, User owner, String[] perms){
		
		policy = SecurityPolicy.createPolicy(owner,perms);
		if(!policy.allowsOwner(user)){
			throw new IllegalAccessError("Policy does not allow this owner:"+user);
		}else{
			this.tag = new SecurityTag(user,this);			
		}
	}
	
	@Override
	public SecurityPolicy getPolicy() {
		return policy;
	}	
	
	@Override
	public void setPermission(String perm, User user) {
		tag.setPermission(perm, user);
	}
	
	@Override
	public int getId() {
		return hashCode();
	}

	@Override
	public SecurityTag getTag() {
		return tag;
	}

	public void setPermissions(User u, List<Secure> securedList, User user, String perm){
		if(policy.getOwner().isSameIdentityAs(u)){
			for(Secure s:securedList){
				s.setPermission(perm,user);
			}
		}
	}
	//TODO add in better support from all the stuff in MockSecuredObject...minimize the amount of security code in there. 

}
