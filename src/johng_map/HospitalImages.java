package johng_map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;

import org.json.*;


public class HospitalImages {
	private static List<String> nofilelist = Arrays.asList((new String[] {""}));//"1932472255","1629243001","1649368523","1295756948","1326041633","1164514741","1447363270","1285662981","1225066327","1700805157","1093748642","1093748642","1659465094","1619941333","1144251059",
	
	private static List<Hospital> hospitals = new ArrayList<Hospital>();

	public static String findImage(String s,int index){
		System.out.println("  findImage: '"+s+"' index: "+index);
		String imageUrl = null;
		
        try{
        	String encodedString = URLEncoder.encode(s, "UTF-8");
            URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+encodedString);
            URLConnection connection = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String output = builder.toString();
            System.out.println("Response:"+output);
            JSONObject json = new JSONObject(output);
            //System.out.println(json.toString(2));
            imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(index).getString("url");
            System.out.print(" Image URL: '"+imageUrl+"', ");
        } catch(Exception e){
            //e.printStackTrace();
        	System.out.print(" - Failed to find:"+e.getMessage());
        }	
        return imageUrl;
	}
	
	public static BufferedImage readImage(String imageUrl){
		BufferedImage image = null;
		
		HttpURLConnection con = null;
		InputStream in = null;
		try {
			URL url = new URL(imageUrl);
			con = (HttpURLConnection)url.openConnection();
		    con.setConnectTimeout(10000);
		    con.setReadTimeout(10000);
		    in = con.getInputStream();
		    image = ImageIO.read(in);
		    //image = ImageIO.read(new URL(imageUrl));
		    if (image != null) {
		    	System.out.print("Loaded, ");
		    } else {
		    	System.out.print("Could not load ");
	        }
		} catch (IOException ex) {
			System.out.print(" - Failed to read:"+ex.getMessage()+":"+imageUrl);
			//ex.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(IOException iox) {
					System.out.print("Couldn't close connection, ");
				}
			}
			if(con != null) {
				con.disconnect();
			}
		}		

		
		return image;
	}
	
	public static void writeToFile(BufferedImage bi, String ext, String outfilename){
		File outputfile = new File(outfilename);
		try {
			ImageIO.write(bi,ext,outputfile);
		} catch (IOException e) {
			System.out.print(" - Failed to write:"+outfilename+":"+e.getMessage());
			//e.printStackTrace();
		}
		System.out.print("Writing file ");
	}
	
	public static String getFileExtension(String url){
		//System.out.println("URL:"+url);
		String ext = "";
		int i = url.lastIndexOf(".");
		if(i>0){
			ext = url.substring(i+1);
		}
		return ext;
	}
	
	public static void readHospitals(){
		TextFile f = new TextFile("data/hospital/hospital.csv");
		//String content = f.getText();
		String[] lines = f.getLines();
		System.out.println("Found "+lines.length+" lines.");
		for(String line:lines){
			//System.out.println("Line:"+line);
			Hospital h = Hospital.parse(line, ";");
			if(h.isValid()){
				hospitals.add(h);
			}else{
				System.out.println("Line invalid.");
			}
		}		
	}		
	public static long getRandomWait(){
		double rv = 1000.0;
		//long factor = 1000L;//seconds
		double randomness = new Long("14000").doubleValue();
		rv = (Math.random() * randomness)+rv;
		return (long)rv;
	}
	
    public static void main(String[] args) {
    	System.out.println("Start.");
    	//TODO Search for image with criteria (format, minimum width, minimum length, min/max aspect ratio.)
    	//System.out.println("Long:"+getRandomWait());
    	
    	String outfolder = "data/hospital_images";
    	
    	String id = "1";
    	String searchString = "Godfather"; 
    	
    	readHospitals();
    	boolean x = true;
    	int i=1;
    	for(Hospital h:hospitals){
    		id = h.id.toString();
    		//if(nofilelist.contains(id)){
	    		System.out.print("\n"+i+":HospitalID:"+id);
	    		searchString = "Photo "+h.name +" "+h.address+" "+ h.city + ", " + h.state;
	    		try {
	    			long wait = getRandomWait();
	    			
	    			if(x){
	    				x=false;
	    			}else{
	    				System.out.print(" : (Waiting:"+wait+") : ");
	    				Thread.sleep(wait);
	    			}
				} catch (InterruptedException e) {
					//e.printStackTrace();
					System.out.println(" - Failed:"+e.getMessage());
				}
	        	System.out.print(" : Finding image search on: '"+searchString+"'.");
	    		findAndSaveImageForHospital(outfolder, id, searchString);
    		//}//if(nofilelist.contains(id))
    		i++;
    	}
    	System.out.println("\nDone.");
    }

	private static void findAndSaveImageForHospital(String outfolder, String id, String searchString) {
		
		int index = 0;
		String url = findImage(searchString,index);
    	if(url!=null){
			String ext = getFileExtension(url);
	    	BufferedImage image = null;
	    	while( (image=readImage(url))==null && index<=10){
	    		index++;
	    		url = findImage(searchString,index);
	    	}
	    	if(image!=null){
		    	String outFileName = (outfolder+"\\"+id+"."+ext);
		    	//System.out.println("Output file:'"+outFileName+"'");
		    	writeToFile(image, ext, outFileName);
		    	System.out.print(" Saving image found at:"+url);
	    	}else{
	    		System.out.print("Failed to find image at url for id:"+id);
	    	}
    	}else{
    		System.out.print("Failed to get url from findImage for id:"+id);
    	}
	}
}