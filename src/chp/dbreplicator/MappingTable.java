package chp.dbreplicator;

import java.util.*;

public class MappingTable {

	
	private String sourceTableName;
	private String destTableName;
	private Map<String,String> columnMapping;
	
	public MappingTable(String source, String dest, Map<String,String> mapping) {
		this.sourceTableName = source;
		this.destTableName = dest;
		this.columnMapping = mapping;
		
	}

	public String getSourceTableName() {
		return sourceTableName;
	}

	public String getDestTableName() {
		return destTableName;
	}

	public Map<String, String> getColumnMapping() {
		return columnMapping;
	}

}
