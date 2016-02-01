package jhg.gambling;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/*
 * Simple plan - Just create a jackpot out of 40% of the tickets
 * 
 * $1000 - (revenue). 1000 tickets at $1
 * 
 * $200 200 x 1    20%  1st place
 * $150  15 x 10   15%  2nd place
 * $1     1 x 100  10%  3rd place  1:10 earn back
 * 
 * Announce the jackpot 
 *  Second place -
 * 10 Third place                       
 * 
 */

public class Gamble {

	private static final NumberFormat formatter = new DecimalFormat("#0.00");  
	
	public static enum Result{
		WIN,LOSE;
	}
	
//		DOUBLE,	BREAK_EVEN,	HALF,	ZERO;
	
	public static void testRoll(){
		
	}
	
	public static double roll(int a, int b){
		if(a<1 || b<1){
			System.out.println("A:"+a+" B:"+b);
		}
		Random rnd = new Random();
		int r1 = rnd.nextInt(a)+1;
		int r2 = rnd.nextInt(b)+1;
		//System.out.println("a: "+a+"  b: "+b+"   r1: "+r1+"  r2:"+r2+"  "+(r1/r2));
		return (double)r1/r2;
	}
	public static double roll(int a, int am, int b, int bm){
		if(a<1 || b<1){
			System.out.println("A:"+a+" B:"+b);
		}
		Random rnd = new Random();
		int r1 = rnd.nextInt(a)+1;
		int r3 = r1-am;
		if(r3<1){
			r3=1;
		}
		int r2 = rnd.nextInt(b)+1;
		int r4 = r2+bm;
		double rv = (double)r3/r4;
		System.out.println("   Player roll d6:"+r1+"-"+am+"="+r3+" Dealer roll d12:"+r2+"+"+bm+"="+r4+  "   score:"+formatter.format(rv));
		//System.out.println("a: "+a+"  b: "+b+"   r1: "+r1+"  r2:"+r2+"  "+(r1/r2));
		
		return rv;
	}
	
	/*
	 * Rules:
	 * 
	 * 1. You pay $5 per game
	 * 2. You can only play up to 50 games.
	 * 3. You have a 50/50 chance of winning. This can go up or down.
	 * 4. First game, you roll a 6, I roll a 12. Your score has to be half my score or better.
	 * 5. If you win, you will get double your money back            - the dealer gets a counter to the dice roll.
	 * 6. If you lose, the dealers keeps your bet and you can quit. if you play again, the dealer loses their counter. 
	 * 7.  
	 * 
	 */
	public static void test2(){
		final int STARTING_MONEY = 250;
		int dealerMoney = STARTING_MONEY;
		//int dealerPaidOut = 0;
		int gamerMoney = STARTING_MONEY; //250;
		int gamerWinnings = 0;
		
		int costPerGame = 5;
		int GAMES = 50; //50;
		
		int dealerCounter = 0;
		int gamerSandbag = -1;
		
		double winval = 0.5;
		
		int dealerDice = 12;
		int gamerDice = 6;
		int lossInARow = 0;
		
		for(int i=1;i<=GAMES;i++){
			dealerMoney += costPerGame;
			gamerMoney -= costPerGame;
			double roll = roll(gamerDice,gamerSandbag,dealerDice,dealerCounter);
			int winningsThisRound = 0;
			if(roll>=winval){
				winningsThisRound += (costPerGame*(dealerCounter+2));
				dealerCounter++;
				lossInARow = 0;
				if(gamerSandbag>0){
					gamerSandbag--;
					winningsThisRound += costPerGame;
					System.out.println("  Sandbag bonus. Sandbag reduced.");
				}
				gamerMoney += winningsThisRound;
				gamerWinnings += winningsThisRound;
				dealerMoney -= winningsThisRound;
			}else{
				lossInARow++;
				if(lossInARow==1){
					System.out.println("   First loss, no sandbag.");
				}
				if(lossInARow>1 && gamerSandbag<5){
					System.out.println("   More than one loss in a row. Sandbagged.");
					gamerSandbag++;
				}
				if(lossInARow>5 && dealerCounter>0){
					System.out.println("   Five in a row loss. Dealer roll bonus reduced. Multiplier reduced.");
					dealerCounter--;
				}

			}
			
			if(dealerCounter>3){                 //The dealer can never give out more than a triple
				dealerCounter--;
			}
			if(gamerSandbag<0){
				System.out.println("   First time bonus!");
				gamerSandbag = 0;
			}
			System.out.println(i+":  Result : "+((roll>=winval)?"WIN!!!":"#loss#")
					+"  winnings:"+winningsThisRound
					+"  SB:"+gamerSandbag
					+"  MP:"+(dealerCounter+1)
					+"  gamerMoney:"+gamerMoney
					+"  dealerMoney:"+dealerMoney
					+"  dealerEarning:"+(dealerMoney-STARTING_MONEY)
					+"  gamerwinnings:"+gamerWinnings
					+"  gamerPercentage:"+formatter.format(((double)gamerMoney/STARTING_MONEY)*100)
					+"  dealerPercentage:"+formatter.format(((double)dealerMoney/STARTING_MONEY)*100)+" \n");//dealerNet: "+(dealerMoney-dealerPaidOut)+"
		}
		
	}
	
	public static void main(String[] args){
		test2();
	}
	
	
	public static void test1(){
		
		
		//System.out.println(roll(25,50));
		final int aSTART = 25;
		final int halfA = 5;
		final int headStart = 4;
		final int bSTART = 50;
		int a = aSTART+headStart;
		int b = bSTART;
		int play = 0;
		final int MAX = 50;
		int win = 0;
		int loss = 0;
		
		
		for(int i=0;i<MAX;i++){
			double val = roll(a,b);
			
			a+=(a>halfA)?-1:0;
			
			if(b>0){
				b+=((play+2)%2==0)?-1:1;
			}
			play++;
			//System.out.println("count: "+play+"  a: "+a+"  b: "+b+" ratio:"+((double)a/b));
			
			if(val>0.5){
				win++;
				String NOTICE = (win==loss)?"  !!!!!!!!! ":"";
				System.out.println("#"+play+" You WIN!!! "+formatter.format(val)+"  wins:"+win+" losses:"+loss+"  "+NOTICE);
			}else{
				loss++;
				String NOTICE = (win==loss)?"  !!!!!!!!! ":"";
				System.out.println("#"+play+" You lose. "+formatter.format(val)+"   wins:"+win+" losses:"+loss+"  "+NOTICE);
			}
		}
		int paid = MAX*5;
		int won = win*10;
		System.out.println("Amount paid: "+paid+" Amount won:"+won+"  Net lost:"+(paid-won));		
		
		//Amount paid: 500 Amount won:300  Net lost:200	
		//Amount paid: 500 Amount won:240  Net lost:260
		//Amount paid: 500 Amount won:290  Net lost:210
		
		/*
		 * desired curve playing 50/50
		 * 
		 * 1,2  0.50
		 * 1,3  0.33
		 * 2,5  0.40
		 * 
		 * 25/50
		 * 24/49 -> 49  25-1/50-1   48.98
		 * 24/50 -> 48
		 * 23/48 -> 47  0.479
		 * 23/49 -> 47  0.469
		 * 23/50 -> 46                23,24,25  48,49,50   23,48
		 * 22/49 -> 45
		 * 22/50 -> 44
		 * 
		 * -1 -1
		 * -1 +1
		 * -1 -1
		 * -1 +1
		 * 
		 * 
		 * First play:   100% win
		 * Second play:   75% win
		 *
		 */
		
	}
	
}
