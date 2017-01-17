package chp.datareceiving;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.StandardWatchEventKinds.*;
//import static java.nio.file.LinkOption.*;

public class ReceiverDaemon {

	private Path path;
	private int interval;
	private List<FileIdentity> identities;
	private FileIntakeDirectory directory;
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final boolean recursive;
    private boolean trace = false;
    
	
    /**
     * Creates a WatchService and registers the given directory
     */
	ReceiverDaemon(Path dir) throws IOException {
		this.path = dir;
		
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = false;
        this.directory = new FileIntakeDirectory();
        this.identities = new ArrayList<FileIdentity>();
        //if (recursive) {
        //   System.out.format("Scanning %s ...\n", dir);
        //    registerAll(dir);
        //    System.out.println("Done.");
        //} else {
            register(dir);
        //}

        // enable trace after initial registration
        this.trace = true;
    }	


    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE,ENTRY_MODIFY);//, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

   


	
    /**
     * Process all events for keys queued to the watcher
     */
    @SuppressWarnings("rawtypes")
	void processEvents() {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("  %s: %s\n", event.kind().name(), child);
                
                onNewFileFound(child); //FIXME also put this in a thread.
                
                directory.print();
                
                System.out.println("\n\nResuming Directory Watch Daemon.");
                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                /*
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
                */
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

	private void start() {
		processEvents();
	}
	
	@SuppressWarnings("boxing")
	private void onNewFileFound(Path filename){
		//TODO cleanup output.
		////System.out.println("Found file: "+filename.toUri().toASCIIString());
		
		FileEntry entry = new FileEntry(filename,FileStatus.RECEIVED);
		directory.add(entry);
		
		FileIdentity fileIdentity = examineFile(filename);//TODO add checks for file extension.
		
		if(fileIdentity!=null){
			entry.identify(fileIdentity);//TODO add notifications
			
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
				entry.setReport(rpt);
				rpt.print();
			}else{
				entry.error();
			}
		}else{
			entry.unidentified();//sends notification.
		}
		
	}
	public FileIdentity examineFile(Path pathAndFile){
		String filename = pathAndFile.getFileName().toString(); 
		
		for(FileIdentity identity:identities){
			if(identity.matches(filename)){
				return identity;
			}
		}
		return null;
	}	

	private ScanReport scanZip(FileEntry entry) {
		FileScanner scanner = new FileScanner(entry);
		scanner.scan();
		return scanner.getReport();
	}

	private ScanReport scanText(FileEntry entry) {
		
		//FileScanner scanner = new FileScanner(entry);
		
		throw new UnsupportedOperationException("Not implemented yet!");
	}	
	
	public FileIntakeDirectory getDirectory(){
		return this.directory;
	}

	private static ReceiverDaemon newInstance(String aPath, int interval) {
		Path dir = Paths.get(aPath);
		ReceiverDaemon rv = null;
		try {
			rv = new ReceiverDaemon(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rv;
	}	
	



	private void registerFileIdentity(FileIdentity identity) {
		identities.add(identity);
	}

	/*
	 * data analyzer
	 * validation
	 * monitor for emails
	 * workflow?
	 */
	public static void main(String[] args){
		System.out.println("Starting Directory Watch Daemon\n");//TODO refactor name of app: Directory Watch Daemon
		String path = "C:\\intake\\";
		int interval = 50;//ms
		ReceiverDaemon daemon = ReceiverDaemon.newInstance(path, interval);//50ms check
		if(daemon!=null){
			
			FileIdentity aventionIdentity = new FileIdentity("Avention File");
			aventionIdentity.nameMatchRule("Avention.*");
			aventionIdentity.setZip(true);
			aventionIdentity.setDataLayout(DataLayout.CSV);
			//ScanRule columnCountRule = new FieldCountRule(156);//FAIL
			ScanRule columnCountRule = new FieldCountRule(157);//PASS
			aventionIdentity.add(columnCountRule);
			
			
			
			daemon.registerFileIdentity(aventionIdentity);
			
			daemon.start();
		}
		System.out.println("Finished");
		

	}

	
}
/**
 * Register the given directory, and all its sub-directories, with the
 * WatchService.
 
private void registerAll(final Path start) throws IOException {
    // register directory and sub-directories
    Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException
        {
            register(dir);
            return FileVisitResult.CONTINUE;
        }
    });
}*/
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