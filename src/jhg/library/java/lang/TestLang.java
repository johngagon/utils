package jhg.library.java.lang;

import jhg.util.Log;

public class TestLang {

	
	public static void testBoolean(){
		
	}
	
	
	
	public static void main(String[] args) {
		/*
		 *  & and						check if even or odd. essentially compares the powers of two factors for each, 
		 *                    & with self is identity or equality
		 *  | or                        
		 *  ~ not
		 *  ^ xor
		 *  << left shift
		 *  >> right shift
		 *  >>> unsigned rshift
		 *  &= 
		 *  |=
		 *  ~=
		 *  ^=
		 *  <<=
		 *  >>=
		 *  >>>=
		 */
		Log.println("Testing bitwise on numbers.");
		int k = 1;
		final int R = 4;
		for(int i = R; i >= (0-R) ; i--){
			for(int j = R; j >= (0-R) ; j--){
				Log.println("-----------------------");
				Log.println("K:"+k);
				int p=i;
				int q=j;
				Log.println(i+" & "+j+" : "+(p&q));
				Log.println(i+" | "+j+" : "+(p|q));
				Log.println(" ~"+i+", ~"+j+"  : "+(~p)+","+(~q));
				Log.println(i+" ^ "+j+" : "+(p^q));
				Log.println(i+" << "+j+" : "+(p<<q));
				Log.println(i+" >> "+j+" : "+(p>>q));
				Log.println(i+" >>> "+j+" : "+(p>>>q));
				
				Log.println(i+" &= "+j+" : "+(p&q));
				Log.println(i+" |= "+j +" : "+(p|q));
				
				//there is no ~=
				Log.println(i+" ^= "+j+" : "+(p^=q));
				Log.println(i+" <<= "+j+" : "+(p<<=q));
				Log.println(i+" >>= "+j+" : "+(p>>=q));
				Log.println(i+" >>>= "+j+" : "+(p>>>=q));
				k++;
			}
		}
		/*
		 * effects of bit masks:
		 * 
		 * | overlay
		 * |= 
		 * & ~ unset
		 * &   check if set
		 * ~ invert (switch all)
		 * all off mask |=0
		 * can go to 2^^30 (power 30) 
// Constants to hold bit masks for desired flags
static final int flagAllOff = 0;  //         000...00000000 (empty mask)
static final int flagbit1 = 1;    // 2^^0    000...00000001
static final int flagbit2 = 2;    // 2^^1    000...00000010
static final int flagbit3 = 4;    // 2^^2    000...00000100
static final int flagbit4 = 8;    // 2^^3    000...00001000
static final int flagbit5 = 16;   // 2^^4    000...00010000
static final int flagbit6 = 32;   // 2^^5    000...00100000
static final int flagbit7 = 64;   // 2^^6    000...01000000
static final int flagbit8 = 128;  // 2^^7    000...10000000
		 */
	}
}
