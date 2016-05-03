package jhg.amlan;

/**
 * Noun
 * 
 * @author jgagon
 *
 */
public class N {

	/*
	 * categories  (can belong to many)
	 * relationships (can be owned or supported or opposed by)
	 * classification (can belong to one)
	 * 
	 * 
	 */
	public static N ROOT = new N();
	/*
	 * Ontology component
	 * 
	 * Individual      John Q. Parker Jr.
	 * Classes,        humans 
	 * Sets            the group of monkeys in that tree
	 * Attributes      the personality of the dolphin
	 * Relation        the role or     (is class, identity), has, 
	 * Restriction     
	 * Rule
	 * Axiom
	 * Event (change of attribut or relation)
	 * 
	 * 
	 * -"Definition"
	 * -"isClassOrIndividual"
	 * 
	 * Energy
	 * 	 -joules
	 * 
	 * Matter 
	 * 	-phase
	 *  -composition
	 *  -temperature, (gas-pressure)
	 *  
	 * 		Mass
	 * 	    Quantity
	 * 	 Object
	 * 		(solid,heap,volume,stream)  	
	 *      -quantity
	 *      -character (lava, aerogel, colloid)
	 *      
	 *      product (biological product)		
	 *              man-made - vehicle, building, furnishing, clothing, food, money, art, 
	 *              
	 * 		Living - (Biological Taxonomy)
	 * 
	 * 			Person
	 * 
	 * Characteristic Event
	 * 		Competition, Recital, Battle
	 * 
	 * Location
	 * 		New Amsterdam
	 * 
	 * Abstraction
	 * 		xenophobia
	 */
	@SuppressWarnings("unused")
	private N parent;
	
	private String name;
	
	//Basic noun
	public N(){
		
	}
	
	//Proper noun
	public N(String aName){
		this.name = aName;
	}
	
	public String n(){
		return this.name;
	}
}
