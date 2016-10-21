package jhg.simms;

import java.util.*;

public class Location {
	
	@SuppressWarnings("unused")
	private String name;

	private List<Character> characters;
	
	public Location(String string) {
		super();
		this.name = string;
		this.characters = new ArrayList<Character>();
	}

	public void addCharacter(Character aCharacter){
		this.characters.add(aCharacter);
	}

	public List<Character> getCharacters() {
		return characters;
	}
	
}
