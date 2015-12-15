package jhg.util.types;

import java.util.regex.*;

public class SocialSecurityNumber {
	private static final String regex = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";
	private static final Pattern pattern = Pattern.compile(regex);
	
	private String value;
	
	public SocialSecurityNumber(String s){
		Matcher matcher = pattern.matcher(s);
		if(matcher.matches()){
			this.value = s;
		}else{
			throw new IllegalArgumentException("Argument '"+s+"' does not match the correct pattern.");
		}
	}

	@Override
	public String toString() {
		return value;
	}
	
	public Integer toInteger(){
		String parsed = value.replaceAll("-","");
		return Integer.parseInt(parsed);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocialSecurityNumber other = (SocialSecurityNumber) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public static void main(String[] args){
		String test = "552-32-3532";
		SocialSecurityNumber ssn = new SocialSecurityNumber(test);
		int x = ssn.toInteger();
		System.out.println("X:"+(x+1));
	}
	
}
/*
^            # Assert position at the beginning of the string.
(?!000|666)  # Assert that neither "000" nor "666" can be matched here.
[0-8]        # Match a digit between 0 and 8.
[0-9]{2}     # Match a digit, exactly two times.
-            # Match a literal "-".
(?!00)       # Assert that "00" cannot be matched here.
[0-9]{2}     # Match a digit, exactly two times.
-            # Match a literal "-".
(?!0000)     # Assert that "0000" cannot be matched here.
[0-9]{4}     # Match a digit, exactly four times.
$            # Assert position at the end of the string.
 */
