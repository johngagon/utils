package jhg.sql.meta;

import java.sql.DatabaseMetaData;



public enum DeferType {
	importedKeyInitiallyDeferred(DatabaseMetaData.importedKeyInitiallyDeferred),
	importedKeyInitiallyImmediate(DatabaseMetaData.importedKeyInitiallyImmediate),
	importedKeyNotDeferrable(DatabaseMetaData.importedKeyNotDeferrable);
	private int code;
	private DeferType(int t){
		this.code = t;
	}
	public static DeferType from(int t){
		for(DeferType n:DeferType.values()){
			if(n.code == t){
				return n;
			}
		}
		return null;			
	}

}
