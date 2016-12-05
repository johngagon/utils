package jhg.weaseler;

import java.util.*;

class Weasel {

	public Weasel(){
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
