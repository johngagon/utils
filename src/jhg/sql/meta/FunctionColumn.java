package jhg.sql.meta;

public class FunctionColumn {
	
	public static enum Type{
		/*
		functionColumnUnknown - nobody knows
		functionColumnIn - IN parameter
		functionColumnInOut - INOUT parameter
		functionColumnOut - OUT parameter
		functionColumnReturn - function return value
		functionColumnResult - Indicates that the parameter or column is a column in the ResultSet
		 */
	}
	public static enum Nullable{
		/*
		functionNoNulls - does not allow NULL values
		functionNullable - allows NULL values
		functionNullableUnknown - nullability unknown

		 */
	}
	
	public static enum Field{
		FUNCTION_CAT,
		FUNCTION_SCHEM,
		FUNCTION_NAME,
		COLUMN_NAME,
		COLUMN_TYPE,//Type
		DATA_TYPE,
		TYPE_NAME,
		PRECISION,
		LENGTH,
		SCALE,
		RADIX,
		NULLABLE,
		REMARKS,
		CHAR_OCTET_LENGTH,
		ORDINAL_POSITION,
		IS_NULLABLE,//TriFlag
		SPECIFIC_NAME;

	}	

	/*
Each row in the ResultSet is a parameter description, column description or return type description with the following fields:

FUNCTION_CAT String => function catalog (may be null)
FUNCTION_SCHEM String => function schema (may be null)
FUNCTION_NAME String => function name. This is the name used to invoke the function
COLUMN_NAME String => column/parameter name
COLUMN_TYPE Short => kind of column/parameter:
functionColumnUnknown - nobody knows
functionColumnIn - IN parameter
functionColumnInOut - INOUT parameter
functionColumnOut - OUT parameter
functionColumnReturn - function return value
functionColumnResult - Indicates that the parameter or column is a column in the ResultSet
DATA_TYPE int => SQL type from java.sql.Types
TYPE_NAME String => SQL type name, for a UDT type the type name is fully qualified
PRECISION int => precision
LENGTH int => length in bytes of data
SCALE short => scale - null is returned for data types where SCALE is not applicable.
RADIX short => radix
NULLABLE short => can it contain NULL.
functionNoNulls - does not allow NULL values
functionNullable - allows NULL values
functionNullableUnknown - nullability unknown
REMARKS String => comment describing column/parameter
CHAR_OCTET_LENGTH int => the maximum length of binary and character based parameters or columns. For any other datatype the returned value is a NULL
ORDINAL_POSITION int => the ordinal position, starting from 1, for the input and output parameters. A value of 0 is returned if this row describes the function's return value. For result set columns, it is the ordinal position of the column in the result set starting from 1.
IS_NULLABLE String => ISO rules are used to determine the nullability for a parameter or column.
YES --- if the parameter or column can include NULLs
NO --- if the parameter or column cannot include NULLs
empty string --- if the nullability for the parameter or column is unknown
SPECIFIC_NAME String => the name which uniquely identifies this function within its schema. This is a user specified, or DBMS generated, name that may be different then the FUNCTION_NAME for example with overload functions
The PRECISION column represents the specified column size for the given parameter or column. For numeric data, this is the maximum precision. For character data, this is the length in characters. For datetime datatypes, this is the length in characters of the String representation (assuming the maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes. For the ROWID datatype, this is the length in bytes. Null is returned for data types where the column size is not applicable.

	 */
}
