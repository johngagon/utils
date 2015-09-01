package jhg.util;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.*;
import java.util.*;
import java.util.regex.*;



public class TextFile {

	private String content;
	
	public TextFile(String fileName) {
		try {
			this.content = new String(readAllBytes(get(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public String getText(){
		return content;
	}

	public String[] getLines(){
		return content.split("\n");
	}
	public Set<String> getWords(){
		Set<String> strings = new TreeSet<String>();
		Pattern pattern = Pattern.compile("[\\w']+");	
		Matcher m = pattern.matcher(content);
		String word = "";
		while ( m.find() ) {
			word = content.substring(m.start(),m.end());
			strings.add(word);
		}
		return strings;
	}
	
	public static void main(String[] args){
		TextFile f = new TextFile("data/notes.txt");
		//String content = f.getText();
		String[] lines = f.getLines();
		//Log.println(content);
		int i=0;
		for(String s:lines){
			Log.print(i+":"+s);
			i++;
			
		}
	}
}
