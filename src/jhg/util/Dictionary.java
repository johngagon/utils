package jhg.util;

import java.util.*;

public class Dictionary {

	private Set<String> words;
	
	public Dictionary() {
		this.words = new TreeSet<String>();
	}

	public void load(String fileName){
		String wordList = new TextFile(fileName).getText();
		String[] wordStrings = wordList.split("\n");
		for(String s:wordStrings){
			words.add(s.trim());
		}
		Log.println("Added "+wordList.length()+" words to dictionary.  Dictionary at "+words.size()+" words.");
	}
	
	public Set<String> wordList(){
		return this.words;
	}

	public boolean contains(String word) {
		return words.contains(word);
	}
	
	public static void main(String[] args){
		Dictionary d = new Dictionary();
		d.load("data/dictionary.txt");
		Set<String> words = d.wordList();
		for(String s:words){
			Log.println(s);
		}
	}	
	
}
