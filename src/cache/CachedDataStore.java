package cache;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.*;

import jhg.util.Log;

public class CachedDataStore {
	public static final long MB = 1024*1024;
	
	private Map<Table,Block> blocks;
	private List<Table> tables;
	private Long limit;
	private Long size;
	
	public CachedDataStore(Long limit) {
		this.limit = limit;
		this.size = 0L;
		this.tables = new ArrayList<Table>();
		blocks = new Hashtable<Table,Block>();

	}
	public List<Table> getTables(){
		return this.tables;
	}
	private static String pct(long l1, long l2){
		double x = l1/l2;
		String pattern = "{0,number,#.##%}";
		return MessageFormat.format(pattern, x);
	}
	public String report(){
		return "Cache ( "+size+" / "+limit+" ) remaining: "+(limit-size)+" "+pct(size,limit);
	}
	
	public Long getLimit(){
		return this.limit;
	}

	@SuppressWarnings({ "unchecked" })
	public List<List<Object>> getData(Table table){
		List<List<Object>> rv = new ArrayList<List<Object>>();
		try{
			Block b = blocks.get(table);
			byte[] bytes = b.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			GZIPInputStream gzipIn = new GZIPInputStream(bais);
			ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
			for(int i = 0; i < b.getCount(); i++){
				List<Object> rowData =	((List<Object>)objectIn.readObject());
				rv.add(rowData);
			}
			objectIn.close();	
		}catch(IOException|ClassNotFoundException e){
			e.printStackTrace();
		}
		return rv;
	}
	public void setTableCount(Table table, ResultSet rs) {
		try{
			if(rs.next()){
				int ct = rs.getInt(1);
				table.setCount(ct);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
	}	
	public void load(Table table, ResultSet rs) { // throws SQLException, CacheFullException, IOException
		

		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzipOut;
			ObjectOutputStream objectOut=null;
			gzipOut = new GZIPOutputStream(baos);
			objectOut = new ObjectOutputStream(gzipOut);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String fieldName = rsmd.getColumnName(i);
				Log.println("   "+fieldName);
			    Field f = new Field(fieldName);
			    table.addField(f);
			}
			
			int rowIndex = 0;
			int tableCount = table.getCount();
			
			Log.println("Results:");
			while(rs.next()){
				rowIndex++;
				reportProgress(rowIndex,tableCount,5);//1,2,4,5,10,20,25,50 %
				//report percentage progress
				List<Object> rowData = new ArrayList<Object>();
			    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			        rowData.add(rs.getObject(i));
			    }			
				objectOut.writeObject(rowData);
			}
			objectOut.close();
			byte[] bytes = baos.toByteArray();			
			long length = bytes.length;
			long leftover = limit - size;
			
			if(length>leftover){
				throw new CacheFullException("Cache full. Tried to add "+length+" to cache ("+size+"/"+limit+") with only "+leftover+" bytes left.");
			}else{
				tables.add(table);
				Block b = new Block(rowIndex);
						
				b.setBytes(bytes);
				blocks.put(table,b);	
				size += length;
				Log.println("Added "+rowIndex+ " rows.");
				Log.println(report());
			}
		}catch(CacheFullException|SQLException|IOException e){
			e.printStackTrace();
		}
		
		/*
		 * Read the resultset a row at a time.
		 * 
		 * read the pk field based on the metadata.
		 * read as string (?) and convert to byte[] for each field.
		 * 
		 * 
		 * 
		 * For each table is a block.
		 *
		 */
	}
	
	public void setTables(List<Table> tables2) {
		this.tables = tables2;
	}
	
	private static void reportProgress(int rowIndex, int tableCount, int i) {
		//if(100%i!=0){			throw new IllegalArgumentException("Valid values for i are: 1,2,4,5,10,20,25,50,100");		}
		int factor = (int)(100/i);
		int topCount = tableCount - (tableCount%factor);
		if(topCount%factor==rowIndex){
			pct((long)rowIndex,(long)topCount);
		}
		
		/*
		 * case 1         1,2,4,5,10,20,25,50 - if i is 1, 
		 * 										    tableCount%
		 * case 2         
		 * case 17        25                        tableCount%(100/i)  17%4 = 1.  topCount = tableCount-1 = 16 
		 *                                          4   (if topCount%(100/i)==rowIndex) message =  pct(rowIndex/topCount)
		 * case 50
		 */
	}
	public static void main(String[] args){
		for(int i=1;i<=17;i++){
			reportProgress(i,17,25);
		}
	}
	
}


/*
ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
GZIPInputStream gzipIn = new GZIPInputStream(bais);
ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
MyObject myObj1 = (MyObject) objectIn.readObject();
MyObject myObj2 = (MyObject) objectIn.readObject();
objectIn.close();
 */

/*
	public Map<Field,String> getRow(Table table, long id){
		Block block = blocks.get(table);
		List<Field> fields = table.getDesiredFields();
		Map<Field,String> data = new Hashtable<Field,String>();
		byte[][] rawdata = block.get(id);
		int i = 0;
		for(Field field:fields){
			byte[] rawval = rawdata[i];
			String value = new String(rawval);
			data.put(field,value);
			i++;
		}
		return data;
	}
*/