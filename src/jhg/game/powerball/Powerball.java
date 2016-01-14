package jhg.game.powerball;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import jhg.util.*;

@SuppressWarnings("boxing")
public class Powerball {

	private int[] whiteballs;
	private int redball;
	private String date;
	public Powerball(String d, int[] w,int r) {
		this.whiteballs = w;
		this.redball = r;
		this.date = d;
	}

	public int[] getWhiteballs() {
		return whiteballs;
	}

	public int getRedball() {
		return redball;
	}

	public String getDate() {
		return date;
	}
	private boolean match(int num){
		for(int i=0;i<whiteballs.length;i++){
			if(num==whiteballs[i])return true;
		}
		return false;
	}
	private int match(Powerball draw) {
		/*
		 * First match powerballs R, R+1, R+2, R+3, R+4, JP
		 * Next match W3,W4,W5
		 * 
		 * W3
		 * 4:R
		 * 
		 */
		boolean rmatch = this.redball == draw.redball;
		List<Integer> matches = new ArrayList<Integer>();
		for(int i=0;i<whiteballs.length;i++){
			if(draw.match(whiteballs[i])){
				matches.add(whiteballs[i]);
			}
		}
		
		int rv = 0;
		if(rmatch){
			rv = rv + 4;
			rv = rv + matches.size();
		}else{
			if(matches.size()>2){
				rv = rv + matches.size();
			}
		}
		if(rv!=0){
			Log.print(this.date+" matches "+draw.date+": score("+rv+") winning "+translateScore(rv)+".  Matching numbers are:");
			if(rmatch){
				Log.println("R("+this.redball+") ");
			}
			for(Integer z:matches){
				Log.print(z+" ");
			}
			Log.print("\n");
		}
		return rv;
		
	}
	public String translateScore(int score){ //matching 3 numbers is always $7. a red or red plus white is $4., 4 is always $100
		switch(score){
			case 1:return "$7";
			case 2:return "$100";
			case 3:return "$1,000,000.00";
			case 4:return "$4";
			case 5:return "$4";
			case 6:return "$7";
			case 7:return "$100";
			case 8:return "$50,000";
			case 9:return "$1,400,000,000.00";
			default:return "Incorrect value passed in.";
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(date+" ");
		for(int wb:this.whiteballs){
			sb.append(formatNumber(wb)+" ");
		}
		sb.append(formatNumber(this.redball));
		return sb.toString();
	}

	//7.0
	public static final int MIN = 1;
	public static final int MAX_WHITE = 69;
	public static final int MAX_RED = 26;
	public static final int NUM_BALLS = 6;
	public static final int NUM_WHITE_BALLS = 5;
	
	
	public static List<Powerball> generate(int qty){
		List<Powerball> tickets = new ArrayList<Powerball>();
		Random random = new Random();
		//int[] nums = new int[NUM_BALLS];
		
		for(int i=0;i<qty;i++){
			StringBuilder sb = new StringBuilder();
			sb.append(getToday()+" ");

			//nums = new int[NUM_BALLS];
			List<Integer> used = new ArrayList<Integer>(5);
			for(int j=0;j<NUM_WHITE_BALLS;j++){
				Integer whiteball = random.nextInt(MAX_WHITE - MIN + 1) + MIN;
				while(used.contains(whiteball)){
					whiteball = random.nextInt(MAX_WHITE - MIN + 1) + MIN;
				}
				used.add(whiteball);
				sb.append(formatNumber(whiteball)+" ");
			}
			int redball = random.nextInt(MAX_RED - MIN + 1) + MIN;
			sb.append(formatNumber(redball));
			tickets.add(Powerball.from(sb.toString()));
		}
		return tickets;
	}
	
	public static String getToday(){
		return DATE_FORMAT.format(new Date());
	}
	
	public static String formatNumber(Integer x){
		return String.format("%02d", x);
	}
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	
	public static void testGenerate(){
		Log.println("Generate");
		List<Powerball> tickets = generate(GEN);
		
		for(Powerball s:tickets){
			Log.println(s.toString());
		}
	}
	
	public static void testCheckNumbers(){
		Log.println("Numbers");
		TextFile n = new TextFile("data/current_numbers.txt");//http://www.lotteryusa.com/powerball/pb-year.html
		String[] nlines = n.getLines();
		for(String s:nlines){
			Log.print(s);
		}
		Log.println("---------------------");
		Log.println("Tickets");
		TextFile t = new TextFile("data/tickets.txt");
		String[] tlines = t.getLines();
		for(String s:tlines){
			Log.print(s);
		}
		Log.println("---------------------");
		Log.println("Matches");
		reportMatches(nlines,tlines);
		
	}
	
	public static final Powerball from(String line){
		Powerball pb = null;
		String[] vals = null;
		try{
			line = line.replaceAll("\n","");
			vals = line.split(" ");
			String date = vals[0];
			int n1 = Integer.parseInt(vals[1]);
			int n2 = Integer.parseInt(vals[2]);
			int n3 = Integer.parseInt(vals[3]);
			int n4 = Integer.parseInt(vals[4]);
			int n5 = Integer.parseInt(vals[5]);
			int[] whiteballs = {n1,n2,n3,n4,n5};
			int redball = Integer.parseInt(vals[6].trim());
			pb = new Powerball(date,whiteballs,redball);
		}catch(Exception e){
			Log.println("Could not parse: '"+line+"'  ");
			if(vals!=null){
				int i=0;
				for(String s:vals){
					Log.println(i+":"+s);
					i++;
				}
			}
			e.printStackTrace();
		}
			
		return pb;
	}
	
	private static void reportMatches(String[] nlines, String[] tlines) {
		/*
		 * loop through tickets first. 
		 */
		for(String line:tlines){
			Powerball ticket = Powerball.from(line);
			if(ticket!=null){
				for(String nline:nlines){
					Powerball draw = Powerball.from(nline);
					if(draw !=null){
						ticket.match(draw);
					}else{
						Log.println("Draw null.");
					}
				}
			}else{
				Log.println("Ticket null.");
			}
			
		}
		
	}
	public static void testGenerateRankBestFitFrequency(){
		TextFile fr = new TextFile("data/frequency.txt");
		String[] lines = fr.getLines();
		for(String line:lines){
			if(line.trim().length()>0){
				String[] val = line.split("\t");
				try{
					if(val!=null && val.length>0){
						if(val[0].trim().length()>0 && val[1].trim().length()>0){
							Integer num = Integer.parseInt(val[0].trim());
							Integer whiteFreq = Integer.parseInt(val[1].trim());
							frequencyWhite.put(num,whiteFreq);
						
							if(val.length>2){
								if(val[2].trim().length()>0){
									Integer redFreq = Integer.parseInt(val[2].trim());
									frequencyRed.put(num,redFreq);
								}
							}
						}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
		Log.println("Red:");
		//printMap(frequencyRed);
		Log.println("White:");
		//printMap(frequencyWhite);
		List<Powerball> tickets = generate(GEN);
		
		int highest = 1;
		int total = 0;
		Powerball best = null;
		for(Powerball pb:tickets){
			//Log.println(score(pb)+":    "+pb.toString());
			int score = score(pb);
			total+=score;
			if(score>highest){
				highest = score;
				best = pb;
			}
		}
		Log.println("Best ["+highest+"] Powerball:[*** "+best+ " ***] out of "+GEN+" generated. Average: "+(total/GEN));
	}
	
	public static int score(Powerball pb){
		int[] whiteballs = pb.whiteballs;
		int score = 0;
		for(int wb:whiteballs){
			int wbscore = frequencyWhite.get(wb);
			score += wbscore;
		}
		score += frequencyRed.get(pb.redball) * 4;
		return score;
	}
	
	public static void printMap(Map<Integer,Integer> map){
		Log.println(map.toString());
	}


	public static void main(String[] args) {
		testCheckNumbers();//put fewer numbers in the numbers file and more numbers in the text file.
		//testGenerateRankBestFitFrequency();
		//testGenerate();
	}
	private static final int GEN = 1000;

	private static Map<Integer,Integer> frequencyWhite = new Hashtable<Integer,Integer>();
	private static Map<Integer,Integer> frequencyRed = new Hashtable<Integer,Integer>();
}
