package jhg.resume;

import java.util.*;

public class Search {

	
	private Score score;

	private double qualify;
	
	public Search(Score s, double d) {
		this.score = s;
		this.qualify = d;
	}

	public String scoreReport(Resume r) {
		String topLine = "Analyzing Resume for :"+r.getFilename()+" .\n";
		int scoreVal = 0;
		int noSkill = 0;
		score.load();
		//String content = r.read();
		
		Set<String> contentWords = r.getWords();
		if(contentWords.size()>0){
			StringBuffer foundLog = new StringBuffer();
			StringBuffer notfoundLog = new StringBuffer();
			for(Skill s:score.skills){
				List<String> matchTokens = s.tokens;
				boolean found = false;
				for(String t:matchTokens){
					if(contentWords.contains(t)){
						foundLog.append("Skill - "+s.name+" : '"+t+"',  adding score: "+s.score+"  ,tally: "+scoreVal+".\n");
						found = true;
						scoreVal += s.score;
						break;
					}
				}
				if(!found){
					noSkill += s.score;
					notfoundLog.append("Not found:  "+s.name+"  - '"+s.tokens+"',  no score ("+s.score+")  .\n");
				}
			}
			/*
			 * iterate over the skills
			 */
			double pct = (double)scoreVal / ((score.getTotal()==0)?1:score.getTotal());
			String finalLine = "Score Report: "+scoreVal+" out of "+score.getTotal()+" possible. Missed "+noSkill+" points.   P: " + pct + "  qualify("+qualify+"): "+(pct>=qualify)+" ";
			return topLine + foundLog.toString() + notfoundLog.toString() + finalLine;
		}else{
			return " No content found. \n";
		}
		 
	}

}
