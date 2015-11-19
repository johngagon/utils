package jhg.diviner;

public enum Pentity {
	KING("\u265A"),   //king
	QUEEN("\u265B"),//queen
	ROOK("\u265C"), //rook
	BISHOP("\u265D"),  //bish
	KNIGHT("\u265E"),
	PAWN("\u265F");  //knight  //PAWN("\u265F");//pawn
	private String sym;
	private Pentity(String s){this.sym=s;}
	public String getSym(){return sym;}	
}
