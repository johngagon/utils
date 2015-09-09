package cache;

//import java.net.InetAddress.Cache;
import java.text.MessageFormat;

/**
 * Serves as a groovy cache for zip byte[] objects
 * @author jgagon
 */

class ZipCache { /*
	public static final double MAX_SCORE = 1000.0
	public static final double MIN_SCORE = 0.01
	public static final double HIT_VALUE = 1.0
	public static final double INIT_VALUE = 50.0
	public static final double NEW_ENTRY_ALLOWANCE = 0.25 //Percentage
	
	private static final long MILLI = 1
	private static final long SECOND = 1000*MILLI
	private static final long MINUTE = 60*SECOND
	private static final long HOUR = 60*MINUTE
	private static final long DAY = 24*HOUR
	
	static def AGES = [(32*DAY):2, (8*DAY):4, (2*DAY):6, (12*HOUR):8, (3*HOUR):10, (45*MINUTE):12, (1*MINUTE):18, (1*SECOND):24, (1*MILLI):36]
	static def freeHeapMemory = Runtime.runtime.freeMemory()
	static def totalHeap= Runtime.runtime.totalMemory()
	static def maxHeap = Runtime.runtime.maxMemory()
	static def spareMemory = 1024*1024*10                 //10MB
	
	/**
	 * The entries stored in the cache by a key.
	 * 
	 * @author jgagon
	 *
	 *
	static class CacheEntry {
		
		byte[] zipbytes
		Date lastUse = new Date();
		double score = INIT_VALUE
		
		/**
		 * Change this to determine initial score. Can also use weight etc.
		 * @return
		 *
		def init(){
			score = INIT_VALUE;
		}
		
		/**
		 * Grab the size of stored bytes
		 * 
		 * @return
		 *
		def size() {
			zipbytes.length;
		}
		
		/**
		 * Update the date.
		 * 
		 * @return
		 *
		def touch(){
			lastUse = new Date();
		}
		
		/**
		 * Change this algorithm to determine hit value.
		 * 
		 * @return
		 *
		def hit(){
			if(score<MAX_SCORE-HIT_VALUE){
				score += HIT_VALUE;
			}
		}
		
		/**
		 * Change this algorithm to determine what factors determine rank
		 * 
		 * @return
		 *
		def stale(){
			Date now = new Date();
			def age = now.time - lastUse.time
			def fresh = true
			AGES.each{key,val->
				if(fresh && age>=key){
					score = score - (score/val);
					fresh = false
				}	
				
			}
			if(score<MIN_SCORE){
				score = MIN_SCORE
			}
			score
		}
		
		
		def stats(){
			"bytes:${size()}  lastUsed:${lastUse}     score: ${score}"
		}

	}
	
	private Map<String,CacheEntry> cache = [:];
	int cacheSize  = 1;
	int bytesUsing = 0;
	
	
	
	/**
	 * Initialize the cache
	 * 
	 * @param sizeInMb
	 * @return
	 *
	def initialize(int sizeInBytes){
		clear()
		return setCacheSize(sizeInBytes)
	}
	
	/**
	 * Give the max size of the cache.
	 * Determined by the current size and the free memory of the heap
	 * @return
	 *
	def maxSize(){
		 freeHeapMemory - spareMemory  
	}
		
	/**
	 * Provide the new entry allowance.
	 * It's the max size allowed an object after freespace is insufficient for new object.
	 * 
	 * @return
	 *
	def allowance(){
		cacheSize * NEW_ENTRY_ALLOWANCE
	}
	
	/**
	 * Give the remaining space
	 * @return
	 *
	def freeSpace(){
		cacheSize - bytesUsing
	}
	
	/**
	 * Set the size
	 * Override setter
	 * 
	 * @param newSize
	 * @return
	 *
	def setCacheSize(int newSize){
		boolean succeed = false
		if(newSize <= cacheSize){
			int amountToReduce = cacheSize - newSize
			evict(amountToReduce)
			succeed = true
		}else if(newSize<=maxSize()){
			cacheSize = newSize
			succeed = true
		}
		succeed
	}  

	/**
	 * Clear the cache.
	 * 	
	 * @return
	 *
	def clear(){
		bytesUsing=0
		cache = [:]
	}
	
	/**
	 * Gives the entry count.
	 * 
	 * @return
	 *
	def count(){
		cache.size()
	}


	/**
	 * Stores the entry, rescores, and updates cache stats 
	 * Aggressive evicting evicts up to the allownace even if there is enough freespace after a single eviction. 
	 * This does not use modest eviction which would calculate and check the freespace available per single eviction. 
	 * 
	 * @param key
	 * @param bytes
	 * @return
	 *
	def store(String key, byte[] bytes){
		rescore()
		boolean success = false
		if(bytes.length <= freeSpace()){
			makEntry(key,bytes)
			success = true
		}else if(bytes.length < allowance() ){
			evict(allowance()) 
			makEntry(key,bytes)
			success = true
		}else{
			log.debug "ZipCache Storage failed: bytes: ${bytes?.length} was >= allowance: ${allowance()} or  > freespace ${freeSpace()}"
			success = false
		}
		success
	}
	private def makEntry(String key, byte[] bytes){
		CacheEntry entry = new CacheEntry(zipbytes:bytes)
		entry.init();
		cache[key]=entry
		bytesUsing += bytes.length
	}
	
	/**
	 * Retrieve the cached object with the key you provide.
	 * @param key
	 * @return
	 *
	def retrieve(String key){
		rescore()
		CacheEntry entry = cache[key]
		entry.touch()
		entry.hit()
		entry.zipbytes
	}
	
	/**
	 * Gets the time this entry was last accessed in the cache without side effect.
	 * 
	 * @param key
	 * @return
	 *
	def entryLastUse(String key){
		return cache[key].lastUse
	}
	
	/**
	 * Gets the size of this cache item without side effect.
	 * 
	 * @param key
	 * @return
	 *
	def entrySize(String key){
		return cache[key].zipbytes.length
	}
	
	/**
	 * Get the score of a cache item without side effect.
	 * @param key
	 * @return
	 *
	def entryScore(String key){
		return cache[key].score
	}
	
	/**
	 * Check without using score.
	 * 
	 * @param key
	 * @return
	 *
	def has(String key){
		return cache[key] != null
	}
	
	/**
	 * Removes the entry manually reducing the cache.
	 * 
	 * @param key
	 * @return
	 *
	def remove(String key){
		CacheEntry entry = cache[key]
		bytesUsing = bytesUsing - entry.size()
		cache.remove(key);
	}
	
	/**
	 * Gives the list of keys
	 * 
	 * @return
	 *
	def keys(){
		return cache.keySet()
	}

	/**
	 * Loop several last use target dates and reduce scores by a percentage based on target date rank. done before every store or retrieve
	 *
	def rescore(){
		cache.each{key,val->
			val.stale()
		}
	}

	/**
	 * Get the lowest scoring cache entry.
	 * 
	 * @return
	 *
	def lowest(){
		double lowest = MAX_SCORE
		String lowestKey = null; 
		cache.each{key,val->
			if(val.score<lowest){
				lowest = val.score
				lowestKey = key
			}
		}
		lowestKey
	}
		
	/**
	 * By a certain amount based on score and size. takes a target size, a percentage, last use age or minimum score
	 *
	def evict(double amt){
		boolean success = false
		if(cache.size()!=0){
			while(amt > 0){
				String nextlowest = lowest()
				CacheEntry entry = cache[nextlowest]
				int length = entry.size()
				remove(nextlowest)
				amt -= length
			}
			success = true
		}
		success
	}   
			    
	def stats(){
		"Cache Stats: percent full|empty: ${formatPct(bytesUsing/cacheSize)}|${formatPct(freeSpace()/cacheSize)} ... size:${cacheSize} used:${bytesUsing} free:${freeSpace()} allowance:${allowance()}  entries:${count()} maxSize:${maxSize()}  "
	}
	
	def formatPct(def fraction){
		MessageFormat.format("{0,number,#.##%}", fraction)
	}
	
	/**
	 * Prints cache info.
	 * 
	 * @return
	 *
	def debug(){
		println "${stats()}"
		cache.each{key,val->
			println "${key} - ${val.stats()}"//bytes:${val.size()}  lastUsed:${val.lastUse}     score: ${val.score}"
		}
		println "-----"
	}
	
	/**
	 * Sample use case sample and hook in for unit testing.
	 * Deprecated for API use.
	 * 
	 * @deprecated
	 *
	static void proveCache(){
		//fill the cache
		//debug the cache
		
		/*
		 * Init tests:
		 *
		 * Over allocate: should fail by returning false
		 * Allocate normal: should succeed by returning true, inspect cache size should refelct allocation
		 *
		 * Store tests:
		 * Add a few objects: inspected cache should show objects and reasonable score
		 * Add an object that would require emptying:
		 * Add an object that goes over limit
		 * Reset the size to something higher.
		 * Add the object and it should allow it if it's under new object threshold.
		 * Inspection of cache should succeed
		 *
		 * Retrieve tests:
		 * Get an object from cache. Inspection should pass.
		 * Remove an object from the cache. Inspection of size should pass.
		 *
		 *
		 *
		 *
		ZipCache cache = new ZipCache()
		cache.debug()
		assert cache.initialize(200) //allowance 50
		cache.debug()
		
		String a = "a234512345123451234512345" //25 bytes
		String b = "b234512345123451234512345" //25 bytes
		String c = "c234512345123451234512345" //25 bytes
		String d = "d234512345123451234512345" //25 bytes
		String e = "e234512345123451234512345" //25 bytes
		String f = "f234512345123451234512345" //25 bytes
		String g = "g234512345123451234512345" //25 bytes
		String h = "h2345123451234512345123451234512345" //35 bytes
		
		//String i = "1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345" //100
		//String j = "12345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345" //175
		assert cache.store("A", a.bytes)
		assert cache.store("B", b.bytes)
		assert cache.store("C", c.bytes)
		assert cache.store("D", d.bytes)
		assert cache.store("E", e.bytes)
		assert cache.store("F", f.bytes)
		assert cache.store("G", g.bytes)
		assert cache.store("H", h.bytes)
		cache.debug()
		
	}
	
*/
}

