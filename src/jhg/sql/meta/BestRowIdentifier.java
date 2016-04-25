package jhg.sql.meta;

public class BestRowIdentifier {

	/*
Each column description has the following columns:

SCOPE short => actual scope of result
bestRowTemporary - very temporary, while using row
bestRowTransaction - valid for remainder of current transaction
bestRowSession - valid for remainder of current session
COLUMN_NAME String => column name
DATA_TYPE int => SQL data type from java.sql.Types
TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
COLUMN_SIZE int => precision
BUFFER_LENGTH int => not used
DECIMAL_DIGITS short => scale - Null is returned for data types where DECIMAL_DIGITS is not applicable.
PSEUDO_COLUMN short => is this a pseudo column like an Oracle ROWID
bestRowUnknown - may or may not be pseudo column
bestRowNotPseudo - is NOT a pseudo column
bestRowPseudo - is a pseudo column
	 */
}
