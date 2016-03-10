package chp.dbreplicator;

import java.sql.*;

public class ColumnDefinition {

	private String colName;
	private String colTypeName="";
	private int colType;
	private int colLen;
	private int colScale;

	public ColumnDefinition(String _colName, String _colTypeName, int _colType) {
		this.colName = _colName;
		this.colTypeName = _colTypeName;
		this.colType = _colType;
	}	
	
	public ColumnDefinition(String _colName, int _colType, int _colLen, int _colScale) {
		this.colName = _colName;
		this.colType = _colType;
		this.colLen = _colLen;
		this.colScale = _colScale;
	}
	public boolean hasLength(){
		return (Types.INTEGER != this.getColType() && Types.BIGINT != this.getColType() && Types.DATE != this.getColType());
	}
	
	
	public String getColName() {
		return colName;
	}

	
	
	public String getColTypeName() {
		return colTypeName;
	}

	public int getColType() {
		return colType;
	}

	public int getColLen() {
		return colLen;
	}

	public int getColScale() {
		return colScale;
	}

	@Override
	public String toString() {
		return "F:"+colName + "| type=" + colType				+ " (" + colLen + ")(" + colScale + ")::"+colTypeName+"";
	}


	
	
}
