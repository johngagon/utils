package calendar;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;

@SuppressWarnings("unused")
public class Phours {
	//http://www.shodor.org/stella2java/rgbint.html
	private static int MARS = 16711680;    //red
	private static int MERCURY = 16744448; //orange
	private static int SUN = 16776960;  //sun
	private static int MOON = 65280; //green
	private static int VENUS = 255; //blue
	private static int SATURN = 8388863;//purple
	private static int JUPITER = 9127187;//brown
	private static int BLACK = 0;
	private static int WHITE = 16777215;
	private static int[] PSEQ = {SUN,VENUS,MERCURY,MOON,SATURN,JUPITER,MARS};
	
	private static int DAY_ODD = 16764216;//dark orange
	private static int DAY_EVEN = 16772275;
	private static int NITE_ODD = 7706879;
	private static int NITE_EVEN = 11781887;
	
	private static int[] HOUR_COL = {DAY_ODD,DAY_EVEN,DAY_ODD,DAY_EVEN,DAY_ODD,DAY_EVEN,DAY_ODD,DAY_EVEN,DAY_ODD,DAY_EVEN,DAY_ODD,DAY_EVEN,NITE_ODD,NITE_EVEN,NITE_ODD,NITE_EVEN,NITE_ODD,NITE_EVEN,NITE_ODD,NITE_EVEN,NITE_ODD,NITE_EVEN,NITE_ODD,NITE_EVEN,NITE_ODD};
	
	
	private static final String[] data = {"07:27","16:57","07:27","16:58","07:27","16:58","07:27","16:59","07:27","17:00","07:27","17:01","07:27","17:02","07:27","17:03","07:27","17:04","07:27","17:05","07:26","17:06","07:26","17:07","07:26","17:08","07:26","17:09","07:25","17:10","07:25","17:11","07:24","17:12","07:24","17:13","07:24","17:14","07:23","17:16","07:22","17:17","07:22","17:18","07:21","17:19","07:21","17:20","07:20","17:21","07:19","17:22","07:19","17:23","07:18","17:25","07:17","17:26","07:16","17:27","07:15","17:28","07:15","17:29","07:14","17:30","07:13","17:32","07:12","17:33","07:11","17:34","07:10","17:35","07:09","17:36","07:08","17:37","07:07","17:39","07:05","17:40","07:04","17:41","07:03","17:42","07:02","17:43","07:01","17:44","07:00","17:45","06:58","17:46","06:57","17:48","06:56","17:49","06:55","17:50","06:53","17:51","06:52","17:52","06:51","17:53","06:49","17:54","06:48","17:55","06:47","17:56","06:45","17:57","06:44","17:59","06:42","18:00","06:41","18:01","06:39","18:02","06:38","18:03","06:37","18:04","06:35","18:05","06:34","18:06","06:32","18:07","06:31","18:08","06:29","18:09","06:28","18:10","06:26","18:11","06:24","18:12","06:23","18:13","06:21","18:14","06:20","18:15","06:18","18:16","06:17","18:17","06:15","18:18","06:14","18:19","06:12","18:20","06:10","18:21","06:09","18:22","06:07","18:23","06:06","18:24","06:04","18:25","06:03","18:26","06:01","18:27","05:59","18:28","05:58","18:29","05:56","18:30","05:55","18:31","05:53","18:31","05:52","18:32","05:50","18:33","05:49","18:34","05:47","18:35","05:46","18:36","05:44","18:37","05:42","18:38","05:41","18:39","05:39","18:40","05:38","18:41","05:36","18:42","05:35","18:43","05:34","18:44","05:32","18:45","05:31","18:46","05:29","18:47","05:28","18:48","05:26","18:49","05:25","18:50","05:24","18:51","05:22","18:52","05:21","18:53","05:20","18:54","05:18","18:55","05:17","18:56","05:16","18:57","05:14","18:58","05:13","18:58","05:12","18:59","05:11","19:00","05:09","19:01","05:08","19:02","05:07","19:03","05:06","19:04","05:05","19:05","05:04","19:06","05:03","19:07","05:02","19:08","05:01","19:09","05:00","19:10","04:59","19:11","04:58","19:12","04:57","19:13","04:56","19:14","04:55","19:14","04:54","19:15","04:53","19:16","04:53","19:17","04:52","19:18","04:51","19:19","04:50","19:20","04:50","19:21","04:49","19:21","04:48","19:22","04:48","19:23","04:47","19:24","04:47","19:24","04:46","19:25","04:46","19:26","04:45","19:27","04:45","19:27","04:44","19:28","04:44","19:29","04:44","19:29","04:43","19:30","04:43","19:31","04:43","19:31","04:43","19:32","04:43","19:32","04:42","19:33","04:42","19:33","04:42","19:34","04:42","19:34","04:42","19:35","04:42","19:35","04:42","19:35","04:42","19:36","04:43","19:36","04:43","19:36","04:43","19:37","04:43","19:37","04:43","19:37","04:44","19:37","04:44","19:37","04:44","19:37","04:44","19:37","04:45","19:38","04:45","19:38","04:46","19:37","04:46","19:37","04:46","19:37","04:47","19:37","04:47","19:37","04:48","19:37","04:49","19:37","04:49","19:36","04:50","19:36","04:50","19:36","04:51","19:36","04:52","19:35","04:52","19:35","04:53","19:34","04:54","19:34","04:54","19:33","04:55","19:33","04:56","19:32","04:56","19:32","04:57","19:31","04:58","19:30","04:59","19:30","05:00","19:29","05:00","19:28","05:01","19:28","05:02","19:27","05:03","19:26","05:04","19:25","05:05","19:24","05:05","19:23","05:06","19:22","05:07","19:21","05:08","19:20","05:09","19:19","05:10","19:18","05:11","19:17","05:12","19:16","05:12","19:15","05:13","19:14","05:14","19:13","05:15","19:12","05:16","19:11","05:17","19:09","05:18","19:08","05:19","19:07","05:20","19:06","05:21","19:04","05:21","19:03","05:22","19:02","05:23","19:00","05:24","18:59","05:25","18:58","05:26","18:56","05:27","18:55","05:28","18:54","05:29","18:52","05:30","18:51","05:31","18:49","05:31","18:48","05:32","18:46","05:33","18:45","05:34","18:43","05:35","18:42","05:36","18:40","05:37","18:39","05:38","18:37","05:39","18:36","05:40","18:34","05:40","18:33","05:41","18:31","05:42","18:29","05:43","18:28","05:44","18:26","05:45","18:25","05:46","18:23","05:47","18:22","05:48","18:20","05:48","18:18","05:49","18:17","05:50","18:15","05:51","18:14","05:52","18:12","05:53","18:10","05:54","18:09","05:55","18:07","05:56","18:06","05:56","18:04","05:57","18:02","05:58","18:01","05:59","17:59","06:00","17:58","06:01","17:56","06:02","17:54","06:03","17:53","06:04","17:51","06:05","17:50","06:06","17:48","06:07","17:47","06:08","17:45","06:08","17:43","06:09","17:42","06:10","17:40","06:11","17:39","06:12","17:37","06:13","17:36","06:14","17:34","06:15","17:33","06:16","17:31","06:17","17:30","06:18","17:29","06:19","17:27","06:20","17:26","06:21","17:24","06:22","17:23","06:23","17:22","06:24","17:20","06:25","17:19","06:26","17:18","06:28","17:16","06:29","17:15","06:30","17:14","06:31","17:13","06:32","17:11","06:33","17:10","06:34","17:09","06:35","17:08","06:36","17:07","06:37","17:06","06:38","17:05","06:39","17:04","06:41","17:03","06:42","17:02","06:43","17:01","06:44","17:00","06:45","16:59","06:46","16:58","06:47","16:57","06:48","16:56","06:49","16:55","06:51","16:55","06:52","16:54","06:53","16:53","06:54","16:52","06:55","16:52","06:56","16:51","06:57","16:51","06:58","16:50","06:59","16:49","07:00","16:49","07:01","16:49","07:02","16:48","07:03","16:48","07:04","16:47","07:05","16:47","07:06","16:47","07:07","16:47","07:08","16:46","07:09","16:46","07:10","16:46","07:11","16:46","07:12","16:46","07:13","16:46","07:14","16:46","07:15","16:46","07:16","16:46","07:16","16:46","07:17","16:46","07:18","16:47","07:19","16:47","07:19","16:47","07:20","16:47","07:21","16:48","07:21","16:48","07:22","16:48","07:22","16:49","07:23","16:49","07:23","16:50","07:24","16:50","07:24","16:51","07:25","16:52","07:25","16:52","07:25","16:53","07:26","16:54","07:26","16:54","07:26","16:55","07:26","16:56"};

