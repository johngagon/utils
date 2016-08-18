package jhg.resume;

import jhg.util.Dictionary;
import jhg.util.Log;


public class Main {

	public static void rateResume(){
		String seniorDev = "data/resumes/score/seniorDev.txt";
		Score s = new Score(seniorDev);
		
		String resumeFileName = "data/resumes/juanYongZhang.txt";
		
		Resume r = new Resume(resumeFileName);
		
		Search search = new Search(s,0.7);
		String report = search.scoreReport(r);
		Log.print(report);
	}
	
	public static void mineResume(){
		Dictionary glueWordDictionary = Dictionary.instantiateDictionary("data/resumes/dictionaries/glueWords.txt");
		Dictionary technicalDictionary = Dictionary.instantiateDictionary("data/resumes/dictionaries/technicalWords.txt");
		Dictionary technologyWords = Dictionary.instantiateDictionary("data/resumes/dictionaries/technologyWords.txt");
		Dictionary industryWords = Dictionary.instantiateDictionary("data/resumes/dictionaries/industryWords.txt");
		
		
		String resumeFileName = "data/resumes/alexHurtt.txt";
		Resume r = new Resume(resumeFileName);
		KeywordFinder finder = new KeywordFinder();
		finder.addDictionary(glueWordDictionary);
		finder.addDictionary(technicalDictionary);
		finder.addDictionary(technologyWords);
		finder.addDictionary(industryWords);
		finder.analzye(r);
		//String report = finder.getReport();
		//Log.print(report);
	}
	
	public static void main(String[] args) {
		try{
			rateResume();
			//mineResume();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
