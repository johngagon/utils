package jhg.sql.meta;

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
	@SuppressWarnings("unused")
	private String tableCatalog,tableSchema,tableName,columnName,grantor,grantee,privilege,isGrantable;
	
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
