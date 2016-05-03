package jhg.util;

import java.util.Hashtable;
import java.util.Map;

public class BusinessObject {

	/*
	 * check not null, etc.
	 * from json
	 * toxml
	 * tocsv
	 * from xml
	 * from csv
	 */
	
	private static final NameSet names = new NameSet();
	private static final Map<String,BusinessObject> bobjects = new Hashtable<String,BusinessObject>();
	
	public static synchronized BusinessObject createInstance(String name){
		if(names.contains(name)){
			throw new IllegalArgumentException("Name: "+name+" already taken.");
		}
		BusinessObject bo = new BusinessObject(name);
		bobjects.put(name,bo);
		names.add(name);
		return bo;
	}
	public static int getCount(){
		return bobjects.size();
	}
	public static BusinessObject getObject(String name){
		return bobjects.get(name);
	}
	
	private String name;
	
	private BusinessObject(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String toJSON(){
		StringBuffer buff = new StringBuffer();
		buff.append("{\"name\": \""+name+"\" ");
		if(!haveMore()){
			buff.append(endJson());
		}
		return buff.toString();
	}
	
	protected boolean haveMore(){
		return false;
	}
	protected final String endJson(){
		return "}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessObject other = (BusinessObject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BusinessObject [name=" + name + "]";
	}
	
	

}
