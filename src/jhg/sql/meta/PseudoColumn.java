package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PseudoColumn {
	
	public static enum Field{
		NIL,
		TABLE_CAT,
		TABLE_SCHEM,
		TABLE_NAME,
		COLUMN_NAME,
		DATA_TYPE,
		COLUMN_SIZE,
		DECIMAL_DIGITS,
		NUM_PREC_RADIX,
		COLUMN_USAGE,
		REMARKS,
		CHAR_OCTET_LENGTH,
		IS_NULLABLE;//TriFlag
	}	
	private String tableCat,tableSchem,tableName,columnName,columnUsage,remarks,isNullable;
	private int dataType;
	private int columnSize,decimalDigits,numPrecRadix,charOctetLength;
	
	public PseudoColumn(ResultSet rs){
		try{
			tableCat = rs.getString(Field.TABLE_CAT.ordinal());
			tableSchem = rs.getString(Field.TABLE_SCHEM.ordinal());
			tableName = rs.getString(Field.TABLE_NAME.ordinal());
			columnName = rs.getString(Field.COLUMN_NAME.ordinal());
			columnUsage = rs.getString(Field.COLUMN_USAGE.ordinal());
			remarks = rs.getString(Field.REMARKS.ordinal());
			isNullable = rs.getString(Field.IS_NULLABLE.ordinal());
			dataType = rs.getInt(Field.DATA_TYPE.ordinal());
			columnSize = rs.getInt(Field.COLUMN_SIZE.ordinal());
			decimalDigits = rs.getInt(Field.DECIMAL_DIGITS.ordinal());
			numPrecRadix = rs.getInt(Field.NUM_PREC_RADIX.ordinal());
			charOctetLength = rs.getInt(Field.CHAR_OCTET_LENGTH.ordinal());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public String getTableCat() {
		return tableCat;
	}

	public String getTableSchem() {
		return tableSchem;
	}

	public String getTableName() {
		return tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getColumnUsage() {
		return columnUsage;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public int getDataType() {
		return dataType;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public int getNumPrecRadix() {
		return numPrecRadix;
	}

	public int getCharOctetLength() {
		return charOctetLength;
	}

	@Override
	public String toString() {
		return "PseudoColumn [tableCat=" + tableCat + ", tableSchem="
				+ tableSchem + ", tableName=" + tableName + ", columnName="
				+ columnName + ", columnUsage=" + columnUsage + ", remarks="
				+ remarks + ", isNullable=" + isNullable + ", dataType="
				+ dataType + ", columnSize=" + columnSize + ", decimalDigits="
				+ decimalDigits + ", numPrecRadix=" + numPrecRadix
				+ ", charOctetLength=" + charOctetLength + "]";
	}
	
/*
Only column descriptions matching the catalog, schema, table and column name criteria are returned. They are ordered by TABLE_CAT,TABLE_SCHEM, TABLE_NAME and COLUMN_NAME.

Each column description has the following columns:

TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
COLUMN_NAME String => column name
DATA_TYPE int => SQL type from java.sql.Types
COLUMN_SIZE int => column size.
DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
NUM_PREC_RADIX int => Radix (typically either 10 or 2)
COLUMN_USAGE String => The allowed usage for the column. The value returned will correspond to the enum name returned by PseudoColumnUsage.name()
REMARKS String => comment describing column (may be null)
CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
YES --- if the column can include NULLs
NO --- if the column cannot include NULLs
empty string --- if the nullability for the column is unknown
The COLUMN_SIZE column specifies the column size for the given column. For numeric data, this is the maximum precision. For character data, this is the length in characters. For datetime datatypes, this is the length in characters of the String representation (assuming the maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes. For the ROWID datatype, this is the length in bytes. Null is returned for data types where the column size is not applicable.
 */
}
