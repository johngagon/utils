package jhg.common.goal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;

import chp.dbutil.Log;
import jhg.common.units.Weight.Unit;

@SuppressWarnings("unused")
public class Goal {

	public static enum Direction{INCREASE,DECREASE}
	
	private String goalName;
	private Direction dir;
	private Double goalChangeValue;
	private Unit unit;
	private Period goalPeriod;
	private Duration measureFrequency;
	private LocalDateTime startDate;
	private Double startingMeasure;
	private LocalDateTime currentTime;
	private Boolean inProgress;
	private Boolean isFinished;
	private List<Measurement> measurements;
	private int accuracy;
	
	public Goal(String s, Direction d, Double v, Unit u) {
		this.goalName = s;
		this.dir = d;
		this.goalChangeValue = v;
		this.unit = u;
		this.inProgress = false;
		this.isFinished = false;
		this.measurements = new ArrayList<Measurement>();
		this.accuracy = 1;//1 decimal places.
	}

	public void setGoalPeriod(Period p) {
		this.goalPeriod = p;
	}

	public void setMeasurementFrequency(Duration dur) {
		this.measureFrequency = dur;
	}

	public void start(LocalDateTime i, Double m) {
		if(this.goalPeriod==null || this.measureFrequency==null){
			throw new IllegalStateException("Need goal period and measure frequency set first.");
		}
		this.startDate = i;
		this.startingMeasure = m;
		this.inProgress = true;
		this.currentTime = i;
	}
	
	public boolean isInProgress() {
		return this.inProgress;
	}	
	
	public boolean isFinished(){
		return this.isFinished;
	}
	
	public void update(LocalDateTime time) {
		if(!isInProgress()){
			throw new IllegalStateException("Not started yet.");
		}
		this.currentTime = time;
	}

	public void addMeasurement(LocalDateTime time, Double v) {
		Measurement m = new Measurement(this,time,v);
		update(time);
		measurements.add(m);
	}	

	//not to be confused with measurement value.
	public Double getExpectedGoalValueOn(LocalDateTime dateTime){
		long totalDaysDiff = ChronoUnit.DAYS.between(startDate, this.getEndDate());
		long lapsedDaysDiff = ChronoUnit.DAYS.between(this.startDate.toLocalDate(),dateTime.toLocalDate());
		
		double percentTime = (double)lapsedDaysDiff/totalDaysDiff;
		//Log.println("Percent through time ("+lapsedDaysDiff+"/"+totalDaysDiff+"): "+percentTime);
		double rv = this.goalChangeValue * percentTime;
		return rv;
	}
	public Double getExpectedGoalMeasureOn(LocalDateTime dateTime){
		Double changeValue = getExpectedGoalValueOn(dateTime);
		if(Direction.DECREASE.equals(dir)){
			return this.startingMeasure - changeValue;
		}else{
			return this.startingMeasure + changeValue;
		}
	}
	
	public Double getGoalChangeValue() {
		return this.goalChangeValue;
	}

	public LocalDateTime getEndDate() {
		if(goalPeriod==null){
			throw new IllegalStateException("No goal period set.");
		}
		LocalDateTime i = startDate.plus(goalPeriod);
		return i;
	}

	public void stop() {
		this.inProgress = false;
		this.isFinished = true;
	}
	
	public Double getActualValue(){
		
		if(Direction.DECREASE.equals(dir)){
			return trimDouble(this.startingMeasure - this.getCurrentMeasurement(),this.accuracy); 
		}else{
			return trimDouble(this.getCurrentMeasurement() - this.startingMeasure,this.accuracy);
		}
	}
	
	public boolean goalSuccess(){
		if(!isFinished()){
			throw new IllegalStateException("Goal period not finished.");
		}
		if(Direction.DECREASE.equals(dir)){
			return this.getCurrentMeasurement() <= this.getGoalMeasurement();
		}else if(Direction.INCREASE.equals(dir)){
			return this.getCurrentMeasurement() >= this.getGoalMeasurement();
		}else{
			throw new IllegalStateException("Direction not defined.");
		}
	}

	public List<Measurement> getMeasurements() {
		return this.measurements;
	}

	public LocalDateTime currently() {
		return this.currentTime;
	}

	public static Double trimDouble(Double d, int i){
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(i,RoundingMode.HALF_UP);
		Double rv = bd.doubleValue();
		return rv;
	}
	
	public Double getCurrentMeasurement() {
		if(measurements.isEmpty()){
			return trimDouble(this.startingMeasure,this.accuracy);
		}else{
			Measurement m = measurements.get(measurements.size()-1);
			return trimDouble(m.getValue(),this.accuracy);
		}
	}

	public String getGoalName() {
		return goalName;
	}

	public Direction getDir() {
		return dir;
	}

	public Unit getUnit() {
		return unit;
	}

	public Period getGoalPeriod() {
		return goalPeriod;
	}

	public Duration getMeasureFrequency() {
		return measureFrequency;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public Double getStartingMeasure() {
		return startingMeasure;
	}
	
	public Double getGoalMeasurement() {
		if(Direction.DECREASE.equals(dir)){
			return startingMeasure - goalChangeValue;
		}else{
			return startingMeasure + goalChangeValue;
		}
	}

	public int getAccuracy() {
		return this.accuracy;
	}
	
	/*
	
	public boolean isDueMeasurement() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	public Double getMeasureIncrementGoal() {
		// TODO Auto-generated method stub
		return null;
	}	
	
	*/
	
	/*
	 *   currentTimeComplete%    expected, actual,  
	 *    
	 */
	
	
	
	
	
	/*
	 * Goal dollars    desireLowQuantity, desireHighQuantity
	 * 
	 * Percentage
	 * 
	 */
	
}
