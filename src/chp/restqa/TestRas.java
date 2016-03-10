package chp.restqa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
//import org.apache.http.HttpResponse;


import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Log;



@SuppressWarnings("unused")
public class TestRas {

	
	private static final String DEV_FILTER_BASE = "http://chp-rasdev01:8080/reportserver/filter/valuequest/";
	private static final String DEV_EXECUTE_BASE = "http://chp-rasdev01:8080/reportserver/execute/valuequest/";
	private static final String STAGE_FILTER_BASE = "http://ras01stage:8080/reportserver/filter/valuequest/";
	private static final String STAGE_EXECUTE_BASE = "http://ras01stage:8080/reportserver/execute/valuequest/";

	private static final String FILTER_SUFFIX = "?user=900";

	
	public static void main(String[] args){
		testRas();
	}	
	
	public static void testConnection(){
		System.out.println("Starting testConnection.");
		DatabaseManager db = new DatabaseManager(Database.DMFMR);
		db.connect();
		if(db.isConnected()){
			//"select * from valuequest.vq_carrier"
			String q = "select distinct benchmark_type from valuequest.market_benchmark where ca_id not in (select filtered_carrier from valuequest.ref_plan_restrict_market_benchmark where carrier = 910)";
			db.query(q);
			if(db.haveResult()){
				db.printResult();
			}			
		}
		db.close();
		System.out.println("Finished testConnection.");		
	}
	
	public static void testRas(){
		String[] filters = {"benchmarkType",
				"carrier",
				"carrierName",
				"carrierOverlap",
				"carriersByState",
				"carrierSubproducts",
				"companyName",
				"competitiveStates",
				"cpt",
				"dataSource",
				"dataType",
				"drg",
				"duns",
				"fromType",
				"market",
				"marketDetails",
				"marketsByState",
				"marketsByZip",
				"marketSelection",
				"marketTransform",
				"marketType",
				"name",
				"network",
				"pc_id",
				"product",
				"productName",
				"stateCode",
				"states",
				"statesByZip",
				"subMarketByMarket",
				"subproduct",
				"toType",
				"upload",
				"uploadDate",
				"year",
				"zip",
				"zips",
				"zipsByMarket",
				"zipsByState"};
		
		
		for(String filter:filters){
			String url = DEV_FILTER_BASE + filter + FILTER_SUFFIX;
			Log.pl("\n"+url);
			//given
			HttpUriRequest req = new HttpGet(url);
			
			//when
			try {
				
				HttpResponse res = HttpClientBuilder.create().build().execute(req);
				
				int actualCode = res.getStatusLine().getStatusCode();
				
				if(HttpStatus.SC_OK==actualCode){
					
					//content type: text/xml
					//Log.pl("mime:"+res.getEntity().getContentType());\
					String xpath = "/filterResult/values/value";
					if(responseXmlHasData(xpath,res)){
						Log.pl("    "+filter+":PASS");
					}else{
						Log.pl("    "+filter+":FAIL");
					}
				}else{
					Log.pl("!!!   url:"+url+"  code:"+actualCode+ "  not ok.");
				}
				
				
			} catch (ClientProtocolException e1) {
				handle(e1);
			} catch (IOException e2) {
				handle(e2);
			}

			//then
			
			
			/*
			 * http://www.baeldung.com/2011/10/13/integration-testing-a-rest-api/
			 * 
            URLConnection connection = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String output = builder.toString();
			*/
			//test the service. 
			/*
			 * a. errors? 
			 * b. good response code.
			 * c. does it parse as a document
			 * d. is values element childless/empty?
			 * e. does the list compare to what's on stage?
			 */
			
		}//for(String filter:filters)
		
		String[] reports = {
				"benchmarkAverageReport",
				"benchmarkBestReport",
				"benchmarkReport",
				"costModelGlance",
				"costModelGlanceAll",
				"costModelReport",
				"cptModifierReport",
				"cptReport",
				"drgReport",
				"mdcReport"
		};
		
		for(String report:reports){
			
		}
		
		
	}//testRas()

	private static void handle(Exception e){
		Log.pl("!!!   Exception: "+e.getClass()+":"+e.getMessage());
	}
	
	private static boolean responseXmlHasData(String xpathStr, HttpResponse res) {
		//String xmlContent = EntityUtils.toString(entity);//Log.pl("Content:\n"+xmlContent);
		boolean rv = false;
		
		try {
			HttpEntity entity = res.getEntity();
			InputStream is = entity.getContent();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(is);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(xpathStr);	
			NodeList nl = (NodeList) expr.evaluate(doc,XPathConstants.NODESET);
			
			
			rv = nl.getLength()>0;
			if(!rv){
				Log.pl("!!!   NodeList length:"+nl.getLength());
			}
		} catch (UnsupportedOperationException e1) {
			handle(e1);
		} catch (IOException e2) {
			handle(e2);
		} catch (ParserConfigurationException e3) {
			handle(e3);
		} catch (SAXException e4) {
			handle(e4);
		} catch (XPathExpressionException e5) {
			handle(e5);
		}
		return rv;
	}
	
	/*
	 * suggested method names are long:

@Test
public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()

	 */

	
}


/*

"benchmarkType",
"carrier",
"carrierName",
"carrierOverlap",
"carriersByState",
"carrierSubproducts",
"companyName",
"competitiveStates",
"cpt",
"dataSource",
"dataType",
"drg",
"duns",
"fromType",
"market",
"marketDetails",
"marketsByState",
"marketsByZip",
"marketSelection",
"marketTransform",
"marketType",
"name",
"network",
"pc_id",
"product",
"productName",
"stateCode",
"states",
"statesByZip",
"subMarketByMarket",
"subproduct",
"toType",
"upload",
"uploadDate",
"year",
"zip",
"zips",
"zipsByMarket",
"zipsByState"
 

URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/uploadDate?user=900
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/carrierOverlap?user=900
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/states?year=2014&upload=1&user=900
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/zips?year=2014&upload=1&user=900
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/uploadDate?user=900
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/carrierOverlap?user=900
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/states?year=2014&upload=1&user=900
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/zips?year=2014&upload=1&user=900
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/carriersByState?user=900&stateCode=AK&year=2014&upload=1
URL: http://chp-rasdev01:8080/reportserver/filter/valuequest/marketsByState?stateCode=AK&marketType=25&year=2014&upload=1&product=19&dataType=null&carrier=111,1395&user=900
URL: http://chp-rasdev01:8080/reportserver/execute/valuequest/costModelReport?user=900&market=0202&stateCode=AK&marketType=20,25&product=19&subproduct=null&year=2014&upload=1&network=T&dataType=null&carrier=111,1395
URL: http://chp-rasdev01:8080/reportserver/execute/valuequest/costModelReport?user=900&market=0201&stateCode=AK&marketType=20,25&product=19&subproduct=null&year=2014&upload=1&network=T&dataType=null&carrier=111,1395
URL: http://localhost:8080/valuequest_core/grails/portlet/generate.dispatch
URL: 

 */
