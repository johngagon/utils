package chp.datareceiving;

import java.nio.file.Path;

public class FileEntry {

	private FileStatus status;
	private FileIdentity identity;
	private Path fullpath;
	private ScanReport report;
	
	public FileEntry(Path aPath, FileStatus received) {
		this.fullpath = aPath;
		this.status = received;
	}

	public void identify(FileIdentity fileIdentity) {
		this.identity = fileIdentity;
		System.out.println("    File Identification Found: "+fileIdentity.toString());
		status = FileStatus.IDENTIFIED;
		notifyListeners();
	}
	public void unidentified(){
		status = FileStatus.UNIDENTIFIED;
		notifyListeners();
	}
	public void pass() {
		status = FileStatus.SCANNING_PASS;
		notifyListeners();
	}
	public void fail() {
		status = FileStatus.SCANNING_FAIL;
		notifyListeners();
	}
	public void scanning() {
		status = FileStatus.SCANNING;
		notifyListeners();
	}
	public void error() {
		status = FileStatus.ERROR;
		notifyListeners();
	}
	public void abandon(){
		status = FileStatus.ABANDONED;
		notifyListeners();
	}
	public void complete(){
		status = FileStatus.COMPLETE;
		notifyListeners();
	}
	public boolean show(){
		boolean cantShow = (FileStatus.ABANDONED.equals(status) || FileStatus.COMPLETE.equals(status));
		return !cantShow;
	}
	public FileIdentity getIdentity() {
		return this.identity;
	}
	public Path getFullPath(){
		return this.fullpath;
	}
	public void notifyListeners(){
		//TODO implement Listeners
		System.out.println("    Status Change: "+this.status.name());
	}

	public void setReport(ScanReport rpt) {
		this.report = rpt;
	}
	public ScanReport getReport(){
		return this.report;
	}
	public String toString(){
		return "filename: "+this.fullpath.getFileName().toString()+",  identity: "+this.identity.toString()+",  status: "+this.status.name()+",  report: "+report.shortString();
	}
}
