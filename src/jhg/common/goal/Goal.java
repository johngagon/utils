package jhg.common.goal;

import java.time.Duration;
import java.time.Period;
import java.util.Date;
import java.util.List;

import jhg.common.goal.Goal.Direction;
import jhg.common.units.Weight.Unit;

public class Goal {

	public Goal(String string, Direction decrease, double d, Unit pounds) {
		// TODO Auto-generated constructor stub
	}

	public static enum Direction{INCREASE,DECREASE}

	public void setPeriod(Period ofMonths) {
		// TODO Auto-generated method stub
		
	}

	public void setMeasurementFrequency(Duration ofDays) {
		// TODO Auto-generated method stub
		
	}

	public void start(Date time, Double weight) {
		// TODO Auto-generated method stub
		
	}

	public void update(Date time) {
		// TODO Auto-generated method stub
		
	}

	public boolean isDueMeasurement() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isInProgress() {
		// TODO Auto-generated method stub
		return false;
	}

	public Double getGoalValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getEndDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getMeasureIncrementGoal() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addMeasurement(Date time, Double weight) {
		// TODO Auto-generated method stub
		
	}

	public List<Measurement> getMeasurements() {
		// TODO Auto-generated method stub
		return null;
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public Date currently() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getCurrentValue() {
		// TODO Auto-generated method stub
		return null;
	}
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
