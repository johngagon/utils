package jhg.mud;

import java.util.*;

public abstract class Base {

	protected List<Item> items;
	protected String name;
	protected String description;
	
	public Base(String aname){
		super();
		items = new Vector<Item>();
		name = aname;
		description = "A very non-descript "+name.toLowerCase();
	}
	
	public void put(Item a){
		this.items.add(a);
	}
	public List<Item> items(){
		return this.items;
	}	
	
	protected void notify(Message msg){
		System.out.println(msg.message());
	}
	protected void notify(String string) {
		System.out.println(string);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String s) {
		this.description = s;
	}	
	
	public void give(String string, Room r) {
		int given = -1;
		for(Item i:items){
			if(i.getName().equals(string)){
				given = items.indexOf(i);
				break;
				
			}
		}
		if(given!=-1){
			r.put(items.remove(given));
		}
	}
	public void give(String string, Player p) {
		int given = -1;
		for(Item i:items){
			if(i.getName().equals(string)){
				given = items.indexOf(i);
				break;
				
			}
		}
		if(given!=-1){
			p.put(items.remove(given));
		}
	}
	
	public void give(String string, Item item) {			//Container
		int given = -1;
		for(Item i:items){
			if(i.getName().equals(string)){
				given = items.indexOf(i);
				break;
				
			}
		}
		if(given!=-1){
			item.put(items.remove(given));
		}
	}
	
	public void give(String string, Character c) {			
		int given = -1;
		for(Item i:items){
			if(i.getName().equals(string)){
				given = items.indexOf(i);
				break;
				
			}
		}
		if(given!=-1){
			c.put(items.remove(given));
		}
	}		
	

	public boolean hasItem(String query) {
		for(Item x:items){
			if(x.name.equals(query)){
				return true;
			}
		}
		return false;
	}
	public Item getItem(String query) {
		for(Item x:items){
			if(x.name.equals(query)){
				return x;
			}
		}
		return null;
	}	
	public List<Item> getItems(){
		return this.items;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
	
	
}
