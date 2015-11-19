package jhg.diviner.cards;

import jhg.util.Log;
import jhg.util.TextFile;

public class ShemhamphoraschCards {

	//FIVE       five crosses: 2625,2626,2628,2628,2629 (2670,2671)
	//NINE       263F,40,41,42 2643,44,45,46,47 (just the planets)
	//TENS        2609, 263F,40,41,42 2643,44,45,46,47 (planets and sun)
	//FOURTEEN   (pick)
	//EIGHTEEN   runic 16A0-16F0(5792-5872)
	//TWENTYFOUR hebrew extended: 05D0-05EA ogham 1680-169C(5760-5788)
	//THIRTYSIX  bopomofo  0x3100-0x312F(12544-12591)
	private static String unitySymbol = "\u25C9";
	public static void printCards(){
		StringBuffer sb = new StringBuffer();
		int two = 17; 		int three = 2;		int four = 7;		
		//int five = 4;		
		int six = 5;		
		int seven = 8;		
		int eight = 3;
		int nine = 8;		
		int ten = 7;		
		int twelve = 6;		
		//int fourteen = 5;	
		int eighteen = 4;	int twentyfour = 3;	int thirtysix = 2;
		
		for(int i=0;i<72;i++){
			if(two>36){two=1;thirtysix--;}
			if(three>24){three=1;twentyfour--;}
			if(four>18){four=1;eighteen--;}
			//if(five>14){five=1;}
			if(six>12){six=1;twelve--;}
			if(seven>10){seven=1;}
			if(eight>9){eight=1;nine--;}
			
			
			if(nine>8){nine=1;}
			if(ten>7){ten=1;}
			if(twelve>6){twelve=1;six--;}
			//if(fourteen>5){fourteen=1;}
			if(eighteen>4){eighteen=1;}
			if(twentyfour>3){twentyfour=1;}
			if(thirtysix>2){thirtysix=1;} 
			
			int j = i+1;
			sb = new StringBuffer();
			sb.append("["+unitySymbol+":"+j+"] ");
			sb.append("["+Duality.values()[thirtysix-1].getSym()+":"+two+"] ");
			sb.append("["+Trinity.values()[twentyfour-1].getSym()+":"+three+"] ");
			sb.append("["+Element.values()[eighteen-1].getSym()+":"+four+"] ");
			//five
			sb.append("["+Color.values()[twelve-1].getSym()+":"+six+"] ");
			//trigram
			sb.append("["+ClassicPlanet.values()[ten-1].getSym()+":"+seven+"] ");
			sb.append("["+Trigram.values()[nine-1].getSym()+":"+eight+"] ");
			sb.append("["+Zodiac.values()[six-1].getSym()+":"+six+"] ");
			Log.println(sb.toString());

			{two++;}
			{three++;}
			{four++;}
			//five++;
			{six++;}
			seven++;
			{eight++;}

			{nine++;}
			ten++;
			{twelve++;}
			//fourteen++;
			{eighteen++;}
			{twentyfour++;}
			{thirtysix++;}			
			

			
		}
		
	}
	
	public static void main(String[] args){
		printCards();
	}
}
		/*
		StringBuffer buff = new StringBuffer();
		for(Zodiac z:Zodiac.values()){
			String msg = z.getSym()+" ";
			buff.append(msg);
			Log.println(msg);
		}
		TextFile.write("data/zodiac.txt", buff.toString());
		//Log.println("<html><body>Air:"+Element.AIR.getSym()+"</body></html>");
		 * 
		 */

/*
			if(two>36){two=1;}if(three>24){three=1;}if(four>18){four=1;}
			//if(five>14){five=1;}
			if(six>12){six=1;}
			//if(seven>10){seven=1;}
			if(eight>9){eight=1;}if(nine>8){nine=1;}
			//if(ten>7){ten=1;}
			if(twelve>6){twelve=1;}
			//if(fourteen>5){fourteen=1;}
			if(eighteen>4){eighteen=1;}if(twentyfour>3){twentyfour=1;}if(thirtysix>2){thirtysix=1;} 
 */
/*
if(j%2==0){two++;}
if(j%3==0){three++;}
if(j%4==0){four++;}
//five++;
if(j%6==0){six++;}
//seven++;
if(j%8==0){eight++;}

if(j%9==0){nine++;}
//ten++;
if(j%12==0){twelve++;}
//fourteen++;
if(j%18==0){eighteen++;}
if(j%24==0){twentyfour++;}
if(j%36==0){thirtysix++;}
*/