package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDefinedTypes {
	
	public static enum Field{
		NIL,
		TYPE_CAT,
		TYPE_SCHEM,
		TYPE_NAME,
		CLASS_NAME,
		DATA_TYPE,
		REMARKS,
		BASE_TYPE;

	}
	private String typeCat,typeSchem,typeName,className,remarks;
	private int dataType;
	private short baseType;
	
	public UserDefinedTypes(ResultSet rs){
		try{
			typeCat = rs.getString(Field.TYPE_CAT.ordinal());
			typeSchem = rs.getString(Field.TYPE_SCHEM.ordinal());
			typeName = rs.getString(Field.TYPE_NAME.ordinal());
			className = rs.getString(Field.CLASS_NAME.ordinal());
			remarks = rs.getString(Field.REMARKS.ordinal());
			dataType = rs.getInt(Field.DATA_TYPE.ordinal());
			baseType = rs.getShort(Field.BASE_TYPE.ordinal());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public String getTypeCat() {
		return typeCat;
	}

	public String getTypeSchem() {
		return typeSchem;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getClassName() {
		return className;
	}

	public String getRemarks() {
		return remarks;
	}

	public int getDataType() {
		return dataType;
	}

	public short getBaseType() {
		return baseType;
	}

	@Override
	public String toString() {
		return "UserDefinedTypes [typeCat=" + typeCat + ", typeSchem="
				+ typeSchem + ", typeName=" + typeName + ", className="
				+ className + ", remarks=" + remarks + ", dataType=" + dataType
				+ ", baseType=" + baseType + "]";
	}
	
	
	
/*
TYPE_CAT String => the type's catalog (may be null)
TYPE_SCHEM String => type's schema (may be null)
TYPE_NAME String => type name
CLASS_NAME String => Java class name
DATA_TYPE int => type value defined in java.sql.Types. One of JAVA_OBJECT, STRUCT, or DISTINCT
REMARKS String => explanatory comment on the type
BASE_TYPE short => type code of the source type of a DISTINCT type or the type that implements the user-generated reference type of the SELF_REFERENCING_COLUMN of a structured type as defined in java.sql.Types (null if DATA_TYPE is not DISTINCT or not STRUCT with REFERENCE_GENERATION = USER_DEFINED)
 */
}
