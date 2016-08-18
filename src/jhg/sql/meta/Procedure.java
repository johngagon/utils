package jhg.sql.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Procedure {
	
	public static enum Type{
		procedureResultUnknown(DatabaseMetaData.procedureResultUnknown),
		procedureNoResult(DatabaseMetaData.procedureNoResult),
		procedureReturnsResult(DatabaseMetaData.procedureReturnsResult);
		private int code;
		private Type(int c){
			this.code = c;
		}
		public static Type from(int c){
			for(Type n:Type.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;			
		}		
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
	
	private String procedureCat, procedureSchem, procedureName, remarks, specificName;
	private Type procedureType;
	
	public Procedure(ResultSet rs){
		try{
			procedureCat = rs.getString(Field.PROCEDURE_CAT.ordinal()); 
			procedureSchem = rs.getString(Field.PROCEDURE_SCHEM.ordinal()); 
			procedureName = rs.getString(Field.PROCEDURE_NAME.ordinal()); 
			remarks = rs.getString(Field.REMARKS.ordinal()); 
			specificName = rs.getString(Field.SPECIFIC_NAME.ordinal());
			procedureType = Type.from(rs.getInt(Field.PROCEDURE_TYPE.ordinal()));
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public String getProcedureCat() {
		return procedureCat;
	}

	public String getProcedureSchem() {
		return procedureSchem;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getSpecificName() {
		return specificName;
	}

	public Type getProcedureType() {
		return procedureType;
	}

	@Override
	public String toString() {
		return "Procedure [procedureCat=" + procedureCat + ", procedureSchem="
				+ procedureSchem + ", procedureName=" + procedureName
				+ ", remarks=" + remarks + ", specificName=" + specificName
				+ ", procedureType=" + procedureType + "]";
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
