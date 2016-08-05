package jhg.monitoring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Monitor {

	
	public static void main(String[] args){
		Map<String,String> websiteFoundTokenMap = new Hashtable<String,String>();
		
		websiteFoundTokenMap.put("https://chp-pidprod01.corp.chpinfo.com/idp/startSSO.ping?PartnerSpId=https://sso.chpinfo.com&TargetResource=https%3A%2F%2Fwww.chpmarketquest.com%2Fportal%2FSSOServlet","MarketQuest is a highly secure platform");
		
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		for(String website:websiteFoundTokenMap.keySet()){
			HttpGet httpGet = new HttpGet(website);
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				
				try {
					entity.writeTo(os);
					String contentString = new String(os.toByteArray());
					String qContent = websiteFoundTokenMap.get(website);
					if(contentString.indexOf(qContent)!=-1){
						System.out.println("Found: "+qContent);
					}else{
						System.out.println("Not Found: "+qContent);
					}
					//System.out.println(contentString);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(response!=null){
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						response = null;
					}
				}
			}
		}
	}
	
}
