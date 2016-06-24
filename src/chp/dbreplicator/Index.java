package chp.dbreplicator;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Index {
	
	public static enum IndexType{
		tableIndexStatistic(DatabaseMetaData.tableIndexStatistic),//tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions
		tableIndexClustered(DatabaseMetaData.tableIndexClustered),//tableIndexClustered - this is a clustered index
		tableIndexHashed(DatabaseMetaData.tableIndexHashed),//tableIndexHashed - this is a hashed index
		tableIndexOther(DatabaseMetaData.tableIndexOther);//tableIndexOther - this is some other style of index
		private short code;
		private IndexType(short c){
			code = c;
		}
		public static IndexType from(short c){
			for(IndexType n:IndexType.values()){
				if(n.code == c){
					return n;
				}
			}
			return null;
		}		
	}
	
	public static enum Field{
		NIL,
		TABLE_CAT,//1 String => table catalog (may be null)
		TABLE_SCHEM,//2 String => table schema (may be null)
		TABLE_NAME,//3 String => table name
		NON_UNIQUE,//4 boolean => Can index values be non-unique. false when TYPE is tableIndexStatistic
		INDEX_QUALIFIER,//5 String => index catalog (may be null); null when TYPE is tableIndexStatistic
		INDEX_NAME,//6 String => index name; null when TYPE is tableIndexStatistic
		TYPE,//7 IndexType  short => index type:
		ORDINAL_POSITION,//8 short => column sequence number within index; zero when TYPE is tableIndexStatistic
		COLUMN_NAME,//9 String => column name; null when TYPE is tableIndexStatistic
		ASC_OR_DESC,//10 String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic
		CARDINALITY,//11 int => When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise, it is the number of unique values in the index.
		PAGES,//12 int => When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index.
		FILTER_CONDITION;//13 String => Filter condition, if any. (may be null)		
	}	
	private String tableCatalog,tableSchema,tableName,indexQualifier,indexName,columnName,ascOrDesc,filterCondition;
	private boolean nonUnique;
	private IndexType type;
	private short ordinalPosition;
	private int cardinality,pages;
	private boolean valid;
	public Index(ResultSet rs){
		try{
			tableCatalog = rs.getString(Field.TABLE_CAT.ordinal());
			tableSchema = rs.getString(Field.TABLE_SCHEM.ordinal());
			tableName = rs.getString(Field.TABLE_NAME.ordinal());
			indexQualifier = rs.getString(Field.INDEX_QUALIFIER.ordinal());
			indexName = rs.getString(Field.INDEX_NAME.ordinal());
			columnName = rs.getString(Field.COLUMN_NAME.ordinal());
			ascOrDesc = rs.getString(Field.ASC_OR_DESC.ordinal());
			filterCondition = rs.getString(Field.FILTER_CONDITION.ordinal());
			
			nonUnique = rs.getBoolean(Field.NON_UNIQUE.ordinal());
			type = IndexType.from(rs.getShort(Field.TYPE.ordinal()));
			ordinalPosition = rs.getShort(Field.ORDINAL_POSITION.ordinal());
			cardinality = rs.getInt(Field.CARDINALITY.ordinal());
			pages = rs.getInt(Field.PAGES.ordinal());
			valid = true;
		} catch (SQLException e) {
			valid = false;
			e.printStackTrace();
		}
	}
	public boolean isValid(){
		return this.valid;
	}

	public String getTableCatalog() {
		return tableCatalog;
	}

	public String getTableSchema() {
		return tableSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public String getIndexQualifier() {
		return indexQualifier;
	}

	public String getIndexName() {
		return indexName;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getAscOrDesc() {
		return ascOrDesc;
	}

	public String getFilterCondition() {
		return filterCondition;
	}

	public boolean isNonUnique() {
		return nonUnique;
	}

	public IndexType getType() {
		return type;
	}

	public short getOrdinalPosition() {
		return ordinalPosition;
	}

	public int getCardinality() {
		return cardinality;
	}

	public int getPages() {
		return pages;
	}

	@Override
	public String toString() {
		return "Index [tableCatalog=" + tableCatalog + ", tableSchema="
				+ tableSchema + ", tableName=" + tableName
				+ ", indexQualifier=" + indexQualifier + ", indexName="
				+ indexName + ", columnName=" + columnName + ", ascOrDesc="
				+ ascOrDesc + ", filterCondition=" + filterCondition
				+ ", nonUnique=" + nonUnique + ", type=" + type
				+ ", ordinalPosition=" + ordinalPosition + ", cardinality="
				+ cardinality + ", pages=" + pages + "]";
	}

	
	
}
