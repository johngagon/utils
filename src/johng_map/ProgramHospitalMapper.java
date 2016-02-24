package johng_map;

import java.util.*;

public class ProgramHospitalMapper {

	private static List<Program> programs   = new ArrayList<Program>();
	private static List<Hospital> hospitals = new ArrayList<Hospital>();
	private static Map<State,List<Program>> statePrograms = new Hashtable<State,List<Program>>();

	
	
	public static void main(String[] args){
		generatePrograms();
		System.out.println("Program Count:"+programs.size());
		readHospitals();
		System.out.println("Hospital Count:"+hospitals.size());
		generateMap();
		generateProgramsPlan();//map to a current carrier id.
		System.out.println("Done.");
	}
	
	public static void generatePrograms(){
		int id = 0;
		for(State state:State.values()){
			id++;
			List<Program> stateProgramList = new ArrayList<Program>();
			Program p = new Program();
			p.id = id;
			p.code = state.name() + Program.Type.ACO.name() + "01";
			p.name = state.getLongName()+" Blue Preferred Health Care Program "+Program.Type.ACO.name();
			p.type = Program.Type.ACO;
			p.state = state;
			programs.add(p);
			stateProgramList.add(p);
			id++;
			p = new Program();
			p.id = id;
			p.code = state.name() + Program.Type.PCMH.name() + "02";
			p.name = state.getLongName()+" Blue Preferred Health Care Program "+Program.Type.PCMH.name();
			p.type = Program.Type.PCMH;
			p.state = state;
			programs.add(p);
			stateProgramList.add(p);
			statePrograms.put(state, stateProgramList);
					
		}
	}
	public static void readHospitals(){
		TextFile f = new TextFile("data/hospital/hospital.csv");
		//String content = f.getText();
		String[] lines = f.getLines();
		for(String line:lines){
			Hospital h = Hospital.parse(line, ",");
			if(h.isValid()){
				hospitals.add(h);
			}
		}		
	}	
	
	/*
	 *  1. Read and parse hospitals and programs.
	 *  2. Function to grab a random program in a state.
	 *  3. Loop through hospitals - generate random programs 
	 */
	public static void generateMap(){
		/*
		 * loop hospitals
		 * get state of hospital
		 * function: getRandomProgram(State s)
		 * 
		 * 
		 */
		Map<Hospital,List<Program>> programHospitals = new Hashtable<Hospital,List<Program>>();
		
		for(Hospital hospital:hospitals){
			State hs = hospital.state;
			
			if(hs!=null){
				List<Program> progs = statePrograms.get(hs);
				
				programHospitals.put(hospital, progs);
			}else{
				System.out.println("Hospital state null:"+hospital.id);
			}
		}
		
		printPrograms();
		
		//printProgramHospitals(programHospitals);
		
	}

	private static void printPrograms(){
		System.out.println("[");
		boolean first = true;
		for(Program p:programs){
			String sep = ",";
			if(first){sep="";first=false;}	
			System.out.println(sep+p.toString());
		}
		System.out.println("]");		
	}
	
	private static void printProgramHospitals(
			Map<Hospital, List<Program>> programHospitals) {
		System.out.println("[");
		boolean first = true;
		for(Hospital key:programHospitals.keySet()){
			String sep = ",";
			if(first){sep="";first=false;}
			List<Program>progs = programHospitals.get(key);
			for(Program p:progs){
				System.out.println(sep+"{"+key.id+":"+p.id+"}");
			}
		}
		System.out.println("]");
	}
	public static void generateProgramsPlan() {
		// TODO Auto-generated method stub
		
	}	


}
