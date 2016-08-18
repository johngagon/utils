package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TablePrivilege {
	
	public static enum Field{
		NIL,
		TABLE_CAT,
		TABLE_SCHEM,
		TABLE_NAME,
		GRANTOR,
		GRANTEE,
		PRIVILEGE,
		IS_GRANTABLE;//TriFlag		
	}	
	
	private String tableCat,tableSchem,tableName,grantor,grantee,privilege,isGrantable;
	
	public TablePrivilege(ResultSet rs){
		try{
			tableCat = rs.getString(Field.TABLE_CAT.ordinal());
			tableSchem = rs.getString(Field.TABLE_SCHEM.ordinal());
			tableName = rs.getString(Field.TABLE_NAME.ordinal());
			grantor = rs.getString(Field.GRANTOR.ordinal());
			grantee = rs.getString(Field.GRANTEE.ordinal());
			privilege = rs.getString(Field.PRIVILEGE.ordinal());
			isGrantable = rs.getString(Field.IS_GRANTABLE.ordinal());
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

	public String getGrantor() {
		return grantor;
	}

	public String getGrantee() {
		return grantee;
	}

	public String getPrivilege() {
		return privilege;
	}

	public String getIsGrantable() {
		return isGrantable;
	}

	@Override
	public String toString() {
		return "TablePrivilege [tableCat=" + tableCat + ", tableSchem="
				+ tableSchem + ", tableName=" + tableName + ", grantor="
				+ grantor + ", grantee=" + grantee + ", privilege=" + privilege
				+ ", isGrantable=" + isGrantable + "]";
	}
	
	
	
/*
Each privilige description has the following columns:

TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
GRANTOR String => grantor of access (may be null)
GRANTEE String => grantee of access
PRIVILEGE String => name of access (SELECT, INSERT, UPDATE, REFRENCES, ...)
IS_GRANTABLE String => "YES" if grantee is permitted to grant to others; "NO" if not; null if unknown
 */
}
