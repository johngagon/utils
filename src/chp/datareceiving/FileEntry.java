package chp.datareceiving;

import java.nio.file.Path;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class FileEntry {

	private FileStatus status;
	private FileIdentity identity;
	private Path fullpath;
	private ScanReport report;
	private StringBuilder errors;
	
	public FileEntry(Path aPath, FileStatus received) {
		this.fullpath = aPath;
		this.status = received;
		this.errors = new StringBuilder();
		notifyListeners();
	}

	public void addError(String s){
		errors.append(s+"\n");
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
		switch(status){
			case RECEIVED: emailReceieved();break;
			case UNIDENTIFIED: emailUnidentified();break;
			case SCANNING_PASS: emailScanPass();break;
			case SCANNING_FAIL: emailScanFail();break;
			case ERROR: emailError();break;
			default:break;
		}
	}

	private void emailScanFail() {
		try{
			Email email = new SimpleEmail();
			email.setHostName("exchange.chpmail.com");
			email.setSmtpPort(25);
			//email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			//email.setSSLOnConnect(true);
			email.setFrom("john.gagon@gmail.com");
			email.setSubject("Scan Failed for '"+this.fullpath.getFileName().toString()+"'");
			email.setMsg(this.getReport().detailString());
			email.addTo("jgagon@chpmail.com");
			email.send();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void emailError() {
		try{
			Email email = new SimpleEmail();
			email.setHostName("exchange.chpmail.com");
			email.setSmtpPort(25);
			//email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			//email.setSSLOnConnect(true);
			email.setFrom("john.gagon@gmail.com");
			email.setSubject("Errors found for '"+this.fullpath.getFileName().toString()+"'");
			email.setMsg(errors.toString());
			email.addTo("jgagon@chpmail.com");
			email.send();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void emailScanPass() {
		try{
			Email email = new SimpleEmail();
			email.setHostName("exchange.chpmail.com");
			email.setSmtpPort(25);
			//email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			//email.setSSLOnConnect(true);
			email.setFrom("john.gagon@gmail.com");
			email.setSubject("Scan passed for '"+this.fullpath.getFileName().toString()+"'");
			email.setMsg(this.getReport().shortString());
			email.addTo("jgagon@chpmail.com");
			email.send();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void emailReceieved() {
		try{
			Email email = new SimpleEmail();
			email.setHostName("exchange.chpmail.com");
			email.setSmtpPort(25);
			//email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			//email.setSSLOnConnect(true);
			email.setFrom("john.gagon@gmail.com");
			email.setSubject("New File received: '"+this.fullpath.getFileName().toString()+"'");
			email.setMsg("New File received: '"+this.fullpath.getFileName().toString()+"'");//TODO add sizes
			email.addTo("jgagon@chpmail.com");
			email.send();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void emailUnidentified() {
		try{
			Email email = new SimpleEmail();
			email.setHostName("exchange.chpmail.com");
			email.setSmtpPort(25);
			//email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			//email.setSSLOnConnect(true);
			email.setFrom("john.gagon@gmail.com");
			email.setSubject("New File could not be identified: '"+this.fullpath.getFileName().toString()+"'");
			email.setMsg("Unidentified: '"+this.fullpath.getFileName().toString()+"'");//TODO add sizes
			email.addTo("jgagon@chpmail.com");
			email.send();	
		}catch(Exception e){
			e.printStackTrace();
		}
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
