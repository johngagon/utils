package jhg.puzzles;

public interface Shape {

	public static final Double PI = 3.1415;
	
	public static class Square{
		private int h = 1;
		private int w = 1;
		
		public Square(int h, int w){
			super();
			this.h = h;
			this.w = w;
		}
		
		public int height(){
			return h;
		}
		public int width(){
			return w;
		}
		
	}
	public int height();
	public int width();

}
