package jhg.common.goal;

import jhg.common.units.Weight;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import chp.dbutil.Log;

public class TestGoal {

	
	
	public static void main(String[] args){
		Goal goal = new Goal("Lose 50 pounds.",Goal.Direction.DECREASE,50.0,Weight.Unit.POUNDS);
		goal.setPeriod(Period.ofMonths(6));
		goal.setMeasurementFrequency(Duration.ofDays(1));
		Calendar cal1 = new GregorianCalendar(2015,Month.DECEMBER.getValue(),8);
		Double weight = 250.0;
		goal.update(cal1.getTime());
		boolean isDue = goal.isDueMeasurement();
		boolean isInProgress = goal.isInProgress();
		goal.start(cal1.getTime(),weight); //starts the time now.
		Double goalWeight = goal.getGoalValue();
		Date endDate = goal.getEndDate();
		Double measurementGoal = goal.getMeasureIncrementGoal();
		Log.println("Goal Weight: "+goalWeight);
			
		/*
		 * scenarios: 
		 * Doing worse                                       RED
		 * Doing better only slowly, won't make it on time.  YELLOW
		 * Going ahead of                                    BRIGHT GREEN
		 * Going just right/paced.                           NORMAL GREEN
		 * 
		 * Percentage reporting.
		 * Recording devidations, number of measurements, each measurement's stoplight, value.
		 * 
		 * late, set Goal Timer., Reminder (timer simulation)
		 */
		for(int i=0;i<=180;i++){
			cal1.add(Calendar.HOUR,24);
			weight -= 0.28;                       //0.25:Yellow,0.40
			goal.addMeasurement(cal1.getTime(),weight);
		}
		List<Measurement> measurements = goal.getMeasurements();
		
		//TreeMap<Date,Double> graphPoints = goal.graph(Duration duration); 
		goal.stop();
		Date date = goal.currently();
		Double finalVal = goal.getCurrentValue();
		Log.println("Final :"+finalVal);
		//goal.triggerExpire();
		//goal.triggerLate();
		
		/*
		 * Define Goal
		 * Define Measurement Frequency
		 * Faux Measurements
		 * Reminders
		 * Calculate Goal stats.
		 * 
		 * Hypothetical test case:
		 * 
		 * Lose 50 pounds this year.
		 * 	Starting measure
		 *  Ending measure
		 *  
		 *  
		 * Eat 2000 calories each day.
		 * Walk each day. 
		 *   	too much
		 *   	too little
		 * 

		 * 
		 * 
		 * 
		 */
	}
	
}
/*
		 * Test defects found caused by developer
		 * Data defects
		 * Lack of definition
		 *
 */

/*
                      0.6, 108 Bright Green
76	Bright green
75	Green            
                      0.4, 72 Green
50	Green
49	Yellow
					 0.25, 45 Yellow
           
1	Yellow
0	Red
                    -0.25, -45 Red
-50	Red
-51	Dark red
				    -0.4, -72 Dark Red

 * 
 */
