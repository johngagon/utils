package jhg.util;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;



public class TestGenericObject implements Constants {

	public static void main(String[] args){
	
		testRecipe();
	}	
	
	private static void debug(GenericObject go){
		System.out.print(go.getClass().getName());
		System.out.print("{ID::"+go.getId());
		System.out.print  (", default:"+go.isDefault());
		System.out.print  (", deleted:"+go.isDeleted());
		System.out.println(", valid:"+go.isValid()+"}");
		if(go.isValid()){
			System.out.println("\tProperties:"+go.getProperties().toString());
		}
	}
	
	public static void testRecipe(){
		GenericObject  go = Recipe.DEFAULT;
		System.out.println("defaulting");
		debug(go);
		
		System.out.println("\npre init");
		go = Recipe.createInstance();
		debug(go);
		
		
		Map<String,Value> init = new Hashtable<String,Value>();
		init.put(Recipe.RECIPE_NAME,new Value("j"){
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
		
		System.out.println("\npost init");
		go.initialize(init);
		debug(go);
		
		System.out.println("\ncopy and modify");
		GenericObject recipeCopy = Recipe.createInstance();
		if(go.isValid()){
			Map<String,Value> copyProps = Recipe.copyProperties(go);
			copyProps.put(GenericObject.DESCRIPTION,new NameValue("hxy"));
			recipeCopy.initialize(copyProps);
			debug(recipeCopy);
		}
		
		System.out.println(L+"size.");
		System.out.println("Qty Generic Objects:"+GenericObject.size());
		
		System.out.println(L+"list.");
		List<GenericObject> gos = GenericObject.getAll();//.getList(GenericObject.Subset.ACTIVE);
		for(GenericObject g:gos){
			debug(g);
		}
		
		System.out.println("\nget 2.");
		GenericObject gg = GenericObject.get(Integer.valueOf(2));
		debug(gg);
		
		System.out.println("\ndelete 1.");
		go.delete();
		debug(go);		
		
		System.out.println(L+"list.");
		gos = GenericObject.getAll();//.getList(GenericObject.Subset.ACTIVE);
		for(GenericObject gi:gos){
			debug(gi);
		}		
		
	}
	
	
	
	public static void testGenericObject(){
		GenericObject  go = GenericObject.DEFAULT;
		System.out.println("defaulting");
		debug(go);	
		
		System.out.println("\npre init");
		go = GenericObject.createInstance();
		debug(go);
		
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
		
		go.initialize(init);
		System.out.println("\npost init");
		debug(go);
		
		System.out.println("\ndeleted.");
		go.delete();
		debug(go);			
	}
}
