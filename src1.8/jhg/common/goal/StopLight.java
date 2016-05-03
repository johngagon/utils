package jhg.common.goal;

public class StopLight {


	
	public static Double BG = 1.5;	  //higher than 1.5
	public static Double G  = 1.0;    //greater than or equal 1.0 and less than or equal 1.5
	public static Double Y	= 0.0;    //greater than 0 and less than 1.0   
	public static Double R  = -1.0;   //opposite direction less than or equal 0.0 and greather than or equal -1.0
	//public static Double DR = -1.5;   //anything lower than -1.0

	public static enum Light{
		BRIGHT_GREEN("#00ff00"),
		GREEN("#006600"),
		YELLOW("#ffff00"),
		RED("#ff0000"),
		DARK_RED("#660000"),
		BLACK("#000000");
		
		private String colorCode;
		private Light(String s){
			this.colorCode = s;
		}
		public String getColorCode(){
			return colorCode;
		}
	}
	public static Light getLight(Double value){
		Light rv = null;
		if(value>BG){
			rv = Light.BRIGHT_GREEN;
		}else if(value<=BG && value >=G){
			rv = Light.GREEN;
		}else if(value<G && value > Y){
			rv = Light.YELLOW;
		}else if(value<=Y && value >=R){
			rv = Light.RED;
		}else if(value<R){
			rv = Light.DARK_RED;
		}else {
			rv = Light.BLACK;
		}
		return rv;
	}
	
	
}
