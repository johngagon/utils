package jhg.sql.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;





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
			code = scope;
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
	
	public BestRowIdentifier(ResultSet rs){
		//typeCatalog = rs.getString(Field.TYPE_CAT.ordinal());
		try{
			scope = Scope.from(rs.getInt(Field.SCOPE.ordinal()));
			columnName = rs.getString(Field.COLUMN_NAME.ordinal());
			typeName = rs.getString(Field.TYPE_NAME.ordinal());
			dataType = rs.getInt(Field.DATA_TYPE.ordinal());
			columnSize = rs.getInt(Field.COLUMN_SIZE.ordinal());
			bufferLength = rs.getInt(Field.BUFFER_LENGTH.ordinal());
			decimalDigits = rs.getShort(Field.DECIMAL_DIGITS.ordinal());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public Scope getScope() {
		return scope;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getDataType() {
		return dataType;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public int getBufferLength() {
		return bufferLength;
	}

	public short getDecimalDigits() {
		return decimalDigits;
	}

	@Override
	public String toString() {
		return "BestRowIdentifier [scope=" + scope + ", columnName="
				+ columnName + ", typeName=" + typeName + ", dataType="
				+ dataType + ", columnSize=" + columnSize + ", bufferLength="
				+ bufferLength + ", decimalDigits=" + decimalDigits + "]";
	}
	
	
	
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
