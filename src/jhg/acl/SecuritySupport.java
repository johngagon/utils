package jhg.acl;

public abstract class SecuritySupport implements Secure {

	private SecurityTag tag;
	private SecurityPolicy policy;
	
	@Override
	public int getId() {
		return hashCode();
	}

	@Override
	public SecurityTag getTag() {
		return tag;
	}

	@Override
	public SecurityPolicy getPolicy() {
		return policy;
	}
	//TODO add in better support from all the stuff in MockSecuredObject...minimize the amount of security code in there. 

}
