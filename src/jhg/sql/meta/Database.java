package jhg.sql.meta;

import java.sql.*;
import java.util.*;

import jhg.util.Log;
import static java.sql.Types.*;
/**
 * DatabaseMetaData wrapper.
 * 
 * @author jgagon
 *
 */
public class Database {
	
	/*
	 * TODO continue impl
	 * Link the objects together in a graph that has public methods. e.g.: Catalogs have Schemas which have Tables which have Indexes, Columns and Primary Keys and Privileges.
	 * Test this out on a real database.
	 * 
	 */

	private DatabaseMetaData dbmd;
	private boolean loaded;
	private List<ClientInfoProperty> clientInfoPropertyList;
	private List<Catalog> catalogList;
	
	private List<Schema> schemaList;
	private List<TableType> tableTypeList;
	private List<Type> typeList;
	
	private Map<String,String> reportDatabaseProperties;
	
	public Database(DatabaseMetaData d){
		super();
		this.dbmd = d;
		this.clientInfoPropertyList = new ArrayList<ClientInfoProperty>();
		this.reportDatabaseProperties = new Hashtable<String,String>();
		this.catalogList = new ArrayList<Catalog>();
		this.schemaList = new ArrayList<Schema>();
		this.tableTypeList = new ArrayList<TableType>();
		this.typeList = new ArrayList<Type>();
		
		this.loaded = false;
	}
	public void load(){
		loadProperties();
		loadClientInfoPropertyList();
		loadCatalogList();
		loadSchemaList();
		loadTableTypeList();
		loadTypeList();
		loaded = true;
	}
	
	 
	public void printClientInfoPropertyList(){
		for(ClientInfoProperty cip:clientInfoPropertyList){
			Log.println(cip.toString());
		}
	}
	
	
	List<Catalog> getCatalogList(){
		return this.catalogList;
	}
	List<Schema> getSchemaList(){
		return this.schemaList;
	}
	List<TableType> getTableTypeList(){
		return this.tableTypeList;
	}
	List<Type> getTypeList(){
		return this.typeList;
	}
	List<ClientInfoProperty> getClientInfoPropertyList(){
		return this.clientInfoPropertyList;
	}
	Map<String,String> getProperties(){
		return this.reportDatabaseProperties;
	}

	
	List<Schema> getSchemas(Catalog catalog, String schemaPattern){
		List<Schema> list = new ArrayList<Schema>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getSchemas(catalog.getTableCatalog(), schemaPattern);
			while(rs.next()){
				Schema c = new Schema(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;		
	}
	List<Attribute> getAttributes(Catalog catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern)
	{
		List<Attribute> list = new ArrayList<Attribute>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getAttributes(catalog.getTableCatalog(), schemaPattern, typeNamePattern, attributeNamePattern);
			while(rs.next()){
				Attribute c = new Attribute(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;		
	}//TODO refactor
	List<Column> getColumns(Catalog catalog, String schemaPattern, String tableNamePattern, String columnNamePattern){
		List<Column> list = new ArrayList<Column>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getColumns(catalog.getTableCatalog(), schemaPattern, tableNamePattern, columnNamePattern);
			while(rs.next()){
				Column c = new Column(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;	
	}
	List<PseudoColumn> getPseudoColumns(Catalog catalog, String schemaPattern, String tableNamePattern, String columnNamePattern){
		List<PseudoColumn> list = new ArrayList<PseudoColumn>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getPseudoColumns(catalog.getTableCatalog(), schemaPattern, tableNamePattern, columnNamePattern);
			while(rs.next()){
				PseudoColumn c = new PseudoColumn(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<FunctionColumn> getFunctionColumns(Catalog catalog, String schemaPattern, String functionNamePattern, String columnNamePattern){
		List<FunctionColumn> list = new ArrayList<FunctionColumn>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getFunctionColumns(catalog.getTableCatalog(), schemaPattern, functionNamePattern, columnNamePattern);
			while(rs.next()){
				FunctionColumn c = new FunctionColumn(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<Function> getFunctions(Catalog catalog, String schemaPattern, String functionNamePattern){
		List<Function> list = new ArrayList<Function>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getFunctions(catalog.getTableCatalog(), schemaPattern, functionNamePattern);
			while(rs.next()){
				Function c = new Function(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<ProcedureColumn> getProcedureColumns(Catalog catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern){
		List<ProcedureColumn> list = new ArrayList<ProcedureColumn>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getProcedureColumns(catalog.getTableCatalog(), schemaPattern, procedureNamePattern, columnNamePattern);
			while(rs.next()){
				ProcedureColumn c = new ProcedureColumn(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<Procedure> getProcedures(Catalog catalog, String schemaPattern, String procedureNamePattern){
		List<Procedure> list = new ArrayList<Procedure>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getProcedures(catalog.getTableCatalog(), schemaPattern, procedureNamePattern);
			while(rs.next()){
				Procedure c = new Procedure(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<SuperTable> getSuperTables(Catalog catalog, String schemaPattern, String tableNamePattern){
		List<SuperTable> list = new ArrayList<SuperTable>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getSuperTables(catalog.getTableCatalog(), schemaPattern, tableNamePattern);
			while(rs.next()){
				SuperTable c = new SuperTable(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<TablePrivilege> getTablePrivileges(Catalog catalog, String schemaPattern, String tableNamePattern){
		List<TablePrivilege> list = new ArrayList<TablePrivilege>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getSuperTables(catalog.getTableCatalog(), schemaPattern, tableNamePattern);
			while(rs.next()){
				TablePrivilege c = new TablePrivilege(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<Table> getTables(Catalog catalog, String schemaPattern, String tableNamePattern, String[] types){
		List<Table> list = new ArrayList<Table>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getTables(catalog.getTableCatalog(), schemaPattern, tableNamePattern, types);
			while(rs.next()){
				Table c = new Table(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<UserDefinedType> getUDTs(Catalog catalog, String schemaPattern, String typeNamePattern, int[] types){
		List<UserDefinedType> list = new ArrayList<UserDefinedType>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getUDTs(catalog.getTableCatalog(), schemaPattern, typeNamePattern, types);
			while(rs.next()){
				UserDefinedType c = new UserDefinedType(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<ExportedKey> getExportedKeys(Catalog catalog, Schema schema, Table table){
		List<ExportedKey> list = new ArrayList<ExportedKey>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getExportedKeys(catalog.getTableCatalog(), schema.getTableSchem(), table.getTableName());
			while(rs.next()){
				ExportedKey c = new ExportedKey(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<ImportedKey> getImportedKeys(Catalog catalog, Schema schema, Table table){
		List<ImportedKey> list = new ArrayList<ImportedKey>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getImportedKeys(catalog.getTableCatalog(), schema.getTableSchem(), table.getTableName());
			while(rs.next()){
				ImportedKey c = new ImportedKey(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<PrimaryKey> getPrimaryKeys(Catalog catalog, Schema schema, Table table){
		List<PrimaryKey> list = new ArrayList<PrimaryKey>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getPrimaryKeys(catalog.getTableCatalog(), schema.getTableSchem(), table.getTableName());
			while(rs.next()){
				PrimaryKey c = new PrimaryKey(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<VersionColumn> getVersionColumns(Catalog catalog, Schema schema, Table table){
		List<VersionColumn> list = new ArrayList<VersionColumn>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getVersionColumns(catalog.getTableCatalog(), schema.getTableSchem(), table.getTableName());
			while(rs.next()){
				VersionColumn c = new VersionColumn(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<BestRowIdentifier> getBestRowIdentifier(Catalog catalog, Schema schema, Table table, int scope, boolean nullable){
		List<BestRowIdentifier> list = new ArrayList<BestRowIdentifier>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getBestRowIdentifier(catalog.getTableCatalog(), schema.getTableSchem(), table.getTableName(),scope, nullable);
			while(rs.next()){
				BestRowIdentifier c = new BestRowIdentifier(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<ColumnPrivilege> getColumnPrivileges(Catalog catalog, Schema schema, Table table, String columnNamePattern){
		List<ColumnPrivilege> list = new ArrayList<ColumnPrivilege>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getColumnPrivileges(catalog.getTableCatalog(), schema.getTableSchem(), table.getTableName(),columnNamePattern);
			while(rs.next()){
				ColumnPrivilege c = new ColumnPrivilege(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	List<Index> getIndexInfo(Catalog catalog, Schema schema, Table table, boolean unique, boolean approximate){
		List<Index> list = new ArrayList<Index>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getIndexInfo(catalog.getTableCatalog(), schema.getTableSchem(), table.getTableName(),unique,approximate);
			while(rs.next()){
				Index c = new Index(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}
	
	List<CrossReference> getCrossReferences(Catalog parentCatalog, Schema parentSchema, Table parentTable, Catalog foreignCatalog, Schema foreignSchema, Table foreignTable){
		List<CrossReference> list = new ArrayList<CrossReference>();
		if(!loaded){
			return list;
		}
		try {		
			ResultSet rs = dbmd.getCrossReference(
					parentCatalog.getTableCatalog(), 
					parentSchema.getTableSchem(), 
					parentTable.getTableName(), 
					foreignCatalog.getTableCatalog(), 
					foreignSchema.getTableSchem(), 
					foreignTable.getTableName()
					);
			while(rs.next()){
				CrossReference c = new CrossReference(rs);
				if(c!=null){
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}	

	
	@SuppressWarnings("unused")
	public static void test(){
		/*
		 * Call all the database meta data methods.
		 */
		Database db = null;
		try {		
			Connection conn = getConnection();//DriverManager.getConnection("");
			if(conn==null){
				Log.println("Connection null.");
				return;
			}
			DatabaseMetaData dbmd = conn.getMetaData();
			db = new Database(dbmd);
		} catch (SQLException e) {
			e.printStackTrace();//avoid this.
		}
		
		StringBuilder sb = new StringBuilder();
		if(db!=null){
			db.load();
			db.printClientInfoPropertyList();
		}else{
			Log.println("Db null.");
		}
		
		
		
	}	
	
	
	private static Connection getConnection(){
		/*
		 * TODO impl
		 */
		return null;
	}
	
	public static Map<String,String> reportDatabaseProperties(DatabaseMetaData dbmd){
		Map<String,String> dbprops = new TreeMap<String,String>();
		try{
			
			dbprops.put("allProceduresAreCallable",dbmd.allProceduresAreCallable()?"Yes":"No");
			dbprops.put("allTablesAreSelectable", dbmd.allTablesAreSelectable()?"Yes":"No");
			dbprops.put("autoCommitFailureClosesAllResultSets", dbmd.autoCommitFailureClosesAllResultSets()?"Yes":"No");
			dbprops.put("dataDefinitionCausesTransactionCommit", dbmd.dataDefinitionCausesTransactionCommit()?"Yes":"No");
			dbprops.put("dataDefinitionIgnoredInTransactions", dbmd.dataDefinitionIgnoredInTransactions()?"Yes":"No");
			dbprops.put("deletesAreDetected:TYPE_FORWARD_ONLY", dbmd.deletesAreDetected(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("deletesAreDetected:TYPE_SCROLL_INSENSITIVE", dbmd.deletesAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("deletesAreDetected:TYPE_SCROLL_SENSITIVE", dbmd.deletesAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			dbprops.put("doesMaxRowSizeIncludeBlobs", dbmd.doesMaxRowSizeIncludeBlobs()?"Yes":"No");
			dbprops.put("generatedKeyAlwaysReturned", dbmd.generatedKeyAlwaysReturned()?"Yes":"No");	
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
			
			dbprops.put("supportsResultSetConcurrency(TYPE_FORWARD_ONLY,CONCUR_READ_ONLY)", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)?"Yes":"No");
			dbprops.put("supportsResultSetConcurrency(TYPE_SCROLL_INSENSITIVE,CONCUR_READ_ONLY)", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)?"Yes":"No");
			dbprops.put("supportsResultSetConcurrency(TYPE_SCROLL_SENSITIVE,CONCUR_READ_ONLY)", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)?"Yes":"No");
			dbprops.put("supportsResultSetConcurrency(TYPE_FORWARD_ONLY,CONCUR_UPDATABLE)", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)?"Yes":"No");
			dbprops.put("supportsResultSetConcurrency(TYPE_SCROLL_INSENSITIVE,CONCUR_UPDATABLE)", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)?"Yes":"No");
			dbprops.put("supportsResultSetConcurrency(TYPE_SCROLL_SENSITIVE,CONCUR_UPDATABLE)", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)?"Yes":"No");
			
			dbprops.put("supportsResultSetConcurrency-HOLD_CURSORS_OVER_COMMIT", dbmd.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT)?"Yes":"No");
			dbprops.put("supportsResultSetConcurrency-CLOSE_CURSORS_AT_COMMIT", dbmd.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT)?"Yes":"No");
			
			dbprops.put("supportsResultSetType-TYPE_FORWARD_ONLY", dbmd.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("supportsResultSetType-TYPE_SCROLL_INSENSITIVE", dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("supportsResultSetType-TYPE_SCROLL_SENSITIVE", dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			
			
			dbprops.put("supportsTransactionIsolationLevel-TRANSACTION_NONE",dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_NONE)?"Yes":"No");
			dbprops.put("supportsTransactionIsolationLevel-TRANSACTION_READ_COMMITTED",dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)?"Yes":"No");
			dbprops.put("supportsTransactionIsolationLevel-TRANSACTION_READ_UNCOMMITTED",dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED)?"Yes":"No");
			dbprops.put("supportsTransactionIsolationLevel-TRANSACTION_REPEATABLE_READ",dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ)?"Yes":"No");
			dbprops.put("supportsTransactionIsolationLevel-TRANSACTION_SERIALIZABLE",dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)?"Yes":"No");
			
			
			dbprops.put("updatesAreDetected-TYPE_FORWARD_ONLY", dbmd.updatesAreDetected(ResultSet.TYPE_FORWARD_ONLY)?"Yes":"No");
			dbprops.put("updatesAreDetected-TYPE_SCROLL_INSENSITIVE", dbmd.updatesAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE)?"Yes":"No");
			dbprops.put("updatesAreDetected-TYPE_SCROLL_SENSITIVE", dbmd.updatesAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE)?"Yes":"No");
			
			int[] types = {ARRAY,BIGINT,BINARY,BIT,BLOB,BOOLEAN,CHAR,CLOB,DATALINK,DATE,DECIMAL,DISTINCT,DOUBLE,FLOAT,INTEGER,JAVA_OBJECT,LONGNVARCHAR,LONGVARBINARY,LONGVARCHAR,NCHAR,NCLOB,NULL,NUMERIC,NVARCHAR,OTHER,REAL,REF,ROWID,SMALLINT,SQLXML,STRUCT,TIME,TIMESTAMP,TINYINT,VARBINARY,VARCHAR};
			for(int i=0;i<types.length;i++){
				for(int j=0;j<types.length;j++){dbprops.put("supportsConvert("+types[i]+","+types[j]+")",dbmd.supportsConvert(types[i],types[j])?"Yes":"No");}
			}
			//String
			dbprops.put("getCatalogSeparator", dbmd.getCatalogSeparator());
			dbprops.put("getCatalogTerm", dbmd.getCatalogTerm());
			dbprops.put("getDatabaseProductName", dbmd.getDatabaseProductName());
			dbprops.put("getDatabaseProductVersion", dbmd.getDatabaseProductVersion());
			dbprops.put("getDriverName", dbmd.getDriverName());
			dbprops.put("getDriverVersion", dbmd.getDriverVersion());
			dbprops.put("getExtraNameCharacters", dbmd.getExtraNameCharacters());
			dbprops.put("getIdentifierQuoteString", dbmd.getIdentifierQuoteString());
			dbprops.put("getSchemaTerm", dbmd.getSchemaTerm());
			dbprops.put("getSearchStringEscape", dbmd.getSearchStringEscape());
			dbprops.put("getSQLKeywords", dbmd.getSQLKeywords());
			dbprops.put("getNumericFunctions", dbmd.getNumericFunctions());
			dbprops.put("getProcedureTerm", dbmd.getProcedureTerm());
			dbprops.put("getStringFunctions", dbmd.getStringFunctions());
			dbprops.put("getSystemFunctions", dbmd.getSystemFunctions());
			dbprops.put("getTimeDateFunctions", dbmd.getTimeDateFunctions());
			dbprops.put("getURL", dbmd.getURL());
			dbprops.put("getUserName", dbmd.getUserName());
			dbprops.put("getRowIdLifetime", dbmd.getRowIdLifetime().name());
			//Integer
			dbprops.put("getDatabaseMajorVersion", String.valueOf(dbmd.getDatabaseMajorVersion()));
			dbprops.put("getDatabaseMinorVersion", String.valueOf(dbmd.getDatabaseMinorVersion()));
			dbprops.put("getDefaultTransactionIsolation", String.valueOf(dbmd.getDefaultTransactionIsolation()));
			dbprops.put("getDriverMajorVersion", String.valueOf(dbmd.getDriverMajorVersion()));
			dbprops.put("getDriverMinorVersion", String.valueOf(dbmd.getDriverMinorVersion()));
			dbprops.put("getJDBCMajorVersion", String.valueOf(dbmd.getJDBCMajorVersion()));
			dbprops.put("getJDBCMinorVersion", String.valueOf(dbmd.getJDBCMinorVersion()));
			dbprops.put("getMaxBinaryLiteralLength", String.valueOf(dbmd.getMaxBinaryLiteralLength()));
			dbprops.put("getMaxCatalogNameLength", String.valueOf(dbmd.getMaxCatalogNameLength()));
			dbprops.put("getMaxCharLiteralLength", String.valueOf(dbmd.getMaxCharLiteralLength()));
			dbprops.put("getMaxColumnNameLength", String.valueOf(dbmd.getMaxColumnNameLength()));
			dbprops.put("getMaxColumnsInGroupBy", String.valueOf(dbmd.getMaxColumnsInGroupBy()));
			dbprops.put("getMaxColumnsInIndex", String.valueOf(dbmd.getMaxColumnsInIndex()));
			dbprops.put("getMaxColumnsInOrderBy", String.valueOf(dbmd.getMaxColumnsInOrderBy()));
			dbprops.put("getMaxColumnsInSelect", String.valueOf(dbmd.getMaxColumnsInSelect()));
			dbprops.put("getMaxColumnsInTable", String.valueOf(dbmd.getMaxColumnsInTable()));
			dbprops.put("getMaxConnections", String.valueOf(dbmd.getMaxConnections()));
			dbprops.put("getMaxCursorNameLength", String.valueOf(dbmd.getMaxCursorNameLength()));
			dbprops.put("getMaxIndexLength", String.valueOf(dbmd.getMaxIndexLength()));
			dbprops.put("getMaxProcedureNameLength", String.valueOf(dbmd.getMaxProcedureNameLength()));
			dbprops.put("getMaxRowSize", String.valueOf(dbmd.getMaxRowSize()));
			dbprops.put("getMaxSchemaNameLength", String.valueOf(dbmd.getMaxSchemaNameLength()));
			dbprops.put("getMaxStatementLength", String.valueOf(dbmd.getMaxStatementLength()));
			dbprops.put("getMaxStatements", String.valueOf(dbmd.getMaxStatements()));
			dbprops.put("getMaxTableNameLength", String.valueOf(dbmd.getMaxTableNameLength()));
			dbprops.put("getMaxTablesInSelect", String.valueOf(dbmd.getMaxTablesInSelect()));
			dbprops.put("getMaxUserNameLength", String.valueOf(dbmd.getMaxUserNameLength()));
			dbprops.put("getResultSetHoldability", String.valueOf(dbmd.getResultSetHoldability()));
			dbprops.put("getSQLStateType", String.valueOf(dbmd.getSQLStateType()));
			
			 

			
		}catch(SQLFeatureNotSupportedException sfnse){
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return dbprops;
	}
		
	private void loadTypeList(){
		try {		
			ResultSet rs = dbmd.getTypeInfo();
			while(rs.next()){
				Type c = new Type(rs);
				if(c!=null){
					typeList.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}	
	private void loadTableTypeList(){
		try {		
			ResultSet rs = dbmd.getTableTypes();
			while(rs.next()){
				TableType c = new TableType(rs);
				if(c!=null){
					tableTypeList.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}	
	private void loadSchemaList(){
		try {		
			ResultSet rs = dbmd.getSchemas();
			while(rs.next()){
				Schema c = new Schema(rs);
				if(c!=null){
					schemaList.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}	
	private void loadCatalogList(){
		try {		
			ResultSet rs = dbmd.getCatalogs();
			while(rs.next()){
				Catalog c = new Catalog(rs);
				if(c!=null){
					catalogList.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	private void loadClientInfoPropertyList(){
		try {		
			ResultSet rs = dbmd.getClientInfoProperties();
			while(rs.next()){
				ClientInfoProperty c = new ClientInfoProperty(rs);
				if(c!=null){
					clientInfoPropertyList.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * TODO add schema, table type and type
	 */
	private void loadProperties(){
		reportDatabaseProperties = reportDatabaseProperties(dbmd);
	}		

	
	public static void main(String[] args){
		test();
	}
	
}
/*
x boolean	supportsResultSetConcurrency(int type, int concurrency)
x boolean	supportsResultSetHoldability(int holdability)
x boolean	supportsResultSetType(int type)
x boolean	supportsTransactionIsolationLevel(int level)
x boolean	updatesAreDetected(int type)
x boolean	supportsConvert(int fromType, int toType)

1.8
boolean	supportsRefCursors()
long	getMaxLogicalLobSize()

dbprops.put("getMaxLogicalLobSize", String.valueOf(dbmd.getMaxLogicalLobSize()));

			dbprops.put("XXX", dbmd.xxxxxx()?"Yes":"No");
			dbprops.put("XXX", dbmd.xxxxxx());
*/

/*
 * 

{return null;}//TODO IMPL

<void>
ClientInfoPropertiy		ResultSet	getClientInfoProperties()
Catalog					ResultSet	getCatalogs()
Schema					ResultSet	getSchemas()
TableType				ResultSet	getTableTypes() 
Type					ResultSet	getTypeInfo()	


<catalog>
Schema			ResultSet	getSchemas(String catalog, String schemaPattern)
Attribute		ResultSet	getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern)
Column			ResultSet	getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
PseudoColumn	ResultSet	getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
FunctionColumn	ResultSet	getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern)
Function		ResultSet	getFunctions(String catalog, String schemaPattern, String functionNamePattern)
ProcedureColumn	ResultSet	getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
Procedure		ResultSet	getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
SuperTable		ResultSet	getSuperTables(String catalog, String schemaPattern, String tableNamePattern)
SuperType		ResultSet	getSuperTypes(String catalog, String schemaPattern, String typeNamePattern)
TablePrivilege	ResultSet	getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
Table			ResultSet	getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
UserDefinedType ResultSet	getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)

<catalog,schema,table>

ExportedKey			ResultSet	getExportedKeys(String catalog, String schema, String table)
ImportedKey			ResultSet	getImportedKeys(String catalog, String schema, String table)
PrimaryKey			ResultSet	getPrimaryKeys(String catalog, String schema, String table)
VersionColumn		ResultSet	getVersionColumns(String catalog, String schema, String table)
BestRowIdentifier	ResultSet	getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
ColumnPrivilege		ResultSet	getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
Index				ResultSet	getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)

<catalog,schema,table,catalog,schema,table>
CrossReference		ResultSet	getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable)

x Attribute
x BestRowIdentifier
x Catalog
x ClientInfoProperty
x Column
x ColumnPrivilege
x CrossReference
x ExportedKey
x FunctionColumn
x ImportedKey
x Index
x PrimaryKey
x Procedure
x ProcedureColumn
x PseudoColumn
x Schema
X SuperTable
X SuperType
x Table
x TablePrivilege
x TableType
x Type
x UserDefinedType
x VersionColumn

Database
DeferType
DeleteRule
ImportRule
UpdateRule
TriFlag

*/	

