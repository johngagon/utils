package johng_map;

public class Program {

	public static enum Type{
		ACO,PCMH
	}
	
	public int id=0;
	public String code="null";
	public String name="null";
	public Type type=Type.ACO;
	public State state = State.DC;
	public String elevator_pitch = "Lorem ipsum.";
	public int members_attributed = 550;
	public double annual_savings = 2325432.99;
	public String video="https://www.youtube.com/watch?v=CIHxczeOIRw"; 
	
	public static Program parse(int id, String line, String delim){
		Program p = new Program();
		String[] values = line.split(";");
		int numFields = 3;
		if(values.length==numFields){
			p.id = id;
			if(values[0]!=null){
				String code = values[0].replaceAll("\"", "");
				p.code = code;
				String stateS = code.substring(0,2);
				State state = State.valueOf(stateS);
				p.state = state;
			}
			if(values[1]!=null){
				String name = values[1].replaceAll("\"", "");
				p.name = name;
			}
			if(values[2]!=null){
				String typeS = values[2].replaceAll("\"", "");
				Type type = Type.valueOf(typeS);
				p.type = type;
			}
		}
		return p;
	}
	
	public String toString(){
		String str ="{id:"+id
				+",code:'"+code
				+"',name:'"+name
				+"',type:'"+type.name()
				+"',state:'"+state.name()
				+"',elevator_pitch:'"+elevator_pitch
				+"',members_attributed:'"+members_attributed
				+"',annual_savings:'"+annual_savings
				+"',video:'"+video
				+"'}"; 
		return str;
	}
	
	public static void main(String[] args){
		testParse();
	}
	
	private static void testParse(){
		String toParse = "\"CAACO01\";\"Collaborative Model\";\"ACO\"";
		Program p = parse(1,toParse,";");
		System.out.println(p);
	}
}
