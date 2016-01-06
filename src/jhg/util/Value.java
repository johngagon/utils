package jhg.util;

public abstract class Value {

	public static class ValidationException extends Exception{

		private static final long serialVersionUID = 1L;

		public ValidationException(String msg){
			super(msg);
		}
	}
	//simple object wrapper
	protected Object val;
	protected boolean valid = false;
	protected String invalidMsg = "Not validated yet.";
	
	public Value(Object t){
		super();
		this.val = t;
		//validate();
		//if(!valid){
		//	throw new IllegalArgumentException("Object "+t.toString()+" is not valid.");
		//}
	}
	public String getValidationMessage(){
		return this.invalidMsg;
	}
	
	public Object getValue(){
		return val;
	}
	
	public abstract void validate() throws ValidationException;
	
	public boolean isValid(){
		return this.valid;
	}
	public String toString(){
		return val.toString();
	}
	
}
