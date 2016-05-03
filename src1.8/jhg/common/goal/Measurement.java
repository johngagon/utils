package jhg.common.goal;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import chp.dbutil.Log;
import jhg.common.goal.Goal.Direction;
import jhg.common.goal.StopLight.Light;

public class Measurement {

	private Goal goal;
	private LocalDateTime dateMeasured;
	private Double value;
	
	public Measurement(Goal g, LocalDateTime d, Double v){
		this.goal = g;
		this.dateMeasured = d;
		this.value = v;
	}
	
	/*
	 * To get this value, determine how far ahead or behind we are to goal.
	 * To do so, get the track value for this time.
	 * Then do a difference between 
	 * 
	 */
	public Light getStopLightOverall(){
		Double expectedValue = goal.getExpectedGoalValueOn(dateMeasured);
		
		if(Direction.DECREASE.equals(goal.getDir())){
			Double actualDecrease = goal.getStartingMeasure() - value; 
			Double ratio = actualDecrease/expectedValue;
			Log.println("Overall: Expected: "+Goal.trimDouble(expectedValue,2)+", Actual Decrease: "+Goal.trimDouble(actualDecrease,2)+", Ratio:"+Goal.trimDouble(ratio,2));
			return StopLight.getLight(ratio);
		}else{
			Double actualIncrease = goal.getStartingMeasure() + value;
			Double ratio = actualIncrease/expectedValue;
			//Log.println("Expected: "+expectedValue+", Actual Increase: "+actualIncrease+", Ratio:"+ratio);
			return StopLight.getLight(ratio);
		}
	}
	
	public Measurement getPreviousMeasurement(){
		List<Measurement> measurements = goal.getMeasurements();
		int index = measurements.indexOf(this);
		if(index!=0){
			return measurements.get(index-1);
		}else{
			return null;
		}
	}
	
	/*
	 * To get this value. 
	 * 
	 * find out the amount of time since last measure.
	 * figure this duration's percentage of overall
	 * figure the corresponding expected fraction of the goal quantity.
	 * determine the amount increased or decreased since last measure.
	 * compare the increase/decrease to the expected increase/decrease and get a proportion and pass to Light.
	 * 
	 */
	public Light getStopLightThisMeasure(){
		Measurement previous = this.getPreviousMeasurement();
		if(previous!=null){
			long totalDaysDiff = ChronoUnit.DAYS.between(goal.getStartDate(),goal.getEndDate());
			long daysDiff = ChronoUnit.DAYS.between(previous.dateMeasured,dateMeasured);
			Double percentTime = (double)daysDiff/totalDaysDiff;
			Double expectedValueDiff = this.goal.getGoalChangeValue() * percentTime;
			//Log.println("This Measure: Percent through time ("+daysDiff+"/"+totalDaysDiff+"): "+percentTime);
			if(Direction.DECREASE.equals(goal.getDir())){
				Double actualDecrease = previous.value - this.value;
				Double ratio = actualDecrease/expectedValueDiff;
				Log.println("From Last " + ": Expected: " + Goal.trimDouble(expectedValueDiff,2) + ", Actual Decrease: " 
						+ Goal.trimDouble(actualDecrease,2)	+ ", Ratio:" + Goal.trimDouble(ratio,2)		);
				return StopLight.getLight(ratio);
			}else{
				Double actualIncrease = value - previous.value;
				Double ratio = actualIncrease/expectedValueDiff;
				//Log.println("Expected: "+expectedValueDiff+", Actual Increase: "+actualIncrease+", Ratio:"+ratio);
				return StopLight.getLight(ratio);				
			}
		}else{
			return this.getStopLightOverall();
		}
		
	}
	
	/*
	 * Figure the light value of how on time or late we are.
	 
	public Light getMeasurementTimingLight(){
		return null;//FIXME
	}
	
	/*
	 * How many measurements expected so far and how many actual?
	
	public Light getMeasurementsOverallPerformance(){
		return null;
	}
	*/
	
	/*
	 * how timely was the measure since last?
	 */
	public Duration getTimeSinceLastMeasurement(){
		return null;//FIXME
	}
	
	/*
	 * get the duration and subtract the allowed
	 */
	public Duration getLateEarly(){
		return null;//FIXME
	}

	public Goal getGoal() {
		return goal;
	}

	public LocalDateTime getDateMeasured() {
		return dateMeasured;
	}

	public Double getValue() {
		return value;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM dd hh:mm a");
		return "[dateMeasured='" 
				+ dateMeasured.format(formatter) 
				+ "', value='" 
				+ Goal.trimDouble(value,goal.getAccuracy())
				+ "', getStopLightThisMeasure:"
				+ this.getStopLightThisMeasure().name()
				+ "', getStopLightOverall:"
				+ this.getStopLightOverall().name()
				
				+"]\n";
	}
	

	
	
}
