package jhg.diviner;

import jhg.util.Log;
import jhg.util.TextFile;
import java.util.*;

public class WordExtractor {

	public static void main(String[] args){
		String fileName = "data/diviner_source.txt";
		TextFile tf = new TextFile(fileName);	
		Set<String> words = tf.getWords();
		
		Log.print(words);
	}
	
}
