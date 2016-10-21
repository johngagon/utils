package jhg.decomposition;

import java.util.Hashtable;
import java.util.Map;

public class DataSet {
	
	
	@SuppressWarnings("unused")
	private Map<String,Double> dataSet1 = new Hashtable<String,Double>();
	
	public DataSet(String txt) {
		dataSet1 = new Hashtable<String,Double>();
		dataSet1 = parse(txt); //if something goes wrong, it's not null.
	}
	private Map<String,Double> parse(String txt){
		return null;//FIXME todo
	}

}
