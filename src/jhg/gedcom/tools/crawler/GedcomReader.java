package jhg.gedcom.tools.crawler;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jhg.util.Log;
import jhg.util.TextFile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("unused")
public class GedcomReader {

	private static final double SIMILARITY_THRESHOLD = 0.65;
	private static final DecimalFormat FORMAT = new DecimalFormat("#.#");
	private static final String WAIT = "4500";//"14000";   //3275 * 9s x 3275 = 29,475s 491m or 8 hours  4.5s = 4h
	private static final String NAME = " NAME ";
	private static final int COMMON_THRESHOLD = 3;
	private static final int offset = 6;
	
	private static boolean TEST = false;
	

	private List<String> nameList;
	private List<Famous> famous;
	private List<String> testList;
	private List<String> tooCommonList;

	public GedcomReader(){
		nameList = new ArrayList<String>();
		testList = new ArrayList<String>();
		testList.add("Mary Bradbury");
		testList.add("Johnathan White");
		famous = new ArrayList<Famous>();
		tooCommonList = new ArrayList<String>();
	}
	
	public void add(String name){
		nameList.add(name);
	}
	
	
	public int getCount(){
		return nameList.size();
	}	
	
	public void search(){
    	boolean x = true;
    	
    	List<String> usingList = (TEST)?testList:nameList;
    	int i=0;
		for(String n:usingList){
			if(n.contains("Ancestry.com Family Trees")){
				continue;
			}
			if(n.contains("Gagon")){
				continue;
			}
			
			String searchStr = "wikipedia historical "+n;
    		try {
    			long wait = getRandomWait();
    			
    			if(x){
    				x=false;
    			}else{
    				//System.out.print(" : (Waiting:"+wait+") : ");
    				Thread.sleep(wait);
    			}
			} catch (InterruptedException e) {
				//e.printStackTrace();
				System.out.println(" - Failed:"+e.getMessage());
			}			
			search(i,n,searchStr);
			i++;
		}
	}
	
	private void search(int num, String name, String searchStr){
		Famous f = new Famous(name);
		String google = "http://www.google.com/search?q=";
		
		String charset = "UTF-8";
		String userAgent = "JohnGagon 1.0 "; // Change this to your company's name and bot homepage!
		try{
			Elements links = Jsoup.connect(google + URLEncoder.encode(searchStr, charset)).userAgent(userAgent).get().select("li.g>h3>a");
	
			for (Element link : links) {
			    String title = link.text();
			    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
			    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
	
			    if (!url.startsWith("http")) {
			        continue; // Ads/news/etc.
			    }
			    if(url.contains("https://en.wikipedia.org")){
			    	title = title.replaceAll("- Wikipedia, the free encyclopedia", "");
			    	title = title.replaceAll("File:", "");
			    	title = title.replaceAll(".jpg", "");
			    	title = title.replaceAll("\\(.*\\)", "");
			    	double score = StringSimilarity.similarity(name, title);
			    	if(score>SIMILARITY_THRESHOLD){
			    		f.add(title, url);
			    		//System.out.print("ADDED ");
			    	}
			    	//System.out.println(name+" :("+FORMAT.format(score)+"):  '" + title +"'       :: '" + url+"'");
			    }
			    
			}
			if(f.linkCount()>0){
				if(f.linkCount()>COMMON_THRESHOLD){
					tooCommonList.add(name);
					Log.println("COMMON:"+name);
				}else{
					famous.add(f);
					printFamous(num,f);
				}
			}else{
				Log.println("#"+num+":"+name);
			}
			//Log.println(name+": "+f.linkCount()+ " links.\n");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void printFamous(int num, Famous f){
		Log.println("\nFAMOUS #"+num+":");
		Log.println("Name:"+f.getName());
		List<Link> links = f.getLinks();
		for(Link l:links){
			Log.println("   Link: \""+l.title+"\" : "+l.url+" ");
		}
		Log.println("-------");
	}
	
	private void printLinks(){
		Log.println("Printing Links\n****************************\n");
		int i=0;
		for(Famous f:famous){
			printFamous(i,f);
			i++;
			//testpush
		}
	}


	public static long getRandomWait(){
		double rv = 1000.0;
		//long factor = 1000L;//seconds
		double randomness = new Long(WAIT).doubleValue();
		rv = (Math.random() * randomness)+rv;
		return (long)rv;
	}	
	
	
	public static void main(String[] args){
		//next step: compare birthdates, eliminate single first name
		try{
			Log.STORE = true;
			Log.filename = "data/famous.log";
			Log.println("Start");
			GedcomReader reader = new GedcomReader();
			readFile(reader);
			Log.println("Count: "+reader.getCount());
			reader.search();
			
			//reader.printLinks();
			Log.println("End");
		}finally{
			Log.flushToFile();
		}
	}
	
	private static void readFile(GedcomReader reader) {
		String fileName = "data/gedcom/jhg20151113.ged";
		TextFile file = new TextFile(fileName);
		String[] lines = file.getLines();
		for(String line:lines){
			if(line.contains(NAME)){
				String name = line.substring(line.indexOf(NAME)+offset,line.length()-1);
				name = name.replaceAll("/", "");
				//Log.println("Name:"+name);
				reader.add(name);
			}
		}
	}
	
	
}
