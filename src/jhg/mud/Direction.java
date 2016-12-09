package jhg.mud;

public enum Direction {

	N("north"),
	S("south"),
	E("east"),
	W("west"),
	U("up"),
	D("down");
	
	private String fullName;
	private Direction(String aName){
		this.fullName = aName;
	}
	public Direction opposite(){
		switch(this){
			case N:return S;
			case S:return N;
			case E:return W;
			case W:return E;
			case U:return D;
			case D:return U;
		}
		return null;
	}
	public String fullName(){
		return this.fullName;
	}
}
