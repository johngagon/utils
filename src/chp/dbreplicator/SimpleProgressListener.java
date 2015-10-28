package chp.dbreplicator;

import jhg.util.Log;

public class SimpleProgressListener implements ProgressListener {

	public SimpleProgressListener() {
		
	}
	public void notify(int progress, int counter){
		Log.println("PROGRESS: "+progress+"%  i:"+counter);
	}

}
