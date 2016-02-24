package jhg.acl;

import java.util.ArrayList;
import java.util.List;

public class MockStandardSecuritySupportedObject{/* extends StandardSecuritySupport {

	
	static{
		ObjectRegistry instance = ObjectRegistry.getInstance();
		instance.register(MockStandardSecuritySupportedObject, MockUser.JOHN);
	}
	
	private String message;
	
	public MockStandardSecuritySupportedObject(User u, String m){
		super(user);
		this.message = m;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	
	
	
	private static List<MockSecuritySupportedObject> instances = new ArrayList<MockSecuritySupportedObject>();	
	

	
	public MockSecuritySupportedObject(User user, String m) {
		super(user,owner, perms);
		this.msg = m;
		tag.setPermission(TINKER,MockUser.find(MockUser.FRANK));//tinker substitute for count.
		instances.add(this);
	}
	
	public String read(User user){
		return (tag.check(user,READ))?msg:"";
	}
	public void write(User user, String m){
		this.msg = tag.check(user, WRITE)?m:msg;
	}
	public void tinker(User user){
		if(!tag.check(user, TINKER)){return;}
	}	
	public void share(User user){
		if(!tag.check(user, SHARE)){return;}
		tag.setPermission(TINKER,MockUser.find(MockUser.JOE));
		tag.setPermission(READ,MockUser.find(MockUser.JOE));
		
		tag.setPermission(TINKER,MockUser.find(MockUser.CHET));
		tag.setPermission(READ,MockUser.find(MockUser.CHET));
		tag.setPermission(WRITE,MockUser.find(MockUser.CHET));		
	}
	public void publish(User user){
		if(!tag.check(user, PUBLISH)){return;}
		tag.setPermission(TINKER,MockUser.find(MockUser.FENTON));
		tag.setPermission(READ,MockUser.find(MockUser.FENTON));
		tag.setPermission(READ,MockUser.find(MockUser.FRANK));
	}
	
	@Override
	public int getId() {
		return hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 *
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 *
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MockStandardSecuritySupportedObject other = (MockStandardSecuritySupportedObject) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}	*/	
	
}
