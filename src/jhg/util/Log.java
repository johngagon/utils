package jhg.util;

import static java.lang.System.out;

public class Log {

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
	public static void println(String s){
		out.println(s);
	}
	public static void print(String s){
		out.print(s);
	}
	public static void test(boolean test, String fail, String pass) {
		if(test){
			out.println(pass);
		}else{
			out.println(fail);
		}
	}
	

}
