package jhg.acl;

//can be role or individual or group or hierarchy or group of groups
public interface User {

	public String getIdentity();
	public boolean isMemberOf(User user);
	public boolean hasMember(User user);
	public boolean isSameIdentityAs(User user);
}
