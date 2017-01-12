package chp.datareceiving;

import java.util.List;

public class ReceiverDaemon {

	
	private FileIntakeDirectory directory;
	
	public FileIdentity examineFile(String filename){
		return null;
	}
	
	private ReceiverDaemon(){
		super();
	}

	private void start() {
		
		String foundFile = "";
		//onNewFileFound(foundFile);
		
	}
	
	private void onNewFileFound(String filename){
		
		FileEntry entry = new FileEntry(filename,FileStatus.RECEIVED);
		directory.add(entry);
		
		FileIdentity fileIdentity = examineFile(filename);
		
		if(fileIdentity!=null){
			entry.identify(fileIdentity);//notifications
			ScanReport rpt = null;
			
			//Fork another asynch so receiver can watch other files while scanning several others. 
			entry.scanning();
			if(fileIdentity.isZip()){
				rpt = scanZip(entry);
			}else{
				rpt = scanText(entry);
			}
			if(rpt!=null){
				if(rpt.pass()){
					entry.pass();
				}else{
					entry.fail();
				}
			}else{
				entry.error();
			}
		}else{
			entry.unidentified();//sends notification.
		}
		
	}
	
	private ScanReport scanText(FileEntry entry) {
		// TODO Auto-generated method stub
		FileScanner scanner = new FileScanner(entry);
		return null;
	}

	private ScanReport scanZip(FileEntry entry) {
		// TODO Auto-generated method stub
		FileScanner scanner = new FileScanner(entry);
		return null;
	}

	public FileIntakeDirectory getDirectory(){
		return null;//TODO impl.
	}

	private static ReceiverDaemon newInstance(String path, int interval) {
		// TODO Auto-generated method stub
		return null;
	}	
	
	/*
	 * data analyzer
	 * validation
	 * monitor for emails
	 * workflow?
	 */
	public static void main(String[] args){
		String path = "C:\\intake\\";
		int interval = 50;//ms
		ReceiverDaemon daemon = ReceiverDaemon.newInstance(path, interval);//50ms check
		daemon.start();
		
		
		/*
		 * Directory to watch. interval.
		 * 
		 * Metadata of the file stored in little database. (file,date,status)  
		 * 
		 * 
		 * Identification
		 * 
		 * 
		 */
		
		/*
		 * 
		 * Load all rules files. (change of rules means restart program)
		 * 
		 * Thread watches directory.
		 * 
		 * Determines file type from name.
		 * 
		 * 
		 * Loads and parses csv using random access file reading.
		 * 
		 * Processes files using a validation tree
		 * 
		 * Logs to log
		 * 
		 * \file_type           filetypename.vps  (validation process script)
		 * 
		 * \receiving 
		 * \receiving           log.txt     file:success|fail   valid rows, invalid rows, total rows, file errors/summary message
		 * \receiving\processed file.csv    success 
		 * \receiving\invalid	file.csv
		 * \receiving\invalid   file.err    errors
		 * 
		 * Log and error file reader.
		 */
	}



	
}
