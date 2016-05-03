package jhg.util.types;

import java.util.regex.Pattern;

public class Text {

	public static final String ALPHANUM_NOSPACE = "[A-Za-z0-9]*";
	public static final String SENTENCE_REGEX = "^\\s+[A-Za-z,;'\\.\"\\s]+[.?!]$";
	
	protected String regex;
	protected String value;
	protected int min;
	protected int max;
	
	
	public Text(String v){
		this(v,ALPHANUM_NOSPACE,0,100);
	}
	
	public static boolean isValid(Text t, String text){
		return text.matches(t.regex);
	}
	
	public Text(String v, String r, int m, int x){
		this.regex = r;
		this.min = m;
		this.max = x;
		boolean isValid = v!=null && !v.isEmpty() && Pattern.matches(regex, v) && v.length()>=min && v.length()<=max;
		if(!isValid){
			throw new IllegalArgumentException("Value:'"+v+"' doesn't match regex:'"+regex+"' or comply with min:"+min+"  and max:"+max+" .");
		}else{
			this.value = v;
		}
	}
	public String toString(){
		return value;
	}

}
