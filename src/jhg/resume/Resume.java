package jhg.resume;

import jhg.util.TextFile;
import java.util.*;

public class Resume {

	private String filename;
	

	public Resume(String resumeFileName) {
		this.filename = resumeFileName;
	}
	public String getFilename(){
		return this.filename;
	}	
	public String read(){
		TextFile f = new TextFile(filename);
		return f.getText().toLowerCase();
	}
	public Set<String> getWords(){
		TextFile f = new TextFile(filename);
		return f.getWordsLowerCase();
	}

}
