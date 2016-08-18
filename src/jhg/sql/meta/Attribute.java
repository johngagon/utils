package jhg.sql.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Attribute {
	public static enum Nullable{
		attributeNoNulls(DatabaseMetaData.attributeNoNulls),
		attributeNullable(DatabaseMetaData.attributeNullable),
		attributeNullableUnknown(DatabaseMetaData.attributeNullableUnknown);
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
		NIL,
		TYPE_CAT,//1 String => type catalog (may be null)
		TYPE_SCHEM,// String => type schema (may be null)
		TYPE_NAME,// String => type name
		ATTR_NAME,// String => attribute name
		DATA_TYPE,// int => attribute type SQL type from java.sql.Types
		ATTR_TYPE_NAME,// String => Data source dependent type name. For a UDT, the type name is fully qualified. For a REF, the type name is fully qualified and represents the target type of the reference type.
		ATTR_SIZE,// int => column size. For char or date types this is the maximum number of characters; for numeric or decimal types this is precision.
		DECIMAL_DIGITS,// int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
		NUM_PREC_RADIX,// int => Radix (typically either 10 or 2)
		NULLABLE,// int => whether NULL is allowed	attributeNoNulls,- might not allow NULL values	attributeNullable - definitely allows NULL values		attributeNullableUnknown - nullability unknown
		REMARKS,// String => comment describing column (may be null)
		ATTR_DEF,// String => default value (may be null)
		SQL_DATA_TYPE,// int => unused
		SQL_DATETIME_SUB,// int => unused
		CHAR_OCTET_LENGTH,// int => for char types the maximum number of bytes in the column
		ORDINAL_POSITION,// int => index of the attribute in the UDT (starting at 1)
		IS_NULLABLE,// String => ISO rules are used to determine the nullability for a attribute.		YES --- if the attribute can include NULLs		NO --- if the attribute cannot include NULLs		empty string --- if the nullability for the attribute is unknown
		SCOPE_CATALOG,// String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
		SCOPE_SCHEMA,// String => schema of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
		SCOPE_TABLE,// String => table name that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
		SOURCE_DATA_TYPE;//21 short => source type of a distinct type or user-generated Ref type,SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
	}
	private String typeCatalog,typeSchema,typeName,attributeName,attributeTypeName,remarks,attributeDef,isNullable,scopeCatalog,scopeSchema,scopeTable;
	private int dataType,attributeSize,decimalDigits,numericPrecisionRadix,sqlDataType,sqlDateTimeSub,charOctetLength,ordinalPosition;
	private Nullable nullable;
	private short sourceDataType;
	
	public Attribute(DatabaseMetaData dbmd, String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException{
		this(dbmd.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern));
	}
	
	public Attribute(ResultSet rs){
		/*
		 * Preconditions: 
		 * - rs can't be null
		 * - must be called from getAttributes from DatabaseMetaData.
		 * - rs.next() already called.
		 */
		try {
			typeCatalog = rs.getString(Field.TYPE_CAT.ordinal());
			typeSchema = rs.getString(Field.TYPE_SCHEM.ordinal());
			typeName = rs.getString(Field.TYPE_NAME.ordinal());
			attributeName = rs.getString(Field.ATTR_NAME.ordinal());
			attributeTypeName = rs.getString(Field.ATTR_TYPE_NAME.ordinal());
			remarks = rs.getString(Field.REMARKS.ordinal());
			attributeDef = rs.getString(Field.ATTR_DEF.ordinal());
			isNullable = rs.getString(Field.IS_NULLABLE.ordinal());
			scopeCatalog = rs.getString(Field.SCOPE_CATALOG.ordinal());
			scopeSchema = rs.getString(Field.SCOPE_SCHEMA.ordinal());
			scopeTable = rs.getString(Field.SCOPE_TABLE.ordinal());
			dataType = rs.getInt(Field.DATA_TYPE.ordinal());
			attributeSize = rs.getInt(Field.ATTR_SIZE.ordinal());
			decimalDigits = rs.getInt(Field.DECIMAL_DIGITS.ordinal());
			numericPrecisionRadix = rs.getInt(Field.NUM_PREC_RADIX.ordinal());
			sqlDataType = rs.getInt(Field.SQL_DATA_TYPE.ordinal());
			sqlDateTimeSub = rs.getInt(Field.SQL_DATETIME_SUB.ordinal());
			charOctetLength = rs.getInt(Field.CHAR_OCTET_LENGTH.ordinal());
			ordinalPosition = rs.getInt(Field.ORDINAL_POSITION.ordinal());
			nullable = Nullable.from(rs.getInt(Field.NULLABLE.ordinal()));
			sourceDataType = rs.getShort(Field.SOURCE_DATA_TYPE.ordinal());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public String getTypeCatalog() {
		return typeCatalog;
	}

	public String getTypeSchema() {
		return typeSchema;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getAttributeTypeName() {
		return attributeTypeName;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getAttributeDef() {
		return attributeDef;
	}

	public String getIsNullable() {
		return isNullable;
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

	public int getAttributeSize() {
		return attributeSize;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public int getNumericPrecisionRadix() {
		return numericPrecisionRadix;
	}

	public int getSqlDataType() {
		return sqlDataType;
	}

	public int getSqlDateTimeSub() {
		return sqlDateTimeSub;
	}

	public int getCharOctetLength() {
		return charOctetLength;
	}

	public int getOrdinalPosition() {
		return ordinalPosition;
	}

	public Nullable getNullable() {
		return nullable;
	}

	public short getSourceDataType() {
		return sourceDataType;
	}

	@Override
	public String toString() {
		return "Attribute [typeCatalog=" + typeCatalog + ", typeSchema="
				+ typeSchema + ", typeName=" + typeName + ", attributeName="
				+ attributeName + ", attributeTypeName=" + attributeTypeName
				+ ", remarks=" + remarks + ", attributeDef=" + attributeDef
				+ ", isNullable=" + isNullable + ", scopeCatalog="
				+ scopeCatalog + ", scopeSchema=" + scopeSchema
				+ ", scopeTable=" + scopeTable + ", dataType=" + dataType
				+ ", attributeSize=" + attributeSize + ", decimalDigits="
				+ decimalDigits + ", numericPrecisionRadix="
				+ numericPrecisionRadix + ", sqlDataType=" + sqlDataType
				+ ", sqlDateTimeSub=" + sqlDateTimeSub + ", charOctetLength="
				+ charOctetLength + ", ordinalPosition=" + ordinalPosition
				+ ", nullable=" + nullable + ", sourceDataType="
				+ sourceDataType + "]";
	}

	
	
}
/*
TYPE_CAT String => type catalog (may be null)
TYPE_SCHEM String => type schema (may be null)
TYPE_NAME String => type name
ATTR_NAME String => attribute name
DATA_TYPE int => attribute type SQL type from java.sql.Types
ATTR_TYPE_NAME String => Data source dependent type name. For a UDT, the type name is fully qualified. For a REF, the type name is fully qualified and represents the target type of the reference type.
ATTR_SIZE int => column size. For char or date types this is the maximum number of characters; for numeric or decimal types this is precision.
DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
NUM_PREC_RADIX int => Radix (typically either 10 or 2)
NULLABLE int => whether NULL is allowed
attributeNoNulls - might not allow NULL values
attributeNullable - definitely allows NULL values
attributeNullableUnknown - nullability unknown
REMARKS String => comment describing column (may be null)
ATTR_DEF String => default value (may be null)
SQL_DATA_TYPE int => unused
SQL_DATETIME_SUB int => unused
CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
ORDINAL_POSITION int => index of the attribute in the UDT (starting at 1)
IS_NULLABLE String => ISO rules are used to determine the nullability for a attribute.
YES --- if the attribute can include NULLs
NO --- if the attribute cannot include NULLs
empty string --- if the nullability for the attribute is unknown
SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
SCOPE_TABLE String => table name that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type,SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
*/