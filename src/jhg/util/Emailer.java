package jhg.util;

import org.apache.commons.mail.*;


public class Emailer {

	
	
	public static void emailSimple(String from, String to, String subject, String message){
		try{
			Email email = new SimpleEmail();
			email.setHostName("exchange.chpmail.com");
			email.setSmtpPort(25);
			//email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			//email.setSSLOnConnect(true);
			email.setFrom(from);
			email.setSubject(subject);
			email.setMsg(message);
			email.addTo(to);
			email.send();	
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public static void testEmailer(){
		try{
			Email email = new SimpleEmail();
			email.setHostName("exchange.chpmail.com");
			email.setSmtpPort(25);
			//email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			//email.setSSLOnConnect(true);
			email.setFrom("john.gagon@gmail.com");
			email.setSubject("TestMail");
			email.setMsg("This is a test mail. ");
			email.addTo("jgagon@chpmail.com");
			email.send();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		testEmailer();
	}
	
	
	
}
/*
ant.mail(mailhost: 'exchange.chpmail.com', mailport: '25', subject: subject)
*/