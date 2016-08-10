package jhg.resume;

import jhg.util.Log;


public class Main {

	public static void rateResume(){
		String seniorDev = "data/resumes/score/seniorDev.txt";
		Score s = new Score(seniorDev);
		
		String resumeFileName = "data/resumes/alexHurtt.txt";
		//String resumeFileName = "data/resumes/nirenBhattarai.txt";
		
		Resume r = new Resume(resumeFileName);
		
		Search search = new Search(s,0.7);
		String report = search.scoreReport(r);
		Log.print(report);
	}
	
	public static void main(String[] args) {
		try{
			rateResume();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
