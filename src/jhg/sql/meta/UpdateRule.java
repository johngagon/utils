package jhg.sql.meta;

import java.sql.DatabaseMetaData;

public enum UpdateRule {
	importedKeyNoAction(DatabaseMetaData.importedKeyNoAction),
	importedKeyCascade(DatabaseMetaData.importedKeyCascade),
	importedKeySetNull(DatabaseMetaData.importedKeySetNull),
	importedKeySetDefault(DatabaseMetaData.importedKeySetDefault),
	importedKeyRestrict(DatabaseMetaData.importedKeyRestrict);
	
	private int code;
	private UpdateRule(int scope){
		scope = code;
	}
	public static UpdateRule from(int c){
		for(UpdateRule n:UpdateRule.values()){
			if(n.code == c){
				return n;
			}
		}
		return null;			
	}
}
/*
public static enum Scope{
	bestRowTemporary(DatabaseMetaData.bestRowTemporary),
	bestRowTransaction(DatabaseMetaData.bestRowTransaction),
	bestRowSession(DatabaseMetaData.bestRowSession);
	private int code;
	private Scope(int scope){
		scope = code;
	}
	public static Scope from(int c){
		for(Scope n:Scope.values()){
			if(n.code == c){
				return n;
			}
		}
		return null;			
	}
}

importedNoAction - do not allow update of primary key if it has been imported
importedKeyCascade - change imported key to agree with primary key update
importedKeySetNull - change imported key to NULL if its primary key has been updated
importedKeySetDefault - change imported key to default values if its primary key has been updated
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)

*/