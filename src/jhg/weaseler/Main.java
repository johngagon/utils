package jhg.weaseler;



import jhg.util.Dictionary;
import jhg.util.Log;

public class Main {

	
	public static void rateArticle(){
		String wordweight = "data/articles/score/badwordweight.txt";
		Score s = new Score(wordweight);
		
		String articleFileName = "data/articles/breitbart.txt";
		
		Article r = new Article(articleFileName);
		
		Search search = new Search(s,0.7);
		String report = search.scoreReport(r);
		Log.println(report);
	}	
	
	public static void main(String[] args){
		
		try{
			rateArticle();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		/*
		Dictionary glueWordDictionary = Dictionary.instantiateDictionary("data/resumes/dictionaries/glueWords.txt");
		
		
		String articleFileName1 = "data/articles/breitbart.txt";
		String articleFileName2 = "data/articles/time.txt";
		
		Article a1 = new Article(articleFileName1);
		Article a2 = new Article(articleFileName2);
		
		
		KeywordFinder finder = new KeywordFinder();
		finder.addDictionary(glueWordDictionary);
		
		finder.analyze(a1);
		//finder.analyze(a2);
		
		*/
	}
	
}
