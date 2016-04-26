package jhg.sql.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Column {
	
	public static enum Nullable{
		columnNoNulls(DatabaseMetaData.columnNoNulls),
		columnNullable(DatabaseMetaData.columnNullable),
		columnNullableUnknown(DatabaseMetaData.columnNullableUnknown);
		private int code;
		private Nullable(int c){
			code = c;
		}
		public static Nullable from(int c){
			for(Nullable n:Nullable.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;
		}
	}	
	
	public static enum Field{
		NIL,//				0. 
		TABLE_CAT,//		1. TABLE_CAT String => table catalog (may be null)
		TABLE_SCHEM,//		2. TABLE_SCHEM String => table schema (may be null)
		TABLE_NAME,//		3. TABLE_NAME String => table name
		COLUMN_NAME,//		4. COLUMN_NAME String => column name
		DATA_TYPE,//		5. DATA_TYPE int => SQL type from java.sql.Types
		TYPE_NAME,//		6. TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
		COLUMN_SIZE,//		7. COLUMN_SIZE int => column size.
		BUFFER_LENGTH,//	8. BUFFER_LENGTH is not used.
		DECIMAL_DIGITS,//	9. DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
		NUM_PREC_RADIX,//	10. NUM_PREC_RADIX int => Radix (typically either 10 or 2)
		NULLABLE,//			11. NULLABLE int => is NULL allowed.
		REMARKS,//			12. REMARKS String => comment describing column (may be null)
		COLUMN_DEF,//		13. COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
		SQL_DATA_TYPE,//		14. SQL_DATA_TYPE int => unused
		SQL_DATETIME_SUB,//		15. SQL_DATETIME_SUB int => unused
		CHAR_OCTET_LENGTH,//		16. CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
		ORDINAL_POSITION,//		17. ORDINAL_POSITION int => index of column in table (starting at 1)
		IS_NULLABLE,//		18. IS_NULLABLE String => ISO rules are used to determine the nullability for a column.YES --- if the column can include NULLs NO --- if the column cannot include NULLs empty string --- if the nullability for the column is unknown
		SCOPE_CATALOG,//		19. SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
		SCOPE_SCHEMA,//		20. SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
		SCOPE_TABLE,//		21. SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
		SOURCE_DATA_TYPE,//		22. SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
		IS_AUTOINCREMENT,//		23. IS_AUTOINCREMENT String => Indicates whether this column is auto incremented YES --- if the column is auto incremented NO --- if the column is not auto incremented empty string --- if it cannot be determined whether the column is auto incremented
		IS_GENERATEDCOLUMN;//		24. IS_GENERATEDCOLUMN String => Indicates whether this is a generated column YES --- if this a generated column NO --- if this not a generated column empty string --- if it cannot be determined whether this is a generated column
		
	}	
	private String tableCatalog,tableSchema,tableName,columnName,typeName,remarks,columnDefinition,scopeCatalog,scopeSchema,scopeTable;
	private int dataType,columnSize,bufferLength,decimalDigits,numberPrecisionRadix,sqlDataType,sqlDateTimeSub,characterOctetLength,ordinalPosition;
	private Nullable nullable;
	private TriFlag isNullable,isAutoIncrement,IsGeneratedColumn;
	private short sourceDataType;
	
	public Column(ResultSet rs){
		try {
			tableCatalog=rs.getString(Field.TABLE_CAT.ordinal());
			tableSchema=rs.getString(Field.TABLE_SCHEM.ordinal());
			tableName=rs.getString(Field.TABLE_NAME.ordinal());
			columnName=rs.getString(Field.COLUMN_NAME.ordinal());
			typeName=rs.getString(Field.TYPE_NAME.ordinal());
			remarks=rs.getString(Field.REMARKS.ordinal());
			columnDefinition=rs.getString(Field.COLUMN_DEF.ordinal());
			scopeCatalog=rs.getString(Field.SCOPE_CATALOG.ordinal());
			scopeSchema=rs.getString(Field.SCOPE_SCHEMA.ordinal());
			scopeTable=rs.getString(Field.SCOPE_TABLE.ordinal());
			dataType=rs.getInt(Field.DATA_TYPE.ordinal());
			columnSize=rs.getInt(Field.COLUMN_SIZE.ordinal());
			bufferLength=rs.getInt(Field.BUFFER_LENGTH.ordinal());
			decimalDigits=rs.getInt(Field.DECIMAL_DIGITS.ordinal());
			numberPrecisionRadix=rs.getInt(Field.NUM_PREC_RADIX.ordinal());
			sqlDataType=rs.getInt(Field.SQL_DATA_TYPE.ordinal());
			sqlDateTimeSub=rs.getInt(Field.SQL_DATETIME_SUB.ordinal());
			characterOctetLength=rs.getInt(Field.CHAR_OCTET_LENGTH.ordinal());
			ordinalPosition=rs.getInt(Field.ORDINAL_POSITION.ordinal());
			nullable=Nullable.from(rs.getInt(Field.NULLABLE.ordinal()));
			isNullable=TriFlag.from(rs.getString(Field.IS_NULLABLE.ordinal()));
			isAutoIncrement=TriFlag.from(rs.getString(Field.IS_AUTOINCREMENT.ordinal()));
			IsGeneratedColumn=TriFlag.from(rs.getString(Field.IS_GENERATEDCOLUMN.ordinal()));
			sourceDataType=rs.getShort(Field.SOURCE_DATA_TYPE.ordinal());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String getTableCatalog() {
		return tableCatalog;
	}

	public String getTableSchema() {
		return tableSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getColumnDefinition() {
		return columnDefinition;
	}

	public String getScopeCatalog() {
		return scopeCatalog;
	}

	public String getScopeSchema() {
		return scopeSchema;
	}

	public String getScopeTable() {
		return scopeTable;
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

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public int getNumberPrecisionRadix() {
		return numberPrecisionRadix;
	}

	public int getSqlDataType() {
		return sqlDataType;
	}

	public int getSqlDateTimeSub() {
		return sqlDateTimeSub;
	}

	public int getCharacterOctetLength() {
		return characterOctetLength;
	}

	public int getOrdinalPosition() {
		return ordinalPosition;
	}

	public Nullable getNullable() {
		return nullable;
	}

	public TriFlag getIsNullable() {
		return isNullable;
	}

	public TriFlag getIsAutoIncrement() {
		return isAutoIncrement;
	}

	public TriFlag getIsGeneratedColumn() {
		return IsGeneratedColumn;
	}

	public short getSourceDataType() {
		return sourceDataType;
	}

	@Override
	public String toString() {
		return "Column [tableCatalog=" + tableCatalog + ", tableSchema="
				+ tableSchema + ", tableName=" + tableName + ", columnName="
				+ columnName + ", typeName=" + typeName + ", remarks="
				+ remarks + ", columnDefinition=" + columnDefinition
				+ ", scopeCatalog=" + scopeCatalog + ", scopeSchema="
				+ scopeSchema + ", scopeTable=" + scopeTable + ", dataType="
				+ dataType + ", columnSize=" + columnSize + ", bufferLength="
				+ bufferLength + ", decimalDigits=" + decimalDigits
				+ ", numberPrecisionRadix=" + numberPrecisionRadix
				+ ", sqlDataType=" + sqlDataType + ", sqlDateTimeSub="
				+ sqlDateTimeSub + ", characterOctetLength="
				+ characterOctetLength + ", ordinalPosition=" + ordinalPosition
				+ ", nullable=" + nullable + ", isNullable=" + isNullable
				+ ", isAutoIncrement=" + isAutoIncrement
				+ ", IsGeneratedColumn=" + IsGeneratedColumn
				+ ", sourceDataType=" + sourceDataType + "]";
	}
	
/*
Each column description has the following columns:

1. TABLE_CAT String => table catalog (may be null)
2. TABLE_SCHEM String => table schema (may be null)
3. TABLE_NAME String => table name
4. COLUMN_NAME String => column name
5. DATA_TYPE int => SQL type from java.sql.Types
6. TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
7. COLUMN_SIZE int => column size.
8. BUFFER_LENGTH is not used.
9. DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
10. NUM_PREC_RADIX int => Radix (typically either 10 or 2)
11. NULLABLE int => is NULL allowed.
		columnNoNulls - might not allow NULL values
		columnNullable - definitely allows NULL values
		columnNullableUnknown - nullability unknown
12. REMARKS String => comment describing column (may be null)
13. COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
14. SQL_DATA_TYPE int => unused
15. SQL_DATETIME_SUB int => unused
16. CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
17. ORDINAL_POSITION int => index of column in table (starting at 1)
18. IS_NULLABLE String => ISO rules are used to determine the nullability for a column.YES --- if the column can include NULLs NO --- if the column cannot include NULLs empty string --- if the nullability for the column is unknown
19. SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
20. SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
21. SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
22. SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
23. IS_AUTOINCREMENT String => Indicates whether this column is auto incremented YES --- if the column is auto incremented NO --- if the column is not auto incremented empty string --- if it cannot be determined whether the column is auto incremented
24. IS_GENERATEDCOLUMN String => Indicates whether this is a generated column YES --- if this a generated column NO --- if this not a generated column empty string --- if it cannot be determined whether this is a generated column

The COLUMN_SIZE column specifies the column size for the given column. For numeric data, this is the maximum precision. For character data, this is the length in characters. For datetime datatypes, this is the length in characters of the String representation (assuming the maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes. For the ROWID datatype, this is the length in bytes. Null is returned for data types where the column size is not applicable.
 */
}
