package jhg.sql.meta;

public enum TriFlag {
	YES("YES"),
	NO("NO"),
	UNDETERMINED("");
	private String code;
	private TriFlag(String s){
		this.code = s;
	}
	public static TriFlag from(String s){
		for(TriFlag f:TriFlag.values()){
			if(f.code.equals(s)){
				return f;
			}
		}
		return null;
	}
}
