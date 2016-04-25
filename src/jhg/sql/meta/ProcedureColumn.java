package jhg.sql.meta;

public class ProcedureColumn {
/*
Each row in the ResultSet is a parameter description or column description with the following fields:

PROCEDURE_CAT String => procedure catalog (may be null)
PROCEDURE_SCHEM String => procedure schema (may be null)
PROCEDURE_NAME String => procedure name
COLUMN_NAME String => column/parameter name
COLUMN_TYPE Short => kind of column/parameter:
procedureColumnUnknown - nobody knows
procedureColumnIn - IN parameter
procedureColumnInOut - INOUT parameter
procedureColumnOut - OUT parameter
procedureColumnReturn - procedure return value
procedureColumnResult - result column in ResultSet
DATA_TYPE int => SQL type from java.sql.Types
TYPE_NAME String => SQL type name, for a UDT type the type name is fully qualified
PRECISION int => precision
LENGTH int => length in bytes of data
SCALE short => scale - null is returned for data types where SCALE is not applicable.
RADIX short => radix
NULLABLE short => can it contain NULL.
procedureNoNulls - does not allow NULL values
procedureNullable - allows NULL values
procedureNullableUnknown - nullability unknown
REMARKS String => comment describing parameter/column
COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
The string NULL (not enclosed in quotes) - if NULL was specified as the default value
TRUNCATE (not enclosed in quotes) - if the specified default value cannot be represented without truncation
NULL - if a default value was not specified
SQL_DATA_TYPE int => reserved for future use
SQL_DATETIME_SUB int => reserved for future use
CHAR_OCTET_LENGTH int => the maximum length of binary and character based columns. For any other datatype the returned value is a NULL
ORDINAL_POSITION int => the ordinal position, starting from 1, for the input and output parameters for a procedure. A value of 0 is returned if this row describes the procedure's return value. For result set columns, it is the ordinal position of the column in the result set starting from 1. If there are multiple result sets, the column ordinal positions are implementation defined.
IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
YES --- if the column can include NULLs
NO --- if the column cannot include NULLs
empty string --- if the nullability for the column is unknown
SPECIFIC_NAME String => the name which uniquely identifies this procedure within its schema.
Note: Some databases may not return the column descriptions for a procedure.

The PRECISION column represents the specified column size for the given column. For numeric data, this is the maximum precision. For character data, this is the length in characters. For datetime datatypes, this is the length in characters of the String representation (assuming the maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes. For the ROWID datatype, this is the length in bytes. Null is returned for data types where the column size is not applicable.
 */
}
