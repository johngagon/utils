package jhg.util;

import java.util.Map;
public class MapUtil {

	public static String mapToString(Map<String,String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		boolean first = true;
		if(first){
			sb.append(",");
			first = false;
		}
		for(String key:map.keySet()){
			sb.append(key+":'"+map.get(key)+"'");
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static void debugMap(Map<String,String> map){
		Log.print("Map size:"+map.size());
		for(String key:map.keySet()){
			Log.println("'"+key+"':'"+map.get(key)+"'");
		}
	}
	
}
