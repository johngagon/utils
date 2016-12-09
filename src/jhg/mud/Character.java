package jhg.mud;

import java.util.*;

public class Character extends Base {
	
	protected Room room;
	
	
	
	public Character(String string) {
		super(string);
		this.items = new Vector<Item>();
		description = name +" appears very ordinary.";
	}
	public void put(Item a){
		a.setCharacter(this);
		super.put(a);
	}	
	public List<Item> getItems(){
		return this.items;
	}
	
	public boolean isInRoom(){
		return this.room != null;
	}
	void setRoom(Room aRoom) {
		this.room = aRoom;
	}	

	public Room getRoom() {
		return room;
	}



}
