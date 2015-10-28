package chp.dbreplicator;

public interface ProgressListener {
	public void notify(int progress, int counter);
}
