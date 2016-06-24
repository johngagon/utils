package jhg.math;

import jhg.util.Log;

public class Numbers {

	
	
	@SuppressWarnings({"boxing"})
	public static void main(String[] args){
		Double e = Math.E;
		Double pi = Math.PI;
		Double g = Double.valueOf("1.61803398875");
		Double z = (360.0/7.0);
		
		Double[] values = {e,pi,g,z};
		//Double[] values2 = values;
		
		
		for(Double x:values){
			for(Double y:values){
				//if(x!=y){
				Log.println("\n------");
				Log.println("x: "+x+" y: "+y+"  x*y: "+(x*y));
				Log.println("x: "+x+" y: "+y+"  x+y: "+(x+y));
				Log.println("x: "+x+" y: "+y+"  x-y: "+(x-y));
				Log.println("y: "+y+" x: "+x+"  y-x: "+(y-x));
				Log.println("x: "+x+" y: "+y+"  x/y: "+(x/y));
				Log.println("y: "+y+" x: "+x+"  y/x: "+(y/x));
				Log.println("x: "+x+" y: "+y+"  x^y: "+(Math.pow(x,y)));
				Log.println("y: "+y+" x: "+x+"  y^x: "+(Math.pow(y,x)));
				//}
			}
		}
		Log.println("\n------");
		Log.println("e*pi: "+(e*pi));
		Log.println("e*pi: "+(e+pi));
		Log.println("e*pi: "+(pi-e));
		Log.println("e*pi: "+(e/pi));
		Log.println("e*pi: "+(pi/e));
		
		
	}
	
}
