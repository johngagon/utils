package jhg.resume;

import java.util.*;

import jhg.util.TextFile;

public class Score {

	List<Skill> skills = new ArrayList<Skill>();
	private String fileName;
	private int totalScore = 0;
	
	public Score(String scoreFileName) {
		this.fileName = scoreFileName;
	}
	
	private static final int SCORE = 0;
	private static final int NAME = 1;
	private static final int TOKENS = 2;
	
	public void load() {
		TextFile f = new TextFile(fileName);
		String[] lines = f.getLines();
		for(String s:lines){
			String str = s.trim();
			Skill skill = new Skill();
			if(s.length()>1){
				String[] skillStr = str.split(":");
				if(skillStr.length<3){
					throw new IllegalStateException("Could not parse line:'"+s+"'");
				}
				try{
					skill.score = Integer.parseInt(skillStr[SCORE]);
					totalScore += skill.score;
				}catch(NumberFormatException nfe){
					throw new IllegalStateException("Could not parse ");
				}
				skill.name = skillStr[NAME];
				skill.tokens = Arrays.asList(skillStr[TOKENS].split(","));
				skills.add(skill);
				//Log.println(skill.toString());
			}else{
				//Log.println("[blank]");
			}
			//Log.println("");
			
		}			
		
	}
	public int getTotal(){
		return totalScore;
	}
}
