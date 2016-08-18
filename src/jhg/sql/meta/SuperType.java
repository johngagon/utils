package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SuperType {
	
	public static enum Field{
		NIL,
		TYPE_CAT,
		TYPE_SCHEM,
		TYPE_NAME,
		SUPERTYPE_CAT,
		SUPERTYPE_SCHEM,
		SUPERTYPE_NAME;		
	}	
	
	private String typeCat,typeSchem,typeName,superTypeCat,superTypeSchem,superTypeName;
	
	public SuperType(ResultSet rs){
		try{
			typeCat = rs.getString(Field.TYPE_CAT.ordinal());
			typeSchem = rs.getString(Field.TYPE_SCHEM.ordinal());
			typeName = rs.getString(Field.TYPE_NAME.ordinal());
			superTypeCat = rs.getString(Field.SUPERTYPE_CAT.ordinal());
			superTypeSchem = rs.getString(Field.SUPERTYPE_SCHEM.ordinal());
			superTypeName = rs.getString(Field.SUPERTYPE_NAME.ordinal());
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

	public String getSuperTypeCat() {
		return superTypeCat;
	}

	public String getSuperTypeSchem() {
		return superTypeSchem;
	}

	public String getSuperTypeName() {
		return superTypeName;
	}

	@Override
	public String toString() {
		return "SuperType [typeCat=" + typeCat + ", typeSchem=" + typeSchem
				+ ", typeName=" + typeName + ", superTypeCat=" + superTypeCat
				+ ", superTypeSchem=" + superTypeSchem + ", superTypeName="
				+ superTypeName + "]";
	}
	
	
/*
TYPE_CAT String => the UDT's catalog (may be null)
TYPE_SCHEM String => UDT's schema (may be null)
TYPE_NAME String => type name of the UDT
SUPERTYPE_CAT String => the direct super type's catalog (may be null)
SUPERTYPE_SCHEM String => the direct super type's schema (may be null)
SUPERTYPE_NAME String => the direct super type's name
 */
}
