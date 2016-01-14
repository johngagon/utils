package jhg.puzzles;

import jhg.util.*;

@SuppressWarnings("unused")
public class Main {
	//TEMPLATE: Log.println("Result:"+false);
	
	
	public static void main(String[] args) {
		testPuzzle10();
	}
	
	private static void testPuzzle10(){
		Object x = "hi";
		String i = " there";
		//make first legal, second not with declarations
		x = x + i;
		x += i;      //not legal  (but compiler doesn't complain 
	}
	private static void testPuzzle9(){
		char x = 'x'; //could also be done with other smaller types
		int i = 0;
		//make first legal, second not legal with declarations.
		x += i;         //works ok because if i is negative, it will convert to (char)and thus chop.
		//x = x + i; //char = char + value (if i is negative...)   here: x is char and i is int, no implicit cast or x converts to int first. (by convention since it auto-widens)
		//dont' use compound assignments on small types. byte,short,char 8,16,16
	}
	private static void testPuzzle8(){
		char x = 'X';
		int i = 0;
		System.out.println(true ? x : 0);//looks like should be X, expression ambiguous type, also parens, may just print nothing. actual: the 0 makes these a char.
		System.out.println(false ? i : x);//loks like should be X.....  that x makes the expression int
		//X 88 -> not fixed, don't use two types in conditional
	}
	private static void testPuzzle7(){
		int x = 1984; //0x7c0
		int y = 2001; //0x7d1
		x ^= y ^= x ^= y; //x swap y  (x=2001,y=1984), y swap x
		Log.println("Result: x = "+x+"; y= "+y);//x=0,y=1984 
		//proper way: int tmp = x; x=y; y=tmp;
	}
	private static void testPuzzle6(){
		Log.println("Result:"+((int) (char) (byte) -1));//byte -1-> char is not signed -> - max char -> -max char in int 65,535
		//correctly predicted (note: starts as int)
		byte b = -1;
		char c = (char)b;//(b & 0xff);//prevents sign extension
		int i = (short)(c & 0xffff);  //masking with ffff flips sign
		Log.println("Result 2:"+i);
	}
	private static void testPuzzle5(){
		//Log.println("Result:"+Long.toHexString(0x100000000L + 0xcafebabe));
		Log.println("Result:"+Long.toHexString(0x100000000L + 0xcafebabeL));//avoid confusing mixed type conversions, remember left side sign bit
	}
	private static void testPuzzle4(){
		//Log.println("Result:"+(12345+54321));//66666  they used lowercase l not 1 for Long. mistyped
		Log.println("Result:"+(12345+5432L));
	}
	private static void testPuzzle3(){
		//final long MICROS = 24 * 60 * 60 * 1000 * 1000; overflows on right side int calc
		final long MICROS = 24L * 60 * 60 * 1000 * 1000; 
		final long MILLIS = 24 * 60 * 60 * 1000;
		Log.println("Result:"+(MICROS/MILLIS));//1000 actual: 5 without fix
	}
	private static void testPuzzle2() {
		Log.println(""+(2.00-1.10));         //0.89999999  not 0.90
	}
	private static void testPuzzle1() {
		Log.println("Puzzle 1:");
		for(int i=-20;i<=20;i++){
			Log.println("Is odd ("+i+") : "+isOdd(i));//start using String.format more.
		}
	}
	
	public static boolean isOdd(int i){		//return i % 2 == 1; //positive 1, not negative 1.
		return i % 2 != 0;		//i & 1 != 0 does the same thing.
	}
	
}
