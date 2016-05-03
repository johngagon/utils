package jhg.util;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 1. Take a text file and create a vocabulary file and an exceptions file (words that don't fit a particular regex)
 * 2. Compare two vocabularies and create a diff of:  In A, not in B. (e.g.: )
 * 		a. A has words too modern based on vocabulary B
 * 		b. B isn't using all the vocabulary found in A.  (A is basic english list, B is instructional text), the reverse says words that are too complex.
 * 
 */
@SuppressWarnings("unused")
public class TextAnalyzer {
	private static Pattern numberPattern;
	
	private Pattern pattern;
	private String content;
	
	private Set<String> vocab;
	private Set<String> unreadable;
	private Set<String> archaic;
	private Set<String> difference;
	private Dictionary dictionary;
	private int numberCount;
	private int properCount;
	
	static{
		numberPattern = Pattern.compile("\\d*");
	}
	
	public TextAnalyzer() {
		this.pattern = Pattern.compile("[\\w']+");		
		
		
		this.vocab = new TreeSet<String>();
		this.unreadable = new TreeSet<String>();
		this.archaic = new TreeSet<String>();
		this.dictionary = new Dictionary();
		//this.dictionary.load("data/dictionary.txt");
		//this.dictionary.load("data/uncommon_dictionary.txt");
		//this.dictionary.load("data/archaic_dictionary.txt");
		
		//this.dictionary.load("data/names.txt");
		numberCount = 0;
		properCount = 0;
	}
	
	
	public void read(String _content){
		this.content = _content;
	}
	public static boolean isNumber(String s){
		Matcher nm = numberPattern.matcher(s);
		return nm.matches() || s.endsWith("th");
	}
	public static boolean isProper(String s){
		return Character.isUpperCase(s.charAt(0));
	}
	

	private static void updateVocab(Set<String> vocab, String fileName){
		Dictionary vocabDic = new Dictionary();
		vocabDic.load(fileName);
		vocab.addAll(vocabDic.wordList());
	}

	public void analyze(){
		Log.println("Analyzing.");
		
		Matcher m = pattern.matcher(content);
		String word = "";
		while ( m.find() ) {
			word = content.substring(m.start(),m.end());
			word = TextUtil.truncPossessive(word);
			if(isNumber(word)){
				numberCount++;
			}else if(isProper(word)){
				properCount++;
			}else{
				if(dictionary.contains(word)){
					vocab.add(word);
				}else{
					archaic.add(word);
				}
			}
			
	
			
			//if it's a proper name, put in proper names.
			//anything not in a spelling dictionary is either archaic or bad scan - have to add words to special dictionary for archaic.
			
		}
		Log.println("Analzyed.");
	}
	
	/**
	 * Creates a dictonary from a sample text.
	 * Truncates possessives and removes numbers and excludes proper names.
	 * 
	 * @param filenameIn
	 * @param filenameOut
	 */
	public void createDictionaryFromText(String filenameIn, String filenameOut){
		String content = new TextFile(filenameIn).getText();
		Set<String> wordList = new TreeSet<String>();
		Matcher m = pattern.matcher(content);
		String word = "";
		while ( m.find() ) {		
			word = content.substring(m.start(),m.end());
			word = TextUtil.truncPossessive(word);
			if(	word.length()>0
				&& word.length() < 50
				&& (!isNumber(word))
				&& (!isProper(word))				){
				wordList.add(word);
				
			}
		}
		writeDictionaryFromWordList(filenameOut, wordList);
	}
	
	public void createStandardizedDictionaryFromText(String filenameIn, Dictionary standardDictonary, 
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
				&& (!isNumber(word))
				&& (!isProper(word))				){
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


	private void writeDictionaryFromWordList(String dictFilename, Set<String> wordList) {
		StringBuffer buff = new StringBuffer();
		for(String w:wordList){
			buff.append(w+"\n");
		}
		TextFile.write(dictFilename,buff.toString());
		Log.println("Wrote dictionary "+dictFilename);
	}	
	
	
	
	public Set<String> unapproved(Dictionary gross, Dictionary approved){
		/*
		 * product the list of words in gross not in approved.
		 */
		Set<String> grossWords = new HashSet<String>(gross.wordList());
		Set<String> approvedWords = approved.wordList();
		grossWords.removeAll(approvedWords);
		return grossWords; 
		
	}
	
	
	
	public static void main(String[] args){
		String fileIn = "data/periodtext/winthrop.txt";
		String dictionaryOut1 = "data/perioddictionaries/winthrop_vocab.txt";
		
		TextAnalyzer analyzer = new TextAnalyzer();
		
		analyzer.createDictionaryFromText(fileIn, dictionaryOut1);

		Dictionary gross1 = new Dictionary();
		gross1.load(dictionaryOut1);
		
		Dictionary approved = new Dictionary();
		approved.load("data/dictionary.txt");
		Set<String> winthropOddness = analyzer.unapproved(gross1,approved);
		Log.println("Winthrop Dictionary exceptions.");
		for(String s:winthropOddness){
			Log.println(s);
		}

		/*
		fileIn = "data/periodtext/duchess.txt";
		String dictionaryOut2 = "data/perioddictionaries/duchess_vocab.txt";
		analyzer.createDictionaryFromText(fileIn, dictionaryOut2);
		*/
		
		
	}

	public static void test2(){
		String fileIn = "data/periodtext/winthrop.txt";
		String dictionaryOut1 = "data/perioddictionaries/winthrop_vocab.txt";
		
		TextAnalyzer analyzer = new TextAnalyzer();
		
		analyzer.createDictionaryFromText(fileIn, dictionaryOut1);

		Dictionary gross1 = new Dictionary();
		gross1.load(dictionaryOut1);
		
		Dictionary approved = new Dictionary();
		approved.load("data/dictionary.txt");
		Set<String> winthropOddness = analyzer.unapproved(gross1,approved);
		Log.println("Winthrop Dictionary exceptions.");
		for(String s:winthropOddness){
			Log.println(s);
		}

		/*
		fileIn = "data/periodtext/duchess.txt";
		String dictionaryOut2 = "data/perioddictionaries/duchess_vocab.txt";
		analyzer.createDictionaryFromText(fileIn, dictionaryOut2);
		*/
				
	}
	public static void test1(){
		
		/*
		 * 1. Load the dictionaries: dictionary.txt, uncommon_dictionary.txt and archaic_dictionary.txt
		 */
		TextAnalyzer ta = new TextAnalyzer();
		
		/*
		 * 2. Read the historical text used as a base and push the word list.
		 */
		//https://archive.org/stream/winthropsjourna05hosmgoog/winthropsjourna05hosmgoog_djvu.txt
		String fileName = "data/document.txt";
		String content = new TextFile(fileName).getText();

		ta.read(content);
		
		/*
		 * 3. Go through the source document and analyze if the word is in the dictionary.
		 * If yes, put it in the vocabulary
		 * If not, put it in the archaic list.
		 */
		ta.analyze();
		Set<String> vocab = ta.vocab;
		
		/*
		 * 4. add  names that I use to the approved vocab.
		 */
		TextAnalyzer.updateVocab(vocab,"data/names.txt");
		TextAnalyzer.updateVocab(vocab,"data/approved_words.txt");
		//Set<String> unreadable = ta.unreadable;
		
		Set<String> archaic = ta.archaic;
		Log.println("Vocab count:"+vocab.size());
		Log.println("Unrecognized count:"+archaic.size());
		Log.divider(80,"-");
		
		//Log.println("VOCAB");
		//Log.divider(80,"-");
		for(String v:vocab){
			//Log.println(v);
		}
		//Log.divider(80,"=");
		//Log.println("UNRECOGNIZED");
		//Log.divider(80,"-");
		
		/*
		 * Print out the list of words in the archaic list.
		 
		for(String a:archaic){
			Log.println(a);
		}
		*/
		
		/*
		 * 5. Read my quotes
		 */
		TextFile ch1 = new TextFile("data/book_ch01.txt");
		String ch1txt = ch1.getText();
		
		QuoteExtractor qe = new QuoteExtractor();
		qe.setText(ch1txt);
		qe.extractWords();
		Set<String> quotes = qe.results();
		
		/*
		 * 6. Count the words in the quotes.
		 */
		Log.divider(80,"=");
		Log.println("Quoted word count:"+quotes.size());
		Log.divider(80,"=");
		
		/*
		 * 7. The words which were not found in colonial vocabulary.
		 */
		quotes.removeAll(vocab);
		Log.println("Quoted words:"+quotes.size()+" after removal of known vocab");
		Log.println("May be too modern a vocabulary word:\n");
		//Log.divider(80,"=");
		for(String q:quotes){
			Log.println("'"+q+"'");
		}
	
		/*
		 * Now, have it show which line.
		 * 
		 * 
		 * x Next: extract the quotes, do a list compare, print diffs
		 * x B section in uncommon dictionary.
		 */
		Log.divider(80,"=");
		Log.println("Finished.");		
	}
	
}

/*
JLanguageTool langTool = new JLanguageTool(new English());
List<RuleMatch> matches = langTool.check("A sentence with a error in the Hitchhiker's Guide tot he Galaxy");
langTool.check()
for (RuleMatch match : matches) {
  System.out.println("Potential error at line " +
      match.getLine() + ", column " +
      match.getColumn() + ": " + match.getMessage());
  System.out.println("Suggested correction: " +
      match.getSuggestedReplacements());
}
http://wiki.languagetool.org/java-api#toc2
https://www.languagetool.org/development/
*/			
