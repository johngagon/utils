package jhg.diviner;

import java.util.Random;


@SuppressWarnings({"unused","boxing"})
public class Geomantic {

	public static enum Shape{
		PATH,POPULACE,CROSSROAD,PRISON,
		DRAGON_TAIL,DRAGON_HEAD,RED,WHITE,
		BOY,GIRL,MAJOR_FORTUNE,MINOR_FORTUNE,
		JOY,GRIEF,GAIN,LOSS
	}
	
	private static final int LIM_M = 4;
	private static final int LIM_N = 2;
	
	private static final String ONEDOT = "  * ";
	private static final String TWODOT = " * * ";
	
	private Integer[][] mothers;
	private Integer[][] daughters;
	private Integer[][] nieces;
	private Integer[][] witnesses;
	private Integer[][] judge;
	
	
	public Geomantic() {
		mothers = new Integer[LIM_M][LIM_M];
		daughters = new Integer[LIM_M][LIM_M];
		nieces = new Integer[LIM_M][LIM_M];
		witnesses = new Integer[LIM_N][LIM_M];
		judge = new Integer[1][LIM_M]; 
	}


	
	private void generate(){
		
		Random r = new Random();
		
		//Mothers
		for(int i=0;i<LIM_M;i++){
			for(int j=0;j<LIM_M;j++){
				mothers[i][j] = r.nextInt(2)+1;
			}
		}
		print(LIM_M,mothers,"Mothers",1);
		
		//Daughters
		for(int i=0;i<LIM_M;i++){
			for(int j=0;j<LIM_M;j++){
				daughters[i][j] = mothers[j][i];
			}
			
		}
		print(LIM_M,daughters,"Daughters",5);
		
		//Nieces
		for(int i=0;i<LIM_M;i++){
			
			int v = mothers  [0][i]  +  mothers[1][i];
			int w = 2-(v%2);
			nieces[0][i] = w; 
			
			v     = mothers  [2][i]  +  mothers[3][i];
			w     = 2-(v%2);
			nieces[1][i] = w; 
			
			v     = daughters[0][i] + daughters[1][i];
			w     = 2-(v%2);
			nieces[2][i] = w; 
			
			v     = daughters[2][i] + daughters[3][i];
			w     = 2-(v%2);
			nieces[3][i] = w; 
			
		}
		print(LIM_M,nieces,"Nieces",9);
		
		//Witnesses
		for(int i=0;i<LIM_M;i++){
			int v = nieces[0][i] + nieces[1][i];
			int w = 2-(v%2); 
			witnesses[0][i] = w;
			v = nieces[2][i] + nieces[3][i];
			w = 2-(v%2); 
			witnesses[1][i] = w;
		} 
		
		print(LIM_N,witnesses,"Witnesses",13);
		
		//Judge
		for(int i=0;i<LIM_M;i++){
			int v = witnesses[0][i] + witnesses[1][i];
			int w = 2-(v%2); 
			judge[0][i] = w;
		} 		
		print(1,judge,"Judge",15);
		
	} 
	
	
	private static void print(int I, Integer[][] m, String n, int pos){
		System.out.println("\n\n"+n.toUpperCase());
		int x = pos;
		for(int i=0;i<I;i++){
			for(int j=0;j<LIM_M;j++){
				String l = "";//"0:"+j+":";
				String line = (m[i][j]==2)?TWODOT:ONEDOT;
				System.out.println(l+line);
			}
			Shape shape = interpret(m[i]);
			System.out.println("Interpretation - \n\tPosition("+x+"): "+interpretPosition(x)+" \n\tShape: "+shape.name().toLowerCase()+" \n\tMeaning: "+interpretShape(shape));
			x++;
			System.out.println(" ");
		}
	}
	
