package jhg.sql.meta;

public class VersionColumn {

	public static enum PseudoColumn{
/*
versionColumnUnknown - may or may not be pseudo column
versionColumnNotPseudo - is NOT a pseudo column
versionColumnPseudo - is a pseudo column
		
 */
	}
	
	public static enum Field{
		NIL,
		SCOPE,
		COLUMN_NAME,
		DATA_TYPE,
		TYPE_NAME,
		COLUMN_SIZE,
		BUFFER_LENGTH,
		DECIMAL_DIGITS,
		PSEUDO_COLUMN;//PseudoColumn		
	}	
	
/*
SCOPE short => is not used
COLUMN_NAME String => column name
DATA_TYPE int => SQL data type from java.sql.Types
TYPE_NAME String => Data source-dependent type name
COLUMN_SIZE int => precision
BUFFER_LENGTH int => length of column value in bytes
DECIMAL_DIGITS short => scale - Null is returned for data types where DECIMAL_DIGITS is not applicable.
PSEUDO_COLUMN short => whether this is pseudo column like an Oracle ROWID
The COLUMN_SIZE column represents the specified column size for the given column. For numeric data, this is the maximum precision. For character data, this is the length in characters. For datetime datatypes, this is the length in characters of the String representation (assuming the maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes. For the ROWID datatype, this is the length in bytes. Null is returned for data types where the column size is not applicable.
 */
}
