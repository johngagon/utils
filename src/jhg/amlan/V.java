package jhg.amlan;

public enum V {

	COME,
	GO,          //x1 comes/goes to destination x2 from origin x3 via route x4 using means/vehicle x5
	MOVE,
	GIVE,
	TAKE,
	LET,
	MAKE,
	PUT,
	SEEM,
	LOOK,
	HEAR,
	SAY
	
	;
	/*
	 * motion (come-movehere) (go-move you-there, else-where)
	 * transfer (give get) 
	 */
	
	private V(){
		
	}
	
	public V x(P pronoun){
		/*
		 * TODO impl
		 */
		return this;
	}

	
	public String out(){
		return "";
	}
	
}
