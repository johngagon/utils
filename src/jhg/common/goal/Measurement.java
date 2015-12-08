package jhg.common.goal;

import java.time.Duration;
import java.util.Date;

import jhg.common.goal.StopLight.Light;

public class Measurement {

	private Goal goal;
	private Date dateMeasured;
	private Double value;
	
	/*
	 * To get this value, determine how far ahead or behind we are to goal.
	 * To do so, get the track value for this time.
	 * Then do a difference.
	 * 
	 */
	public Light getStopLightOverall(){
		return null;//FIXME impl
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
		return null;//FIXME impl
	}
	
	/*
	 * Figure the light value of how on time or late we are.
	 */
	public Light getMeasurementTimingLight(){
		return null;//FIXME
	}
	
	/*
	 * How many measurements expected so far and how many actual?
	 */
	public Light getMeasurementsOverallPerformance(){
		return null;
	}
	
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
}
