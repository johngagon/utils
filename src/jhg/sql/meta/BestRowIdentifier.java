package jhg.sql.meta;

import java.sql.DatabaseMetaData;

/*
 * FIXME add constructor with ResultSet, make getters and toString() and test out.
 */
public class BestRowIdentifier {

	public static enum Scope{
		bestRowTemporary(DatabaseMetaData.bestRowTemporary),
		bestRowTransaction(DatabaseMetaData.bestRowTransaction),
		bestRowSession(DatabaseMetaData.bestRowSession);
		private int code;
		private Scope(int scope){
			scope = code;
		}
		public static Scope from(int c){
			for(Scope n:Scope.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;			
		}
	}
	public static enum PseudoColumn{
		bestRowUnknown(DatabaseMetaData.bestRowUnknown),
		bestRowNotPseudo(DatabaseMetaData.bestRowNotPseudo),
		bestRowPseudo(DatabaseMetaData.bestRowPseudo);	
		private int code;
		private PseudoColumn(int c){
			this.code = c;
		}
		public static PseudoColumn from(int c){
			for(PseudoColumn n:PseudoColumn.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;			
		}		
	}
	public static enum Field{
		NIL,//0
		SCOPE,
		COLUMN_NAME,
		DATA_TYPE,
		TYPE_NAME,
		COLUMN_SIZE,
		BUFFER_LENGTH,
		DECIMAL_DIGITS,
		PSEUDO_COLUMN;
	}
	Scope scope;
	String columnName,typeName;
	int dataType,columnSize,bufferLength;
	short decimalDigits;
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
