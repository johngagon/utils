package chp.dbreplicator;

import java.util.*;

public class MappingTable {

	private String sourceQual;//, targetDatabase;
	private String[] tableList;

	public MappingTable(String _sourceQual, String[] _tableList) {  //,	String _targetDatabase
		this.sourceQual = _sourceQual;
		
		this.tableList = _tableList;
	}

	public String getSourceQual() {
		return sourceQual;
	}

	public String[] getTableList() {
		return tableList;
	}



}
/*
 * 
 * 
 * 
 * 
	private String targetDatabase;
	this.targetDatabase = _targetDatabase;
	public String getTargetDatabase() {
		return targetDatabase;
	}



private String sourceQuery;
private String countQuery;
private String destTableName;
private List<String> sourceColumns;
private List<String> destColumns;


public MappingTable(String source, String dest, List<String> srccols, List<String> destcols) {
	this.sourceQuery = source;
	this.destTableName = dest;
	this.sourceColumns = srccols;
	this.destColumns = destcols;
	
}
	public String getSourceQuery() {
		return sourceQuery;
	}

	public String getDestTableName() {
		return destTableName;
	}
*/