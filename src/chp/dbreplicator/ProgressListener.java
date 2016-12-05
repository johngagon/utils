package chp.dbreplicator;

public interface ProgressListener {
	public void notify(long progress, long counter, long total, long startTime, long timeEstimate);
}
