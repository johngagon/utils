package jhg.acl;

import java.util.ArrayList;
import java.util.List;

public class MockUser implements User {

	private String identity;
	public MockUser(String name){
		this.identity = name;
	}
	@Override
	public String getIdentity() {
		return identity;
	}
	
	public static final String JOHN = "John";
	public static final String FRANK = "Frank";//can't see name, can count.
	public static final String JOE = "Joe";
	public static final String CHET = "Chet";
	public static final String FENTON = "Fenton";
	public static final String JOHNQ = "JohnQ";
	
	private static User owner = new MockUser(JOHN);
	private static User blinded = new MockUser(FRANK);
	private static User sharedReader = new MockUser(JOE);
	private static User sharedContributer = new MockUser(CHET);
	private static User publishedReader = new MockUser(FENTON);
	public static List<User> allUsers(){
		List<User> l = new ArrayList<User>();
		l.add(owner);
		l.add(blinded);
		l.add(sharedReader);
		l.add(sharedContributer);
		l.add(publishedReader);
		return l;
	}
	//no one's allowed to write when it's public
	private static User johnq = new MockUser(JOHNQ);
	public static User find(String s) {
		switch(s){
			case JOHN : return owner;
			case FRANK: return blinded;
			case JOE: return sharedReader;
			case CHET: return sharedContributer;
			case FENTON: return publishedReader;
			default : return johnq;
		}
	}
	@Override
	public boolean isMemberOf(User user) {
		return this.equals(user);////single identity although we could check list contains.
	}
	@Override
	public boolean hasMember(User user) {
		return this.equals(user);//single identity although we could check list contains.
	}
	@Override
	public boolean isSameIdentityAs(User user) {
		return this.equals(user);
	}
	
}
