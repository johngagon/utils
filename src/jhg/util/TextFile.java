package jhg.util;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



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
	
	public static boolean write(String filename, String data) {
		BufferedWriter writer = null;
		boolean rv = false;
		try {
			File file = new File(filename);
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(data);
			rv = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer!=null)
					writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rv;
	}
	
	public static boolean append(String filename, String data) {
		BufferedWriter writer = null;
		boolean rv = false;
		try {
			File file = new File(filename);
			writer = new BufferedWriter(new FileWriter(file,true));
			writer.write(data);
			rv = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer!=null)
					writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rv;
	}	
		  
	
	public static void main(String[] args){
		testWrite();
	}
	
	public static void testWrite(){
		String filename = "data/out.txt";
		String data = "abc\ndef\nghi";
		if(TextFile.write(filename,data)){
			Log.print("Success.");
		}
	}
	
	private static Map<String, String> parseMapping(String[] lines) {
		Map<String,String> rv = new LinkedHashMap<String,String>();
		for(String line:lines){
			
			if(!line.startsWith("#") && line.contains(":")){
				
				int colonPos = line.indexOf(":");
				int endPos = line.length()-1;
				String left = line.substring(0,colonPos);
				String right = line.substring(colonPos+1,endPos);
				rv.put(left,right);
			}
		}
		
		return rv;
	}	

	public Map<String, String> getMapping(){
		String[] lines = this.getLines();
		return parseMapping(lines);
	}
	
	public static void testRead(){
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
