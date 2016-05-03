package jhg.diviner.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jhg.util.Log;

@SuppressWarnings("boxing")
public class Card {
	
	/*
	 * 2:Duality
	 * 3:Trinity
	 * 4:Element
	 * 5:Pentity
	 * 6:Color
	 * 7:Planet
	 * 8:Trigram
	 * 
	 * 9: ClassicPlanets/Muses
	 * 10: Sephiroth Tree of Life
	 * 12: Zodiac
	 * 14: 2690-C
	 * 18: 
	 * x 24: Futhark
	 * 36: Bopomofo
	 * 72: Shemhamphorasch
	 */
	
	public static class Twos{
		private static final int TOP = 36;
		private final int rank;
		private final Duality suit;
		private Twos(int num, Duality suit){this.rank = num;this.suit = suit;}
		public int getRank() {return rank;}
		public Duality getSuit() {return suit;}
		public Bopomofo getInverseSuit(){
			Bopomofo z = Bopomofo.values()[rank-1];
			return z;
		}
		public int getInverseRank(){
			return suit.ordinal()+1;
		}		
		private static final List<Twos> protoDeck = new ArrayList<Twos>();
	    static {
	        for (Duality suit : Duality.values()){
	        	for (int i = 1; i <= TOP; i++){protoDeck.add(new Twos(i,suit));}	}
	    }
	    public static List<Twos> newDeck() {return new ArrayList<Twos>(protoDeck);}	    
	}
	
	public static class Threes{
		private static final int TOP = 24;
		private final int rank;
		private final Trinity suit;
		private Threes(int num, Trinity suit){this.rank = num;this.suit = suit;}
		public int getRank() {return rank;}
		public Trinity getSuit() {return suit;}
		public Futhark getInverseSuit(){
			Futhark z = Futhark.values()[rank-1];
			return z;
		}
		public int getInverseRank(){
			return suit.ordinal()+1;
		}		
		private static final List<Threes> protoDeck = new ArrayList<Threes>();
	    static {
	        for (Trinity suit : Trinity.values()){
	        	for (int i = 1; i <= TOP; i++){protoDeck.add(new Threes(i,suit));}	}
	    }
	    public static List<Threes> newDeck() {return new ArrayList<Threes>(protoDeck);}	    
	}
	
	public static class Fours{
		private static final int TOP = 18;
		private final int rank;
		private final Element suit;
		private Fours(int num, Element suit){this.rank = num;this.suit = suit;}
		public int getRank() {return rank;}
		public Element getSuit() {return suit;}
		public Dunon getInverseSuit(){
			Dunon z = Dunon.values()[rank-1];
			return z;
		}
		public int getInverseRank(){
			return suit.ordinal()+1;
		}		
		private static final List<Fours> protoDeck = new ArrayList<Fours>();
	    static {
	        for (Element suit : Element.values()){
	        	for (int i = 1; i <= TOP; i++){protoDeck.add(new Fours(i,suit));}	}
	    }
	    public static List<Fours> newDeck() {return new ArrayList<Fours>(protoDeck);}	    
	}	
	
	public static class Fives{
		private static final int MAX = 5;
		private static final int TOP = 14;
		private final int rank;
		private final Pentity suit;
		private Fives(int num, Pentity suit){this.rank = num;this.suit = suit;}
		public int getRank() {return rank;}
		public Pentity getSuit() {return suit;}
		public Fortnight getInverseSuit(){                  
			if(suit.equals(Pentity.PAWN)){
				return Fortnight.A15;
			}else{
				Fortnight z = Fortnight.values()[rank-1];   
				return z;
			}
		}
		public int getInverseRank(){
			if(suit.equals(Pentity.PAWN)){
				return this.rank;
			}else{
				return suit.ordinal()+1;
			}
		}		
		private static final List<Fives> protoDeck = new ArrayList<Fives>();
	    static {
	    	protoDeck.add(new Fives(1,Pentity.PAWN));                                
	    	for (int j=0;j<MAX;j++){
	    		Pentity suit = Pentity.values()[j];
	        	for (int i = 1; i <= TOP; i++){protoDeck.add(new Fives(i,suit));}	}
	        protoDeck.add(new Fives(2,Pentity.PAWN));
	    }
	    public static List<Fives> newDeck() {return new ArrayList<Fives>(protoDeck);}	    
	}	
	
