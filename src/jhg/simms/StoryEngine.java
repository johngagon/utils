package jhg.simms;

import jhg.util.Log;

public class StoryEngine {
	
	/*
	 * Start off with this being a simulation.
	 * Don't stop with avoidance but try other actions besides approaching for love.
	 * Make the story come out more story like. (have one side be stats or print on two lines, top line is story, bottom line is rolls, mark each line)
	 * Add new locations to their world.
	 * Add children etc to their world.
	 * Give the world some disaster events.
	 * Create some creatures, intrigues/lies., information about people, 
	 * Basically get this to generate plot, work on a new plot action capability each visit.
	 * 
	 * TODO make shyness a threshold value, if one is shy, the shyness has to be overcome via rolls.
	 */

	
	public static void main(String[] args){
		Log.println("Starting.");
		
		
		//girl chases boy.
		Character bo = new Character("Bo",Gender.MALE,Orientation.STRAIGHT,2,6);//bo is shy
		Character hope = new Character("Hope",Gender.FEMALE,Orientation.STRAIGHT,7,6);//hope is not shy. 
		/*
		 * TODO instead of libido call it interest (and rate their mutual interest).
		 * 
		 * friendliness works the same way. 
		 */
		
		Location location = new Location("bar");
		
		
		Story story = new Story();
		story.addLocation(location);
		location.addCharacter(bo);;
		location.addCharacter(hope);
		story.start();
		
		Log.println("\n\n\nEnding.");
		/*
		
Sample output:

Bo entered the scene.
Hope enters the scene.

if there are attractive folks on the scene and the gender is compatible with orientation, then the brave one will talk to the most
attractive, if the receiver is shy, they will evade. 

depending on the sexual need level, the brave one will pursue the shy one until the shy one accepts conversation. if the shy one
has needs they are not revealing, they might give in (modifies evade). if the need is not strong enough or shyness too great, the shy one
leaves and the brave one leaves
		
properties:

sexdrive
bravery


methods:

approach
acknowledge
evade
do nothing

		
		 */
		
		
		
	}
	
}
