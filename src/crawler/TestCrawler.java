package crawler;


import java.util.concurrent.*;

import jhg.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.*;

public class TestCrawler implements Runnable {

	
	private String urlString;
	
	public TestCrawler(String s) {
		this.urlString = s;
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
	private String read(URL url){
		String line = "NONE";
		try{
		    URLConnection connection = url.openConnection();
		    StringBuilder builder = new StringBuilder();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		    while((line = reader.readLine()) != null) {
		        builder.append(line);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	    return line;
	}
	
	@Override
	public void run(){
		URL url = valid(urlString);
		String page = read(url);
		
	}
	
	public void stop(){
		
	}
	

	
	public static void main(String[] args){
		int hour = 13;
		int min = 0;
		String urlStr = "http://www.google.com";
		Log.println("Starting");
		LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("America/New York");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNext5 ;
        zonedNext5 = zonedNow.withHour(hour).withMinute(min).withSecond(0);
        if(zonedNow.compareTo(zonedNext5) > 0)
            zonedNext5 = zonedNext5.plusDays(1);

        Duration duration = Duration.between(zonedNow, zonedNext5);
        long initalDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);            
        scheduler.scheduleAtFixedRate(new TestCrawler(urlStr), initalDelay,
                                      24*60*60, TimeUnit.SECONDS);
		 
	}

}
