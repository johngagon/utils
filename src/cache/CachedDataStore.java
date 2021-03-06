package cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import jhg.util.Log;

@SuppressWarnings("boxing")
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

	public String report(){
		long freemem = Runtime.getRuntime().freeMemory();
		long remaining = (limit-size);
		return "Cache ( "+size+" / "+limit+" ) remaining: "+remaining+" "+pct(size,limit)+ " Free Heap:"+freemem+" Heap-remining:"+(freemem-remaining)+". ";
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
				Log.print("   ,"+fieldName);
			    Field f = new Field(fieldName);
			    table.addField(f);
			}
			Log.println("");
			int rowIndex = 0;
			int tableCount = table.getCount();
			final int FIVE_PCT = 5;
			//Log.println("\nResults:");
			while(rs.next()){
				rowIndex++;
				int progress = reportProgress(rowIndex,tableCount,FIVE_PCT);//1,2,4,5,10,20,25,50 %
				if(progress!=-1){
					Log.print(progress+"% ");
				}
				
				List<Object> rowData = new ArrayList<Object>();
			    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			        rowData.add(rs.getObject(i));
			    }			
				objectOut.writeObject(rowData);
			}
			Log.println("");
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
				Log.println("Added "+rowIndex+ " rows. Used:"+length+" bytes.");
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

	
	public static String pct(long l1, long l2){
		long l3 = (long)(100*l1)/l2;
		return l3+"%";
	}	
	
	/**
	 * Returns a progress value if the rowIndex hits a milestone based on the increment and total count given.
	 * 
	 * @param index    The current row, check and see if need to report.
	 * @param totalCount  Any positive value, the 100/increment should be less than this value.
	 * @param increment   Valid values: 1,2,4,5,10,20,25,50,100
	 * @return  -1 if not at increment, a multiple of increment i if progress milestone hit.
	 */
	public static int reportProgress(int index, int totalCount, int increment) {
		int[] valid = {1,2,4,5,10,20,25,50,100};
		boolean found = false;
		for(int v:valid){
			if(increment==v){
				found=true;break;
			}
		}
		if(!found){
			throw new IllegalArgumentException("Valid values for i are: 1,2,4,5,10,20,25,50,100");
		}
		
		//Log.print("-increment(original):"+increment);
		int factor = (int)(100/increment);                          
		while(factor>totalCount){
			switch(increment){
				case 1: increment=2;break;
				case 2: increment=4;break;
				case 4: increment=5;break;
				case 5: increment=10;break;
				case 10: increment=20;break;
				case 20: increment=25;break;
				case 25: increment=50;break;
				case 50: increment=100;break;
				
			}
			factor = (int)(100/increment);
		}
		int topCount = totalCount - (totalCount%factor);
		
		int countFactor = (int)topCount/factor;
		/*
		Log.print(" -increment:"+increment);
		Log.print(" -Factor:"+factor);
		Log.print(" -Top count:"+topCount);
		Log.print(" -Count factor:"+countFactor);
		Log.println(" -RowIndex:"+rowIndex);
		*/
		int pct = -1;
		if(index%countFactor==0 && !(index>topCount)){   //   when rowCount is 5,10,15 or 20
			
			pct = (100*index)/topCount;
			//Log.println(pct+"%");
		}
		
		/*
		 * case 1         1,2,4,5,10,20,25,50 - if i is 1, 
		 * 										    tableCount%
		 * case 2         
		 * case 17        25                        tableCount%(100/i)  17%4 = 1.  topCount = tableCount-1 = 16 
		 *                                          4   (if topCount%(100/i)==rowIndex) message =  pct(rowIndex/topCount)
		 *                                          
		 * case 50
		 */
		return pct;
	}
	
	public static void main(String[] args){
		int topCount = 3423;
		int milestone = 5;
		Log.println("Starting");
		for(int i=1;i<=topCount;i++){
			int progress=reportProgress(i,topCount,milestone);
			if(progress!=-1){
				Log.println(progress+"%");
			}
		}
		Log.println("Finished");
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