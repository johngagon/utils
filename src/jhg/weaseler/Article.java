package jhg.weaseler;

import java.util.HashSet;
import java.util.Set;

import jhg.util.TextFile;

public class Article {


	private String filename;
	

	public Article(String resumeFileName) {
		this.filename = resumeFileName;
	}
	public String getFilename(){
		return this.filename;
	}	
	public String read(){
		TextFile f = new TextFile(filename);
		if(!f.hasContent()){
			return "";
		}else{
			return f.getText().toLowerCase();
		}
	}
	public Set<String> getWords(){
		TextFile f = new TextFile(filename);
		if(f.hasContent()){
			return f.getWordsLowerCase();
		}else{
			return new HashSet<String>();
		}
		
	}
	@Override
	public String toString() {
		return "Article [filename=" + filename + "]";
	}


}
