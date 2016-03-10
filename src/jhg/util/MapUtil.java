package jhg.util;

import java.util.*;
import jhg.util.Log;
public class MapUtil {

	
	
	public static void debugMap(Map<String,String> map){
		Log.print("Map size:"+map.size());
		for(String key:map.keySet()){
			Log.println("'"+key+"':'"+map.get(key)+"'");
		}
	}
	
}
