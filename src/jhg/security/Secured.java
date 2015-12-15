package jhg.security;

public interface Secured {

	public SecurityTag getTag();
	
	public boolean canRoleDo(Action action);
	
	public void perform(Role role,Action action);
	
}
