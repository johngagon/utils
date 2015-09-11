package cache;

public class Upload {

	
	private String year;
	private String id;
	
	public Upload(String year, String id) {
		this.year = year;
		this.id = id;
	}
	public String year(){
		return this.year;
	}
	public String id(){
		return this.id;
	}

}
