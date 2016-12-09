package jhg.mud;

import java.util.*;

public class Room extends Base {

	private Map<Direction,Room> adjacent;
	private List<Character> characters;
	private List<Item> items;
	
	Room(String aname){
		super(aname);
		adjacent = new Hashtable<Direction,Room>();
		characters = new Vector<Character>();
		items = new Vector<Item>();
	}
	public List<Character> getCharacters(){
		return this.characters;
	}
	public boolean hasCharacter(String query) {
		for(Character c:characters){
			if(c.name.equals(query)){
				return true;
			}
		}
		return false;
	}
	public Character getCharacter(String query) {
		for(Character c:characters){
			if(c.name.equals(query)){
				return c;
			}
		}
		return null;		
	}	
	
	void addRoom(Direction d, Room r){
		adjacent.put(d,r);
		r.adjacent.put(d.opposite(),this);
	}

	public void put(Item a){
		a.setRoom(this);
		super.put(a);
	}	
	
	public void put(Character c) {
		characters.add(c);
		c.setRoom(this);
	}

	
	public boolean hasRoom(Direction d){
		return this.adjacent.containsKey(d);
	}
	
	public Room getRoom(Direction d){
		return this.adjacent.get(d);
	}




}
