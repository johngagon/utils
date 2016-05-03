package jhg.amlan.predicates;

@SuppressWarnings("unused")
public class Come extends Common {

	public Come(){
		super(Argument.NULL);
	}
	public Come(Argument subject){
		super(subject);
	}
	
	private Argument destination;
	private Argument origin;
	private Argument route;
	private Argument vehicle;
	public void x(Argument destination, Argument origin, Argument route, Argument vehicle){
		
	}
	
	//klama     kla      come        x1 comes/goes to destination x2 from origin x3 via route x4 using means/vehicle x5
	/*
	 * to
	 * from
	 * via
	 * by/with
	 */
}
