package jhg.ddl;



import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

/**
 * Adaptor to the DDL Utils from Apache.
 * 
 * @author jgagon
 *
 */
public class DdlUtil {


	public static void main(String[] args){
		initDataSource();
		
	}

	public static void initDataSource(){
		Jdbc3PoolingDataSource source = new Jdbc3PoolingDataSource();
		source.setDataSourceName("DataMart New PG 9.3");
		source.setServerName("localhost");
		source.setDatabaseName("data_mart");
		source.setUser("app_benchmarking");
		source.setPassword("B3nchM4rK_away!");
		source.setMaxConnections(10);
		try {
			new InitialContext().rebind("DataSource", source);
		} catch (NamingException e) {
			e.printStackTrace();
		}		
	}
	public static DataSource getDataSource(){
		DataSource source = null;
		try {
		    source = (DataSource)new InitialContext().lookup("DataSource");
		} catch (NamingException e) {
		   e.printStackTrace();
		} finally {
			
		}	
		return source;
	}


	
}
