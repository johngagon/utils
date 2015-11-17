package jhg.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {

	private Set<String> words;
	
	public Dictionary() {
		this.words = new TreeSet<String>();
	}
	public static Dictionary instantiateDictionary(String filename){
		Dictionary d = new Dictionary();
		d.load(filename);
		return d;
	}
	
	public void load(String fileName){
		String wordList = new TextFile(fileName).getText();
		String[] wordStrings = wordList.split("\n");
		for(String s:wordStrings){
			words.add(s.trim());
		}
		Log.println("Added "+wordStrings.length+" words to dictionary.  Dictionary at "+words.size()+" words.");
	}
	
	public Set<String> wordList(){
		return this.words;
	}

	public boolean contains(String word) {
		return words.contains(word);
	}


	public List<String> findUnknownWords(String dtext, boolean ignoreProperNouns) {
		List<String> result = new ArrayList<String>();
		
		String[] textWords = dtext.split("\\s+"); //space char
		for (int i = 0; i < textWords.length; i++) {
			textWords[i] = textWords[i].replaceAll("[^\\w]", ""); //remove non words. (It may also be necessary to adjust the character class)
		}
		for(String textWord:textWords){
			if(!this.words.contains(textWord.trim()) && textWord.length()>0){
				if(!isCapitalized(textWord)){
					result.add(textWord);
				}
			}
		}
		
		return result;
	}

	private boolean isCapitalized(String textWord) {
		return Character.isUpperCase(textWord.charAt(0));
	}	
	
	public static void createStandardizedDictionaryFromText(String filenameIn, Dictionary standardDictonary, 
			String dictionaryFileOut, String unfamiliarWordsFile, String blatantExceptionFile){
		String content = new TextFile(filenameIn).getText();
		Set<String> wordList = new TreeSet<String>();
		Set<String> unfamiliarList = new TreeSet<String>();
		Set<String> blatantList = new TreeSet<String>();
		
		Pattern _pattern = Pattern.compile("[\\w']+");	
		Matcher m = _pattern.matcher(content);
		String word = "";
		while ( m.find() ) {		
			word = content.substring(m.start(),m.end());
			word = TextUtil.truncPossessive(word);
			if(	word.length()>0
				&& word.length() < 50
				&& (!TextAnalyzer.isNumber(word))
				&& (!TextAnalyzer.isProper(word))				){
				if(standardDictonary.contains(word)){
					wordList.add(word);
				}else{
					unfamiliarList.add(word);
				}
				
			}else{
				blatantList.add(word);
			}
		}
		writeDictionaryFromWordList(dictionaryFileOut, wordList);
		writeDictionaryFromWordList(unfamiliarWordsFile, unfamiliarList);
		writeDictionaryFromWordList(blatantExceptionFile, blatantList);
	}
	
	public static void mergeDictionaries(String dictFilename, Dictionary[] dictionaries){
		Set<String> newList = new TreeSet<String>();
		for(Dictionary d:dictionaries){
			newList.addAll(d.wordList());
		}
		writeDictionaryFromWordList(dictFilename, newList);
	}

	private static void writeDictionaryFromWordList(String dictFilename, Set<String> wordList) {
		StringBuffer buff = new StringBuffer();
		for(String w:wordList){
			buff.append(w+"\n");
		}
		TextFile.write(dictFilename,buff.toString());
		Log.println("Wrote dictionary "+dictFilename);
	}		
	
	
	public static void main(String[] args){
		
		Dictionary old = Dictionary.instantiateDictionary("data/perioddictionaries/olddictionary.txt");
		Dictionary duchess = Dictionary.instantiateDictionary("data/perioddictionaries/duchess_current_dictionary.txt");
		Dictionary winthrop = Dictionary.instantiateDictionary("data/perioddictionaries/winthrop_current_dictionary.txt");
		String filename = "data/perioddictionaries/combined_dictionary.txt";
		Dictionary[] dictionaries = {old,duchess,winthrop}; 
		Dictionary.mergeDictionaries(filename, dictionaries);
		
	}
	
	@SuppressWarnings("unused")
	private static void testCreate(){
		String fileIn = "data/periodtext/winthrop.txt";
		String dictionaryOut1d = "data/perioddictionaries/raw/winthrop_dict.txt";
		String dictionaryOut1u = "data/perioddictionaries/raw/winthrop_unfam.txt";
		String dictionaryOut1b = "data/perioddictionaries/raw/winthrop_err.txt";
		Dictionary approved = Dictionary.instantiateDictionary("data/dictionary.txt");//TODO add validation.
		Dictionary.createStandardizedDictionaryFromText(fileIn, approved, dictionaryOut1d, dictionaryOut1u, dictionaryOut1b);

		fileIn = "data/periodtext/duchess.txt";
		dictionaryOut1d = "data/perioddictionaries/raw/duchess_dict.txt";
		dictionaryOut1u = "data/perioddictionaries/raw/duchess_unfam.txt";
		dictionaryOut1b = "data/perioddictionaries/raw/duchess_err.txt";
		Dictionary.createStandardizedDictionaryFromText(fileIn, approved, dictionaryOut1d, dictionaryOut1u, dictionaryOut1b);
		
	}
	
	@SuppressWarnings("unused")
	private static void test1(){
		Dictionary d = new Dictionary();
		d.load("data/dictionary.txt");
		Set<String> words = d.wordList();
		for(String s:words){
			Log.println(s);
		}		
	}
	
	
}
