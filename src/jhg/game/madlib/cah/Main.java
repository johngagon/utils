package jhg.game.madlib.cah;

public class Main {

	public static void main(String[] arg){
		
		Template a = new Template("And the Academy Award for $1 goes to $2.",2);
		Template b = new Template("Maybe she's born with it. Maybe it's $1.",1);
		Filler x = new Filler("Count Chocula");
		Filler y = new Filler("Estrogen");
		Filler z = new Filler("Friendly Fire");
		
		Combiner c = new Combiner();
		c.add(a);
		c.add(b);
		c.add(x);
		c.add(y);
		
		MergeOut o = c.next();
		String s = o.asString();
		System.out.println(s);
	}
	
	
	
}
