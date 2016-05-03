package chp.dbreplicator;

import java.sql.Types;

public class ColumnDefinition {


	
	private String colName;
	private String colTypeName="";
	private int colType;
	private int colLen;
	private int colScale;
	private boolean nullable;
	private int ordinal;
	public ColumnDefinition(String _colName, String _colTypeName, int _colType, int _o) {
		this.colName = _colName;
		this.colTypeName = _colTypeName;
		this.colType = _colType;
		this.nullable = true;
		this.ordinal = _o;
	}	
	
	public ColumnDefinition(String _colName, int _colType, int _colLen, int _colScale, int _o) {
		this.colName = _colName;
		this.colType = _colType;
		this.colLen = _colLen;
		this.colScale = _colScale;
		this.nullable = true;
		this.ordinal = _o;
	}
	public ColumnDefinition(String _colName, String _colTypeName, int _colType, boolean notNull, int _o) {
		this.colName = _colName;
		this.colTypeName = _colTypeName;
		this.colType = _colType;
		this.nullable = !notNull;
		this.ordinal = _o;
	}	
	
	public ColumnDefinition(String _colName, int _colType, int _colLen, int _colScale, boolean notNull, int _o) {
		this.colName = _colName;
		this.colType = _colType;
		this.colLen = _colLen;
		this.colScale = _colScale;
		this.nullable = !notNull;
		this.ordinal = _o;
	}
	
	public boolean hasLength(){
		return (Types.INTEGER != this.getColType() && Types.BIGINT != this.getColType() && Types.DATE != this.getColType());
	}
	
	
	public String getColName() {
		return colName;
	}

	@SuppressWarnings("boxing")
	public Integer getOrdinal(){
		return ordinal;
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
	
	public boolean getNullable() {
		return nullable;
	}	

	@Override
	public String toString() {
		return "F:"+colName + "| type=" + colType				+ " (" + colLen + ")(" + colScale + ")::"+colTypeName+"";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colLen;
		result = prime * result + ((colName == null) ? 0 : colName.hashCode());
		result = prime * result + colScale;
		result = prime * result + colType;
		result = prime * result
				+ ((colTypeName == null) ? 0 : colTypeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnDefinition other = (ColumnDefinition) obj;
		if (colLen != other.colLen)
			return false;
		if (colName == null) {
			if (other.colName != null)
				return false;
		} else if (!colName.equals(other.colName))
			return false;
		if (colScale != other.colScale)
			return false;
		if (colType != other.colType)
			return false;
		if (colTypeName == null) {
			if (other.colTypeName != null)
				return false;
		} else if (!colTypeName.equals(other.colTypeName))
			return false;
		return true;
	}




	
	
}
