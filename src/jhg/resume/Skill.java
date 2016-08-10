package jhg.resume;

import java.util.*;

class Skill {

	public Skill(){
		super();
	}
	int score = 1;
	String name = "";
	List<String> tokens = new ArrayList<String>();
	
	@Override
	public String toString() {
		return "Skill [score=" + score + ", name=" + name + ", tokens="	+ tokens + "]";
	}

	
	
}
