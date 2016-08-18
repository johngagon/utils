package jhg.sql.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Function {

	public static enum Type{
		functionResultUnknown(DatabaseMetaData.functionResultUnknown),
		functionNoTable(DatabaseMetaData.functionNoTable),
		functionReturnsTable(DatabaseMetaData.functionReturnsTable);
		private int code;
		private Type(int c){
			code = c;
		}
		public static Type from(int c){
			for(Type n:Type.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;			
		}

	}
	public static enum Field{
		FUNCTION_CAT,
		FUNCTION_SCHEM,
		FUNCTION_NAME,
		REMARKS,
		FUNCTION_TYPE,//Type
		SPECIFIC_NAME;
	}	
	private String functionCat,functionSchem,functionName,remarks,specificName;
	private Type functionType;
	public Function(ResultSet rs){
		try{
			functionCat = rs.getString(Field.FUNCTION_CAT.ordinal());
			functionSchem = rs.getString(Field.FUNCTION_SCHEM.ordinal());
			functionName = rs.getString(Field.FUNCTION_NAME.ordinal());
			remarks = rs.getString(Field.REMARKS.ordinal());
			specificName = rs.getString(Field.SPECIFIC_NAME.ordinal());
			functionType = Type.from(rs.getInt(Field.FUNCTION_TYPE.ordinal()));
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}
	public String getFunctionCat() {
		return functionCat;
	}
	public String getFunctionSchem() {
		return functionSchem;
	}
	public String getFunctionName() {
		return functionName;
	}
	public String getRemarks() {
		return remarks;
	}
	public String getSpecificName() {
		return specificName;
	}
	public Type getFunctionType() {
		return functionType;
	}
	@Override
	public String toString() {
		return "Function [functionCat=" + functionCat + ", functionSchem="
				+ functionSchem + ", functionName=" + functionName
				+ ", remarks=" + remarks + ", specificName=" + specificName
				+ ", functionType=" + functionType + "]";
	}
	
/*
functionResultUnknown - Cannot determine if a return value or table will be returned
functionNoTable- Does not return a table
functionReturnsTable - Returns a table
 */
	
	
		

	
}
/*

FUNCTION_CAT String => function catalog (may be null)
FUNCTION_SCHEM String => function schema (may be null)
FUNCTION_NAME String => function name. This is the name used to invoke the function
REMARKS String => explanatory comment on the function
FUNCTION_TYPE short => kind of function:
functionResultUnknown - Cannot determine if a return value or table will be returned
functionNoTable- Does not return a table
functionReturnsTable - Returns a table
SPECIFIC_NAME String => the name which uniquely identifies this function within its schema. This is a user specified, or DBMS generated, name that may be different then the FUNCTION_NAME for example with overload functions


*/