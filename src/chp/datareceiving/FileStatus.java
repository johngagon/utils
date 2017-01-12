package chp.datareceiving;

public enum FileStatus {
	RECEIVED,
	ERROR,
	IDENTIFIED,
	UNIDENTIFIED,
	SCANNING,
	SCANNING_PASS,
	SCANNING_FAIL,
	//LOAD_READY,
	//LOADING,
	//MISC,
	MANUAL_FAIL,
	ABANDONED,
	COMPLETE;
}
