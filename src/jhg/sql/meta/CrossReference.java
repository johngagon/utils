package jhg.sql.meta;

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
