package jhg.acl;



public interface Secure {

	
	public int getId(); 
	public SecurityTag getTag();
	public SecurityPolicy getPolicy();
	public void setPermission(String perm,User user);
}
