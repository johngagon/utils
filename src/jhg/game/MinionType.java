package jhg.game;





public class MinionType {

	
	
	private int id;
	private String name;
	private Category category;
	private Stage evolution;
	private Division weightClass;				//defaults: attack, defend, deconstruct credit (level -1), undefended attack 
	private MinionType previousForm;
	private MinionType evolveTo;
	//levels: embryo, immature, mature, fantastic, avatar
	
	/*
	 * Tiny                                   1d6.
	 * 
	 * Division -> number of d6               3d6 3-18     
	 * Stage    -> additional number          1d6+0  (embryo=no fight).   1-6, +5  3d6+5 (3d6+3*5). 3d6+15 15-33
	 * 
	 * Fighting: 
	 * 
	 * tiny can take out young,adult,avatar / has spells early. "Infect"
	 * 
	 */
}
