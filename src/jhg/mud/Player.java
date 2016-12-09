package jhg.mud;

import java.util.*;

public class Player extends Character {

	
	public Player(String string) {
		super(string);
	}

	public void put(Item a){
		a.setPlayer(this);
		super.put(a);
	}		
	
	public void look(String query){
		String msg = Message.CANTFIND.message();
		/*
		 * 1 The room itself
		 * 2 An object in the room
		 * 3 A character in the room OR Another player in the room
		 * 
		 * 4 An item on the character OR player in the room
		 * 5 An object in another object in the room.
		 * An item they are holding.
		 * 
		 * 
		 */
		if(query.equals(room.name)){
			msg =  room.getDescription();//1
		}else{
			if(room!=null){

				if(room.hasCharacter(query)){
					msg =  room.getCharacter(query).getDescription();//3
				}
				if(room.hasItem(query)){
					msg =  room.getItem(query).getDescription();//2
				}
				

				List<Character> characters = room.getCharacters();
				for(Character c:characters){
					if(c.hasItem(query)){
						msg =  c.getItem(query).getDescription();//4
					}
				}
				List<Item> roomItems = room.getItems();
				for(Item item:roomItems){
					if(item.hasItem(query)){
						msg =  item.getItem(query).getDescription();//5
					}
				}				
			}
			for(Item item:items){
				if(item.hasItem(query)){
					msg =  item.getItem(query).getDescription();//5
				}
				
			}
			
		}
		notify(msg);
	}
	

	public void move(Direction d){
		if(room==null){
			notify(Message.PLAYER_IS_IN_THE_VOID);
		}else{
			if(!room.hasRoom(d)){
				notify(Message.NO_EXIT_HERE);
			}else{
				Room r = room.getRoom(d);
				r.put(this);
				notify(this.name+" goes "+d.fullName()+" to "+r.name);
			}
		}
	}


	public void north() { 
		move(Direction.N);
	}
	public void east() {
		move(Direction.E);
	}
	public void south() {
		move(Direction.S);
	}
	public void west() {
		move(Direction.W);
	}
	public void up(){
		move(Direction.U);
	}
	public void down(){
		move(Direction.U);
	}	
	
	


}
