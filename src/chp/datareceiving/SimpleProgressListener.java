package chp.datareceiving;

import static java.lang.System.out;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SimpleProgressListener implements ProgressListener {

	public SimpleProgressListener() {
		
	}
	public void notify(long progress, long counter, long total, long startTime, long timeEstimate){
		long now = System.currentTimeMillis();
		out.println("##"
				+" PROGRESS: "+progress+"% "
				+"  Units: "+counter +"/"+total
				+"  Started: "+formatTimeInstant(startTime)
				+"  Now: "+formatTimeInstant(now)
				+"  Finish*:"+getFinishTime(startTime,timeEstimate)
				+"  (Dur: "+formatTimeMeasure(timeEstimate)+") "// (not elapsed, estimate)
				+"  Remaining*:"+formatTimeMeasure((startTime + timeEstimate)-now)
				+"");
	}
	
	
	public String getFinishTime(long start, long estimate){
		long end = start + estimate;
		return formatTimeInstant(end);
	}
	
	public String formatTimeInstant(long millis){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(millis);
		String shortTimeStr = sdf.format(date);
		return shortTimeStr;		
	}
	
	@SuppressWarnings("boxing")
	public String formatTimeMeasure(long millis){
		return String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
			);		
	}
}
