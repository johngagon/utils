package jhg.diviner;

import java.util.*;

import jhg.util.Log;

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
	 * 14:
	 * 18: 
	 * 24: Futhark
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
		public Planet getInverseSuit(){
			Planet z = Planet.values()[rank-1];
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
	
	public String render(){
		StringBuffer sb = new StringBuffer();
		sb.append("["+rank+"] ");
		sb.append("["+two.suit.getSym()+":"+two.rank+"] ");
		sb.append("["+three.suit.getSym()+":"+three.rank+"] ");
		sb.append("["+four.suit.getSym()+":"+four.rank+"] ");
		sb.append("["+five.suit.getSym()+":"+five.rank+"] ");
		sb.append("["+six.suit.getSym()+":"+six.rank+"] ");
		sb.append("["+seven.suit.getSym()+":"+seven.rank+"] ");
		sb.append("["+eight.suit.getSym()+":"+eight.rank+"] ");
		sb.append("["+eight.getInverseSuit().getSym()+":"+eight.getInverseRank()+"] ");//9s
		sb.append("["+six.getInverseSuit().getSym()+":"+six.getInverseRank()+"] ");//12s
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
	

	public static void main(String[] args){
		List<Card> deck = Card.newDeck();
		for(Card c:deck){
			Log.println(c.render());
		}
	}
}
