package chp.datareceiving;

import java.nio.file.Path;
import java.util.Date;

import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class FileEntry implements Comparable<FileEntry>{

	private FileStatus status;
	private FileIdentity identity;
	private String filename;
	private Path fullPath;
	private ScanReport report;
	private StringBuilder errors;
	private Date scanStart;
	private Date scanFinish;
	private FileIntakeDirectory dir;
	public FileEntry(FileIntakeDirectory fid, String aPath, Path fullpath) {
		this.dir = fid;
		this.filename = aPath;
		this.fullPath = fullpath;
		this.errors = new StringBuilder();
		this.status = FileStatus.RECEIVED;
		this.scanStart = new Date(0);
		this.scanFinish = new Date(0);
		this.report = new ScanReport();
	}
	 
	void mockScan(long x){
		this.scanStart = new Date();
		this.scanFinish = new Date(scanStart.getTime()+x);
	}

	public void addError(String s){
		errors.append(s+"\n");
	}
	
	@SuppressWarnings("boxing")
	public Long getDuration(){ //TODO validate preconditions
		Long difference = scanFinish.getTime() - scanStart.getTime();
		return difference/1000;
	}
	
	
	public void identify(FileIdentity fileIdentity) {
		this.identity = fileIdentity;
		
		//System.out.println("    File Identification Found: "+fileIdentity.toString());
		if(!(FileIdentity.UNKNOWN.equals(fileIdentity))){
			status = FileStatus.IDENTIFIED;
			this.report.identify();
			notifyListeners();
		}else{
			status = FileStatus.NOT_IDENTIFIABLE;
		}
		
		
	}
	public void unidentified(){
		status = FileStatus.NOT_IDENTIFIABLE;
		//notifyListeners();
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

	public void notifyListeners(){
		//TODO implement Listeners
		//System.out.println("    Status Change: "+this.status.name());
		switch(status){
			case RECEIVED: emailReceieved();break;
			case NOT_IDENTIFIABLE: emailUnidentified();break;
			case SCANNING_PASS: emailScanPass();break;
			case SCANNING_FAIL: emailScanFail();break;
			case ERROR: emailError();break;
			default:break;
		}
	}

	/*
	 * FIXME separate this out into an email listener
	 */
	private String from(){
		return "donotreply@chpmail.com";
	}
	private String[] to(){
		return new String[]{"jgagon@chpmail.com","akhan@chpmail.com","skhodab@chpmail.com","lcolbourn@chpmail.com"};
	}
	private String subjectPrefix(){
		return "Data Intake Platform: ";
	}
	
	private void emailScanFail() {
		try{
			Email email = new SimpleEmail();
			email.setHostName("exchange.chpmail.com");
			email.setSmtpPort(25);
			//email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			//email.setSSLOnConnect(true);
			email.setFrom(from());
			email.setSubject(subjectPrefix()+"Scan Failed for '"+this.filename+"'");
			email.setMsg(this.getReport().detailString());
			String[] sarr = to();
			for(String s:sarr){
				email.addTo(s);
			}
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
			email.setFrom(from());
			email.setSubject(subjectPrefix()+"Errors found for '"+this.filename+"'");
			email.setMsg(errors.toString());
			String[] sarr = to();
			for(String s:sarr){
				email.addTo(s);
			}			
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
			email.setFrom(from());
			email.setSubject(subjectPrefix()+"Scan passed for '"+this.filename+"'");
			//email.setMsg(this.dir.toPrettyString());//this.getReport().shortString());
			String[] sarr = to();
			for(String s:sarr){
				email.addTo(s);
			}
			email.setContent(this.dir.toPrettyString(), "text/html");
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
			email.setFrom(from());
			email.setSubject(subjectPrefix()+"New File received: '"+this.filename+"'");
			email.setMsg("New File received: '"+this.filename+"' on "+new Date().toString());//TODO add sizes
			String[] sarr = to();
			for(String s:sarr){
				email.addTo(s);
			}
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
			email.setFrom(from());
			email.setSubject(subjectPrefix()+"New File could not be identified: '"+this.filename+"'");
			email.setMsg("Unidentified: '"+this.filename+"'");//TODO add sizes
			String[] sarr = to();
			for(String s:sarr){
				email.addTo(s);
			}
			email.send();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Path getFullPath(){
		return this.fullPath;
	}

	public void setReport(ScanReport rpt) {
		this.report = rpt;
	}
	
	public ScanReport getReport(){
		return this.report;
	}
	
	public String toString(){
		StringBuffer buff = new StringBuffer();
		buff.append("filename: "+this.filename);
		buff.append(",  identity: "+this.identity.toString());
		buff.append(",  status: "+this.status.name());
		buff.append(",  report: "+report.shortString());
		return buff.toString();
	}
	
	public String[] toArray(){
		return new String[] {filename,identity.toString(),status.name(),report.shortString(),format(this.scanStart),format(this.scanFinish),this.getDuration().toString()};
	}
	
	public String format(Date d){
		if(d.getTime()==0){
			return "";
		}else{
			return d.toString();
		}
	}
	
	public void setScanStart(Date date) {
		this.scanStart = date;
	}
	public void setScanEnd(Date date){
		this.scanFinish = date;
	}


	@Override
	public int compareTo(FileEntry o) {
		return this.status.compareTo(o.status);
	}

	/**
	 * Circumvents the notifications, useful for mock 
	 * 
	 * @param fs
	 */
	public void setStatus(FileStatus fs) {
		this.status = fs;
	}

}
