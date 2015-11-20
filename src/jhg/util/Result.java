package jhg.util;

import java.util.*;

public class Result {

	private Map<String,Object> values;
	private Map<String,Throwable> errors;
	private String message;
	private boolean isSuccessful;
	
	
	
	public Result(Map<String, Object> values, Map<String, Throwable> errors, String message, boolean isSuccessful) {
		super();
		this.values = values;
		this.errors = errors;
		this.message = message;
		this.isSuccessful = isSuccessful;
	}
	public Result(boolean isSuccessful) {
		super();
		this.values = new Hashtable<String,Object>();
		this.errors = new Hashtable<String,Throwable>();
		this.message = "";
		this.isSuccessful = isSuccessful;
	}	
	
	
	
	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
	public void setErrors(Map<String, Throwable> errors) {
		this.errors = errors;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getValues() {
		return values;
	}
	
	public Map<String, Throwable> getErrors() {
		return errors;
	}
	public String getMessage() {
		return message;
	}
	public boolean isSuccessful() {
		return isSuccessful;
	}
	
	
	

}
