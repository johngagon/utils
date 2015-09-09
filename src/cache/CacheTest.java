package cache;

import java.sql.ResultSet;
import java.util.*;


public class CacheTest {

	private static String[] tableList = {"valuequest_modifier_long_desc","valuequest_mdc","valuequest_vq_carrier"
		,"valuequest_drg","valuequest_market_zip_code","valuequest_market_cost_model_glance"
		,"valuequest_market","valuequest_cpt_modifier","valuequest_cpt"
		,"valuequest_member_summary","valuequest_market_benchmark","valuequest_market_cost_model_four_bucket"
		,"valuequest_market_mdc","valuequest_market_cost_model","valuequest_market_drg"
		,"valuequest_market_cpt","valuequest_market_cpt_modifier"};
	private static Map<String,String[]> indexes = new Hashtable<String,String[]>();
	
	public static void main(String[] args){
		/*
		 * Years: 2012-2, 2013-1, 2013-2
		 */
		System.out.println("Starting.");

		indexes.put("valuequest_modifier_long_desc",new String[]{"modifier"});//," cq_year"," upload"
		indexes.put("valuequest_mdc",new String[]{"mdc_code"});
		indexes.put("valuequest_vq_carrier",new String[]{"ca_id"});
		
		indexes.put("valuequest_drg",new String[]{"drg_code"," cq_year"," upload"});
		indexes.put("valuequest_market_zip_code",new String[]{"market_code"," ca_id"," pc_id"," zip_code"," cq_year"," upload"," st_code"," data_type"," pdr_prod_id"});
		indexes.put("valuequest_market_cost_model_glance",new String[]{"market_code"," cm_code"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," st_code"," data_type"," pdr_prod_id"});
		
		indexes.put("valuequest_market",new String[]{"market_code"," ca_id"," pc_id"," cq_year"," upload"," data_type"," pdr_prod_id"});
		indexes.put("valuequest_cpt_modifier",new String[]{"modifier"," cq_year"," upload"});
		indexes.put("valuequest_cpt",new String[]{"cpt_code"," cq_year"," upload"});
		
		indexes.put("valuequest_member_summary",new String[]{"market_code"," ca_id"," pc_id"," cq_year"," upload"," normalization"," claims_analysis"," st_code"," ageband"," gender"," data_type"," pdr_prod_id"});
		indexes.put("valuequest_market_benchmark",new String[]{"market_code"," ca_id"," pc_id"," cq_year"," upload"," st_code"," benchmark_type"," benefit_category_id"," network"," pdr_prod_id"});
		indexes.put("valuequest_market_cost_model_four_bucket",new String[]{"market_code"," cm_code"," ca_id"," pc_id"," cq_year"," upload"," normalization"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"});
		
		indexes.put("valuequest_market_mdc",new String[]{"market_code"," mdc_code"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"});
		
		
		//Cache ( 13238654 / 268435456 ) remaining: 255 196 802
		indexes.put("valuequest_market_cost_model",new String[]{"market_code"," cm_code"," ca_id"," pc_id"," cq_year"," upload"," normalization"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"});
		
		//Cache ( 25257158 / 268435456 ) remaining: 243 178 298     8:30pm (started at 8:24pm)
		
		indexes.put("valuequest_market_drg",new String[]{"market_code"," drg_code"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"});

		// 8:52.... 
		
		indexes.put("valuequest_market_cpt",new String[]{"market_code"," cpt_code"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"});
		
		//
		
		indexes.put("valuequest_market_cpt_modifier",new String[]{"market_code"," cpt_code"," modifier"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"});;
		
		//
		
		
		//25257158
				

		
		
		
		long limit = 256 * CachedDataStore.MB;            //256 MB
		int year = 2013;
		int upload = 2;
		List<Table> tables = Table.from(tableList,indexes);
		
		CachedDataStore cds = new CachedDataStore(limit); //create one for year and upload.
		
		DBReader db = new DBReader();
		//DBReader db = new DBReader(connection data);//
		
		db.connect();
		
		
		if(db.isConnected()){
			
			//List<Table> tables = cds.getTables();// db.configure(tableList);
			
			for(Table table:tables){
				//System.out.println(table);
				
				//use a predictable ordering and index.
				
				//TODO add the filters in a kind of cycle set so as to make a unique block of the data.
				//we'll order by the pk fields and get a consistent ordering and use an index.
				
				//db.query("select count(*) from "+table+" where cq_year = '"+year+"' and upload = '"+upload+"' order by "+table.getIndexesAsCommaString());
				db.query("select count(*) from "+table.getName()+" where cq_year = '"+year+"' and upload = '"+upload+"' order by "+table.getIndexesAsCommaString());
				
				if(db.haveResult()){
					ResultSet rs = db.getResult();
					cds.setTableCount(table,db.getResult());
				}
						
				db.query("select * from "+table.getName()+" where cq_year = '"+year+"' and upload = '"+upload+"' order by "+table.getIndexesAsCommaString());
				if(db.haveResult()){  //order by the indexed field
					cds.load(table,db.getResult());
				}
				
			}
		}
		db.close();
		
		cds.setTables(tables);
		
		//debug(cds, tables);
		
		
		
		
		
		System.out.println("Finished.");		
		/*
		 * 1. name a data source and set some limits:
		 *     a. max size of cache.
		 *     b. max wait times (for result set)
		 *     
		 * 2. query desired data from configured list going in smallest first.
		 *    a. progress indication based on a preliminary count.
		 * 
		 * 3. stuff data into the cache - the cache will
		 * 4. wait for either a report of the memory in cache used or a CacheFullException 
		 * 5. add timer
		 */
		
	}

	private static void debug(CachedDataStore cds) {
		for(Table table:cds.getTables()){
			List<List<Object>> data = cds.getData(table);
			for(List<Object> rowData:data){
			    for (int colIndex = 0; colIndex < rowData.size(); colIndex++) {
			        String objType = "null";
			        String objString = "";
			        Object columnObject = rowData.get(colIndex);
			        if (columnObject != null) {
			            objString = columnObject.toString() + " ";
			            objType = columnObject.getClass().getName();
			        }
			        System.out.println(String.format("  %s: %s(%s)",
			                table.getFieldName(colIndex), objString, objType));
			    }		
			}
		}
	}

}
/*

1. Query the data (grab only what's needed) bits at a time.
2. Convert to bytes/compress and index in memory.
3. Determine the size of that byte[] (max). (pass or fail).
4. Try to query the in memory data, set up a schedule to read it.

Goal: fit all the records before maxing out eclipse.

--select count(*) from valuequest_market_cpt_modifier           --61,701,826
--select count(*) from valuequest_market_cpt                    --42,328,031
--select count(*) from valuequest_market_drg                    --10,079,482
--select count(*) from valuequest_market_cost_model              --1,224,819
--select count(*) from valuequest_market_mdc                       --387,846
--select count(*) from valuequest_market_cost_model_four_bucket    --234,255
--select count(*) from valuequest_market_benchmark                 --196,644
--select count(*) from valuequest_member_summary                   --127,167
--select count(*) from valuequest_cpt                               --41,569
--select count(*) from valuequest_cpt_modifier                      --41,569
--select count(*) from valuequest_market                            --41,569
--select count(*) from valuequest_market_cost_model_glance          --33,465
--select count(*) from valuequest_market_zip_code                   --14,723
--select count(*) from valuequest_drg                                --2,262
-----------------------------
--select count(*) from valuequest_vq_carrier                           --188
--select count(*) from valuequest_mdc                                   --87
--select count(*) from valuequest_modifier_long_desc                    --18

*/

/*
valuequest_modifier_long_desc,{"modifier"," cq_year"," upload"}
valuequest_mdc,{"mdc_code"," upload"," cq_year"}

valuequest_cpt,{"cpt_code"," cq_year"," upload"}
valuequest_cpt_modifier,{"modifier"," cq_year"," upload"}
valuequest_drg,{"drg_code"," cq_year"," upload"}
valuequest_market,{"market_code"," ca_id"," pc_id"," cq_year"," upload"," data_type"," pdr_prod_id"}
valuequest_market_benchmark,{"market_code"," ca_id"," pc_id"," cq_year"," upload"," st_code"," benchmark_type"," benefit_category_id"," network"," pdr_prod_id"}
valuequest_market_cost_model,{"market_code"," cm_code"," ca_id"," pc_id"," cq_year"," upload"," normalization"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"}
valuequest_market_cost_model_four_bucket,{"market_code"," cm_code"," ca_id"," pc_id"," cq_year"," upload"," normalization"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"}
valuequest_market_cost_model_glance,{"market_code"," cm_code"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," st_code"," data_type"," pdr_prod_id"}
valuequest_market_cpt,{"market_code"," cpt_code"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"}
valuequest_market_cpt_modifier,{"market_code"," cpt_code"," modifier"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"};
valuequest_market_drg,{"market_code"," drg_code"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"}
valuequest_market_mdc,{"market_code"," mdc_code"," ca_id"," pc_id"," cq_year"," upload"," claims_analysis"," network"," st_code"," data_type"," pdr_prod_id"}
valuequest_market_zip_code,{"market_code"," ca_id"," pc_id"," zip_code"," cq_year"," upload"," st_code"," data_type"," pdr_prod_id"}
valuequest_member_summary,{"market_code"," ca_id"," pc_id"," cq_year"," upload"," normalization"," claims_analysis"," st_code"," ageband"," gender"," data_type"," pdr_prod_id"}
valuequest_vq_carrier,{"ca_id"," cq_year"," upload"}

	
 */
/*
 *  cq_year, upload (are on all tables)
 *  
 *   
KEY: cq_year, upload

valuequest_data	cpt				PRIMARY KEY(cpt_code)
valuequest_data	cpt_modifier			PRIMARY KEY(modifier)
valuequest_data	drg				PRIMARY KEY(drg_code)
valuequest_data	mdc				PRIMARY KEY(mdc_code)
valuequest_data	modifier_long_desc		PRIMARY KEY(modifier)
valuequest_data	vq_carrier			PRIMARY KEY(ca_id)

KEY: market_code, ca_id, pc_id, pdr_prod_id
valuequest_data	market				PRIMARY KEY(data_type)   
valuequest_data market_benchmark		PRIMARY KEY(st_code, benchmark_type, benefit_category_id, network)

	KEY: data_type
	KEY: st_code
	valuequest_data	market_zip_code			PRIMARY KEY(zip_code)
		KEY: claims_analysis
		valuequest_data	market_cost_model_glance	PRIMARY KEY(cm_code) 
		valuequest_data	member_summary			PRIMARY KEY(normalization, ageband, gender)

			KEY: network
			valuequest_data	market_cost_model		PRIMARY KEY(cm_code, normalization)
			valuequest_data	market_cost_model_four_bucket	PRIMARY KEY(cm_code, normalization)
			valuequest_data	market_cpt			PRIMARY KEY(cpt_code)
			valuequest_data	market_drg			PRIMARY KEY(drg_code)
			valuequest_data	market_mdc			PRIMARY KEY(mdc_code)
			valuequest_data	market_cpt_modifier		PRIMARY KEY(cpt_code, modifier);
		

 *   
 */