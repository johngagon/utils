package jhg.simms;

import java.util.*;

import jhg.util.Log;

public class Story {

	private List<Location> locations;
	
	
	public Story(){
		super();
		locations = new ArrayList<Location>();
	}
	
	public void addLocation(Location aLocation){
		this.locations.add(aLocation);
	}
	private boolean isRomanticallyCompatible(Character first, Character another){
		boolean rv = false;
		if(first.orientation.equals(Orientation.STRAIGHT) && another.orientation.equals(Orientation.STRAIGHT)){
			if(first.gender.equals(Gender.MALE) && another.gender.equals(Gender.FEMALE)){
				rv = true;
			}
			if(first.gender.equals(Gender.FEMALE) && another.gender.equals(Gender.MALE)){
				rv = true;
			}
		}else if(first.orientation.equals(Orientation.GAY) && another.orientation.equals(Orientation.GAY)){
			if(first.gender.equals(Gender.MALE) && another.gender.equals(Gender.MALE)){
				rv = true;
			}
			if(first.gender.equals(Gender.FEMALE) && another.gender.equals(Gender.FEMALE)){
				rv = true;
			}			
		}
		//TODO add other complexity.
		return rv;
	}
	private boolean notEngaging(Character first, Character second){
		boolean engaging = first.engaging(second) || second.engaging(first);
		return !engaging;
	}
	private boolean notAvoiding(Character first, Character second){
		boolean avoiding = first.avoiding(second) || second.avoiding(first);
		return !avoiding;
	}	
	
	public void start() {
		Log.println("Starting");
		for(Location l:locations){
			List<Character> characters = l.getCharacters();
			for(Character c: characters){
				for(Character d:characters){
					if(!c.equals(d)){
						
						if(isRomanticallyCompatible(c,d) && notEngaging(c,d) && notAvoiding(c,d)){
							Log.println("\n\n"+c.getName()+" is romantically compatible with "+d.getName());
							if(c.bravery>d.bravery){
								Log.println(c.getName()+" is braver than "+d.getName()+" "+c.bravery+" vs "+d.bravery);
								while((!d.avoiding(c)) && (!c.engaging(d))){
									c.approach(d);
								}
								/*
								if(d.avoiding(c)){
									Log.println(d.getName()+" is already avoiding "+c.getName());
								}
								if(c.engaging(d)){
									Log.println(c.getName()+" is already engaging "+d.getName());
								}
								*/
							}else{
								Log.println(d.getName()+" is braver than "+c.getName()+" "+d.bravery+" vs "+c.bravery);
								while((!c.avoiding(d)) && (!d.engaging(c))){
									d.approach(c);
								}
								/*
								if(c.avoiding(d)){
									Log.println(c.getName()+" is already avoiding "+d.getName());
								}	
								if(d.engaging(c)){
									Log.println(d.getName()+" is already engaging "+c.getName());
								}	
								*/							
							}
						}
						
						/*
						 * oneKnowsTheOtherHasSomethingTheyWant
						 */
						
						
					}
				}
			}
		}
		
	}
	

	
	
	
}
