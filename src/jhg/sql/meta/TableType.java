package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableType {
	
	public static enum Field{
		NIL,
		TABLE_TYPE;
	}	
	
	private String tableType;
	
	public TableType(ResultSet rs){
		try{
			tableType = rs.getString(Field.TABLE_TYPE.ordinal());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public String getTableType() {
		return tableType;
	}

	@Override
	public String toString() {
		return "TableType [tableType=" + tableType + "]";
	}
	
	
/*
The table type is:

TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
 */
}
