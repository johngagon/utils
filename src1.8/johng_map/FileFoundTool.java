package johng_map;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class FileFoundTool {

	private static List<Hospital> hospitals = new ArrayList<Hospital>();
	private static List<String> files = new ArrayList<String>();
	
	public static void readHospitals(){
		TextFile f = new TextFile("bdtc_hospital.txt");
		//String content = f.getText();
		String[] lines = f.getLines();
		for(String line:lines){
			Hospital h = Hospital.parse(line, ",");
			if(h.isValid()){
				hospitals.add(h);
			}
		}		
	}	
	
	public static void readFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	    	
	        if (fileEntry.isDirectory()) {
	            //listFilesForFolder(fileEntry);
	        	//System.out.println("File:"+fileEntry.getName()+" is a directory.");
	        } else {
	        	String name = fileEntry.getName();
	        	name = name.substring(0,name.lastIndexOf("."));
	            //System.out.println(name);
	            files.add(name);
	        }
	    }
	}
	
	public static void main(String[] args){
		String outfolder = "hospital_images";
		
		readHospitals();
		readFilesForFolder(new File(outfolder));
		
		
		for(Hospital hospital:hospitals){
			String id = hospital.id.toString();
			if(!files.contains(id)){
				System.out.println(id);
			}
		}
		
		
	}
	
	
}
