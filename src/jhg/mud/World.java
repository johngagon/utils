package jhg.mud;

import java.util.*;

public class World {

	private Set<String> names;
	
	private Room home;
	
	public World(){
		this.home = new Room("Center of the Universe");
		this.names = new HashSet<String>();
	}
	
	public Set<String> getNames(){
		return this.names;
	}
	
	public Room homeRoom() {
		return home;
	}

	public Room createRoom(String name, Room hookRoom, Direction direction) {
		Room r = new Room(name);
		hookRoom.addRoom(direction,r);
		return r;
	}

	public Player createPlayer(String string) {
		return new Player(string);
	}

	public Character createCharacter(String string) {
		return new Character(string);
	}

	public Item createItem(String string) {
		return new Item(string);
	}



	
}
