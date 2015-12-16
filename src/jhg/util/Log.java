package jhg.util;

import static java.lang.System.out;

import java.util.*;

public class Log {

	public static boolean STORE = true;
	public static String filename = "data/log.txt";
	private static StringBuffer buff = new StringBuffer();

	public static void quoteln(String s){
		out.println("'"+s+"'");
	}
	public static void divider(int length, String s){
		out.print("\n");
		for(int i=0;i<length;i++){
			out.print(s);
		}
		out.print("\n");
	}
	public static void flushToFile(){
		TextFile.write(filename,buff.toString());		
	}
	public static void println(String s){
		if(STORE){
			buff.append(s);
		}

		out.println(s);
	}
	public static void print(String s){
		out.print(s);
	}
	
	@SuppressWarnings("rawtypes")
	public static void print(Collection list){
		for(Object o:list){
			out.println(o);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void print(Map map){
		for(Object k:map.keySet()){
			out.println(k+":"+map.get(k));
		}
	}
	
	public static void test(boolean test, String fail, String pass) {
		if(test){
			out.println(pass);
		}else{
			out.println(fail);
		}
	}
	

}
