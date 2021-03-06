package chp.dbreplicator;

import static java.lang.System.out;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Log implements Logging {
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
			printt(c);
		}
		cr();
	}
	public void print(String s){
		out.println(s);
	}
	public void pp(String s){
		out.print(s);
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
	
	private static long lastTime = 0L;
    private static long minuteElapse = 0L;
    private static final long MINUTE = 60000L;	
    private static final long MAX_TIME = 5000L;
	public static void profile(String s){
		long now = System.currentTimeMillis();
        DecimalFormat f = new DecimalFormat("00000000000000000000");//9223372036854775807 is max long
        long diff = now-lastTime;
        minuteElapse = minuteElapse+diff;
        String diffStr = null;
        if(diff>MAX_TIME){
        	if(lastTime==0L){
        		diffStr = "***"+f.format(0L);
        	}else{
        		diffStr = "!!!"+f.format(diff);
        	}
        }else{
            diffStr = "---"+f.format(diff);
        }
        out.println( " P:"+diffStr+" "+s );

        if(minuteElapse>=MINUTE){
            System.out.println( "Minute mark "+String.valueOf(new Date()));
            minuteElapse = 0L;
        }
        lastTime = now;		
		
	}
	
	public static void printt(String s){
		out.print(s);
	}
	
	public static void print(List<String> list){
		out.print("[");
		boolean first = true;
		for(String s:list){
			if(first){
				out.print(s);
				first=false;
			}else{
				out.print(","+s);
			}
		}
		out.print("]");
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

	public static void printt(List<Object> data) {
		for(Object o:data){
			pl(o.toString());
		}
		
	}
	

}