	public static class Sixes{
		private static final int TOP = 12;
		private final int rank;
		private final Color suit;
		private Sixes(int num, Color suit){this.rank = num;this.suit = suit;}
		public int getRank() {return rank;}
		public Color getSuit() {return suit;}
		public Zodiac getInverseSuit(){
			Zodiac z = Zodiac.values()[rank-1];
			return z;
		}
		public int getInverseRank(){
			return suit.ordinal()+1;
		}
		private static final List<Sixes> protoDeck = new ArrayList<Sixes>();
	    static {
	        for (Color suit : Color.values()){
	        	for (int i = 1; i <= TOP; i++){protoDeck.add(new Sixes(i,suit));}	}
	    }
	    public static List<Sixes> newDeck() {return new ArrayList<Sixes>(protoDeck);}	    
	}	

	public static class Sevens{
		private static final int MAX = 7;
		private static final int TOP = 10;
		private final int rank;
		private final ClassicPlanet suit;
		private Sevens(int num, ClassicPlanet suit){this.rank = num;this.suit = suit;}
		public int getRank() {return rank;}
		public ClassicPlanet getSuit() {return suit;}
		public Ogham getInverseSuit(){                      //an inverse suit of say Sun:10 in 7s, is  Kappa:1 
			if(suit.equals(ClassicPlanet.EARTH)){           //what about a joker like Earth 1 or 2?    Alpha:8, not 7., so the first and second suits get a higher number.
				return Ogham.A11;							//but ideally, it translates into the joker on the other one.
			}else{
				Ogham z = Ogham.values()[rank-1];
				return z;
			}
		}
		public int getInverseRank(){
			if(suit.equals(ClassicPlanet.EARTH)){
				return rank;
			}else{
				return suit.ordinal()+1;
			}
		}			
		private static final List<Sevens> protoDeck = new ArrayList<Sevens>();
	    static {
	    	protoDeck.add(new Sevens(1,ClassicPlanet.EARTH));
	    	for (int j=0;j<MAX;j++){
	    		ClassicPlanet suit = ClassicPlanet.values()[j];
	        	for (int i = 1; i <= TOP; i++){protoDeck.add(new Sevens(i,suit));}	
	        }
	        protoDeck.add(new Sevens(2,ClassicPlanet.EARTH));
	    }
	    public static List<Sevens> newDeck() {return new ArrayList<Sevens>(protoDeck);}	    
	}		

	public static class Eights{
		private static final int TOP = 9;
		private final int rank;
		private final Trigram suit;
		private Eights(int num, Trigram suit){this.rank = num;this.suit = suit;}
		public int getRank() {return rank;}
		public Trigram getSuit() {return suit;}
		public Discipline getInverseSuit(){
			Discipline z = Discipline.values()[rank-1];
			return z;
		}
		public int getInverseRank(){
			return suit.ordinal()+1;
		}		
		private static final List<Eights> protoDeck = new ArrayList<Eights>();
	    static {
	        for (Trigram suit : Trigram.values()){
	        	for (int i = 1; i <= TOP; i++){protoDeck.add(new Eights(i,suit));}	}
	    }
	    public static List<Eights> newDeck() {return new ArrayList<Eights>(protoDeck);}	    
	}	
	
	private static final int TOP = 72;
	
	private final int rank;
	private final Twos two;
	private final Threes three;
	private final Fours four;
	private final Fives five;
	private final Sixes six;
	private final Sevens seven;
	private final Eights eight;
	private Card(int num, Twos _two, Threes _three, Fours _four, Fives _five, Sixes _six, Sevens _seven, Eights _eight){
		this.rank=num;this.two=_two;this.three=_three;this.four=_four;this.five=_five;this.six=_six;this.seven=_seven;this.eight=_eight;
	}
	
