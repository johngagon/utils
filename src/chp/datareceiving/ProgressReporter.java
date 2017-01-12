package chp.datareceiving;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

//import jhg.util.Log;

public class ProgressReporter {

	public static enum Marker{
		PERCENT(1),                //leaving out 2,4,
		TICKS(5),
		TENTHS(10),
		FIFTHS(20),
		QUARTERS(25),
		HALVES(50),
		WHOLE(100);
		//FIFTY(2),
		//TWENTY-FIVE(4),
		
		
		private int value;
		private Marker(int val){
			this.value = val;
		}
		int increment(){
			return this.value;
		}
		
	}
	private long counter;
	private int increment;
	private long totalCount;
	private long progress;
	private long time1;
	private long time2;
	private long timeDiff;
	private long timeEstimate;
	private long timeElapsed;
	private long startTime;
	private List<ProgressListener> listeners;
	
	public ProgressReporter(int totalWork, Marker marker){
		out.println("ProgressReporter :: total: "+totalWork+"  , marker: "+marker.name()+" .");
		this.totalCount = totalWork;
		this.increment = marker.value;
		this.counter = 0; 
		this.listeners = new ArrayList<ProgressListener>();
		//this.listeners.add(new SimpleProgressListener());
		this.progress = -1;
		this.timeEstimate = 0L;
		this.timeElapsed = 0L;
		this.timeDiff = 0L;
		this.startTime = System.currentTimeMillis();
	}
	
	public long getEstimate(){
		return timeEstimate;
	}
	
	public long getElapsed(){
		return timeElapsed;
	}
	
	public long getTotalCount(){
		return totalCount;
	}
	public void report(){
		counter++;
		logProgress(counter);
	}

	public void completeWork(){
		counter++;
		reportProgress(counter);
		if(progress!=-1){
			notifyListeners();
		}
		
		if(counter==1){
			time1 = System.currentTimeMillis();
		}
		/*
		if(counter==2){
			time2 = System.currentTimeMillis();
			timeDiff = time2-time1;
			timeEstimate = timeDiff * totalCount;
		}
		if(timeEstimate!=0L){
			timeElapsed = counter*timeEstimate;
		}
		*/
		
	}
	public void completeWork(int chunk){
		for(int i=0;i<chunk;i++){
			completeWork();
		}
	}	
	private void notifyListeners() {
		for(ProgressListener pl:listeners){
			pl.notify(progress, counter, totalCount, startTime, timeEstimate);
		}
	}


	public void reportProgress(long index) {
		int factor = (int)(100/increment);                          
		while(factor>totalCount){
			switch(increment){
				case 1: increment=2;break;
				case 2: increment=4;break;
				case 4: increment=5;break;
				case 5: increment=10;break;
				case 10: increment=20;break;
				case 20: increment=25;break;
				case 25: increment=50;break;
				case 50: increment=100;break;
				
			}
			factor = (int)(100/increment);
		}
		long topCount = totalCount - (totalCount%factor);
		
		int countFactor = (int)topCount/factor;

		long pct = -1;
		if(index%countFactor==0 && !(index>topCount)){   //   when rowCount is 5,10,15 or 20
			
			pct = (100*index)/topCount;
			time2 = System.currentTimeMillis();
			timeDiff = time2-time1;
			long currentEstimate = timeDiff * factor;
			if(timeEstimate==0L){
				timeEstimate = currentEstimate; 
			}else{
				timeEstimate = (timeEstimate + currentEstimate)/2; //take average
			}
			time1 = time2;
		}

		this.progress = pct;		
	}
	
	public void logProgress(long i){
		if(progress!=-1){
			out.println(progress+"%  i:"+i);
		}			
	}

	public static void main(String[] args){
		ProgressReporter pr = new ProgressReporter(3423,Marker.TICKS);
		pr.addListener(new SimpleProgressListener());
		
		out.println("Starting");
		for(int i=1;i<=pr.getTotalCount();i++){
			pr.completeWork();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//pr.logProgress(i);
		}
		out.println("Finished");	
	}
	long getProgress(){
		return this.progress;
	}
	
	public void addListener(ProgressListener progressListener) {
		this.listeners.add(progressListener);
	}
	public void remListener(ProgressListener progressListener) {
		this.listeners.remove(progressListener);
	}
	public List<ProgressListener> getListeners(){
		return this.listeners;
	}
	
	
	
	
	
	
	@SuppressWarnings("unused")
	private static void statictest(){
		int workCount = 3423;
		int milestone = 5;
		out.println("Starting");
		for(long i=1;i<=workCount;i++){
			long progress=reportProgress(i,workCount,milestone);
			if(progress!=-1){
				out.println(progress+"%");
			}
		}
		out.println("Finished");		
	}
	
	/**
	 * Returns a progress value if the rowIndex hits a milestone based on the increment and total count given.
	 * 
	 * @param index    The current row, check and see if need to report.
	 * @param totalCount  Any positive value, the 100/increment should be less than this value.
	 * @param increment   Valid values: 1,2,4,5,10,20,25,50,100
	 * @return  -1 if not at increment, a multiple of increment i if progress milestone hit.
	 */
	public static long reportProgress(long index, long totalCount, int increment) {
		int[] valid = {1,2,4,5,10,20,25,50,100};
		boolean found = false;
		for(int v:valid){
			if(increment==v){
				found=true;break;
			}
		}
		if(!found){
			throw new IllegalArgumentException("Valid values for i are: 1,2,4,5,10,20,25,50,100");
		}
		
		//out.print("-increment(original):"+increment);
		long factor = (long)(100/increment);                          
		while(factor>totalCount){
			switch(increment){
				case 1: increment=2;break;
				case 2: increment=4;break;
				case 4: increment=5;break;
				case 5: increment=10;break;
				case 10: increment=20;break;
				case 20: increment=25;break;
				case 25: increment=50;break;
				case 50: increment=100;break;
				
			}
			factor = (int)(100/increment);
		}
		long topCount = totalCount - (totalCount%factor);
		
		long countFactor = (int)topCount/factor;
		/*
		out.print(" -increment:"+increment);
		out.print(" -Factor:"+factor);
		out.print(" -Top count:"+topCount);
		out.print(" -Count factor:"+countFactor);
		out.println(" -RowIndex:"+rowIndex);
		*/
		long pct = -1;
		if(index%countFactor==0 && !(index>topCount)){   //   when rowCount is 5,10,15 or 20
			
			pct = (100*index)/topCount;
			//out.println(pct+"%");
		}
		
		/*
		 * case 1         1,2,4,5,10,20,25,50 - if i is 1, 
		 * 										    tableCount%
		 * case 2         
		 * case 17        25                        tableCount%(100/i)  17%4 = 1.  topCount = tableCount-1 = 16 
		 *                                          4   (if topCount%(100/i)==rowIndex) message =  pct(rowIndex/topCount)
		 *                                          
		 * case 50
		 */
		return pct;
	}
		
}
