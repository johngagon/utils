package chp.datareceiving;

public class ReceiverDaemon {

	
	
	public static void main(String[] args){
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