	public int getRank(){return rank;}
	public Twos getTwo(){return two;}
	public Threes getThree(){return three;}
	public Fours getFour(){return four;}
	public Fives getFive(){return five;}
	public Sixes getSix(){return six;}
	public Sevens getSeven(){return seven;}
	public Eights getEight(){return eight;}
	
	private static String f(int num){
		return String.format("%02d", num);
	}
	
	public String render(){
		StringBuffer sb = new StringBuffer();
		sb.append("["+f(rank)+"] ");
		
		sb.append("\t["+two.suit.getSym()+":"+f(two.rank)    +"] ");
		sb.append("\t["+three.suit.getSym()+":"+f(three.rank)+"] ");
		sb.append("\t["+four.suit.getSym()+":"+f(four.rank)  +"] ");
		sb.append("\t["+five.suit.getSym()+":"+f(five.rank)  +"] ");
		sb.append("\t["+six.suit.getSym()+":"+f(six.rank)    +"] ");
		sb.append("\t["+seven.suit.getSym()+":"+f(seven.rank)+"] ");
		sb.append("\t["+eight.suit.getSym()+":"+f(eight.rank)+"] ");
		
		sb.append("\t["+eight.getInverseSuit().getSym()+":"+f(eight.getInverseRank())+"] ");//9s
		sb.append("\t["+seven.getInverseSuit().getSym()+":"+f(seven.getInverseRank())+"] ");//10s
		sb.append("\t["+six.getInverseSuit().getSym()+":"+f(six.getInverseRank())    +"] ");//12s
		sb.append("\t["+five.getInverseSuit().getSym()+":"+f(five.getInverseRank())  +"] ");//14s   problematic
		sb.append("\t["+four.getInverseSuit().getSym()+":"+f(four.getInverseRank())  +"] ");//18s   problematic
		sb.append("\t["+three.getInverseSuit().getSym()+":"+f(three.getInverseRank())+"] ");//24s futhark
		sb.append("\t["+two.getInverseSuit().getSym()+":"+f(two.getInverseRank())    +"] ");//36s     bopo
		/*
		 * 14:
		 * 18: 
		 * 24: Futhark
		 * 36: Bopomofo
		 * 72: Shemhamphorasch
		 */
		
		return sb.toString();
	}
	
	private static final List<Card> protoDeck = new ArrayList<Card>();
	
	static {
		List<Twos> twosDeck = Twos.newDeck();Collections.shuffle(twosDeck);
		List<Threes> threesDeck = Threes.newDeck();Collections.shuffle(threesDeck);
		List<Fours> foursDeck = Fours.newDeck();Collections.shuffle(foursDeck);
		List<Fives> fivesDeck = Fives.newDeck();Collections.shuffle(fivesDeck);
		List<Sixes> sixesDeck = Sixes.newDeck();Collections.shuffle(sixesDeck);
		List<Sevens> sevensDeck = Sevens.newDeck();Collections.shuffle(sevensDeck);
		List<Eights> eightsDeck = Eights.newDeck();Collections.shuffle(eightsDeck);
		
		for(int i=1;i<=TOP;i++){
			Twos _2 = twosDeck.get(i-1);
			Threes _3 = threesDeck.get(i-1);
			Fours _4 = foursDeck.get(i-1);
			Fives _5 = fivesDeck.get(i-1);
			Sixes _6 = sixesDeck.get(i-1);
			Sevens _7 = sevensDeck.get(i-1);
			Eights _8 = eightsDeck.get(i-1);
			protoDeck.add(new Card(i,_2,_3,_4,_5,_6,_7,_8));
		}
	}
	public static List<Card> newDeck() {return new ArrayList<Card>(protoDeck);}
	
	public static void printCards(){
		
		List<Card> deck = Card.newDeck();
		for(Card c:deck){
			Log.println(c.render());
		}		
	}
	public static void unicodeTest(){
		for(int i=9728;i<=9983;i++){
			char c = (char)i;
			
			Log.println(i+":"+c);
		}
	}

	public static void main(String[] args){
		//Log.println("num:"+f(9));
		printCards();
		//unicodeTest();
	}
}




/*

Doesn't print.

9748,9749 raindrop,coffee
9752,9753 Shamrock, heart bullet
9840-9983. 

 * 
 * 
 */
