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
	
	FileScanner(FileEntry anEntry){
		this.report = new ScanReport();
		this.entry = anEntry;
		this.identity  = entry.getIdentity();
		this.rules = identity.getRules();
		this.layout = identity.getLayout();		
	}
	public ScanReport getReport(){
		return report;
	}	
	

	
	public void scan(){
		

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
		    while ((line = reader.readLine()) != null && (lineLimit==-1 || lineNo <= lineLimit)) {
		        processLine(lineNo,line);
		        lineNo++;
		    }
		} catch (IOException ioe) {
		    // do something, probably not a text file
		    ioe.printStackTrace();
		}
	}	
	private void processLine(int lineNo,String s){
		for(ScanRule rule:rules){
			boolean pass = rule.check(s);
			if(!pass){
				report.recordFail(lineNo,rule);
			}
		}
	}
	
	
}
/*
private boolean isRandom(){
	return false;//TODO impl "is Random" checked.
}
*/