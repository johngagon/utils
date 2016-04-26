package jhg.sql.meta;

public class SuperTable {

	public static enum Field{
		NIL,
		TABLE_CAT,
		TABLE_SCHEM,
		TABLE_NAME,
		SUPERTABLE_NAME;		
	}	
	
/*
Each type description has the following columns:

TABLE_CAT String => the type's catalog (may be null)
TABLE_SCHEM String => type's schema (may be null)
TABLE_NAME String => type name
SUPERTABLE_NAME String => the direct super type's name
 */
}
