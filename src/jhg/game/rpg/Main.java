package jhg.game.rpg;

public class Main {

	public static void main(String[] args) {
		/*
		 * Instantiate a world.
		 * 
		 * 
		 * skill requires energy.
		 * 
		 * Create a grid (8x8) of squares and assign addresses to connect them.
		 * Get random starting point and determine win square and win square challenge.
		 * 
		 * For each grid, create challenges:
		 * 		shopping challenge: 
		 * 
		 * 		Challenge: Shopkeeper   Skill: Barter,Gold        Chance is sliding scale with gold.  Thief bonus.
		 *      Challenge: Hidden Item  Skill: Search   Rewards: Item (key)  Chance improves with skill experience.
		 *      Challenge: Lock Door    Item:  Key  OR Skill: Tinker      Energy:1  Rewards: New Skill-Pick Lock + access next room.
		 *      Challenge: Orc          Skill: Fight    Energy:2  Success: Item.  Fail: Incapacitate.
		 *      Challenge: Chest        Rewards: Gold/Items   Skills/Items:
		 *      Challenge: Trainer      Rewards: (special gold item)  
		 *      Challenge: Heavy door   Skill: Open
		 *      
		 *      If incapacitated, can't use energy until someone else heals you or your team has solved challenge in which case it is 
		 *      assumed that you rest up. 
		 *      
		 *      If all are incapacitated, the group may get robbed of items (esp gold) using a theft check (World skill)
		 * 
		 *      
		 * 
		 * Player :uses  <item> doing <skill> on <challenge>  
		 * Player :does <skill> on <challenge> 
		 * Player :take <item> from <challenge>
		 * Player :look                             -> sees a challenge.
		 * Player :through <door challenge>         -> moves to next room.
		 * 
		 * Create four characters with character classes.
		 * Give them an amount of energy and money and have them attempt win the game.
		 * 
		 * 
		 * Database of challenges, items, skills, rooms.
		 */
		World world = new World("Narnia");
		final int DIM = 8;
		Square[][] squares = new Square[DIM][DIM];
		for(int i=0;i<DIM;i++){
			for(int j=0;j<DIM;j++){
				squares[i][j] = new Square();
				
				
			}
		}
		/*
		 * after created, add exit challenges. 
		 *   1 exit   80%
		 *   2 exit   10%
		 *   3 exits   4%
		 *   >3 exits  3%  (random 4,5,6,7,8)
		 *   0 exits   2% (requires summon challenge)
		 *   very randomly use a teleport. 1%
		 *   	if(teleport) randomly select another square
		 *      else, for each exit, randomly pick from a:-1,-1, b:-1,0, c:-1,+1, d:0,-1, e:0,+1, f:+1,-1, g:+1,0, h:+1,+1
		 *      (if ==0, (+(DIM-1))  if (==DIM-1)->(-(DIM-1)) )  else ^^
		 *      
		 *   be sure to vary type of exit: locked, hidden, heavy, plain, 
		 *   add the skill / item(s) needed and those given. 
		 *   
		 *   add a pursuer that crawls all the rooms.
		 */
		for(int i=0;i<DIM;i++){
			for(int j=0;j<DIM;j++){
				
				//squares[i][j].addExitChallenge();
				world.add(squares[i][j]);
				/*
				 * use Dice and RandomDraw X of Y
				 */
			}
		}
		
		
		/*
		 * Add other challenges randomly. 
		 * 
		 */
		
		/*
		 * Create the characters
		 */
		/*
		 * Create the 
		 */
		

	}

}
