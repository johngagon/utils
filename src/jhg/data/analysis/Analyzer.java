package jhg.data.analysis;

import java.util.*;

import jhg.util.Log;

public class Analyzer {

	private Analysis analysis;
	private List<String> errors;
	
	private Analyzer(String name){
		super();
		this.analysis = new Analysis(name);
		this.errors = new ArrayList<String>();
	}

	public void setHeader(Map<String,Integer> headerMap){
		analysis.setHeader(headerMap);
	}
	
	public Analysis getAnalysis(){
		return this.analysis;
	}

	public static Analyzer defaultInstance(String name) {
		return new Analyzer(name);
	}

	public void addRecord(String[] record) {
		analysis.addRecord(record);
		
	}

	public boolean analyze() {
		//TODO
		return true;
	}

	public void reportErrors() {
		for(String s:errors){
			Log.println(s);
		}
	}
	
	public static void main(String[] args){
		Test.execute();
	}
	
	
}
