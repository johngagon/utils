package chp.datareceiving;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.StandardWatchEventKinds.*;
//import static java.nio.file.LinkOption.*;

public class ReceiverDaemon {

	
	private Path path;
	
	private List<FileIdentity> identities;
	private FileIntakeDirectory directory;
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    //private final boolean recursive;
    private boolean trace = false;
    
	
    /**
     * Creates a WatchService and registers the given directory
     */
	ReceiverDaemon(Path dir) throws IOException {
		this.path = dir;
		
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        //this.recursive = false;
        this.directory = new FileIntakeDirectory();
        this.identities = new ArrayList<FileIdentity>();
        //if (recursive) {
        //   System.out.format("Scanning %s ...\n", dir);
        //    registerAll(dir);
        //    System.out.println("Done.");
        //} else {
            register(path);
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
				entry.setReport(rpt);
				if(rpt.pass()){
					entry.pass();
				}else{
					entry.fail();
				}
				
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
			
			FileIdentity aventionIdentity = new FileIdentity("Avention File");  //TODO make File identity parsable from configuration
			aventionIdentity.nameMatchRule("Avention.*");
			aventionIdentity.setZip(true);
			
			aventionIdentity.setDataLayout(DataLayout.CSV);
			//ScanRule columnCountRule = new FieldCountRule(156);//FAIL
			ScanRule columnCountRule = new FieldCountRule(157);//PASS
			
			//FIRST RUN int[] requiredIndexes = {0,1,2,5,6,7,8,9,10,13,15,16,17,18,19,20,21,22,23,24,25,36,37,38,39,40,41,42,44,45,46,47,51,53,54,86,87,88,100};
			//SECOND RUN int[] requiredIndexes = {0,1,2,5,7,9,10,15,16,17,18,19,20,21,22,23,24,25,36,37,38,39,40,41,42,44,45,46,47,86,87,88,100};
			//THIRD RUN int[] requiredIndexes = {0,1,2,5,7,9,10,15,16,17,18,19,20,21,22,23,24,25,36,37,38,100};
			//FOURTH RUN int[] requiredIndexes = {0,1,2,5,7,9,10,36,37};
			int[] requiredIndexes = {0,1,9,10,37};//int,text,text,text,text
			//FAIL CASE (includes 3) has lots of nulls: int[] requiredIndexes = {0,1,2,3,5,6,7,8,9,10,13,15,16,17,18,19,20,21,22,23,24,25,36,37,38,39,40,41,42,44,45,46,47,51,53,54,86,87,88,100};
			ScanRule requiredRule = new RequiredRule(requiredIndexes);

			int[] numericIndexes = {0};
			ScanRule numericRule = new PatternRule(numericIndexes,PatternRule.NUMBER);
			
			
			String header = "KeyID\tCompanyName\tAddress1\tAddress2\tAddress3\tCity\tStateOrProvinceAbbrev\tPostalCode\tCounty\tCountryName\tCountryISO2\tPhone\tFax\tPrimaryURL\tEmployees\tIndustryGroupName\tIndustrySectorName\tOS2010IndustryName\tPrimaryNAIC\tPrimaryNAICDesc\tPrimaryUSSIC\tPrimaryUSSICDesc\tPrimaryUK2007SIC\tPrimaryUK2007SICDesc\tPrimaryNAICS2012\tPrimaryNAICS2012Desc\tCurrencyISO3\tCurrencyName\tSalesUSD\tSalesGBP\tSalesEUR\tSales\tAssetsUSD\tAssetsGBP\tAssetsEUR\tAssets\tOwnershipType\tEntityType\tBusinessDescription\tParentKeyID\tParentName\tParentCountry\tParentCountryID\tParentDuns\tUltimateParentKeyID\tUltimateParentName\tUltimateParentCountry\tUltimateParentCountryID\tUltimateParentDuns\tTickerExchange\tTickerSymbol\tAbiNumber\tRegNo\tCreditRatingUS\tCreditNumericScoreUS\tCreditLimitUS\tCreditFlagUS\tCreditLimitUK\tCreditLimitCurrency\tCreditLimitDate\tCreditRatingDate\tCreditRatingUK\tCreditRatingUKIndex\tCreditRatingDescr\tSales1YearGrowth\tTotalAssets1YrGrowth\tNetIncome1YrGrowth\tPreTaxProfit\tPreTaxProfitUSD\tPreTaxProfitGBP\tPreTaxProfitEUR\tNetIncome\tOperatingMargin\tWorkingCapital\tCurrentAssets\tFixedAssets\tCurrentLiabilities\tTotalLiabilities\tTotalLiabilitiesUSD\tTotalLiabilitiesGBP\tTotalLiabilitiesEUR\tLongTermDebt\tYearFounded\tMonthFounded\tDayFounded\tFortune1000Ranking\tDomesticUltimateParentKeyID\tDomesticUltimateParentCompanyName\tDomesticUltimateParentCountry\tDomesticUltimateParentDuns\tPercentSalesGrowth3Year\tPercentSalesGrowth5Year\tPercentEmployeesGrowth3Year\tPercentEmployeesGrowth5Year\tEmployeesHereCount\tParentEmployeeCount\tUltimateParentEmployeeCount\tCompanyLinkedIn\tInactiveCompanyFlag\tDunsNumber\tDedupID1\tFirstName1\tMiddleName1\tLastName1\tPrefix1\tSuffix1\tExecutiveTitle1\tOSFunctionIDs1\tOSFunctionNames1\tLevelName1\tEmail1\tDedupID2\tFirstName2\tMiddleName2\tLastName2\tPrefix2\tSuffix2\tExecutiveTitle2\tOSFunctionIDs2\tOSFunctionNames2\tLevelName2\tEmail2\tDedupID3\tFirstName3\tMiddleName3\tLastName3\tPrefix3\tSuffix3\tExecutiveTitle3\tOSFunctionIDs3\tOSFunctionNames3\tLevelName3\tEmail3\tDedupID4\tFirstName4\tMiddleName4\tLastName4\tPrefix4\tSuffix4\tExecutiveTitle4\tOSFunctionIDs4\tOSFunctionNames4\tLevelName4\tEmail4\tDedupID5\tFirstName5\tMiddleName5\tLastName5\tPrefix5\tSuffix5\tExecutiveTitle5\tOSFunctionIDs5\tOSFunctionNames5\tLevelName5\tEmail5\tAV_affiliateID1\tAV_Affiliatetype1";
			//TODO Email report: should describe each test run.
			
			aventionIdentity.add(columnCountRule);
			aventionIdentity.add(requiredRule);
			aventionIdentity.add(numericRule);
			aventionIdentity.addHeader(header);
			
			
			
			daemon.registerFileIdentity(aventionIdentity);
			
			daemon.start();
		}
		System.out.println("Finished");
		//FIXME - email: Scan passed but getting fail inside.

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