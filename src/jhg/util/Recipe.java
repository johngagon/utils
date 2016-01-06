/**
 * 
 */
package jhg.util;

import java.util.Hashtable;
import java.util.TreeSet;

import jhg.util.Value.ValidationException;

/**
 * @author jgagon
 *
 */
public class Recipe extends GenericObject {
	
	/*
	 * Extensions:
	 * 1. declare field names.
	 * 2. statically add above to fieldNames
	 * 3. extend the constructor with call to super(); and nothing else.
	 * 4. implement a static createInstance return resulting new instance that constructor.
	 * 
	 * Highly recommended:
	 * 1. override validate (call super first)
	 * 
	 * Optional:
	 * 1. override toString
	 * 2. implement any other rules etc.
	 * 
	 */
	
	public static final String RECIPE_NAME = "rname";
	public static final String DESCRIPTION = "description";
	
	
	static{
		fieldNames.clear();//clear or don't clear
		fieldNames.add(RECIPE_NAME);
		fieldNames.add(DESCRIPTION);
	}	
	
	protected Recipe() {
		super();
	}	

	public static Recipe createInstance(){
		return new Recipe();
	}
	
	protected void validate() throws ValidationException{
		super.validate();
		/*
		 * check that the types used in test generic are correct.
		 * also, try initializing with nothing more than a list of strings
		 */
	}

}
