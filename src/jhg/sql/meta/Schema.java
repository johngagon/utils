package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Schema {
	
	public static enum Field{
		NIL,
		TABLE_SCHEM,
		TABLE_CATALOG;		
	}	
	private String tableSchem,tableCatalog;
	public Schema(ResultSet rs){
		try{
			tableSchem = rs.getString(Field.TABLE_SCHEM.ordinal());
			tableCatalog = rs.getString(Field.TABLE_SCHEM.ordinal());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}
	public String getTableSchem() {
		return tableSchem;
	}
	public String getTableCatalog() {
		return tableCatalog;
	}
	@Override
	public String toString() {
		return "Schema [tableSchem=" + tableSchem + ", tableCatalog="
				+ tableCatalog + "]";
	}
	
	
/*
The schema columns are:

TABLE_SCHEM String => schema name
TABLE_CATALOG String => catalog name (may be null)
 */
}
