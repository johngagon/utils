package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnPrivilege {
	
	public static enum Field{
		NIL,
		TABLE_CAT,
		TABLE_SCHEM,
		TABLE_NAME,
		COLUMN_NAME,
		GRANTOR,
		GRANTEE,
		PRIVILEGE,
		IS_GRANTABLE;
		
	}
	//@SuppressWarnings("unused")
	private String tableCatalog,tableSchema,tableName,columnName,grantor,grantee,privilege,isGrantable;

	public ColumnPrivilege(ResultSet rs){
		try{
			tableCatalog = rs.getString(Field.TABLE_CAT.ordinal());
			tableSchema = rs.getString(Field.TABLE_SCHEM.ordinal());
			tableName = rs.getString(Field.TABLE_NAME.ordinal());
			columnName = rs.getString(Field.COLUMN_NAME.ordinal());
			grantor = rs.getString(Field.GRANTOR.ordinal());
			grantee = rs.getString(Field.GRANTEE.ordinal());
			privilege = rs.getString(Field.PRIVILEGE.ordinal());
			isGrantable = rs.getString(Field.IS_GRANTABLE.ordinal());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public String getTableCatalog() {
		return tableCatalog;
	}

	public String getTableSchema() {
		return tableSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public String getColumnName() {
		return columnName;
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
		return "ColumnPrivilege [tableCatalog=" + tableCatalog
				+ ", tableSchema=" + tableSchema + ", tableName=" + tableName
				+ ", columnName=" + columnName + ", grantor=" + grantor
				+ ", grantee=" + grantee + ", privilege=" + privilege
				+ ", isGrantable=" + isGrantable + "]";
	}
	
	

	
	
/*
Each privilige description has the following columns:

TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
COLUMN_NAME String => column name
GRANTOR String => grantor of access (may be null)
GRANTEE String => grantee of access
PRIVILEGE String => name of access (SELECT, INSERT, UPDATE, REFRENCES, ...)
IS_GRANTABLE String => "YES" if grantee is permitted to grant to others; "NO" if not; null if unknown
 */
}
