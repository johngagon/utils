package johng_map;

import johng_map.Program.Type;

public class Hospital implements Comparable<Hospital> {

	public Integer id=-1;
	public State state=State.UD;
	public String name="Undefined";
	public String city = "";
	public String address = "";
	
	public static Hospital parse(String line, String delim){
		Hospital h = new Hospital();
		String[] values = line.split(delim+"(?=([^\"]*\"[^\"]*\")*[^\"]*$)");//= line.split(delim);
		for(String v:values){
			System.out.println("  Value:"+v);
		}
		int numFields = 4;
		if(values.length>=numFields){
			if(values[0]!=null){
				String idS = values[0].replaceAll("\"", "");
				Integer id = -1;
				try{
					id = Integer.parseInt(idS);
				}catch(NumberFormatException nfe){
					System.out.println("Line:"+line+" had exception:"+nfe.getMessage()+" the value of the id string is '"+idS+"'");
					
					//nfe.printStackTrace();
				}
				h.id = id;
			}else{
				h.id = -1;
			}

			if(values[1]!=null){
				String name = values[1].replaceAll("\"", "");
				h.name = name;
			}else{
				h.name = "Undefined.";
			}		
			if(values[2]!=null){
				String address = values[3].replaceAll("\"", "");
				h.address = address;
			}else{
				h.address = "";
			}				

			if(values[3]!=null){
				String city = values[3].replaceAll("\"", "");
				h.city = city;
			}else{
				h.city = "";
			}
			if(values[4]!=null){
				String stateS = values[4].replaceAll("\"", "").trim();//.substring(0,2);
				//System.out.println("State code:'"+stateS+"'");
				State state = State.valueOf(stateS);//State.fromLongName(stateS);
				if(state==null){
					System.out.println("Hospital id:"+h.id+" has null state from String:'"+stateS+"'");
				}
				h.state = state;
			}else{
				h.state = State.UD;
			}
			

			
		}else{
			//System.out.println("Parsing resulted in the wrong amount of data."+line);
		}
		return h;
	}
	
	public boolean isValid(){
		return (this.id!=-1 && (!State.UD.equals(this.state)));
	}
	
	public String toString(){
		String str ="hospital:{id:"+id+",name:'"+name+"',state:'"+state.name()+"'}"; 
		return str;
	}
	
	public static void main(String[] args){
		testParse();
	}
	
	private static void testParse(){
		String toParse = "38367;\"Florida\";\"Florida Hospital Wesley Chapel\"";
		Hospital h = parse(toParse,";");
		System.out.println(h);
	}

	@Override
	public int compareTo(Hospital o) {
		return this.id.compareTo(o.id);
	}	
	
}
