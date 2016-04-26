package jhg.sql.meta;

public class SuperType {
	
	public static enum Field{
		NIL,
		TYPE_CAT,
		TYPE_SCHEM,
		TYPE_NAME,
		SUPERTYPE_CAT,
		SUPERTYPE_SCHEM,
		SUPERTYPE_NAME;		
	}	
	
/*
TYPE_CAT String => the UDT's catalog (may be null)
TYPE_SCHEM String => UDT's schema (may be null)
TYPE_NAME String => type name of the UDT
SUPERTYPE_CAT String => the direct super type's catalog (may be null)
SUPERTYPE_SCHEM String => the direct super type's schema (may be null)
SUPERTYPE_NAME String => the direct super type's name
 */
}
