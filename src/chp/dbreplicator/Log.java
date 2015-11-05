package chp.dbreplicator;

import static java.lang.System.out;

import java.util.*;

public class Log {
	public static boolean STACKS = false;
	
	private static final int LIM = 500;
	private static int limit;
	public static List<String> errors;
	
	static{
		errors = new Vector<String>();
		limit = 0;
	}
	
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
	
	public static void cr(){
		out.println("");
	}
	
	public static void cr(int times){
		for(int i=0;i<times;i++){
			out.println("");
		}
	}
	
	public static void hr(int length){
		hr(length,"-");
	}
	
	public static void hr(int length,String c){
		for(int i=0;i<length;i++){
			print(c);
		}
		cr();
	}
	
	public static void pl(String s){
		out.println(s);
	}
	
	public static void err(String s){
		if(limit<=LIM){
			errors.add(s);
			limit++;
	    }else{
	    	Log.pl("Log full:"+s);
	    }
	}
	
	public static void err(Exception e){
		err(e.getMessage());
	}	
	
	public static void error(Exception e) {//throws Exception{
		err(e.getMessage());
		exception(e);
		//throw e;
	}
	
	public static void exception(Exception e){
		if(STACKS){
			e.printStackTrace();
		}else{
			pl(e.getMessage());
		}
	}
	
	public static void flushErrors(){
		cr();
		pl("Errors");
		hr(80);
		for(String s:errors){
			pl(s);
		}
		limit = 0;
		errors.clear();
	}
	
	public static void print(String s){
		out.print(s);
	}
	
	public static void print(Collection<Object> list){
		for(Object o:list){
			out.println(o);
		}
	}
	
	public static void test(boolean test, String fail, String pass) {
		if(test){
			out.println(pass);
		}else{
			out.println(fail);
		}
		/*
		 * Other ideas: print tables in fixed width
		 * indent
		 * push error messages.
		 */
	}
	

}
