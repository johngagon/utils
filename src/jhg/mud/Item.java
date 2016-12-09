package jhg.mud;
;

public class Item  extends Base {
	
	private Room room;
	private Player player;
	private Character character;
	private Item container;
	
	
	public Item(String string) {
		super(string);
	}
	

	
	private void clear(){
		room = null;
		player = null;
		character = null;
		container = null;
	}
	
	public boolean isInRoom(){
		return this.room != null;
	}
	void setRoom(Room aRoom) {
		clear();
		this.room = aRoom;
	}	
	public Room getRoom() {
		return room;
	}

	public boolean isOnCharacter(){
		return this.character != null;
	}
	void setCharacter(Character a) {
		clear();
		this.character = a;
	}	
	public Character getCharacter() {
		return character;
	}
	
	public boolean isOnPlayer(){
		return this.room != null;
	}
	void setPlayer(Player a) {
		clear();
		this.player = a;
	}	
	public Player getPlayer() {
		return this.player;
	}	

	public boolean isInContainer(){
		return this.container != null;
	}
	public Item getContainer() {
		return this.container;
	}	
	
	
	private void setContainer(Item a) {
		clear();
		this.container = a;
	}	
	

	public void put(Item a){
		a.setContainer(this);
		super.put(a);
	}



}
