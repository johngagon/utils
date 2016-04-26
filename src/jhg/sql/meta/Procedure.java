package jhg.sql.meta;

public class Procedure {
	
	public static enum Type{
		/*
		procedureResultUnknown - Cannot determine if a return value will be returned
		procedureNoResult - Does not return a return value
		procedureReturnsResult - Returns a return value

		 */
	}
	
	public static enum Field{
		NIL,
		PROCEDURE_CAT,
		PROCEDURE_SCHEM,
		PROCEDURE_NAME,
		REMARKS,
		PROCEDURE_TYPE,//Type
		SPECIFIC_NAME;		
	}	
	
/*
Retrieves a description of the stored procedures available in the given catalog.
Only procedure descriptions matching the schema and procedure name criteria are returned. They are ordered by PROCEDURE_CAT, PROCEDURE_SCHEM, PROCEDURE_NAME and SPECIFIC_ NAME.

Each procedure description has the the following columns:

PROCEDURE_CAT String => procedure catalog (may be null)
PROCEDURE_SCHEM String => procedure schema (may be null)
PROCEDURE_NAME String => procedure name
reserved for future use
reserved for future use
reserved for future use
REMARKS String => explanatory comment on the procedure
PROCEDURE_TYPE short => kind of procedure:
procedureResultUnknown - Cannot determine if a return value will be returned
procedureNoResult - Does not return a return value
procedureReturnsResult - Returns a return value
SPECIFIC_NAME String => The name which uniquely identifies this procedure within its schema.
A user may not have permissions to execute any of the procedures that are returned by getProcedures
 */
}
