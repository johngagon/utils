package jhg.sql.meta;

public class ClientInfoProperty {
	
	public static enum Field{
		NIL,//0
		NAME,
		MAX_LEN,
		DEFAULT_VALUE,
		DESCRIPTION;
	}	
	@SuppressWarnings("unused")
	private String name,defaultValue,description;
	@SuppressWarnings("unused")
	private int maxLength;
	
/*
Retrieves a list of the client info properties that the driver supports. The result set contains the following columns
NAME String=> The name of the client info property
MAX_LEN int=> The maximum length of the value for the property
DEFAULT_VALUE String=> The default value of the property
DESCRIPTION String=> A description of the property. This will typically contain information as to where this property is stored in the database.
The ResultSet is sorted by the NAME column
 */
}