package chp.datareceiving;

public class FileEntry {

	private FileStatus status;
	
	public FileEntry(String filename2, FileStatus received) {
		// TODO Auto-generated constructor stub
	}

	String filename;

	public void identify(FileIdentity fileIdentity) {
		// TODO Auto-generated method stub
		status = FileStatus.IDENTIFIED;
	}
	public void unidentified(){
		status = FileStatus.UNIDENTIFIED;
	}
	public void pass() {
		// TODO Auto-generated method stub
		status = FileStatus.SCANNING_PASS;
	}
	public void fail() {
		// TODO Auto-generated method stub
		status = FileStatus.SCANNING_FAIL;
	}
	public void scanning() {
		// TODO Auto-generated method stub
		status = FileStatus.SCANNING;
	}
	public void error() {
		// TODO Auto-generated method stub
		status = FileStatus.ERROR;
	}
	public void abandon(){
		status = FileStatus.ABANDONED;
	}
	public void complete(){
		status = FileStatus.COMPLETE;
	}
	public boolean show(){
		boolean cantShow = (FileStatus.ABANDONED.equals(status) || FileStatus.COMPLETE.equals(status));
		return !cantShow;
	}
	public FileIdentity getIdentity() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
