package jhg.util;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataStructureUtil {
	public static Integer[] autoBoxIntArray(int[] array){
		Integer[] result = new Integer[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}
	public static Long[] autoBoxLongArray(long[] array){
		Long[] result = new Long[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}
	public static Boolean[] autoBoxBooleanArray(boolean[] array){
		Boolean[] result = new Boolean[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}	
	public static Character[] autoBoxCharArray(char[] array){  //note: there is very little reason to use this. it's here for consistency sake.
		Character[] result = new Character[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}		
	public static Double[] autoBoxDoubleArray(double[] array){
		Double[] result = new Double[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}	
	//todo: autobox the matrices
	
	public static int[] createIntArray(int q, int defaultValue){
		int[] r = new int[q];
		for(int i=0;i<q;i++){
			r[i] = defaultValue;
		}
		return r;
	}
	public static long[] createLongArray(int q, long defaultValue){
		long[] r = new long[q];
		for(int i=0;i<q;i++){
			r[i] = defaultValue;
		}
		return r;
	}	
	public static boolean[] createBooleanArray(int q, boolean defaultValue){
		boolean[] r = new boolean[q];
		for(int i=0;i<q;i++){
			r[i] = defaultValue;
		}
		return r;
	}	
	public static double[] createDoubleArray(int q, double defaultValue){
		double[] r = new double[q];
		for(int i=0;i<q;i++){
			r[i] = defaultValue;
		}
		return r;
	}	
	public static char[] createCharArray(int q, char defaultValue){
		char[] r = new char[q];
		for(int i=0;i<q;i++){
			r[i] = defaultValue;
		}
		return r;
	}
	public static String[] createStringArray(int q, String defaultValue){
		String[] r = new String[q];
		for(int i=0;i<q;i++){
			r[i] = defaultValue;
		}
		return r;
	}	
	
	public static int[][] createIntMatrix(int p, int q, int defaultValue){
		int[][] r = new int[p][q];
		for(int i=0;i<p;i++){
			for(int j=0;j<q;j++){
				r[i][j] = defaultValue;
			}
		}
		return r;
	}
	public static long[][] createLongMatrix(int p, int q, long defaultValue){
		long[][] r = new long[p][q];
		for(int i=0;i<p;i++){
			for(int j=0;j<q;j++){
				r[i][j] = defaultValue;
			}
		}
		return r;
	}	
	public static boolean[][] createBooleanMatrix(int p, int q, boolean defaultValue){
		boolean[][] r = new boolean[p][q];
		for(int i=0;i<p;i++){
			for(int j=0;j<q;j++){
				r[i][j] = defaultValue;
			}
		}
		return r;
	}
	public static double[][] createDoubleMatrix(int p, int q, double defaultValue){
		double[][] r = new double[p][q];
		for(int i=0;i<p;i++){
			for(int j=0;j<q;j++){
				r[i][j] = defaultValue;
			}
		}
		return r;
	}	
	public static char[][] createCharMatrix(int p, int q, char defaultValue){
		char[][] r = new char[p][q];
		for(int i=0;i<p;i++){
			for(int j=0;j<q;j++){
				r[i][j] = defaultValue;
			}
		}
		return r;
	}		
	public static String[][] createStringMatrix(int p, int q, String defaultValue){
		String[][] r = new String[p][q];
		for(int i=0;i<p;i++){
			for(int j=0;j<q;j++){
				r[i][j] = defaultValue;
			}
		}
		return r;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map createHashtable(Set keys, Object defaultValue){
		Map rv = new Hashtable();
		for(Object key:keys){
			rv.put(key,defaultValue);
		}
		return rv;
	}
	
	public static boolean validateArray(Object[] arr){
		if(arr==null){return false;}
		if(arr.length==0){return true;}
		for(int i=0;i<arr.length;i++){
			if(arr[i]==null)return false;
		}
		return true;
	}

	
	
	@SuppressWarnings("rawtypes")
	public static boolean validateList(List list){
		if(list==null){return false;}
		if(list.size()==0){return true;}
		for(Object o:list){
			if(o==null)return false;
		}
		return true;
	}	
	
	@SuppressWarnings("rawtypes")
	public static boolean validateMap(Map map){
		if(map==null)return false;
		if(map.size()==0){return true;}
		for(Object o:map.keySet()){
			if(o==null)return false;
			if(map.get(o)==null){
				return false;
			}
		}
		return true;
	}	
	
	public static void main(String[] args){
		String[] sa = new String[5];
		for(int i=0;i<sa.length;i++){
			System.out.println(sa[i].length());
		}
	}
	
	/*
	 * int,long,boolean,double,char,String (object)
	 */
}
