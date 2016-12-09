package jhg.mud;

import java.util.*;

public class Main {

	/*
	 * placing various items.
	 * looking at each thing.
	 * transferring items.
	 */
	
	public static void main(String[] args){
		
		World world = new World();
		Room home = world.homeRoom();
		Room easthome = world.createRoom("East Home",home,Direction.E);//Fix overlap by using coordinates.
		Player p = world.createPlayer("John");
		
		Character c = world.createCharacter("Shaggybum");
		Item v = world.createItem("Hammer");
		Item w = world.createItem("Wand");
		Item x = world.createItem("Sword");
		Item y = world.createItem("Chair");
		Item z = world.createItem("Bucket");
		home.put(p);
		p.put(w);
		home.put(y);
		c.put(x);
		z.put(v);
		
		
		p.north();//room is home.
		Room r = p.getRoom();
		assert r.equals(home);
		System.out.println("r: "+r.getName());
		
		p.east();
		r = p.getRoom();
		assert r.equals(easthome);
		System.out.println("r: "+r.getName());
		
		p.west();
		r = p.getRoom();
		assert r.equals(home);
		System.out.println("r: "+r.getName());
		
		
		home.give("Chair",p);
		c.give("Sword", p);
		p.look("Sword");
		p.give("Sword",home);
		System.out.println("Done");

		/*

		 * 
		 */
	}
	
}
