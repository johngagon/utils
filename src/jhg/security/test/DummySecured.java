package jhg.security.test;

import jhg.security.*;

/**
 * Mutable string class.
 * @author jgagon
 *
 */
public class DummySecured implements Secured {

	private String val;
	
	public DummySecured(){
		this.val = "";
	}
	public DummySecured(String v){
		this.val = v;
	}	
	
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}

	@Override
	public SecurityTag getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTag(SecurityTag tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canRoleDo(Action action) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void perform(Role role, Action action) {
		// TODO Auto-generated method stub
		
	}

}
