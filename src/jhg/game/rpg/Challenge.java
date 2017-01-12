package jhg.game.rpg;

import java.util.*;

public class Challenge {

	public Challenge(String string) {
		// TODO Auto-generated constructor stub
	}
	public static enum Type{
		FINAL,
		EXIT,
		
	}
	
	public static enum Difficulty{
		EASY,
		HARD;
	}
	
	private Square nextArea;
	private Map<Card,Integer> requires;
	private List<Card> rewards;
	private boolean passed;
	public void add(Condition hidden) {
		// TODO Auto-generated method stub
		
	}

	
}
