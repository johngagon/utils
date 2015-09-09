package cache;

public class Field {

	/*
	 * jdbc java sql type
	 * size etc.
	 */
	private int coltype;
	private String colname;
	private boolean isPk;
	
	
	public Field(String colname){
		this(colname,java.sql.Types.VARCHAR);
	}
	
	public Field(String colname, int coltype) {
		this(colname,coltype,false);
	}
	
	public Field(String colname, int coltype, boolean isPk) {
		this.coltype = coltype;
		this.colname = colname;
		this.isPk = isPk;
	}

	public int getColtype() {
		return coltype;
	}

	public String getColname() {
		return colname;
	}

	public boolean isIsPk() {
		return isPk;
	}

}
