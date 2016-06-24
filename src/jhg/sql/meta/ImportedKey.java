package jhg.sql.meta;

public class ImportedKey {
	

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
		UPDATE_RULE,//ImportRule
		DELETE_RULE,//ImportRule
		FK_NAME,
		PK_NAME,
		DEFERRABILITY;//DeferType 		
	}	
	
/*
PKTABLE_CAT String => primary key table catalog being imported (may be null)
PKTABLE_SCHEM String => primary key table schema being imported (may be null)
PKTABLE_NAME String => primary key table name being imported
PKCOLUMN_NAME String => primary key column name being imported
FKTABLE_CAT String => foreign key table catalog (may be null)
FKTABLE_SCHEM String => foreign key table schema (may be null)
FKTABLE_NAME String => foreign key table name
FKCOLUMN_NAME String => foreign key column name
KEY_SEQ short => sequence number within a foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key).
UPDATE_RULE short => What happens to a foreign key when the primary key is updated:
importedNoAction - do not allow update of primary key if it has been imported
importedKeyCascade - change imported key to agree with primary key update
importedKeySetNull - change imported key to NULL if its primary key has been updated
importedKeySetDefault - change imported key to default values if its primary key has been updated
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
DELETE_RULE short => What happens to the foreign key when primary is deleted.
importedKeyNoAction - do not allow delete of primary key if it has been imported
importedKeyCascade - delete rows that import a deleted key
importedKeySetNull - change imported key to NULL if its primary key has been deleted
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
importedKeySetDefault - change imported key to default if its primary key has been deleted
FK_NAME String => foreign key name (may be null)
PK_NAME String => primary key name (may be null)
DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit
importedKeyInitiallyDeferred - see SQL92 for definition
importedKeyInitiallyImmediate - see SQL92 for definition
importedKeyNotDeferrable - see SQL92 for definition 
 */
}