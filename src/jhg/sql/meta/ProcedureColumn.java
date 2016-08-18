package jhg.sql.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import jhg.sql.meta.Procedure.Type;

public class ProcedureColumn {
	/*
	procedureColumnUnknown - nobody knows
	procedureColumnIn - IN parameter
	procedureColumnInOut - INOUT parameter
	procedureColumnOut - OUT parameter
	procedureColumnReturn - procedure return value
	procedureColumnResult - result column in ResultSet
*/	
	public static enum Type{
		procedureColumnUnknown(DatabaseMetaData.procedureColumnUnknown),
		procedureColumnIn(DatabaseMetaData.procedureColumnIn),
		procedureColumnInOut(DatabaseMetaData.procedureColumnInOut),
		procedureColumnOut(DatabaseMetaData.procedureColumnOut),
		procedureColumnReturn(DatabaseMetaData.procedureColumnReturn),
		procedureColumnResult(DatabaseMetaData.procedureColumnResult);
		private int code;
		private Type(int c){
			this.code = c;
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
	public static enum Nullable{
		procedureNoNulls(DatabaseMetaData.procedureNoNulls),
		procedureNullable(DatabaseMetaData.procedureNullable),
		procedureNullableUnknown(DatabaseMetaData.procedureNullableUnknown);
		private int code;
		private Nullable(int c){
			this.code = c;
		}
		public static Nullable from(int c){
			for(Nullable n:Nullable.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;			
		}		
/*
		procedureNoNulls - does not allow NULL values
		procedureNullable - allows NULL values
		procedureNullableUnknown - nullability unknown

 */
	}

	public static final String NULL = "NULL";
	public static final String TRUNCATE = "TRUNCATE";
	public static final String YES = "YES";
	public static final String NO = "NO";
	
	public static enum Field{
		NIL,
		PROCEDURE_CAT,
		PROCEDURE_SCHEM,
		PROCEDURE_NAME,
		COLUMN_NAME,
		COLUMN_TYPE,//Type
		DATA_TYPE,
		TYPE_NAME,
		PRECISION,
		LENGTH,
		SCALE,
		RADIX,
		NULLABLE,//Nullable
		REMARKS,
		COLUMN_DEF,
		TRUNCATE,
		NULL,
		SQL_DATA_TYPE,
		SQL_DATETIME_SUB,
		CHAR_OCTET_LENGTH,
		ORDINAL_POSITION,
		IS_NULLABLE,//TriFlag
		SPECIFIC_NAME;

	}	
	
	
	private String procedureCat,procedureSchem,procedureName,columnName,typeName,remarks,isNullable,specificName;
	private int dataType,precision,length,sqlDataType,sqlDatetimeSub,charOctetLength,ordinalPosition;
	private short scale,radix;
	private Type columnType;
	private Nullable nullable;
	
	public ProcedureColumn(ResultSet rs){
		try{
			procedureCat = rs.getString(Field.PROCEDURE_CAT.ordinal());
			procedureSchem = rs.getString(Field.PROCEDURE_SCHEM.ordinal());
			procedureName = rs.getString(Field.PROCEDURE_NAME.ordinal());
			columnName = rs.getString(Field.COLUMN_NAME.ordinal());
			typeName = rs.getString(Field.TYPE_NAME.ordinal());
			remarks = rs.getString(Field.REMARKS.ordinal());
			isNullable = rs.getString(Field.IS_NULLABLE.ordinal());
			specificName = rs.getString(Field.SPECIFIC_NAME.ordinal());
			dataType = rs.getInt(Field.DATA_TYPE.ordinal());
			precision = rs.getInt(Field.PRECISION.ordinal());
			length = rs.getInt(Field.LENGTH.ordinal());
			sqlDataType = rs.getInt(Field.SQL_DATA_TYPE.ordinal());
			sqlDatetimeSub = rs.getInt(Field.SQL_DATETIME_SUB.ordinal());
			charOctetLength = rs.getInt(Field.CHAR_OCTET_LENGTH.ordinal());
			ordinalPosition = rs.getInt(Field.ORDINAL_POSITION.ordinal());
			scale = rs.getShort(Field.SCALE.ordinal());
			radix = rs.getShort(Field.RADIX.ordinal());
			columnType = Type.from(rs.getInt(Field.COLUMN_TYPE.ordinal()));
			nullable = Nullable.from(rs.getInt(Field.NULLABLE.ordinal()));
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public static String getNull() {
		return NULL;
	}

	public static String getTruncate() {
		return TRUNCATE;
	}

	public static String getYes() {
		return YES;
	}

	public static String getNo() {
		return NO;
	}

	public String getProcedureCat() {
		return procedureCat;
	}

	public String getProcedureSchem() {
		return procedureSchem;
	}

	public String getProcedureName() {
		return procedureName;
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

	public String getIsNullable() {
		return isNullable;
	}

	public String getSpecificName() {
		return specificName;
	}

	public int getDataType() {
		return dataType;
	}

	public int getPrecision() {
		return precision;
	}

	public int getLength() {
		return length;
	}

	public int getSqlDataType() {
		return sqlDataType;
	}

	public int getSqlDatetimeSub() {
		return sqlDatetimeSub;
	}

	public int getCharOctetLength() {
		return charOctetLength;
	}

	public int getOrdinalPosition() {
		return ordinalPosition;
	}

	public short getScale() {
		return scale;
	}

	public short getRadix() {
		return radix;
	}

	public Type getColumnType() {
		return columnType;
	}

	public Nullable getNullable() {
		return nullable;
	}

	@Override
	public String toString() {
		return "ProcedureColumn [procedureCat=" + procedureCat
				+ ", procedureSchem=" + procedureSchem + ", procedureName="
				+ procedureName + ", columnName=" + columnName + ", typeName="
				+ typeName + ", remarks=" + remarks + ", isNullable="
				+ isNullable + ", specificName=" + specificName + ", dataType="
				+ dataType + ", precision=" + precision + ", length=" + length
				+ ", sqlDataType=" + sqlDataType + ", sqlDatetimeSub="
				+ sqlDatetimeSub + ", charOctetLength=" + charOctetLength
				+ ", ordinalPosition=" + ordinalPosition + ", scale=" + scale
				+ ", radix=" + radix + ", columnType=" + columnType
				+ ", nullable=" + nullable + "]";
	}
	
	
	
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
