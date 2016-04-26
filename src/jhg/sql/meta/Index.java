package jhg.sql.meta;

public class Index {
	
	public static enum IndexType{
		/*
		tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions
		tableIndexClustered - this is a clustered index
		tableIndexHashed - this is a hashed index
		tableIndexOther - this is some other style of index		
		*/
	}
	
	public static enum Field{
		NIL,
		TABLE_CAT,
		TABLE_SCHEM,
		TABLE_NAME,
		NON_UNIQUE,
		INDEX_QUALIFIER,
		INDEX_NAME,
		TYPE,//IndexType
		ORDINAL_POSITION,
		COLUMN_NAME,
		ASC_OR_DESC,
		CARDINALITY,
		PAGES,
		FILTER_CONDITION;		
	}	
	
/*
Each index column description has the following columns:

TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
NON_UNIQUE boolean => Can index values be non-unique. false when TYPE is tableIndexStatistic
INDEX_QUALIFIER String => index catalog (may be null); null when TYPE is tableIndexStatistic
INDEX_NAME String => index name; null when TYPE is tableIndexStatistic
TYPE short => index type:
tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions
tableIndexClustered - this is a clustered index
tableIndexHashed - this is a hashed index
tableIndexOther - this is some other style of index
ORDINAL_POSITION short => column sequence number within index; zero when TYPE is tableIndexStatistic
COLUMN_NAME String => column name; null when TYPE is tableIndexStatistic
ASC_OR_DESC String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic
CARDINALITY int => When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise, it is the number of unique values in the index.
PAGES int => When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index.
FILTER_CONDITION String => Filter condition, if any. (may be null)
 */
}
