package jhg.acl;

import java.util.*;

@SuppressWarnings("rawtypes")
public class ObjectRegistry {

	
	
	private Map<Class,User> owners;
	
	public static final ObjectRegistry getInstance(){
		return instance;
	}
	private static final ObjectRegistry instance = new ObjectRegistry();
	
	private ObjectRegistry(){
		owners = new Hashtable<Class,User>();
	}
	
	public User getOwner(Class c){
		return owners.get(c);
	}
	public void register(Class c, User owner){
		owners.put(c, owner);
	}
}
