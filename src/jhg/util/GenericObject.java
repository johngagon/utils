package jhg.util;

import java.util.*;

import jhg.util.Value.ValidationException;

@SuppressWarnings("boxing")//@SuppressWarnings("rawtypes")
public class GenericObject { 
	//add logging?

	public static final GenericObject DEFAULT = new GenericObject();
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	
	protected static Set<String> fieldNames = new TreeSet<String>();
	
	static{
		fieldNames.add(NAME);
		fieldNames.add(DESCRIPTION);
	}
	
	private static Integer nextId = 0;
	private static List<GenericObject> instances = new ArrayList<GenericObject>();
	
	
	public static GenericObject createInstance(){
		GenericObject go = new GenericObject(new Hashtable<String,Value>());
		go.isValid = false;
		return go;
	}
	public static List<GenericObject> getList(){
		List<GenericObject> gList = new ArrayList<GenericObject>();
		for(GenericObject g:instances){
			if(!g.deleted){
				gList.add(g);
			}
		}
		return gList;
	}
	//TODO way to purge deleted.
	
	public static boolean hasObject(Integer id){
		return instances.get(id)!=null;
	}
	
	public static GenericObject get(Integer id){
		if(!hasObject(id)){
			throw new IllegalArgumentException("Generic Object id:"+id+" doesn't exist.");
		}
		return instances.get(id);
	}

	

	//subclasses will override these names.
	
	private Integer id=0;
	private boolean isDefault=false;
	private boolean isValid;
	private boolean deleted=false;
	
	private Map<String,Value> props;

	
	
	private GenericObject(){
		this.id = -1;
		this.isDefault = true;
		this.isValid = false;
	}
	
	
	private GenericObject(Map<String,Value> m){
		this.props = m ;
		nextId++;
		this.id = nextId;
		instances.add(this);
	}
	
	protected void validate() throws ValidationException{
		this.isValid = false;//subclasses should further validate for type, size, etc. go through the map and place validation rules on each object.
		boolean tempValid = true;
		for(String s:props.keySet()){
			Value v = props.get(s);
			v.validate();
			tempValid = tempValid && v.isValid();
		}
		isValid = tempValid;
	}
	
	public void delete(){
		this.deleted = true;
	}
	public boolean isDeleted(){
		return this.deleted;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	
	public void initialize(Map<String,Value> p) throws ValidationException{
		if(p==null)throw new IllegalArgumentException("Cannot initialize with null properties.");
		if(p.size()!=fieldNames.size())throw new IllegalArgumentException("Cannot initialize without all properties. Map size:"+p.size()+" expected:"+fieldNames.size());
		for(String k:p.keySet()){
			if(k==null)throw new IllegalArgumentException("Cannot initialize properties with null keys.");
			if(!fieldNames.contains(k))throw new IllegalArgumentException("Cannot initialize properties with bad key names.");
			if(p.get(k)==null)throw new IllegalArgumentException("Cannot initialize properties with null values.");
		}
		this.props = p;
		validate();

	}

	public Object getProperty(String k){
		if(!isValid()){
			throw new IllegalStateException("Not valid.");
		}
		return props.get(k);
	}

	public boolean isDefault(){
		return this.isDefault;
	}
	
	public boolean isValid(){
		return this.isValid;
	}
	

	
	public Map<String,Object> getProperties(){
		if(!isValid()){
			throw new IllegalStateException("Not valid.");
		}
		return Collections.unmodifiableMap(props);
	}
	
	/*
	 * Generic object has this:
	 * uniqueness
	 * not null pattern
	 * 
	 * hashCode
	 * equals
	 * toString (basic)
	 */
	@Override
	public String toString() {
		return "GenericObject [id=" + id + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		GenericObject other = (GenericObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
