package crawler;

//import java.util.*;
import java.net.*;
import java.util.concurrent.*;

import jhg.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.*;

public class TestCrawler implements Runnable {

	private String[] urls;
	
	
	public TestCrawler(String[] _urls) {
		
		this.urls = _urls;
	}
	
	private URL valid(String url){
		URL rv = null;
		try{
			rv = new URL(url);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rv;
	}
	
	@SuppressWarnings("unused")
	private int readResponseCode(URL url){
		int code = -1;
		try{
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			code = connection.getResponseCode();
		}catch(Exception e){
			e.printStackTrace();
		}
		return code;
	}
	
	@SuppressWarnings("unused")
	private String read(URL url){
		StringBuilder builder = new StringBuilder();
		
		try{
		    URLConnection connection = url.openConnection();
		    String line = null;
		    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		    while((line = reader.readLine()) != null) {
		        builder.append(line);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	    return builder.toString();
	}
	
	@Override
	public void run(){
		for(String urlString:urls){
			URL url = valid(urlString);
			//String page = read(url);
			int code = readResponseCode(url);
			System.out.println(code);
		}
	}
	
	public void stop(){
		
	}
	
	public static void main(String[] args){
		int hour = 14;
		int min = 47;
		String controlURL = "http://www.google.com";//control
		
		String devMQURL = "https://chp-pidspdev01.corp.chpinfo.com/idp/startSSO.ping?PartnerSpId=https://ssodev.chpinfo.com&TargetResource=http%3A%2F%2Ftest.chpmarketquest.com%2Fportal%2FSSOServlet";
		//-1		javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
		
		String[] urls = {controlURL,devMQURL};
		
		Log.println("Starting");
		LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("America/New_York");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNext5 ;
        zonedNext5 = zonedNow.withHour(hour).withMinute(min).withSecond(0);
        if(zonedNow.compareTo(zonedNext5) > 0)
            zonedNext5 = zonedNext5.plusDays(1);

        Duration duration = Duration.between(zonedNow, zonedNext5);
        long initalDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);            
        scheduler.scheduleAtFixedRate(new TestCrawler(urls), initalDelay,
                                      24*60*60, TimeUnit.SECONDS);
		 
	}

}
