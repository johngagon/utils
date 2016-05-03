package jhg.common.goal;

import jhg.common.units.Weight;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import chp.dbutil.Log;

@SuppressWarnings("unused")
public class TestGoal {

	
	
	
	public static void main(String[] args){
		Goal goal = new Goal("Lose 50 pounds.",Goal.Direction.DECREASE,50.0,Weight.Unit.POUNDS);
		goal.setGoalPeriod(Period.ofMonths(6));
		goal.setMeasurementFrequency(Duration.ofDays(1));
		LocalDateTime startDate = LocalDateTime.now();//LocalDate.now().atStartOfDay().toLocalDateTime(ZoneOffset.UTC);//plus(16,ChronoUnit.HOURS);//LocalDate.of(2015,Month.DECEMBER.getValue(),8);
		Double weight = 250.0;
		
		//boolean isDue = goal.isDueMeasurement(); if the current time is past the last measurement time, then true.
		boolean isInProgress = goal.isInProgress();
		
		goal.start(startDate,weight); //starts the time now.
		Log.println("Starting Weight: "+goal.getStartingMeasure());
		goal.update(startDate);
		Log.println("Goal Start Date: "+goal.getStartDate() +", End Date: "+goal.getEndDate());
		Double goalWeight = goal.getGoalChangeValue();
		LocalDateTime endDate = goal.getEndDate();
		//Double measurementGoal = goal.getMeasureIncrementGoal();
		Log.println("Goal Weight Loss: "+goalWeight);
			
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
		LocalDateTime newLocalDateTime = startDate;
		
		newLocalDateTime = newLocalDateTime.plus(Duration.ofDays(4));
		goal.addMeasurement(newLocalDateTime,249.0);
		
		newLocalDateTime = newLocalDateTime.plus(Duration.ofDays(3));
		goal.addMeasurement(newLocalDateTime,248.7);
		
		newLocalDateTime = newLocalDateTime.plus(Duration.ofDays(2));
		goal.addMeasurement(newLocalDateTime,248.6);
		
		newLocalDateTime = newLocalDateTime.plus(Duration.ofDays(6));
		goal.addMeasurement(newLocalDateTime,246.5);

		newLocalDateTime = newLocalDateTime.plus(Duration.ofDays(1));
		goal.addMeasurement(newLocalDateTime,244.0);		
		
		newLocalDateTime = newLocalDateTime.plus(Duration.ofDays(167));
		goal.addMeasurement(newLocalDateTime,199.5);		
		
		/* Regular Measurement
		for(int i=0;i<=180;i++){
			newLocalDateTime = newLocalDateTime.plus(Duration.ofDays(1));
			weight -= 0.28;                       //0.25:Yellow,0.40
			goal.addMeasurement(newLocalDateTime,weight);
		}
		*/
		List<Measurement> measurements = goal.getMeasurements();
		
		//TreeMap<Date,Double> graphPoints = goal.graph(Duration duration); 
		goal.stop();
		LocalDateTime date = goal.currently();
		Double finalVal = goal.getCurrentMeasurement();
		Log.println("Goal Measurement: "+goal.getGoalMeasurement());
		Log.println("Final Measure:"+finalVal);
		Log.println("Actual Weight Loss :"+goal.getActualValue());
		Log.println("Goal Success :"+goal.goalSuccess()+"\nMeasurements:\n\n");
		
		for(Measurement m:measurements){
			Log.println("Measurement "+m.toString());
		}
		
		
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
