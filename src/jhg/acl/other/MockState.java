package jhg.acl.other;


public enum MockState implements AccessState{

	PRIVATE,
	SHARED,
	PUBLISHED;
	

	@Override
	public String getStateName() {
		return this.name();
	}
}
