package jhg.util;

public class NameValue extends Value {

	public NameValue(String t) {
		super(t);
		
	}

	@Override
	public void validate() throws ValidationException {
		valid = val instanceof String; //new default keyword
		//System.out.println("valid."+( val instanceof String));
		String sval = (String)val;
		valid = valid && sval.length()>2;
		invalidMsg = (valid)?"Valid":"Must be string with length greater than 2.";
		if(!valid){
			throw new ValidationException(invalidMsg);
		}
	}
	
	public String getName(){
		return (String)val;
	}
	public String toString(){
		return val.toString();
	}


}
