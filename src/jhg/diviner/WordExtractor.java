package jhg.diviner;

import java.util.Set;

import jhg.util.Log;
import jhg.util.TextFile;

public class WordExtractor {

	public static void main(String[] args){
		String fileName = "data/diviner_source.txt";
		TextFile tf = new TextFile(fileName);	
		Set<String> words = tf.getWords();
		
		Log.print(words);
	}
	
}
