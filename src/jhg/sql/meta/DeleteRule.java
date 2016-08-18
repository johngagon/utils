package jhg.sql.meta;

import java.sql.DatabaseMetaData;

public enum DeleteRule {
	importedKeyNoAction(DatabaseMetaData.importedKeyNoAction),
	importedKeyCascade(DatabaseMetaData.importedKeyCascade),
	importedKeySetNull(DatabaseMetaData.importedKeySetNull),
	importedKeyRestrict(DatabaseMetaData.importedKeyRestrict),
	importedKeySetDefault(DatabaseMetaData.importedKeySetDefault);
	private int code;
	private DeleteRule(int t){
		this.code = t;
	}
	public static DeleteRule from(int t){
		for(DeleteRule d:DeleteRule.values()){
			if(d.code == t){
				return d;
			}
		}
		return null;
	}
}
/*

importedKeyNoAction - do not allow delete of primary key if it has been imported
importedKeyCascade - delete rows that import a deleted key
importedKeySetNull - change imported key to NULL if its primary key has been deleted
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
importedKeySetDefault - change imported key to default if its primary key has been deleted

*/