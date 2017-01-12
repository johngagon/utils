package chp.dbreplicator.etl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import chp.dbreplicator.Logging;

public class TextLog implements Logging {
	private String filename;
	private File file;

	public TextLog(String str){
		super();
		this.filename = str;
		file = new File(filename);//doesn't have to close.
		
		
	}
	public void pp(String data){
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(file,true));
			writer.write(data+"\n");
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
		
	}
	public void print(String data) {
		pp(data+"\n");
	}	
	public void printException(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		print(sw.toString());
	}		
}