	public static void main(String[] args){

		try {
			makeImage() ;
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void makeImage() throws ParseException, IOException{
		final int WIDTH = 1440+24;
		final int HEIGHT = 365+12;
		final int URN = 12;
		final int HOURS = 24;
		
		BufferedImage img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		int x = 0;
		int y = 0;
		int rgb = 0;

		int i = 0;
		int dawnint = 0;
		int duskint = 0;
		int lengthOfDay = 0; // in minutes
		int lengthOfNight = 0;
		int dayHour = 0;
		int nightHour = 0;
		int hourAtHand = 0;
		int dayAtHand = 0;
		int planetIndex = 0;
		int nextHour = 0;
		int lines = 0;
		int divs = 0;
		for(int yi=0;yi<HEIGHT;yi++){
			if(yi%15==14){
				for(int xi=0;xi<WIDTH;xi++){
					img.setRGB(xi, yi, BLACK);
				}
				lines++;
				
			}else{
				dawnint = getMinutes(data[i-lines]);
				i++;
				duskint = getMinutes(data[i-lines]);
				i++;
				lengthOfDay = duskint - dawnint;
				lengthOfNight = 1440 - lengthOfDay;
				dayHour = (int)Math.round(lengthOfDay/12);
				nightHour = (int)Math.round(lengthOfNight/12);
				nextHour = dawnint+dayHour;
						
				for(int xi=dawnint;xi<duskint;xi++){
					if(xi==nextHour){
						hourAtHand++;                   //12
						planetIndex = (planetIndex==6)?0:planetIndex+1;                  //12
				
						nextHour = nextHour + dayHour;
					}
					System.out.print(hourAtHand);
					if(xi%61==60){
						divs++;
						img.setRGB(xi, yi, BLACK);  //
					}else{
						img.setRGB(xi, yi, HOUR_COL[hourAtHand]);  //
					}
				}
				nextHour = duskint + nightHour;
				for(int xi=duskint;xi<WIDTH;xi++){
					if(xi==nextHour){
						hourAtHand++;
						planetIndex = (planetIndex==6)?0:planetIndex+1;                  
						nextHour = nextHour + nightHour;
						if(nextHour >=WIDTH){
							nextHour = nextHour - WIDTH; 
						}
					}
					System.out.print(planetIndex);
					if(xi%61==60){
						divs++;
						img.setRGB(xi, yi, BLACK);  //
					}else{				
						img.setRGB(xi, yi, HOUR_COL[hourAtHand]);
					}
				}
				for(int xi=0;xi<dawnint;xi++){
					if(xi==nextHour){
						hourAtHand++;
						planetIndex = (planetIndex==6)?0:planetIndex+1;
						nextHour = nextHour + nightHour;
					}
	
					System.out.print(hourAtHand);
					if(xi%61==60){
						divs++;
						img.setRGB(xi, yi, BLACK);  //
					}else{
						img.setRGB(xi, yi, HOUR_COL[hourAtHand]);//PSEQ[planetIndex]
					}
				}				
				hourAtHand = 0;
				System.out.println("");
			}
		}
		File f = new File("C:\\MyFile.png");
		ImageIO.write(img, "PNG", f);
		System.out.println("DONE.");

	}
	private static int getMinutes(String s){
	    String[] hourMin = s.split(":");
	    int hour = Integer.parseInt(hourMin[0]);
	    int mins = Integer.parseInt(hourMin[1]);
	    int hoursInMins = hour * 60;
	    return hoursInMins + mins;
	}
	
}
