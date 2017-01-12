package jhg.game.rpg;

public class Planned {

	
	
	public static void generate(){
		
		World dungeon = new World("Dungeon");
		Square r1c1 = new Square("The dirty cell.");
		Square r1c2 = new Square("The guarded hallway.");
		
		//starter skills
		Skill search = new Skill("search",CharClass.MAGE);
		Skill look = new Skill("look");                   //anyone.
		Skill picklock = new Skill("picklock",CharClass.ROGUE);
		Item plainkey = new Item("key");
		
		Condition hidden = new Condition("hidden");
		hidden.add(search,2);//1 is easy, 10 is hard.
		hidden.add(look,8);
		
		Condition locked = new Condition("locked");
		locked.either(plainkey);
		locked.either(picklock,2);
		
		
		Challenge door = new Challenge("Door");
		
		door.add(hidden);//difficulty 50-50 chance
		door.add(locked);
		//door.add(Condition.LOCKED);
		
		r1c1.addExit(r1c2,door);
	}
}
