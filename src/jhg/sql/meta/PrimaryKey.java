package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrimaryKey {
	
	public static enum Field{
		NIL,
		TABLE_CAT,
		TABLE_SCHEM,
		TABLE_NAME,
		COLUMN_NAME,
		KEY_SEQ,
		PK_NAME;		
	}	
	
	private String tableCat,tableSchem,tableName,columnName,pkName;
	private short keySeq;
	
	public PrimaryKey(ResultSet rs){
		try{
			tableCat = rs.getString(Field.TABLE_CAT.ordinal());
			tableSchem = rs.getString(Field.TABLE_SCHEM.ordinal());
			tableName = rs.getString(Field.TABLE_NAME.ordinal());
			columnName = rs.getString(Field.COLUMN_NAME.ordinal());
			pkName = rs.getString(Field.PK_NAME.ordinal());
			keySeq = rs.getShort(Field.KEY_SEQ.ordinal());
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

	public String getColumnName() {
		return columnName;
	}

	public String getPkName() {
		return pkName;
	}

	public short getKeySeq() {
		return keySeq;
	}

	@Override
	public String toString() {
		return "PrimaryKey [tableCat=" + tableCat + ", tableSchem="
				+ tableSchem + ", tableName=" + tableName + ", columnName="
				+ columnName + ", pkName=" + pkName + ", keySeq=" + keySeq
				+ "]";
	}

	
	
	
/*
Each primary key column description has the following columns:

TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
COLUMN_NAME String => column name
KEY_SEQ short => sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key).
PK_NAME String => primary key name (may be null)
 */
}
