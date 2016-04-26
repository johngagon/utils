package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Catalog {
	
	public static enum Field{
		NIL,
		TABLE_CAT
	}
	
	private String tableCatalog;
	public Catalog(ResultSet rs){
		try {
			this.tableCatalog = rs.getString(Field.TABLE_CAT.ordinal());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getTableCatalog() {
		return tableCatalog;
	}
	
	@Override
	public String toString() {
		return "Catalog [tableCatalog=" + tableCatalog + "]";
	}
	
	
/*
 The catalog column is:

TABLE_CAT String => catalog name
 */
}
