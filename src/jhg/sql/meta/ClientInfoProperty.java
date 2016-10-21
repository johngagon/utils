package jhg.sql.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientInfoProperty {
	
	public static enum Field{
		NIL,//0
		NAME,
		MAX_LEN,
		DEFAULT_VALUE,
		DESCRIPTION;
	}	
	//@SuppressWarnings("unused")
	private String name,defaultValue,description;
	//@SuppressWarnings("unused")
	private int maxLength;
	
	public ClientInfoProperty(ResultSet rs){
		try{
			name = rs.getString(Field.NAME.ordinal());
			defaultValue = rs.getString(Field.DEFAULT_VALUE.ordinal());
			description = rs.getString(Field.DESCRIPTION.ordinal());
			maxLength = rs.getInt(Field.MAX_LEN.ordinal());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public String toString() {
		return "ClientInfoProperty [name=" + name + ", defaultValue="
				+ defaultValue + ", description=" + description
				+ ", maxLength=" + maxLength + "]";
	}
	
	
	

	
/*
Retrieves a list of the client info properties that the driver supports. The result set contains the following columns
NAME String=> The name of the client info property
MAX_LEN int=> The maximum length of the value for the property
DEFAULT_VALUE String=> The default value of the property
DESCRIPTION String=> A description of the property. This will typically contain information as to where this property is stored in the database.
The ResultSet is sorted by the NAME column
 */
}
