package chp.datareceiving;

import java.util.*;
import java.util.regex.Pattern;

public class FileIdentity {
	
	private Pattern nameMatchRule;
	private Boolean isZip;
	private List<ScanRule> rules;
	private DataLayout layout;
	private String name;
	private boolean hasHeader;
	private String[] headers;
	private String header;
	
	public FileIdentity(String s){
		super();
		this.name = s;
		isZip = false;
		this.rules = new ArrayList<ScanRule>();
		this.layout = DataLayout.CSV;
		this.hasHeader = true;
		
	}
	public String toString(){
		return this.name;
	}

	public Boolean isZip() {
		return this.isZip;
	}
	public boolean hasHeader(){
		return this.hasHeader;
	}
	public void noHeader(){
		this.hasHeader = false;
	}
	public List<ScanRule> getRules() {
		return this.rules;
	}

	public DataLayout getLayout() {
		return this.layout;
	}

	public void nameMatchRule(String string) {
		this.nameMatchRule = Pattern.compile(string);
	}
	public boolean matches(String name){
		return nameMatchRule.matcher(name).find();
	}

	@SuppressWarnings("boxing")
	public void setZip(boolean b) {
		this.isZip = b;
	}
	public void setDataLayout(DataLayout dl){
		this.layout = dl;
	}


	public void add(ScanRule rule) {
		this.rules.add(rule);
	}
	public void addHeader(String s) {
		this.header = s;
		this.headers = s.split("\\t");
	}
	public String header(){
		return this.header;
	}
	public String[] headers(){
		return this.headers;
	}
}
