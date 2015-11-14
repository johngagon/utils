package jhg.gedcom.tools.crawler;

import java.util.*;
import java.net.*;

public class Famous {

	private String name;
	private List<Link> links;
	public Famous(String _name){
		this.name = _name;
		this.links = new ArrayList<Link>();
	}
	
	public void add(String _title, String _url){
		Link link = new Link();
		link.title = _title;
		link.url = _url;
		links.add(link);
	}
	public List<Link> getLinks(){
		return this.links;
	}
	public String getName(){
		return this.name;
	}

	public int linkCount() {
		return links.size();
	}
}
