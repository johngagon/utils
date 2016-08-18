package jhg.sql.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Type {
	
	public static enum Nullable{
		typeNoNulls(DatabaseMetaData.typeNoNulls),
		typeNullable(DatabaseMetaData.typeNullable),
		typeNullableUnknown(DatabaseMetaData.typeNullableUnknown);			
	/*
		typeNoNulls - does not allow NULL values
		typeNullable - allows NULL values
		typeNullableUnknown - nullability unknown	
	 */
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
	}
	
	public static enum Searchable{
		typePredNone(DatabaseMetaData.typePredNone),
		typePredChar(DatabaseMetaData.typePredChar),
		typePredBasic(DatabaseMetaData.typePredBasic),
		typeSearchable(DatabaseMetaData.typeSearchable);		
		private int code;
		private Searchable(int c){
			this.code = c;
		}
		public static Searchable from(int c){
			for(Searchable n:Searchable.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;			
		}		
/*
		typePredNone - No support
		typePredChar - Only supported with WHERE .. LIKE
		typePredBasic - Supported except for WHERE .. LIKE
		typeSearchable - Supported for all WHERE ..
		
 */
	}
	
	public static enum Field{
		NIL,
		TYPE_NAME,
		DATA_TYPE,
		PRECISION,
		LITERAL_PREFIX,
		LITERAL_SUFFIX,
		CREATE_PARAMS,
		NULLABLE,//Nullable
		CASE_SENSITIVE,
		SEARCHABLE,//Searchable
		UNSIGNED_ATTRIBUTE,
		FIXED_PREC_SCALE,
		AUTO_INCREMENT,
		LOCAL_TYPE_NAME,
		MINIMUM_SCALE,
		MAXIMUM_SCALE,
		SQL_DATA_TYPE,
		SQL_DATETIME_SUB,
		NUM_PREC_RADIX;		
	}	
	
	private String typeName,literalPrefix,literalSuffix,createParams,localTypeName;
	private int dataType;
	private int precision,sqlDataType,sqlDatetimeSub,numPrecRadix;
	private short minimumScale,maximumScale;
	private boolean caseSensitive,unsignedAttribute,fixedPrecScale,autoIncrement;
	private Nullable nullable;
	private Searchable searchable;
	
	public Type(ResultSet rs){
		try{
			typeName = rs.getString(Field.TYPE_NAME.ordinal());
			literalPrefix = rs.getString(Field.LITERAL_PREFIX.ordinal());
			literalSuffix = rs.getString(Field.LITERAL_SUFFIX.ordinal());
			createParams = rs.getString(Field.CREATE_PARAMS.ordinal());
			localTypeName = rs.getString(Field.LOCAL_TYPE_NAME.ordinal());
			
			dataType = rs.getInt(Field.DATA_TYPE.ordinal());
			precision = rs.getInt(Field.PRECISION.ordinal());
			sqlDataType = rs.getInt(Field.SQL_DATA_TYPE.ordinal());
			sqlDatetimeSub = rs.getInt(Field.SQL_DATETIME_SUB.ordinal());
			numPrecRadix = rs.getInt(Field.NUM_PREC_RADIX.ordinal());
			
			minimumScale = rs.getShort(Field.MINIMUM_SCALE.ordinal());
			maximumScale = rs.getShort(Field.MAXIMUM_SCALE.ordinal());
			
			caseSensitive = rs.getBoolean(Field.CASE_SENSITIVE.ordinal());
			unsignedAttribute = rs.getBoolean(Field.UNSIGNED_ATTRIBUTE.ordinal());
			fixedPrecScale = rs.getBoolean(Field.FIXED_PREC_SCALE.ordinal());
			autoIncrement = rs.getBoolean(Field.AUTO_INCREMENT.ordinal());
			
			nullable = Nullable.from(rs.getInt(Field.NULLABLE.ordinal()));
			searchable = Searchable.from(rs.getInt(Field.SEARCHABLE.ordinal()));
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public String getTypeName() {
		return typeName;
	}

	public String getLiteralPrefix() {
		return literalPrefix;
	}

	public String getLiteralSuffix() {
		return literalSuffix;
	}

	public String getCreateParams() {
		return createParams;
	}

	public String getLocalTypeName() {
		return localTypeName;
	}

	public int getDataType() {
		return dataType;
	}

	public int getPrecision() {
		return precision;
	}

	public int getSqlDataType() {
		return sqlDataType;
	}

	public int getSqlDatetimeSub() {
		return sqlDatetimeSub;
	}

	public int getNumPrecRadix() {
		return numPrecRadix;
	}

	public short getMinimumScale() {
		return minimumScale;
	}

	public short getMaximumScale() {
		return maximumScale;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public boolean isUnsignedAttribute() {
		return unsignedAttribute;
	}

	public boolean isFixedPrecScale() {
		return fixedPrecScale;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public Nullable getNullable() {
		return nullable;
	}

	public Searchable getSearchable() {
		return searchable;
	}

	@Override
	public String toString() {
		return "Type [typeName=" + typeName + ", literalPrefix="
				+ literalPrefix + ", literalSuffix=" + literalSuffix
				+ ", createParams=" + createParams + ", localTypeName="
				+ localTypeName + ", dataType=" + dataType + ", precision="
				+ precision + ", sqlDataType=" + sqlDataType
				+ ", sqlDatetimeSub=" + sqlDatetimeSub + ", numPrecRadix="
				+ numPrecRadix + ", minimumScale=" + minimumScale
				+ ", maximumScale=" + maximumScale + ", caseSensitive="
				+ caseSensitive + ", unsignedAttribute=" + unsignedAttribute
				+ ", fixedPrecScale=" + fixedPrecScale + ", autoIncrement="
				+ autoIncrement + ", nullable=" + nullable + ", searchable="
				+ searchable + "]";
	}
	
/*
TYPE_NAME String => Type name
DATA_TYPE int => SQL data type from java.sql.Types
PRECISION int => maximum precision
LITERAL_PREFIX String => prefix used to quote a literal (may be null)
LITERAL_SUFFIX String => suffix used to quote a literal (may be null)
CREATE_PARAMS String => parameters used in creating the type (may be null)
NULLABLE short => can you use NULL for this type.
typeNoNulls - does not allow NULL values
typeNullable - allows NULL values
typeNullableUnknown - nullability unknown
CASE_SENSITIVE boolean=> is it case sensitive.
SEARCHABLE short => can you use "WHERE" based on this type:
typePredNone - No support
typePredChar - Only supported with WHERE .. LIKE
typePredBasic - Supported except for WHERE .. LIKE
typeSearchable - Supported for all WHERE ..
UNSIGNED_ATTRIBUTE boolean => is it unsigned.
FIXED_PREC_SCALE boolean => can it be a money value.
AUTO_INCREMENT boolean => can it be used for an auto-increment value.
LOCAL_TYPE_NAME String => localized version of type name (may be null)
MINIMUM_SCALE short => minimum scale supported
MAXIMUM_SCALE short => maximum scale supported
SQL_DATA_TYPE int => unused
SQL_DATETIME_SUB int => unused
NUM_PREC_RADIX int => usually 2 or 10
 */
}
