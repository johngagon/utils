package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CrossReference {
	
	public static enum Field{
		NIL,
		PKTABLE_CAT,
		PKTABLE_SCHEM,
		PKTABLE_NAME,
		PKCOLUMN_NAME,
		FKTABLE_CAT,
		FKTABLE_SCHEM,
		FKTABLE_NAME,
		FKCOLUMN_NAME,
		KEY_SEQ,
		UPDATE_RULE,//ImportRTule
		DELETE_RULE,//ImportRule
		FK_NAME,
		PK_NAME,
		DEFERRABILITY;//DeferType		
	}	
	
	private String pkTableCat, pkTableSchem, pkTableName, pkColumnName, fkTableCat, fkTableSchem, fkTableName, fkColumnName, fkName, pkName;
	private short keySeq;
	private UpdateRule updateRule;
	private DeleteRule deleteRule;
	private DeferType deferrability;
	
	public CrossReference(ResultSet rs){
		try{
			 pkTableCat = rs.getString(Field.PKTABLE_CAT.ordinal());
			 pkTableSchem = rs.getString(Field.PKTABLE_SCHEM.ordinal());
			 pkTableName = rs.getString(Field.PKTABLE_NAME.ordinal());
			 pkColumnName = rs.getString(Field.PKCOLUMN_NAME.ordinal());
			 fkTableCat = rs.getString(Field.FKTABLE_CAT.ordinal()); 
			 fkTableSchem = rs.getString(Field.FKTABLE_SCHEM.ordinal());
			 fkTableName = rs.getString(Field.FKTABLE_NAME.ordinal());
			 fkColumnName = rs.getString(Field.FKCOLUMN_NAME.ordinal());
			 fkName = rs.getString(Field.FK_NAME.ordinal());
			 pkName = rs.getString(Field.PK_NAME.ordinal());
			 keySeq = rs.getShort(Field.KEY_SEQ.ordinal());
			 updateRule = UpdateRule.from(rs.getInt(Field.UPDATE_RULE.ordinal()));
			 deleteRule = DeleteRule.from(rs.getInt(Field.DELETE_RULE.ordinal()));
			 deferrability = DeferType.from(rs.getInt(Field.DEFERRABILITY.ordinal()));
			
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public String getPkTableCat() {
		return pkTableCat;
	}

	public String getPkTableSchem() {
		return pkTableSchem;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public String getPkColumnName() {
		return pkColumnName;
	}

	public String getFkTableCat() {
		return fkTableCat;
	}

	public String getFkTableSchem() {
		return fkTableSchem;
	}

	public String getFkTableName() {
		return fkTableName;
	}

	public String getFkColumnName() {
		return fkColumnName;
	}

	public String getFkName() {
		return fkName;
	}

	public String getPkName() {
		return pkName;
	}

	public short getKeySeq() {
		return keySeq;
	}

	public UpdateRule getUpdateRule() {
		return updateRule;
	}

	public DeleteRule getDeleteRule() {
		return deleteRule;
	}

	public DeferType getDeferrability() {
		return deferrability;
	}

	@Override
	public String toString() {
		return "CrossReference [pkTableCat=" + pkTableCat + ", pkTableSchem="
				+ pkTableSchem + ", pkTableName=" + pkTableName
				+ ", pkColumnName=" + pkColumnName + ", fkTableCat="
				+ fkTableCat + ", fkTableSchem=" + fkTableSchem
				+ ", fkTableName=" + fkTableName + ", fkColumnName="
				+ fkColumnName + ", fkName=" + fkName + ", pkName=" + pkName
				+ ", keySeq=" + keySeq + ", updateRule=" + updateRule
				+ ", deleteRule=" + deleteRule + ", deferrability="
				+ deferrability + "]";
	}
	
	
	
	
/*
PKTABLE_CAT String => parent key table catalog (may be null)
PKTABLE_SCHEM String => parent key table schema (may be null)
PKTABLE_NAME String => parent key table name
PKCOLUMN_NAME String => parent key column name
FKTABLE_CAT String => foreign key table catalog (may be null) being exported (may be null)
FKTABLE_SCHEM String => foreign key table schema (may be null) being exported (may be null)
FKTABLE_NAME String => foreign key table name being exported
FKCOLUMN_NAME String => foreign key column name being exported
KEY_SEQ short => sequence number within foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key).
UPDATE_RULE short => What happens to foreign key when parent key is updated:
importedNoAction - do not allow update of parent key if it has been imported
importedKeyCascade - change imported key to agree with parent key update
importedKeySetNull - change imported key to NULL if its parent key has been updated
importedKeySetDefault - change imported key to default values if its parent key has been updated
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
DELETE_RULE short => What happens to the foreign key when parent key is deleted.
importedKeyNoAction - do not allow delete of parent key if it has been imported
importedKeyCascade - delete rows that import a deleted key
importedKeySetNull - change imported key to NULL if its primary key has been deleted
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
importedKeySetDefault - change imported key to default if its parent key has been deleted
FK_NAME String => foreign key name (may be null)
PK_NAME String => parent key name (may be null)
DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit
importedKeyInitiallyDeferred - see SQL92 for definition
importedKeyInitiallyImmediate - see SQL92 for definition
importedKeyNotDeferrable - see SQL92 for definition
 */
}
