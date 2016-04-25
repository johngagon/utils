package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Table {
	public static final String NOT_PROVIDED = "";
	public static enum Field{
		NIL,						//0.  
		TABLE_CAT,					//1.  String => table catalog (may be null)
		TABLE_SCHEM,				//2.  String => table schema (may be null)
		TABLE_NAME,					//3.  String => table name
		TABLE_TYPE,					//4.  String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
		REMARKS,					//5.  String => explanatory comment on the table
		
		TYPE_CAT,					//6.  String => the types catalog (may be null)
		TYPE_SCHEM,					//7.  String => the types schema (may be null)
		TYPE_NAME,					//8.  String => type name (may be null)
		SELF_REFERENCING_COL_NAME,	//9.  String => name of the designated "identifier" column of a typed table (may be null)
		REF_GENERATION				//10.  String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)		
	}
	private String tableCatalog,tableSchema,tableName,tableType,remarks,typeCatalog,typeSchema,typeName,selfReferencesColumnName,referenceGeneration;
	public Table(ResultSet rs){
		try {
			tableCatalog = rs.getString(Field.TABLE_CAT.ordinal());
			tableSchema = rs.getString(Field.TABLE_SCHEM.ordinal());
			tableName = rs.getString(Field.TABLE_NAME.ordinal());
			tableType = rs.getString(Field.TABLE_TYPE.ordinal());
			remarks = rs.getString(Field.REMARKS.ordinal());
			/*
			typeCatalog = NOT_PROVIDED;
			typeSchema = NOT_PROVIDED;
			typeName = NOT_PROVIDED;
			selfReferencesColumnName = NOT_PROVIDED;
			referenceGeneration = NOT_PROVIDED;
			 
			 * add database and driver conditionals here.
			 * 
			typeCatalog = rs.getString(Field.TYPE_CAT.ordinal()); 
			typeSchema = rs.getString(Field.TYPE_SCHEM.ordinal());
			typeName = rs.getString(Field.TYPE_NAME.ordinal());
			selfReferencesColumnName = rs.getString(Field.SELF_REFERENCING_COL_NAME.ordinal());
			referenceGeneration = rs.getString(Field.REF_GENERATION.ordinal());		
			*/	
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
	public String getTableType() {
		return tableType;
	}
	public String getRemarks() {
		return remarks;
	}
	public String getTypeCatalog() {
		return typeCatalog;
	}
	public String getTypeSchema() {
		return typeSchema;
	}
	public String getTypeName() {
		return typeName;
	}
	public String getSelfReferencesColumnName() {
		return selfReferencesColumnName;
	}
	public String getReferenceGeneration() {
		return referenceGeneration;
	}
	@Override
	public String toString() {
		return "Table ["
				+"tableCatalog=" + tableCatalog 
				+ ", tableSchema="+ tableSchema 
				+ ", tableName=" + tableName 
				+ ", tableType=" + tableType 
				+ ", remarks=" + remarks 
				+ ((tableCatalog!=null)? ", typeCatalog="	+ typeCatalog:"") 
				+ ((typeSchema!=null)?", typeSchema=" + typeSchema:"") 
				+ ((typeName!=null)?", typeName="	+ typeName:"") 
				+ ((selfReferencesColumnName!=null)?", selfReferencesColumnName=" + selfReferencesColumnName:"") 
				+ ((referenceGeneration!=null)?", referenceGeneration=" + referenceGeneration:"") 
				+ "]";
	}
	
	
}
