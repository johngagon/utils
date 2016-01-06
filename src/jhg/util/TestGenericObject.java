package jhg.util;

import java.util.*;

import jhg.util.Value.ValidationException;

public class TestGenericObject {

	public static void main(String[] args){
		
		GenericObject go = GenericObject.createInstance();
		System.out.println(go.isDefault());
		System.out.println(go.isDeleted());
		System.out.println(go.isValid());
		//System.out.println(go.getProperties().toString());
		
		Map<String,Value> init = new Hashtable<String,Value>();
		init.put(GenericObject.NAME,new Value("j"){
			public void validate() throws ValidationException{//TODO simplify this
				valid = val instanceof String; //new default keyword
				invalidMsg = (valid)?"Valid":"Not String";
				if(!valid){
					throw new ValidationException(invalidMsg);
				}
			}
		});
		init.put(GenericObject.DESCRIPTION,new NameValue("gxy"));
		//init.put(GenericObject.NAME,"g");
		
		try {
			go.initialize(init);
		} catch (ValidationException e) {
			System.out.println(e.getMessage());
		}//map of required properties
		/*
		for(String s:GenericObject.fieldNames){
			System.out.println(go.getProperty(s));
		}
		*/
		System.out.println(go.getProperties().toString());
		
		System.out.println(go.isDefault());
		System.out.println(go.isDeleted());
		System.out.println(go.isValid());		
		go.delete();
		System.out.println("deleted.");
		System.out.println(go.isDefault());
		System.out.println(go.isDeleted());
		System.out.println(go.isValid());		
		go = GenericObject.DEFAULT;
		System.out.println("----");
		System.out.println(go.isDefault());
		
	}	
	
}
