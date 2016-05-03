package jhg.sql;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

import jhg.util.*;

@SuppressWarnings("rawtypes")
public class DatabaseUtil {

	private static Map<Integer,Class> m = new Hashtable<Integer,Class>();
	static {
		m.put(Types.ARRAY,Array.class);
		m.put(Types.BIGINT,Long.class);
		m.put(Types.BIT,Boolean.class);
		m.put(Types.BLOB,Blob.class);
		m.put(Types.BOOLEAN,Boolean.class);
		m.put(Types.CHAR,String.class);
		
		m.put(Types.VARBINARY, Byte[].class);
		m.put(Types.CLOB, Clob.class);
		
		m.put(Types.DATE, java.sql.Date.class);
		m.put(Types.DECIMAL, BigDecimal.class);
		
		m.put(Types.DOUBLE, Double.class);
		m.put(Types.FLOAT, Float.class);
		m.put(Types.INTEGER, Integer.class);
		m.put(Types.JAVA_OBJECT, Object.class);
		m.put(Types.LONGNVARCHAR, String.class);
		m.put(Types.LONGVARBINARY, Byte[].class);
		m.put(Types.LONGVARCHAR, String.class);
		m.put(Types.NCHAR, String.class);
		m.put(Types.NCLOB, NClob.class);
		m.put(Types.NULL, Object.class);
		m.put(Types.NUMERIC, BigDecimal.class);
		m.put(Types.NVARCHAR, String.class);
		m.put(Types.OTHER, Object.class);
		m.put(Types.REAL, Float.class);
		m.put(Types.REF, Ref.class);
		m.put(Types.REF_CURSOR, Ref.class);
		m.put(Types.SMALLINT, Short.class);
		m.put(Types.SQLXML, SQLXML.class);
		m.put(Types.STRUCT, Struct.class);
		m.put(Types.TIME, Time.class);
		m.put(Types.TIME_WITH_TIMEZONE, Time.class);
		m.put(Types.TIMESTAMP, Timestamp.class);
		m.put(Types.TIMESTAMP_WITH_TIMEZONE, Timestamp.class);
		m.put(Types.TINYINT, Byte.class);
		m.put(Types.VARBINARY, Byte[].class);
		m.put(Types.VARCHAR, String.class);		
		/*

		
		m.put(Types.ROW_ID, XXX.class);
		m.put(Types.DISTINCT, XXX.class);
		m.put(Types.DATALINK, XXX.class);
		*/
			//Types.BINARY,BIT,BLOB,BOOLEAN,CHAR,CLOB,DATALINK,DATE,DECIMAL,DISTINCT,DOUBLE,FLOAT,INTEGER,JAVA_OBJECT
			//LONGNVARCHAR,LONGVARBINARY,LONGVARCHAR,NCHAR,NCLOB,NULL,NUMERIC,NVARCHAR,OTHER,REAL,REF,REF_CURSOR,ROW_ID
			//SMALLINT,SQLXML,STRUCT,TIME,TIME_WITH_TIMEZONE,TIMESTAMP,TIMESTAMP_WITH_TIMEZONE,TINYINT,VARBINARY,VARCHAR
	}
	
	@SuppressWarnings("unchecked")
	public MultiType readResult(ResultSet rs, int columnIndex){
		MultiType mt = new MultiType();//default null
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int type = rsmd.getColumnType(columnIndex);
			if(!rs.wasNull()){
				mt = new MultiType(rs.getObject(columnIndex, m.get(type)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mt;
	}
	

	
	public static void main(String[] args){
		Log.println("Starting.");
		DBReader db = new DBReader(Database.SQLPROD);
		db.connect();
		Log.print(db.reportDatabaseProperties());
		db.close();
		Log.println("Ending.");
	}
}
