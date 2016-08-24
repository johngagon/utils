package jhg.game.alchemist;

public class Game {

	
	public Mon birth(Mon a, Mon b){
		return null;//TODO impl
	}
	public Mon birth(Spore a, Mon b){
		return null;//TODO impl
	}
	public Mon birth(Spore a, Spore b){
		return null;//TODO impl
	}
	
	
	/*
	 * Combine success = roll between average and sum health+mana points. number of wins and losses
	 * 
	 * Calculate level. 
	 * 
	 * 	e.g.: fire, water: 1+1 ore 2nd level. 
	 *  additive:
	 * 
	 * Calculate bucket from level.
	 *  	each spore gets 2 points and up to 2 trades / the max bucket of points up to level 3.
	 *  
	 *  	at level 4 and 5 and above, random between x/2 rounded down + x/2 and can trade any stat. 
	 *  
	 * Calculate stats from bucket, calculate random trades.
	 *  
	 * 	stats: hp, mp, ap, dp.
	 * 
	 * 		health points  consumed by lack of defense
	 * 		mana points:   consumed by attacking
	 * 		attack point:  how much attack
	 * 		defense point: how much defense
	 *      wins:
	 *      losses:
	 * 
	 *  bucket: 1 pt = 10hp, 2mp, 1ap, 1dp
	 *  trade:  10hp, 2mp, 1ap, 1dp = 1pt
	 *  
	 *  minimum: 10hp, 5mp, 0ap, 0dp, (not like magic)
	 *    
	 *  base spore: HP:10, MP:5, AP:1, DP:1
	 *  
	 *  each spore gets 2 points and up to 2 trades / the max bucket of points up to level 3.
	 *  
	 *  at level 4 and 5 and above, random between x/2 rounded down + x/2 and can trade any stat. 
	 *  
	 *  fire spore: HP:10, MP:7*, AP:3*, DP:0    (2 pts, 1 trade)
	 *  aqua spore: HP:20, MP:5,  AP:1, DP:2     (2 pts)
	 *  tera spore: HP:30, MP:5,  AP:0, DP:3     (2 pts, 1 trade)
	 *  aero spore: HP:20, MP:5,  AP:2, DP:1	 (2 pts)	 
	 * 
	 *   
	 * 
	 * add to index
	 * 
	 */
	
	
	/*
	 * Combat: simultaneous.
	 * 
	 *   
	 * 
	 * 
	 * Attack-defense: fire vs terra: 3-3   (roll 3d6) (roll 3d6). higher rolls determines success of attack or defense.
	 *                attack success: if(mp>0)roll 3d2      for each "2", subtract 1 hp from defender.
	 *                defend success: if(mp>0)roll 3d2      for each "2", subtract 1 mp from attacker.
	 *                              
	 * Attack-defense: terra vs fire: 0-0   (0d6)          
	 * 
	 * When one's mp is   0, the one who is not 0 subdues.  They keep their hp/heal after but the subdued one loses 1mp and attacker gains 1mp
	 * When one's hp is   0, the one who is not 0 kills.    The one who is 0 is gone. 
	 * 
	 * 
	 * 
	 */
	
}
