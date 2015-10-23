package chp.dbutil;

import java.sql.*;
import java.io.*;

import chp.dbutil.ProgressReporter.Marker;

public class Test {


	
	public static void main(String[] args){
		
		String[] tables = {"employer.company","employer.company_branch","employer.company_sic","employer.du_employee_counts"};//
		String[] orders = {"duns_number asc","branch_duns asc","duns_number asc","domestic_ultimate_duns, carrier_group_code"}; 

		
		final String FDELIM = "|";
		final String RDELIM = "~\n";
			
		
		Log.println("Start");
		DBReader rdbms = new DBReader(Database.PGDEVOLD);
		rdbms.connect();
		try{
			int x = 0;
			for(String table:tables){
				String countsql = "select count(*) from "+table;
				ResultSet rs = rdbms.query(countsql);
				int rows = -1;
				if(rdbms.haveResult()){
					rows = rdbms.getCountResult();
				}
				rs.close();
				
				String sql = "select * from "+table+" order by "+orders[x];
				String filename = "C:/data/"+table+".ddf";
				rs = rdbms.query(sql);
				FileWriter fw = new FileWriter(filename);
				StringBuffer line = new StringBuffer();
				int count = rs.getMetaData().getColumnCount();
				
				ProgressReporter pr = new ProgressReporter(rows,Marker.PERCENT);
				Log.println("Records Count: "+rows+" , Columns Count: "+count);
				int j = 0;
				while(rs.next()){
					line = new StringBuffer();
					j++;

					for(int i=1;i<=count;i++){
						String val = rs.getString(i);
						line.append(val);
						if(i<count){
							line.append(FDELIM);
						}
					}
					pr.logProgress(j);
					line.append(RDELIM);
					
					fw.append(line.toString());
				}
				rs.close();
				Log.println("Wrote file: "+filename);
				fw.close();
				x++;
			}//for
		}
		catch(Exception e){
			e.printStackTrace();
		}
		rdbms.close();
		Log.println("End");
		
		/*
		 * File test
		 * 
		 * 1. count the data in the table. (set any automatic filters on the table, if the table has autofilters, prompt e.g.: year)
		 * 2. if the count is higher than a limit MAX on that table, read the top X (1000) rows and analyze, otherwise proceed 
		 *   a. analyze the data for values that are repeated in a column by doing a distinct count and group on each 
		 * 			look for:  
		 * 				groups where the top count comes in under the limit (for all the data)
		 * 				AND where the number of groups is under a parition limit P   (can be job wide)
		 * 			b. if successful, report and proceed, otherwise, report a failure.
		 * 			c. when proceeding, automatically generate a list of queries per values. add this one filter to the filename. 
		 * 3. (in Table), offer the option to map the fields. when this is done, a list of fields and their aliases are plugged in the select clause.
		 * 
		 * 4. store the data to a file per table    FILENAME: <table>_(<filtercol>_<filterval>_)<yyyyMMdd>.dat, compress the data
		 *         
		 * 
		 * Virtual Database - file memory cache test.
		 * 
		 * 1. Create presorts and pre-filters (based on queries).  (anytime the order by or where).
		 * 2. The data is assumed to be read only.
		 * 3. Based on the cache policy, the virtual database should load or store rows for each table and know where each is. (and which partition)
		 * 4. Be able to query one record, all records, 
		 * 
		 * Filter Group (in memory). is it in partition?
		 * 
		 * Alabama - [1,5,20,30...]    Row...id, partition nvp, bool cache. seek position 
		 * Alaska - [2,22,55...]
		 * 
		 * if in cache, grab bytes from map.
		 * if on disk, (partition), load the partition/file - try to random access using seeks
		 * if each row is in different partitions - this is slow without a seek point.
		 * 
		 *  Auto Imposed Query limits / truncate
		 * 
		 * Sort Index
		 * 
		 * (read the database sort field and id field in several passes and store the sort field and id in a map)
		 * 
		 * 
		 * partition - 
		 * 
		 * CachePolicy
		 * 		LoadEntireTable
		 * 		LoadMostRequested  - keeps a hit score for each row. 
		 * 				when row is read: hit++
		 * 		NoLoad		
		 * 
		 */
	}
	
}
