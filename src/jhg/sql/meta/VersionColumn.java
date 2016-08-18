package jhg.sql.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;



public class VersionColumn {

	public static enum PseudoColumn{
		versionColumnUnknown(DatabaseMetaData.versionColumnUnknown),
		versionColumnNotPseudo(DatabaseMetaData.versionColumnNotPseudo),
		versionColumnPseudo(DatabaseMetaData.versionColumnPseudo);
		private int code;
		private PseudoColumn(int scope){
			code = scope;
		}
		public static PseudoColumn from(int c){
			for(PseudoColumn n:PseudoColumn.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;			
		}		
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
	private String columnName,typeName;
	private int dataType,columnSize,bufferLength;
	private short scope,decimalDigits;
	private PseudoColumn pseudoColumn;
	
	public VersionColumn(ResultSet rs){
		try{
			columnName = rs.getString(Field.COLUMN_NAME.ordinal());
			typeName = rs.getString(Field.TYPE_NAME.ordinal());
			dataType = rs.getInt(Field.DATA_TYPE.ordinal());
			columnSize = rs.getInt(Field.COLUMN_SIZE.ordinal());
			bufferLength = rs.getInt(Field.BUFFER_LENGTH.ordinal());
			scope = rs.getShort(Field.SCOPE.ordinal());
			decimalDigits = rs.getShort(Field.DECIMAL_DIGITS.ordinal());
			pseudoColumn = PseudoColumn.from(rs.getInt(Field.PSEUDO_COLUMN.ordinal()));
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
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

	public short getScope() {
		return scope;
	}

	public short getDecimalDigits() {
		return decimalDigits;
	}

	public PseudoColumn getPseudoColumn() {
		return pseudoColumn;
	}

	@Override
	public String toString() {
		return "VersionColumn [columnName=" + columnName + ", typeName="
				+ typeName + ", dataType=" + dataType + ", columnSize="
				+ columnSize + ", bufferLength=" + bufferLength + ", scope="
				+ scope + ", decimalDigits=" + decimalDigits
				+ ", pseudoColumn=" + pseudoColumn + "]";
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
