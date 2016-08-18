package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SuperTable {

	public static enum Field{
		NIL,
		TABLE_CAT,
		TABLE_SCHEM,
		TABLE_NAME,
		SUPERTABLE_NAME;		
	}	
	private String tableCat,tableSchem,tableName,superTableName;
	
	public SuperTable(ResultSet rs){
		try{
			tableCat = rs.getString(Field.TABLE_CAT.ordinal());
			tableSchem = rs.getString(Field.TABLE_SCHEM.ordinal());
			tableName = rs.getString(Field.TABLE_NAME.ordinal());
			superTableName = rs.getString(Field.SUPERTABLE_NAME.ordinal());
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

	public String getSuperTableName() {
		return superTableName;
	}

	@Override
	public String toString() {
		return "SuperTable [tableCat=" + tableCat + ", tableSchem="
				+ tableSchem + ", tableName=" + tableName + ", superTableName="
				+ superTableName + "]";
	}
	
	
	
/*
Each type description has the following columns:

TABLE_CAT String => the type's catalog (may be null)
TABLE_SCHEM String => type's schema (may be null)
TABLE_NAME String => type name
SUPERTABLE_NAME String => the direct super type's name
 */
}
