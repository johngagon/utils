package jhg.util.types;

public class CreditCardNumber {

	private int[] cardNum;
	
	public CreditCardNumber(int[] card){
		if(card==null || card.length!=16 || !CreditCardNumber.check(card)){
			throw new IllegalArgumentException("Card number appears invalid.");
		}else{
			cardNum = card;
		}
	}
	public int[] getNumber(){
		return this.cardNum;
	}
	
	public static boolean check(int[] digits) {
		int sum = 0;
		int length = digits.length;
		for (int i = 0; i < length; i++) {
		   // get digits in reverse order
		   int digit = digits[length - i - 1];
		   // every 2nd number multiply with 2
		   if (i % 2 == 1) {
		       digit *= 2;
		   }
		   sum += digit > 9 ? digit - 9 : digit;
		 }
		 return sum % 10 == 0;
	}
	
	public static void main(String[] args){
		//I have checked the proposed solution for your example credit card numbers 4388576018402626 (invalid) and 4388576018410707 (valid)
		int[] invalid = {4,3,8,8, 5,7,6,0, 1,8,4,0, 2,6,2,6};
		int[] valid =   {4,3,8,8, 5,7,6,0, 1,8,4,1, 0,7,0,7};
		System.out.println("Invalid:"+check(invalid));
		System.out.println("Valid:"+check(valid));
		
	}
}
/*
 * Other types to consider: CUSIP, Stock symbol, option symbol.
 * Hospital code
 * Airport code
 * SIC
 * DUNS
 * Latlong
 * 
 * IP, domain name, email
 * 
 */