	public static void main(String[] args){
		String question = "Who moved my cheese?";// "Will I live in a home that's paid for within the next five years?";
		System.out.println("Question:"+question);
		Geomantic g = new Geomantic();
		g.generate();
	}
	private static String interpretTriplicity(Integer[] a, Integer[] b, Integer[] c){
		String result = "";
		return result;
	}
	private static String interpretPosition(int position){
		
		switch(position){
			case 1: return "Querent's Sun House ";
			case 2: return "House of Money ";
			case 3: return "House of Communication and Commutes ";
			case 4: return "House of Home ";
			case 5: return "House of Pleasures ";
			case 6: return "House of Regular Doings ";
			case 7: return "House of Partnerships oft including love ";
			case 8: return "House of Intimacy ";
			case 9: return "House of Awareness ";
			case 10: return "House of Career and Position, Destiny ";
			case 11: return "House of Wishes and Commradery ";
			case 12: return "House of Karma and Secrets ";
			case 13: return "Witness of the Past ";
			case 14: return "Witness of the Future ";
			case 15: return "Judge final answer ";
			default: return "";
		}
		
	}	
	private static String interpretShape(Shape s){
		
		switch(s){
			case PATH: 			return "Generally bad.     Good for travels,           		   bad luck in other issues";            
			case DRAGON_TAIL: 	return "Generally bad.     Good for completions,       	       bad in anything else in the extreme.";
			case BOY: 			return "Generally bad.     Good for sex and contests.  		   bad for anything else";
			case MINOR_FORTUNE: return "Generally good.    Good depending              		   bad if stability needed";
			case GIRL: 			return "Generally good.    Good esp inf feminine  			   bad in contests";
			case LOSS: 			return "Generally bad.     Good for love or desired loss       bad for anything else";
			case PRISON: 		return "Generally bad.     Good for questions of stability     bad usually denoting restriction";
			case JOY: 			return "Generally good.    Good for all emotional related      bad for addictions or impatience";
			case DRAGON_HEAD: 	return "Generally depends. Good for beginnings and other good  bad when other draws are bad";
			case CROSSROAD: 	return "Generally depends. Neutral - Good in good, decisions   bad in bad. Haste issues";
			case GAIN: 			return "Generally good.    Good for obtaining needs            bad for hoarding and greed issues.";
			case MAJOR_FORTUNE: return "Generally good.    Good almost always                  bad if early challenges are not tolerable.";
			case RED: 			return "Generally bad.     Good where evil, conflicts vice     bad where good. Conflicting. victimhood.";
			case WHITE: 		return "Generally good.    Good with peace and purity          bad with some creativity";
			case GRIEF: 		return "Generally bad.     Good for numb, catharsis lack       bad with just about everything else";
			case POPULACE: 		return "Generally depends. Good with other good                bad with other bad";
				                        //7 bad, 6 good, 3 depends.
		}
		return "";	
	}

	private static Shape interpret(Integer[] stack){
		switch(stack[0]){	case 1:		switch(stack[1]){	case 1:	switch(stack[2]){	case 1: switch(stack[3]){
			case 1: return Shape.PATH;
			case 2: return Shape.DRAGON_TAIL;
						}																case 2: 	switch(stack[3]){	
			case 1: return Shape.BOY;
			case 2: return Shape.MINOR_FORTUNE;
						} 	}								case 2: switch(stack[2]){	case 1: switch(stack[3]){
			case 1: return Shape.GIRL;
			case 2: return Shape.LOSS;
						}																case 2:		switch(stack[3]){
			case 1: return Shape.PRISON;
			case 2: return Shape.JOY; 						}	}	}	
							case 2:	switch(stack[1]){		case 1:	switch(stack[2]){	case 1:		switch(stack[3]){
			case 1: return Shape.DRAGON_HEAD;
			case 2: return Shape.CROSSROAD;
						} 																case 2:	switch(stack[3]){
			case 1: return Shape.GAIN;
			case 2: return Shape.MAJOR_FORTUNE;				}	}	
															case 2:	switch(stack[2]){	case 1:	switch(stack[3]){
			case 1: return Shape.RED;
			case 2: return Shape.WHITE;
						}																case 2:	switch(stack[3]){
			case 1: return Shape.GRIEF;
			case 2: return Shape.POPULACE;
						}	}	}
		}
		return null;
	}
}
//System.out.println(i+":"+j+":"+mothers[i][j]);
//System.out.println(i+":"+j+":"+daughters[i][j]+", "+j+":"+i+":"+mothers[j][i]);