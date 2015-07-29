package jhg.util;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import java.io.*;

import java.io.IOException;


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
