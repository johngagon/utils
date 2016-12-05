package jhg.weaseler;

import java.util.*;

import jhg.util.TextFile;

public class Score {

	List<Weasel> weasels = new ArrayList<Weasel>();
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
			Weasel weasel = new Weasel();
			if(s.length()>1){
				String[] weaselStr = str.split(":");
				if(weaselStr.length<3){
					throw new IllegalStateException("Could not parse line:'"+s+"'");
				}
				try{
					weasel.score = Integer.parseInt(weaselStr[SCORE]);
					totalScore += weasel.score;
				}catch(NumberFormatException nfe){
					throw new IllegalStateException("Could not parse ");
				}
				weasel.name = weaselStr[NAME];
				weasel.tokens = Arrays.asList(weaselStr[TOKENS].split(","));
				weasels.add(weasel);
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
