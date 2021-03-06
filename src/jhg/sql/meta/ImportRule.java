package jhg.sql.meta;

import java.sql.DatabaseMetaData;

public enum ImportRule {
	importedKeyNoAction(DatabaseMetaData.importedKeyNoAction),
	importedKeyCascade(DatabaseMetaData.importedKeyCascade),
	importedKeySetNull(DatabaseMetaData.importedKeySetNull),
	importedKeyRestrict(DatabaseMetaData.importedKeyRestrict),
	importedKeySetDefault(DatabaseMetaData.importedKeySetDefault);
	private int code;
	private ImportRule(int t){
		this.code = t;
	}
	public static ImportRule from(int t){
		for(ImportRule d:ImportRule.values()){
			if(d.code == t){
				return d;
			}
		}
		return null;
	}
}
/*
ImportedKey:
UPDATE:
importedNoAction - do not allow update of primary key if it has been imported
importedKeyCascade - change imported key to agree with primary key update
importedKeySetNull - change imported key to NULL if its primary key has been updated
importedKeySetDefault - change imported key to default values if its primary key has been updated
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)

DELETE:
importedKeyNoAction - do not allow delete of primary key if it has been imported
importedKeyCascade - delete rows that import a deleted key
importedKeySetNull - change imported key to NULL if its primary key has been deleted
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
importedKeySetDefault - change imported key to default if its primary key has been deleted


*/