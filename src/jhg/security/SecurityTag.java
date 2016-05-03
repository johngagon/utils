package jhg.security;

import java.util.ArrayList;
import java.util.List;

/**
 * Security tag goes on any object.
 * The object has permissions to read 
 * 
 * @author jgagon
 *
 */
public class SecurityTag {

	private List<Action> activeActions;
	public SecurityTag(){
		super();
		this.activeActions = new ArrayList<Action>();
		
	}
	

	public boolean isAllowedAction(Action action) {
		return activeActions.contains(action);
	}

}
