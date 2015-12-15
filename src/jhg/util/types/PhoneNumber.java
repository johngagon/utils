package jhg.util.types;

public class PhoneNumber{

	private String value;
	
	public PhoneNumber(String s){
		if(!validatePhoneNumber(s)){
			throw new IllegalArgumentException("Phone number does not appear valid: '"+s+"' .");
		}else{
			this.value = s;
		}
	}
	
    private static boolean validatePhoneNumber(String phoneNo) {
    	if(phoneNo==null){
    		return false;
    	}
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
        //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
        //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        //return false if nothing matches the input
        else return false;
         
    }	
	@Override
	public String toString() {
		return value;
	}
	
	public Integer toInteger(){
		String parsed = value.replaceAll("-","");
		parsed = parsed.replaceAll("\\(","");
		parsed = parsed.replaceAll("\\)","");
		parsed = parsed.replaceAll("\\.","");
		parsed = parsed.replaceAll(" ","");
		
		return Integer.parseInt(parsed);
	}

	@Override
	public int hashCode() {
		final int prime = 91;
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
		PhoneNumber other = (PhoneNumber) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public static void main(String[] args){
		String test = "123.456.7890";
		PhoneNumber p = new PhoneNumber(test);
		int x = p.toInteger();
		System.out.println("X:"+(x+1));
		System.out.println("Phone:"+p);
	}	
}
/*
Phone number 1234567890 validation result: true
Phone number 123-456-7890 validation result: true
Phone number 123-456-7890 x1234 validation result: true
Phone number 123-456-7890 ext1234 validation result: true
Phone number (123)-456-7890 validation result: true
Phone number 123.456.7890 validation result: true
Phone number 123 456 7890 validation result: true
 */
