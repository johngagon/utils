package chp.datareceiving;

import java.util.*;

public class FileScanner {
	private ScanReport report;
	private FileEntry entry;
	
	FileScanner(FileEntry entry){
		
	}
	
	
	private boolean isRandom(){
		return false;//TODO impl
	}
	
	public void scan(){
		
		FileIdentity identity  = entry.getIdentity();
		List<ScanRule> rules = identity.getRules();
		DataLayout layout = identity.getLayout();
		
	}
	public ScanReport getReport(){
		//TODO impl
		return null;
	}
}
