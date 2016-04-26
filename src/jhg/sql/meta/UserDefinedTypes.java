package jhg.sql.meta;

public class UserDefinedTypes {
	
	public static enum Field{
		NIL,
		TYPE_CAT,
		TYPE_SCHEM,
		TYPE_NAME,
		CLASS_NAME,
		DATA_TYPE,
		REMARKS,
		BASE_TYPE;

	}	
	
/*
TYPE_CAT String => the type's catalog (may be null)
TYPE_SCHEM String => type's schema (may be null)
TYPE_NAME String => type name
CLASS_NAME String => Java class name
DATA_TYPE int => type value defined in java.sql.Types. One of JAVA_OBJECT, STRUCT, or DISTINCT
REMARKS String => explanatory comment on the type
BASE_TYPE short => type code of the source type of a DISTINCT type or the type that implements the user-generated reference type of the SELF_REFERENCING_COLUMN of a structured type as defined in java.sql.Types (null if DATA_TYPE is not DISTINCT or not STRUCT with REFERENCE_GENERATION = USER_DEFINED)
 */
}
