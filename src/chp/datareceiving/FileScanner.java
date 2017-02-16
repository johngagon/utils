package chp.datareceiving;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;

public class FileScanner {
	private ScanReport report;
	private FileEntry entry;
	private FileIdentity identity;
	private List<ScanRule> rules;
	private DataLayout layout;
	
	static final int ERROR_LIMIT = 100;
	
	FileScanner(FileEntry anEntry){
		
		this.entry = anEntry;
		this.report = anEntry.getReport();
		this.identity  = entry.getIdentity();
		this.rules = identity.getRules();
		this.layout = identity.getLayout();		
	}
	public ScanReport getReport(){
		return report;
	}	
	

	
	public void scan(){
		entry.setScanStart(new Date());
		if(DataLayout.CSV.equals(layout)){
			System.out.println("    Scan Start: CSV");
			readCSVFile();
			
		}else if(DataLayout.FIXED_WIDTH.equals(layout)){
			//TODO impl fixed width layout
			System.out.println("Fixed width");
		}else{
			//TODO handle lack of data layout.
			System.out.println("Data Layout not provided.");
		}
		entry.setScanEnd(new Date());
	}
	
	
	@SuppressWarnings("rawtypes")
	private void readCSVFile(){
		
		ZipFile zipFile = null;
		try{
			String zipname = entry.getFullPath().toString();
			//System.out.println("readCSVZip: "+zipname);
			zipFile = new ZipFile(zipname);
			for(Enumeration e = zipFile.entries(); e.hasMoreElements();){
				ZipEntry entry = (ZipEntry)e.nextElement();
				System.out.println("    Zip Entry: '"+entry.getName()+"'");
				if(!entry.isDirectory()){
					if(FilenameUtils.getExtension(entry.getName()).equals("txt")){
						long start = System.currentTimeMillis();
						final int lineLimit = -1;
						//processFileQuicker(zipFile, entry, lineLimit); //      110,     2581368
						//processFileFrugally(zipFile, entry, lineLimit);  // 378,      3201808
						processFile(zipFile, entry, lineLimit); //           83,     13656672
						
						long end =  System.currentTimeMillis();
						long dur = end-start;
						System.out.println("    Scan End: Duration - "+dur);
					    
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(zipFile!=null){
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					zipFile = null;
				}
			}
		}		
	}
	private void processFile(ZipFile zipFile, ZipEntry entry, int lineLimit) throws IOException {
		InputStream in = zipFile.getInputStream(entry);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		
		int lineNo = 1;
		
		try {
		    while ((line = reader.readLine()) != null && (lineLimit==-1 || lineNo <= lineLimit) && report.getTotalErrors()<ERROR_LIMIT) {
		        processLine(lineNo,line);
		        lineNo++;
		    }
		} catch (IOException ioe) {
		    // do something, probably not a text file
		    ioe.printStackTrace();
		}
	}	
	private void processLine(int lineNo,String s){
		
		//System.out.println("Processing line: "+lineNo);
		if(identity.hasHeader() && lineNo==1){
			//System.out.println("Header:'"+s+"'");
			return;//TODO optionally check header names against a standard list.
		}
		for(ScanRule rule:rules){
			boolean pass = rule.check(s);
			boolean passesField = true;
			
			if(!pass){
				report.recordFail(lineNo,rule);
				
			}
			if(pass && rule.doFieldCheck()){
				boolean[] passes = rule.fieldCheck(s);
				int i = 0;
				int[] indexes = rule.getIndexes();
				//int x = passes.length;
				//int y = indexes.length;
				for(boolean b:passes){
					passesField = passesField && b;
					if(!b){
						int index = indexes[i];
						int headersSize = identity.headers().length;
						report.recordFail(lineNo,index,identity.headers()[index],rule);
					}
					i++;
				}
				if(!passesField){
					
					System.out.println("  Failed "+lineNo+":"+s);
				}else{
					report.recordPass();
					//System.out.println("  Passed "+lineNo);
				}
			}
		}
	}
	
	
}
/*
private boolean isRandom(){
	return false;//TODO impl "is Random" checked.
}
*/