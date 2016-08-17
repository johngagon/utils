package jhg.simms;

import java.util.*;

import jhg.util.Log;

public class Character {

	private String name;
	
	//1-10 
	int bravery;
	int libido;

	Gender gender;
	Orientation orientation;

	List<Character> avoiding;
	List<Character> engaging;
	

	public Character(String string, Gender g, Orientation o, int int1, int int2) {
		super();
		this.name = string;
		this.bravery = int1;
		this.libido = int2;
		this.gender = g;
		this.orientation = o;
		this.avoiding = new ArrayList<Character>();
		this.engaging = new ArrayList<Character>();
	}
	
	public boolean avoiding(Character c){
		return avoiding.contains(c);
	}
	public boolean engaging(Character c){
		return engaging.contains(c);
	}	
	public void avoid(Character c){
		Log.println(this.getName()+" is now avoiding "+c.getName());
		this.avoiding.add(c);
	}
	public void engage(Character c){
		Log.println(this.getName()+" is now engaging with "+c.getName());
		this.engaging.add(c);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}




	public String getName() {
		return name;
	}



	public int getBravery() {
		return bravery;
	}



	public int getLibido() {
		return libido;
	}



	public Gender getGender() {
		return gender;
	}



	public Orientation getOrientation() {
		return orientation;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	private int roll(int limit){
		int rv = (int)(Math.random()*limit+1);
		
		return rv;
	}	
	
	public void approach(Character c) {
		int roll = roll(10);
		Log.println("\n"+this.getName()+" approaches "+c.getName()+" with them checking bravery with a roll of "+roll+" <= "+c.bravery+".");
		if(roll <= c.bravery){
			engage(c);
			if(this.libido>=5 && c.libido>=5){
				c.engage(this);
			}else{
				if(c.libido<10){
					c.libido++;
				}
				if(this.libido<10){
					this.libido++;
				}
			}
		}else{
			if(roll==10){
				c.avoid(this);
			}else{
				Log.println(c.name+" evades but does not avoid "+this.name+".");
			}
		}
		/*
		 * formula: 
		 * 
		 * assuming attraction. (use types)    TODO implement likes and has later e.g.: black hair
		 * 
		 * 
		 * roll bravery on c, 
		 * add bonus for libido of this+c / 2
		 * if roll < bravery: success. 
		 * 		engage		
		 * else 
		 * 		if(roll==10) avoid
		 * 		else  no result.
		 * 
		 *  
		 */
		
		
	}



	
	

}
