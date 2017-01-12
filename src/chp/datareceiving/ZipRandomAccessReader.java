package chp.datareceiving;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.*;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.channels.*;
import java.nio.*;

import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.io.*;

public class ZipRandomAccessReader {

	public static void main(String[] args){
		//466 MB Avention 10-11-16.zip
		
		  
		String dir = "C:\\intake\\";
		String filename = "Avention 10-11-16.zip";
		long startFreeMemory = Runtime.getRuntime().freeMemory();
		
		
		ZipFile zipFile = null;
		try{
			zipFile = new ZipFile(dir+filename);
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
						System.out.println("Duration: "+dur);
					    
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
		long endFreeMemory = Runtime.getRuntime().freeMemory();
		long diffFreeMemory = startFreeMemory - endFreeMemory;
		System.out.println("Used memory:"+ diffFreeMemory);
		/*
		 * buffered input stream. 
		 * loop through file entries and create streams
		 * log memory
		 */
	}
	
	private static void processFileQuicker(ZipFile zipFile, ZipEntry entry, int lineLimit) throws IOException {
		
		InputStream is  = zipFile.getInputStream(entry);
	    
		long currStart, currStop, currInter, oldPos;
	    boolean pastHeader = false, startedBlock = false;
	    int BUFF = 8 * 1024;
	    byte buffer[] = new byte[ BUFF ];
	    FastBufferedInputStream fbis = new FastBufferedInputStream( is, BUFF );
	    int lineNo = 1;
	    int l = 0;
	    while ( ( l = fbis.readLine( buffer ) ) != -1  && (lineLimit==-1 || lineNo <= lineLimit)) {
	    	processLine(lineNo,buffer);
	    	lineNo++;
	    }

	}		
	
	private static void processFileFrugally(ZipFile zipFile, ZipEntry entry, int lineLimit) throws IOException {
		InputStream in = zipFile.getInputStream(entry);
		Scanner sc = null;
		try {
		    sc = new Scanner(in, "UTF-8");
		    int lineNo = 1;
		    while (sc.hasNextLine() &&  (lineLimit==-1 || lineNo <= lineLimit)) {
		        String line = sc.nextLine();
		        processLine(lineNo,line);
		        lineNo++;
		    }
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (in != null) {
		        in.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}

	}	
	
	/*
	 * Dur: 4221         4523
	 * Mem: 31,607,312   20,525,256
	 */
	private static void processFile(ZipFile zipFile, ZipEntry entry, int lineLimit) throws IOException {
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

	private static void processLine(int lineNo,char[] s){
		System.out.println(lineNo+":"+String.valueOf(s));
	}

	
	
	private static void processLine(int lineNo,byte[] s){
		System.out.println(lineNo+":"+new String(s));
	}	
	
	private static void processLine(int lineNo,String s){
		System.out.println(lineNo+":"+s);
		//System.out.println(Runtime.getRuntime().freeMemory()+":"+s);
	}
	/*
	 * 2079447
		Duration:         38184   2GB 38s
		Used memory: -194421112
	 */
	
	/*
	 * super fast: java -server -XX:CompileThreshold=2 -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -Xmx1000m Test
	 * http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly#Conclusions
	 */
	
	
	
	
}
/*
	    	byte[] array = new byte[ 4096 ];
	    	long count = 0;
	    	int start, len;
	    	
	    	int lineNo = 1;
	    	
	    	for(;;) {
	    		start = 0;
	    		while( ( len = fbis.readLine( array, start, array.length - start, FastBufferedInputStream.ALL_TERMINATORS ) ) == array.length - start  && lineNo <= lineLimit) {
	    			start += len;
	    			//array = ByteArrays.grow( array, array.length + 1 );
	    			processLine(lineNo,array);
	    			array = new byte[4096];
	    			lineNo++;
	    		};
	    		
	    		if ( len != -1 ) count++;
	    		else break;
	    	}	    	
*/