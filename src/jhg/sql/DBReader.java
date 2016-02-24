package jhg.sql;

import java.sql.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import jhg.util.*;

public class DBReader {
	
	private Database database;
	private Connection conn;
	private ResultSet rs;
	
	public boolean isConnected(){
		boolean rv = false;
		if(conn!=null){
			try{
				rv = !conn.isClosed();
			}catch(Exception e){
				System.out.println(e.getMessage());
				rv = false;
			}
		}
		return rv;
	}
	public boolean haveResult(){
		boolean rv = false;
		if(rs!=null){
			rv = true;
		}
		return rv;
	}
	
	public DBReader(Database db) {
		
		this.database = db;
		
	}
	public void connect(){
		if(database.PG.equals(database.rdbms())){
			connectPG();
		}else{
			connectMS();
		}
	}
	
	public Map<String,String> reportDatabaseProperties(){
		Map<String,String> dbprops = new TreeMap<String,String>();
		try{
			DatabaseMetaData dbmd = this.conn.getMetaData();
			dbprops.put("allProceduresAreCallable",dbmd.allProceduresAreCallable()?"Yes":"No");
			dbprops.put("allTablesAreSelectable", dbmd.allTablesAreSelectable()?"Yes":"No");
			dbprops.put("autoCommitFailureClosesAllResultSets", dbmd.autoCommitFailureClosesAllResultSets()?"Yes":"No");
			dbprops.put("dataDefinitionCausesTransactionCommit", dbmd.dataDefinitionCausesTransactionCommit()?"Yes":"No");
			dbprops.put("dataDefinitionIgnoredInTransactions", dbmd.dataDefinitionIgnoredInTransactions()?"Yes":"No");
			dbprops.put("deletesAreDetected:TYPE_FORWARD_ONLY", dbmd.deletesAreDetected(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("deletesAreDetected:TYPE_SCROLL_INSENSITIVE", dbmd.deletesAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("deletesAreDetected:TYPE_SCROLL_SENSITIVE", dbmd.deletesAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			dbprops.put("doesMaxRowSizeIncludeBlobs", dbmd.doesMaxRowSizeIncludeBlobs()?"Yes":"No");
			//dbprops.put("generatedKeyAlwaysReturned", dbmd.generatedKeyAlwaysReturned()?"Yes":"No");	
			dbprops.put("insertsAreDetected: TYPE_FORWARD_ONLY", dbmd.insertsAreDetected(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("insertsAreDetected: TYPE_SCROLL_INSENSITIVE", dbmd.insertsAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("insertsAreDetected: TYPE_SCROLL_SENSITIVE", dbmd.insertsAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			dbprops.put("isCatalogAtStart", dbmd.isCatalogAtStart()?"Yes":"No");
			dbprops.put("isReadOnly", dbmd.isReadOnly()?"Yes":"No");
			dbprops.put("locatorsUpdateCopy", dbmd.locatorsUpdateCopy()?"Yes":"No");
			dbprops.put("nullPlusNonNullIsNull", dbmd.nullPlusNonNullIsNull()?"Yes":"No");
			dbprops.put("nullsAreSortedAtEnd", dbmd.nullsAreSortedAtEnd()?"Yes":"No");
			dbprops.put("nullsAreSortedAtStart", dbmd.nullsAreSortedAtStart()?"Yes":"No");			
			dbprops.put("nullsAreSortedHigh", dbmd.nullsAreSortedHigh()?"Yes":"No");
			dbprops.put("nullsAreSortedLow", dbmd.nullsAreSortedLow()?"Yes":"No");
			dbprops.put("othersDeletesAreVisible:TYPE_FORWARD_ONLY", dbmd.othersDeletesAreVisible(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("othersDeletesAreVisible:TYPE_SCROLL_INSENSITIVE", dbmd.othersDeletesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("othersDeletesAreVisible:TYPE_SCROLL_SENSITIVE", dbmd.othersDeletesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			
			dbprops.put("othersInsertsAreVisible :TYPE_FORWARD_ONLY", dbmd.othersInsertsAreVisible(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("othersInsertsAreVisible :TYPE_SCROLL_INSENSITIVE", dbmd.othersInsertsAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("othersInsertsAreVisible :TYPE_SCROLL_SENSITIVE", dbmd.othersInsertsAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			
			dbprops.put("othersUpdatesAreVisible :TYPE_FORWARD_ONLY", dbmd.othersUpdatesAreVisible(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("othersUpdatesAreVisible :TYPE_SCROLL_INSENSITIVE", dbmd.othersUpdatesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("othersUpdatesAreVisible :TYPE_SCROLL_SENSITIVE", dbmd.othersUpdatesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			
			dbprops.put("ownDeletesAreVisible :TYPE_FORWARD_ONLY", dbmd.ownDeletesAreVisible(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("ownDeletesAreVisible :TYPE_SCROLL_INSENSITIVE", dbmd.ownDeletesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("ownDeletesAreVisible :TYPE_SCROLL_SENSITIVE", dbmd.ownDeletesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			
			dbprops.put("ownInsertsAreVisible :TYPE_FORWARD_ONLY", dbmd.ownInsertsAreVisible(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("ownInsertsAreVisible :TYPE_SCROLL_INSENSITIVE", dbmd.ownInsertsAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("ownInsertsAreVisible :TYPE_SCROLL_SENSITIVE", dbmd.ownInsertsAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			
			dbprops.put("ownUpdatesAreVisible :TYPE_FORWARD_ONLY", dbmd.ownUpdatesAreVisible(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("ownUpdatesAreVisible :TYPE_SCROLL_INSENSITIVE", dbmd.ownUpdatesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("ownUpdatesAreVisible :TYPE_SCROLL_SENSITIVE", dbmd.ownUpdatesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			
			dbprops.put("storesLowerCaseIdentifiers", dbmd.storesLowerCaseIdentifiers()?"Yes":"No");
			dbprops.put("storesLowerCaseQuotedIdentifiers", dbmd.storesLowerCaseQuotedIdentifiers()?"Yes":"No");
			dbprops.put("storesMixedCaseIdentifiers", dbmd.storesMixedCaseIdentifiers()?"Yes":"No");
			dbprops.put("storesMixedCaseQuotedIdentifiers", dbmd.storesMixedCaseQuotedIdentifiers()?"Yes":"No");
			dbprops.put("storesUpperCaseIdentifiers", dbmd.storesUpperCaseIdentifiers()?"Yes":"No");
			dbprops.put("storesUpperCaseQuotedIdentifiers", dbmd.storesUpperCaseQuotedIdentifiers()?"Yes":"No");
			dbprops.put("supportsAlterTableWithAddColumn", dbmd.supportsAlterTableWithAddColumn()?"Yes":"No");
			dbprops.put("supportsAlterTableWithDropColumn", dbmd.supportsAlterTableWithDropColumn()?"Yes":"No");
			dbprops.put("supportsANSI92EntryLevelSQL", dbmd.supportsANSI92EntryLevelSQL()?"Yes":"No");
			dbprops.put("supportsANSI92FullSQL", dbmd.supportsANSI92FullSQL()?"Yes":"No");
			dbprops.put("supportsANSI92IntermediateSQL", dbmd.supportsANSI92IntermediateSQL()?"Yes":"No");
			dbprops.put("supportsBatchUpdates", dbmd.supportsBatchUpdates()?"Yes":"No");
			dbprops.put("supportsCatalogsInDataManipulation", dbmd.supportsCatalogsInDataManipulation()?"Yes":"No");
			dbprops.put("supportsCatalogsInIndexDefinitions", dbmd.supportsCatalogsInIndexDefinitions()?"Yes":"No");
			dbprops.put("supportsCatalogsInPrivilegeDefinitions", dbmd.supportsCatalogsInPrivilegeDefinitions()?"Yes":"No");
			dbprops.put("supportsCatalogsInProcedureCalls", dbmd.supportsCatalogsInProcedureCalls()?"Yes":"No");
			dbprops.put("supportsCatalogsInTableDefinitions", dbmd.supportsCatalogsInTableDefinitions()?"Yes":"No");
			dbprops.put("supportsColumnAliasing", dbmd.supportsColumnAliasing()?"Yes":"No");
			dbprops.put("supportsConvert", dbmd.supportsConvert()?"Yes":"No");
			dbprops.put("supportsCoreSQLGrammar", dbmd.supportsCoreSQLGrammar()?"Yes":"No");
			dbprops.put("supportsCorrelatedSubqueries", dbmd.supportsCorrelatedSubqueries()?"Yes":"No");
			dbprops.put("supportsDataDefinitionAndDataManipulationTransactions", dbmd.supportsDataDefinitionAndDataManipulationTransactions()?"Yes":"No");
			dbprops.put("supportsDataManipulationTransactionsOnly", dbmd.supportsDataManipulationTransactionsOnly()?"Yes":"No");
			dbprops.put("supportsDifferentTableCorrelationNames", dbmd.supportsDifferentTableCorrelationNames()?"Yes":"No");
			dbprops.put("supportsExpressionsInOrderBy", dbmd.supportsExpressionsInOrderBy()?"Yes":"No");
			dbprops.put("supportsExtendedSQLGrammar", dbmd.supportsExtendedSQLGrammar()?"Yes":"No");
			dbprops.put("supportsFullOuterJoins", dbmd.supportsFullOuterJoins()?"Yes":"No");
			dbprops.put("supportsGetGeneratedKeys", dbmd.supportsGetGeneratedKeys()?"Yes":"No");
			dbprops.put("supportsGroupBy", dbmd.supportsGroupBy()?"Yes":"No");
			dbprops.put("supportsGroupByBeyondSelect", dbmd.supportsGroupByBeyondSelect()?"Yes":"No");
			dbprops.put("supportsGroupByUnrelated", dbmd.supportsGroupByUnrelated()?"Yes":"No");
			dbprops.put("supportsIntegrityEnhancementFacility", dbmd.supportsIntegrityEnhancementFacility()?"Yes":"No");
			dbprops.put("supportsLikeEscapeClause", dbmd.supportsLikeEscapeClause()?"Yes":"No");
			dbprops.put("supportsLimitedOuterJoins", dbmd.supportsLimitedOuterJoins()?"Yes":"No");
			dbprops.put("supportsMinimumSQLGrammar", dbmd.supportsMinimumSQLGrammar()?"Yes":"No");
			dbprops.put("supportsMixedCaseIdentifiers", dbmd.supportsMixedCaseIdentifiers()?"Yes":"No");
			dbprops.put("supportsMixedCaseQuotedIdentifiers", dbmd.supportsMixedCaseQuotedIdentifiers()?"Yes":"No");
			dbprops.put("supportsMultipleOpenResults", dbmd.supportsMultipleOpenResults()?"Yes":"No");
			dbprops.put("supportsMultipleResultSets", dbmd.supportsMultipleResultSets()?"Yes":"No");
			dbprops.put("supportsMultipleTransactions", dbmd.supportsMultipleTransactions()?"Yes":"No");
			dbprops.put("supportsNamedParameters", dbmd.supportsNamedParameters()?"Yes":"No");
			dbprops.put("supportsNonNullableColumns", dbmd.supportsNonNullableColumns()?"Yes":"No");
			dbprops.put("supportsOpenCursorsAcrossCommit", dbmd.supportsOpenCursorsAcrossCommit()?"Yes":"No");
			dbprops.put("supportsOpenCursorsAcrossRollback", dbmd.supportsOpenCursorsAcrossRollback()?"Yes":"No");
			dbprops.put("supportsOpenStatementsAcrossCommit", dbmd.supportsOpenStatementsAcrossCommit()?"Yes":"No");
			dbprops.put("supportsOpenStatementsAcrossRollback", dbmd.supportsOpenStatementsAcrossRollback()?"Yes":"No");
			dbprops.put("supportsOrderByUnrelated", dbmd.supportsOrderByUnrelated()?"Yes":"No");
			dbprops.put("supportsOuterJoins", dbmd.supportsOuterJoins()?"Yes":"No");
			dbprops.put("supportsPositionedDelete", dbmd.supportsPositionedDelete()?"Yes":"No");
			dbprops.put("supportsPositionedUpdate", dbmd.supportsPositionedUpdate()?"Yes":"No");
			dbprops.put("supportsSavepoints", dbmd.supportsSavepoints()?"Yes":"No");
			dbprops.put("supportsSchemasInDataManipulation", dbmd.supportsSchemasInDataManipulation()?"Yes":"No");
			dbprops.put("supportsSchemasInIndexDefinitions", dbmd.supportsSchemasInIndexDefinitions()?"Yes":"No");
			dbprops.put("supportsSchemasInPrivilegeDefinitions", dbmd.supportsSchemasInPrivilegeDefinitions()?"Yes":"No");
			dbprops.put("supportsSchemasInProcedureCalls", dbmd.supportsSchemasInProcedureCalls()?"Yes":"No");
			dbprops.put("supportsSchemasInTableDefinitions", dbmd.supportsSchemasInTableDefinitions()?"Yes":"No");
			dbprops.put("supportsSelectForUpdate", dbmd.supportsSelectForUpdate()?"Yes":"No");
			dbprops.put("supportsStatementPooling", dbmd.supportsStatementPooling()?"Yes":"No");
			dbprops.put("supportsStoredFunctionsUsingCallSyntax", dbmd.supportsStoredFunctionsUsingCallSyntax()?"Yes":"No");
			dbprops.put("supportsStoredProcedures", dbmd.supportsStoredProcedures()?"Yes":"No");
			dbprops.put("supportsSubqueriesInComparisons", dbmd.supportsSubqueriesInComparisons()?"Yes":"No");
			dbprops.put("supportsSubqueriesInExists", dbmd.supportsSubqueriesInExists()?"Yes":"No");
			dbprops.put("supportsSubqueriesInIns", dbmd.supportsSubqueriesInIns()?"Yes":"No");
			dbprops.put("supportsSubqueriesInQuantifieds", dbmd.supportsSubqueriesInQuantifieds()?"Yes":"No");
			dbprops.put("supportsTableCorrelationNames", dbmd.supportsTableCorrelationNames()?"Yes":"No");
			dbprops.put("supportsTransactions", dbmd.supportsTransactions()?"Yes":"No");
			dbprops.put("supportsUnion", dbmd.supportsUnion()?"Yes":"No");
			dbprops.put("supportsUnionAll", dbmd.supportsUnionAll()?"Yes":"No");
			dbprops.put("usesLocalFilePerTable", dbmd.usesLocalFilePerTable()?"Yes":"No");			
			/*
boolean	supportsRefCursors()
boolean	supportsResultSetConcurrency(int type, int concurrency)
boolean	supportsResultSetHoldability(int holdability)
boolean	supportsResultSetType(int type)
boolean	supportsTransactionIsolationLevel(int level)
boolean	updatesAreDetected(int type)
boolean	supportsConvert(int fromType, int toType)

			dbprops.put("XXX", dbmd.xxxxxx()?"Yes":"No");
			dbprops.put("XXX", dbmd.xxxxxx()?"Yes":"No");
			dbprops.put("XXX", dbmd.xxxxxx()?"Yes":"No");
			dbprops.put("XXX", dbmd.xxxxxx()?"Yes":"No");
			dbprops.put("XXX", dbmd.xxxxxx()?"Yes":"No");
			dbprops.put("XXX", dbmd.xxxxxx()?"Yes":"No");
			dbprops.put("XXX", dbmd.xxxxxx()?"Yes":"No");

ResultSet	getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern)
ResultSet	getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
ResultSet	getCatalogs()
ResultSet	getClientInfoProperties()
ResultSet	getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
ResultSet	getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
ResultSet	getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable)
ResultSet	getExportedKeys(String catalog, String schema, String table)
ResultSet	getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern)
ResultSet	getFunctions(String catalog, String schemaPattern, String functionNamePattern)
ResultSet	getImportedKeys(String catalog, String schema, String table)
ResultSet	getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
ResultSet	getPrimaryKeys(String catalog, String schema, String table)
ResultSet	getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
ResultSet	getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
ResultSet	getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
ResultSet	getSchemas()
ResultSet	getSchemas(String catalog, String schemaPattern)
ResultSet	getSuperTables(String catalog, String schemaPattern, String tableNamePattern)
ResultSet	getSuperTypes(String catalog, String schemaPattern, String typeNamePattern)
ResultSet	getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
ResultSet	getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
ResultSet	getTableTypes()
ResultSet	getTypeInfo()
ResultSet	getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
ResultSet	getVersionColumns(String catalog, String schema, String table)

String	getCatalogSeparator()
String	getCatalogTerm()
String	getDatabaseProductName()
String	getDatabaseProductVersion()
String	getDriverName()
String	getDriverVersion()
String	getExtraNameCharacters()
String	getIdentifierQuoteString()
String	getSchemaTerm()
String	getSearchStringEscape()
String	getSQLKeywords()
String	getNumericFunctions()
String	getProcedureTerm()
String	getStringFunctions()
String	getSystemFunctions()
String	getTimeDateFunctions()
String	getURL()
String	getUserName()


long	getMaxLogicalLobSize()
RowIdLifetime	getRowIdLifetime()

int	getDatabaseMajorVersion()
int	getDatabaseMinorVersion()
int	getDefaultTransactionIsolation()
int	getDriverMajorVersion()
int	getDriverMinorVersion()
int	getJDBCMajorVersion()
int	getJDBCMinorVersion()
int	getMaxBinaryLiteralLength()
int	getMaxCatalogNameLength()
int	getMaxCharLiteralLength()
int	getMaxColumnNameLength()
int	getMaxColumnsInGroupBy()
int	getMaxColumnsInIndex()
int	getMaxColumnsInOrderBy()
int	getMaxColumnsInSelect()
int	getMaxColumnsInTable()
int	getMaxConnections()
int	getMaxCursorNameLength()
int	getMaxIndexLength()
int	getMaxProcedureNameLength()
int	getMaxRowSize()
int	getMaxSchemaNameLength()
int	getMaxStatementLength()
int	getMaxStatements()
int	getMaxTableNameLength()
int	getMaxTablesInSelect()
int	getMaxUserNameLength()
int	getResultSetHoldability()
int	getSQLStateType()




			*/
		}catch(SQLFeatureNotSupportedException sfnse){
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return dbprops;
	}
	
	private void connectMS(){
		try{
			Class.forName(database.driver());
			System.out.println("Connecting with URL:'"+database.url()+"'");
			conn = DriverManager.getConnection(database.url());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void connectPG() {
		//"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/DM_DEV"
		// jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/DM_DEV
		try{
			Class.forName(database.driver());
			Log.println("Connecting with URL:'"+database.url()+"'");
			conn = DriverManager.getConnection(database.url(),database.user(),database.password());
		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
	public void closeRs(){
		try{
			if(rs!=null){
				Statement stmt = rs.getStatement();
				if(stmt!=null){
					stmt.close();
				}
				rs.close();
				rs = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void close(){
		try{
			if(conn!=null){
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql){
		Log.println("SQL:"+sql);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	public void printResult(){
		try{
		int i = 0;
			while(rs.next()){
				i++;
				String s1 = rs.getString(1);
				System.out.println(i+":"+s1);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public ResultSet getResult(){
		return this.rs;
	}

	public static void testSimple(){		
		
		DBReader db = new DBReader(Database.SQLPROD);
		String[] tableNames = {"valuequest_vq_carrier"};
		db.connect();
		if(db.isConnected()){
			for(String table:tableNames){
				db.query("select * from "+table);
				if(db.haveResult()){
					db.printResult();
				}			
			}
		}
		db.close();		
	}
	
	public static void testReportDb(){
		DBReader db = new DBReader(Database.SQLPROD);
		/*
		 * get database properties
		 * 
		 */
	}
	
	public static void main(String[] args){
		System.out.println("Starting.");
		testReportDb();
		System.out.println("Finished.");
	}
	
	public int getCountResult(){
		int val = -1;
		try{
			if(rs.next()){
				val = rs.getInt(1);
			} 
		}catch(Exception e){
			Log.println(e.getMessage());
		}
		return val;
	}	
	
	
	
	public static void testSimpleTableRead(){
		System.out.println("Starting.");
		DBReader db = new DBReader(Database.SQLPROD);
		db.connect();
		if(db.isConnected()){
			db.query("select * from valuequest_vq_carrier");
			if(db.haveResult()){
				db.printResult();
			}			
		}
		db.close();
		System.out.println("Finished.");		
	}

	
	/*
	public List<Table> configure(String[] tableList) {
		List<Table> tables = new ArrayList<Table>();
		DatabaseMetaData dbmd;
		try {
			dbmd = conn.getMetaData();
	
			for(String tableName:tableList){
				ResultSet dbmdrs = dbmd.getColumns(null,null,tableName,null);
				Table table = new Table(tableName);
				while(dbmdrs.next()){
					String colname = dbmdrs.getString(4);
					//int    columnType = dbmdrs.getInt(5);
					Field f = new Field(colname);
					table.addField(f);
				}
				dbmdrs = dbmd.getBestRowIdentifier(null, null, tableName, DatabaseMetaData.bestRowSession,false);  //.getPrimaryKeys(null,null,tableName);
				int pkCount = 0;
				while(dbmdrs.next()){
					pkCount++;
					if(pkCount>1){
						break;
					}else{
					    String pkname = dbmdrs.getString(2);//4
					    table.setPk(pkname);
					}
				}
				tables.add(table);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return tables;
	}
	*/
}

