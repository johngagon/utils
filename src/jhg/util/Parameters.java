package jhg.util;

import java.util.*;

public class Parameters {

	private Map<String,Object> values;
	
	public Parameters(){
		this.values = new Hashtable<String,Object>();
	}
	
	public Parameters(Map<String,Object> values) {
		this.values = values;
		
	}

	public Map<String, Object> getValues() {
		return values;
	}
	
	public static boolean isValid(Map<String,Object> map){
		boolean rv = false;
		int size = map.size();
		int i = 1;
		for(String s:map.keySet()){
			Object o = map.get(s);
			if(o==null){
				break;
			}
			i++;
		}
		if(i==size){
			rv = true;
		}
		return rv;
	}
	

}
