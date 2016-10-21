package jhg.resume;

import java.util.*;

import jhg.util.Dictionary;
import jhg.util.Log;
import jhg.util.RegexUtil;

public class KeywordFinder {
	
	private List<Dictionary> dictionaries = new ArrayList<Dictionary>();
	private int[] counts;
	public KeywordFinder(){
		super();
	}

	public void addDictionary(Dictionary d) {
		dictionaries.add(d);
	}

	public void analzye(Resume r) {
		counts = new int[dictionaries.size()];
		//for(int c:counts){
		//	c=0;
		//}
		Set<String> contentWords = r.getWords();
		Set<String> copiedContentWords = new HashSet<String>(contentWords);
		if(contentWords.size()>0){
			int index = 0;
			for(Dictionary d:dictionaries){
				
				for(String word:contentWords){
					if(d.contains(word)){
						copiedContentWords.remove(word);
						counts[index]++;
					}
				}
				contentWords = new HashSet<String>(copiedContentWords);
				Log.println("count: "+counts[index]);
				index++;
				
			}
			for(String word:contentWords){
				if(!RegexUtil.match("[0-9]+", word.trim())){
					//Log.println("'"+word+"'");
					Log.println(word);
				}else{
					
				}
				
			}
		}
		
	}

	public String getReport() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
