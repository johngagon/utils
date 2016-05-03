package jhg.acl;

import java.util.ArrayList;
import java.util.List;

public class AccessLog {

	private List<String> messages;
	public AccessLog(){
		super();
		this.messages = new ArrayList<String>();
	}
	public void log(Secure s, User u, String perm, boolean allow){
		String allowStr = (allow)?"allowed":"denied";
		String m = "User "+u.getIdentity() + " "+allowStr+" permission to "+perm+" on "+s.getId()+"."; 
		messages.add(m);
	}
	public void print(){
		for(String m:messages){
			System.out.println(m);
		}
	}
	public void logBadSetAttempt(String permission, Secure secured) {
		String m = "Permission "+permission+" not found on policy for "+secured.getClass()+".";
		messages.add(m);
	}
	public void logBadSetAttempt(User user, Secure secured) {
		String m = "User "+user.getIdentity()+" not found on policy for "+secured.getClass()+".";
		messages.add(m);
	}
	public void clear() {
		messages.clear();
		
	}
}
