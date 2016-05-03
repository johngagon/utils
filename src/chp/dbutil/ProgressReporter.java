package chp.dbutil;

import jhg.util.Log;

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
	private int increment;
	private int totalCount;
	
	public ProgressReporter(int totalWork, Marker marker){
		this.totalCount = totalWork;
		this.increment = marker.value;
		 
	}
	public int getTotalCount(){
		return totalCount;
	}
	public int reportProgress(int index) {
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
		int topCount = totalCount - (totalCount%factor);
		
		int countFactor = (int)topCount/factor;

		int pct = -1;
		if(index%countFactor==0 && !(index>topCount)){   //   when rowCount is 5,10,15 or 20
			
			pct = (100*index)/topCount;
			
		}

		return pct;		
	}
	public void logProgress(int i){
		int progress=this.reportProgress(i);
		if(progress!=-1){
			Log.println(progress+"% :"+i+"/"+this.totalCount);
		}			
	}

	public static void main(String[] args){
		ProgressReporter pr = new ProgressReporter(3423,Marker.TICKS);
		Log.println("Starting");
		for(int i=1;i<=pr.getTotalCount();i++){
			pr.logProgress(i);
		}
		Log.println("Finished");	
	}
	
	@SuppressWarnings("unused")
	private static void testReportProgress(){
		ProgressReporter pr = new ProgressReporter(3423,Marker.TICKS);
		Log.println("Starting");
		for(int i=1;i<=pr.getTotalCount();i++){
			int progress=pr.reportProgress(i);
			if(progress!=-1){
				Log.println(progress+"%");
			}			
		}
		Log.println("Finished");			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unused")
	private static void statictest(){
		int workCount = 3423;
		int milestone = 5;
		Log.println("Starting");
		for(int i=1;i<=workCount;i++){
			int progress=reportProgress(i,workCount,milestone);
			if(progress!=-1){
				Log.println(progress+"%");
			}
		}
		Log.println("Finished");		
	}
	/**
	 * Returns a progress value if the rowIndex hits a milestone based on the increment and total count given.
	 * 
	 * @param index    The current row, check and see if need to report.
	 * @param totalCount  Any positive value, the 100/increment should be less than this value.
	 * @param increment   Valid values: 1,2,4,5,10,20,25,50,100
	 * @return  -1 if not at increment, a multiple of increment i if progress milestone hit.
	 */
	public static int reportProgress(int index, int totalCount, int increment) {
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
		
		//Log.print("-increment(original):"+increment);
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
		int topCount = totalCount - (totalCount%factor);
		
		int countFactor = (int)topCount/factor;
		/*
		Log.print(" -increment:"+increment);
		Log.print(" -Factor:"+factor);
		Log.print(" -Top count:"+topCount);
		Log.print(" -Count factor:"+countFactor);
		Log.println(" -RowIndex:"+rowIndex);
		*/
		int pct = -1;
		if(index%countFactor==0 && !(index>topCount)){   //   when rowCount is 5,10,15 or 20
			
			pct = (100*index)/topCount;
			//Log.println(pct+"%");
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
