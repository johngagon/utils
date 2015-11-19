package jhg.game;

public class Game {

	
	public Game(){
		
	}
	
	public void init(){
		
		Player gamePlayer = new Player("Dealer");
		
		Category creature = new Category("Creature",null);
		
		Category animal = new Category("Animal",creature);
		
		Category piscean = new Category("Piscean",animal);		//shark, eel, fish
		Category avian = new Category("Avian",animal);         //prairie fowl, water fowl, gulls, eagles, carrion, corvids, small birds
		Category reptile = new Category("Reptile",animal);     //turtle, lizard, snake, croc
		Category mammal = new Category("Mammal",animal);		//canine:cat,dog,bear,lion  mice, bats, mongoose/badger, monkey, deer, horse, cow, sheep/goat, pig, hippo, giraffe
		Category amphibian = new Category("Amphibian",animal); //frog, newt
		Category creeper = new Category("Creeper",animal);              //swarm, invertebrate, worm, crustacean: scorp/spider/crab/shrimp/lobster, jellyfish, starfish
		
		/* Reptile:fire, Piscean:water, Avian:air, Mammal:earth    Amphibian:water,earth  Creeper: Air, Fire  */
		
		
		Category spore = new Category("Spore",creature);
		Category shroom = new Category("Shroom",spore);     //spores air
		Category blight = new Category("Blight",spore);     //mold, water
		Category germ = new Category("Germ",spore);         //germ: earth       bacteria/parasites
		Category virus = new Category("Virus",spore);       //transforms: fire
		
		
		
		Category plant = new Category("Plant",creature);
		
		Category vine = new Category("Vine",plant);        //       earthy     
		Category treant = new Category("Treant",plant);    //       air
		
		Category trap = new Category("Trap",plant);		//holds   fire
		Category kelp = new Category("Kelp",plant);		//drowns  water
		/* Vine: earth,  treant: air,  kelp: water                */		
		//evolve, mature forms, supernatural form
		
		/*             weakness    strength   complement   mirror      
		 * air         earth       fire       water        air       chickens hate foxes,    attacks snakes, likes fish
		 * water       fire        earth      air          water     shark    hate lizards,  attack mice?    like eagles
		 * fire        air         water      earth        fire      
 		 * earth       water       air        fire         earth     
		 *             -1/-1       +1/+1      -1/+1        +1/-1
		 *             
		 *             
		 *  Size scale:
		 *  
		 *  Giant class: E elephant, 
		 *               F dinosaur, giant (for not usually giant), 
		 *               A condor, 
		 *               W whale
		 *               octopus, toad
		 *  
		 *  Large class: horse, lion, tiger, bear, water buffalo, buffalo, moose, hippo, giraffe 
		 *                eagle, 
		 *                shark, dolphin
		 *                croc, 
		 *   
		 *  Medium class: bobcat, wolf/large dog, pig, human/ape
		 *                larger fish/smaller sharks, deer, 
		 *                iguana, 
		 *                vultures, osprey/small hawks
		 *   
		 *  Small class:  house cat, house dogs smaller than wolves, monkey, bat 
		 *                lizard/snake/turtle, crab, 
		 *                most birds
		 *                most fish
		 *   
		 *  
		 *  Decay beats giant (whale, elephant, condor, komono/croc/dino, (smaller:octopus, giant toad)?)
		 *      
		 *  
		 *  
		 */
		
		MinionType lizardEgg = new MinionType();
		
	}
	
	public void startGame(){
		/*
		 * Set up players and names, give life.
		 * 
		 * During a turn, a player draws a card (type). 
		 * If "played", it becomes a "minion". 
		 * 
		 * 
		 * A player can attack another player directly each turn. - 1 life. 
		 * Using bigger cards, a player can attack more points undefended.
		 * Players can also fend off attacks. 
		 * Unlike magic, the attacks are rolled, defense is rolled. modifying performed based on element.
		 * 
		 * If the attack roll > defense roll -> defense creature buried.
		 * If the defender's attack roll is > attacker's defense roll, the attacker dies.
		 * 
		 * 
		 * If the attacker is using infection, the attacker cannot go over defense roll: rolls one more attack die, one less defense. 
		 * If the attacker is using strangle,   
		 * 
		 * Infected creatures de-evolve each turn. Player takes a life first turn, two life second turn etc until creature dies. 
		 * Strangled creatures cannot fight.
		 * 
		 * Infection does little initial damage but builds up every turn. 
		 * 		An attacker who 
		 * Plants can prevent attack once successfully defended or attacked. 
		 * 
		 * 
		 * There are ONLY creatures. no "artifacts", no "enchantments", no "instants"/"sorceries". 
		 * Creatures can take the place of all these. they all have a utility. 
		 * 
		 * 
		 * Actions:
		 * 
		 * Gain a life
		 * Draw card
		 * Make one minion from a card.
		 * 		Make another at the cost of life (for certain cards)
		 * Evolve a minion. (or swarm a minion)
		 * Make an attack. 
		 *    (directly on player who has no cards)
		 *    player blocks.(1 card only)
		 * roll outcomes/modify and apply fatality where needed.. 
		 * 
		 * 
		 */
		
	}
	
	public void registerPlayerTurn(Player player){
		
	}
	
	public Player getNextTurnPlayer(){
		return null;//TODO impl
	}
	
}
