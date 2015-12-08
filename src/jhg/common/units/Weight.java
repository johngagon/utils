package jhg.common.units;

public class Weight {
	public static enum Unit{
		POUNDS(1.0),
		KILOGRAMS(2.2046);
		private Double conversionToPounds;
		private Unit(double _c){
			this.conversionToPounds = _c;
		}
		public Double getFactor(){
			return this.conversionToPounds;
		}
		public Double toPounds(double qty){
			return null;//FIXME todo
		}
		public Double toUnit(double qty, Unit unit){
			return null;//FIXME todo
		}
		
	}
	
	public static void main(String[] args){
		
	}
}
