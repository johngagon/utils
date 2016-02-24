package jhg.acl;

import java.util.Date;
import java.util.List;

public class TestAcl {

	public static void start(){
		System.out.println("TestAcl Start "+new Date());
		System.out.println("-----------------------------");
	}
	public static void end(){
		System.out.println("TestAcl End "+new Date());
	}
	
	
	public static void main(String[] args){
		start();
		testSecuritySupportObject();
		end();
	}
	private static void testSecuritySupportObject(){
		User creator = MockUser.find(MockUser.JOHN);
		 
		MockSecuritySupportedObject secure = new MockSecuritySupportedObject(creator,"A");
		//String msg = secure.read(creator);
		//secure.write(creator,"A");
		//secure.tinker(creator);
		
		List<User> users = MockUser.allUsers();
		
		testPermsAll(users,secure);
		secure.getTag().printAclTable();
		
		secure.share(creator);
		testPermsAll(users,secure);
		secure.getTag().printAclTable();
		
		secure.publish(creator);
		testPermsAll(users,secure);
		secure.getTag().printAclTable();
				
	}
	
	@SuppressWarnings("unused")
	private static void testSecuredObject(){
		User creator = MockUser.find(MockUser.JOHN);
		 
		MockSecuredObject secure = new MockSecuredObject(creator,"A");

		
		List<User> users = MockUser.allUsers();
		
		testPermsAll(users,secure);
		secure.printAclTable();
		
		secure.share(creator);
		testPermsAll(users,secure);
		secure.printAclTable();
		
		secure.publish(creator);
		testPermsAll(users,secure);
		secure.printAclTable();
		
		//secure.flushAccessLog();		
	}
	
	private static void testPermsAll(List<User> users, Mock secure){
		for(User u:users){
			secure.read(u);
			secure.write(u,"A");
			secure.tinker(u);			
		}
	}	

}
