package jhg.mud;

public enum Message {

	CANTFIND("You can't see any."), 
	PLAYER_IS_IN_THE_VOID("This player is not located in the world."), 
	NO_EXIT_HERE("There's no exit here.");
	
	private String msg;
	private Message(String s){
		this.msg = s;
	}
	public String message(){
		return this.msg;
	}
	
}
