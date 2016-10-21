
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Troy Thompson, Bob Byron<p>
 * Company:      JavaUnderground<p>
 * @author       Troy Thompson, Bob Byron
 * @version 1.1
 */
package jhg.log.jdbc;

import java.sql.SQLException;

public class ParameterIndexOutOfBoundsException extends SQLException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1824047458693513361L;

	public ParameterIndexOutOfBoundsException() {
        super();
    }

    public ParameterIndexOutOfBoundsException(String s) {
        super(s);
    }
}