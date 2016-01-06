package jhg.util;

import java.util.*;

import jhg.util.Value.ValidationException;

/**
 * GenericObjects meets several constraints for safe programming.
 * 1. Every instance is unique.
 * 2. Every instance is locatable by an integer and statically managed by class. You can look to see if it's there.
 * 3. Generic objects can have a variety of properties, each has unique name in a Set<String>
 * 4. Those properties are checked when initialized. All properties initialized at once.
 * 5. There is a default instance in the event your program doesn't yet have assignment.
 * 6. Deleted objects fill a position but do not waste space or leave holes in lists.
 * 7. Shallow immutable.
 * 
 * @author jgagon
 *
 */
@SuppressWarnings("boxing")//@SuppressWarnings("rawtypes")
public class GenericObject { 
	//add logging?

	/*
	 * Events: create, initialize, delete
	 * initialize fail
	 * delete fail
	 */
	
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	
	/*
	 * Note: not final
	 */
	protected static final Set<String> fieldNames = new TreeSet<String>();
	
	static{
		fieldNames.clear();
		fieldNames.add(NAME);
		fieldNames.add(DESCRIPTION);
	}
	
	private static Integer nextId = 0;
	private static List<GenericObject> instances = new ArrayList<GenericObject>();
	
	public static final GenericObject DEFAULT = new GenericObject(0);
	
	/*
	 * Note: not final.
	 */
	public static GenericObject createInstance(){
		GenericObject go = new GenericObject();
		go.isValid = false;
		return go;
	}
	
	public static final Map<String,Value> copyProperties(GenericObject go){
		if(go==null)throw new IllegalArgumentException("Parameter can't be null.");
		Map<String,Value> unmodifiableMap = go.getProperties();
		Map<String,Value> copied = new Hashtable<String,Value>(unmodifiableMap);
		return copied;
	}
	
	public static final int size(){
		return instances.size();
	}
	
	public static enum Subset{
		DELETED,ACTIVE,UNINITIALIZED
	}
	
	public static final List<GenericObject> getAll(){
		return Collections.unmodifiableList(instances);
	}
	
	public static final List<GenericObject> getList(Subset s){
		List<GenericObject> gList = new ArrayList<GenericObject>();
		for(GenericObject g:instances){
			if(Subset.ACTIVE.equals(s)){
				if(!g.deleted && g.isValid && !g.isDefault){
					gList.add(g);
				}
			}else if(Subset.DELETED.equals(s)){
				if(g.deleted && g.isValid && !g.isDefault){
					gList.add(g);
				}				
			}else if(Subset.UNINITIALIZED.equals(s)){
				if(!g.isValid && !g.isDefault){
					gList.add(g);
				}
			}
		}
		return gList;
	}
	//TODO way to purge deleted.
	
	public static final boolean hasObject(Integer id){
		return instances.get(id)!=null;
	}
	
	public static final GenericObject get(Integer id){
		if(!hasObject(id)){
			throw new IllegalArgumentException("Generic Object id:"+id+" doesn't exist.");
		}
		return instances.get(id);
	}

	

	//subclasses will override these names.
	
	private Integer id=0;
	protected boolean isDefault=false;
	protected boolean isValid;
	protected boolean deleted=false;
	
	private Map<String,Value> props = new Hashtable<String,Value>();
	
	private GenericObject(int x){
		this.id = -1;
		this.isDefault = true;
		this.isValid = false;
		instances.add(this);
	}
	
	/*
	 * Note: protected
	 */
	protected GenericObject(){
		
		nextId++;
		this.id = nextId;
		this.isValid = false;
		instances.add(this);
	}
	
	/*
	 * Note: not final
	 */
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
	
	public final void delete(){
		if(DEFAULT.equals(this))throw new IllegalStateException("Cannot delete default instance.");
		this.deleted = true;
		this.isValid = false;
		this.props = new Hashtable<String,Value>();
	}
	
	public final boolean isDeleted(){
		return this.deleted;
	}
	
	public final Integer getId(){
		return this.id;
	}
	
	/*
	 * There are 8 conditions for intializing:
	 * 1. Can't already be initialized.						Invariant: IllegalStateException
	 * 2. Cannot initialize the default instance.
	 * 3. Cannot initialize with null properties parameter	Preconditions: IllegalArgumentException
	 * 4. Cannot initialize with missing properties
	 * 5. Cannot initialize with any null keys.
	 * 6. Cannot initialize with with bad properties
	 * 7. Cannot initialize with any null property values.
	 * 8. Properties have to have valid values. 			Postconditions: ValidatoinException->IllegalArgumentException
	 */
	public final void initialize(Map<String,Value> p){
		if(isValid)throw new IllegalStateException("Object already initialized.");
		if(DEFAULT.equals(this))throw new IllegalStateException("Cannot initialize default instance.");
		if(p==null)throw new IllegalArgumentException("Cannot initialize with null properties.");
		if(p.size()!=fieldNames.size())throw new IllegalArgumentException("Cannot initialize without all properties. Map size:"+p.size()+" expected:"+fieldNames.size());
		for(String k:p.keySet()){
			if(k==null)throw new IllegalArgumentException("Cannot initialize properties with null keys.");
			if(!fieldNames.contains(k))throw new IllegalArgumentException("Cannot initialize properties with bad key names.");
			if(p.get(k)==null)throw new IllegalArgumentException("Cannot initialize properties with null values.");
		}
		this.props = p;
		try{
			validate();
		}catch(ValidationException ve){
			throw new IllegalArgumentException(ve.getMessage());
		}
	}

	public final Value getProperty(String k){
		if(!isValid()){
			throw new IllegalStateException("Not valid.");
		}
		return props.get(k);
	}

	public final boolean isDefault(){
		return this.isDefault;
	}
	
	public final boolean isValid(){
		return this.isValid;
	}
	
	public final Map<String,Value> getProperties(){
		if(!isValid()){
			throw new IllegalStateException("Not valid.");
		}
		return Collections.unmodifiableMap(props);
	}
	
	/*
	 * Generic object has this:
	 * uniqueness
	 * not null pattern
	 * Note: not final
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
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}



	@Override
	public final boolean equals(Object obj) {
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
