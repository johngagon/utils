package jhg.monitoring;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import jhg.util.Log;

import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
//import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class ApplicationMonitoring {

	
	
	public static void main(String[] args){
		try {
			new ApplicationMonitoring()
			//.devApplicatonCheck();
			.productionApplicatonCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
	@Test
	public void devApplicatonCheck() throws Exception {
		//https://chp-pidspdev01.corp.chpinfo.com/idp/startSSO.ping?PartnerSpId=https://ssodev.chpinfo.com
		applicatonCheck("https://chp-pidspdev01.corp.chpinfo.com/idp/startSSO.ping?PartnerSpId=https://ssodev.chpinfo.com&TargetResource=https%3A%2F%2Ftest.chpmarketquest.com%2Fportal%2FSSOServlet",
				"test.chpmarketquest.com", "Dev");
	}
	
	
	
	@Test
	public void stageApplicatonCheck() throws Exception {
		applicatonCheck("https://stage.chpmarketquest.com/portal/server.pt?open=512&objID=312&PageID=150852&cached=true&mode=2&userID=2203", 
				"stage.chpmarketquest.com", "Stage");
	}
	
	
	@Test
	public void productionApplicatonCheck() throws Exception {
		applicatonCheck("https://chp-pidprod01.corp.chpinfo.com/idp/startSSO.ping?PartnerSpId=https://sso.chpinfo.com&TargetResource=https%3A%2F%2Fwww.chpmarketquest.com%2Fportal%2FSSOServlet", 
				"www.chpmarketquest.com", "Production");
		//"chp-pidprod01"
	}	
	
	
	@Test
	public void applicatonCheck(String server, String internServer, String testName) throws Exception {
		
		 LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		 java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		 java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		 final WebClient webClient = new WebClient();
		 

		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
	    
		
		
		Log.println(testName+" - submittingForm()");
		
	    webClient.getOptions().setUseInsecureSSL(true);
	    //Map<String,String> websiteMap = new Hashtable<String,String>();
	    //websiteMap.put("https://chp-pidprod01.corp.chpinfo.com/idp/startSSO.ping?PartnerSpId=https://sso.chpinfo.com&TargetResource=https%3A%2F%2Fwww.chpmarketquest.com%2Fportal%2FSSOServlet","MarketQuest is a highly secure platform");
	    //for(String website:websiteMap.keySet()){ 

	    	String website = server;
		    String formName = "lform";
		    String userIDName = "pf.username";
		    String passwdName = "pf.pass";
		    String termsName = "in_cb_agree";
		    String userid = "bcbsal";
		    String passwd = "bcbs@L";
		    String submitName = "in_bu_Login";
		    
		    final HtmlPage page1 = webClient.getPage(website);
		    webClient.waitForBackgroundJavaScript(5000);
		    Log.println("Login page loaded.");
		    // Get the form that we are dealing with and within that form, 
		    // find the submit button and the field that we want to change.
		    final HtmlForm form = page1.getFormByName(formName);
	
		    //final HtmlSubmitInput button = form.getButtonByName(submitName);//.getInputByName(submitName);
		    final HtmlButtonInput button = (HtmlButtonInput)form.getInputByName(submitName);
		    final HtmlTextInput textField1 = form.getInputByName(userIDName);
		    final HtmlPasswordInput textField2 = (HtmlPasswordInput)form.getInputByName(passwdName);
		    final HtmlCheckBoxInput checkboxTerms = (HtmlCheckBoxInput)form.getInputByName(termsName);
		    // Change the value of the text field
		    textField1.setValueAttribute(userid);
		    textField2.setValueAttribute(passwd);
		    if(checkboxTerms!=null){
		    	checkboxTerms.setChecked(true);
		    }
		    // Now submit the form by clicking the button and get back the second page.
		    Log.println("Form filled.");
		    webClient.waitForBackgroundJavaScript(5000);
		    @SuppressWarnings("unused")
			
		    final HtmlPage page2 = button.click();  //FIXME doesn't work in dev, only prod
		    
		    Log.println("Button for login clicked.");
		    webClient.waitForBackgroundJavaScript(50000);
		    
		    //Log.println(page2.asText());
		    Log.println("Post login page loaded.");
		    //PROD benchmarking:                      https://www.chpmarketquest.com/portal/server.pt?open=512&objID=312&PageID=150861&cached=true&mode=2&userID=2169

		    String appPageName, applicationPage, verificationTag, appFormName = "";

		    switch(testName){
		    
		    	case "Dev":
				    //TODO softcode
				    appPageName = "Benchmarking";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=330&PageID=150949&cached=true&mode=2&userID=2169";
				    verificationTag        = "Total Discount Detail";
				    appFormName            = "hewittReport";
				    
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);
				    
				    appPageName = "Employer Search";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=251&PageID=151176&cached=true&mode=2&userID=2169";
				    verificationTag        = "Benefit Sponsor Report";
				    appFormName            = "EmployerSearch";
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);
		
				    appPageName = "Market Reports";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=363&PageID=151443&cached=true&mode=2&userID=2169";
				    verificationTag        = "Cost Model";
				    appFormName            = "reportForm";
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);		    		
		    		break;
		    
		    	case "Stage":
				    //TODO softcode
				    appPageName = "Benchmarking";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=312&PageID=150861&cached=true&mode=2&userID=2169";
				    verificationTag        = "Total Discount Detail";
				    appFormName            = "hewittReport";
				    
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);
				    
				    appPageName = "Employer Search";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=251&PageID=151176&cached=true&mode=2&userID=2169";
				    verificationTag        = "Benefit Sponsor Report";
				    appFormName            = "EmployerSearch";
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);
		
				    appPageName = "Market Reports";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=363&PageID=151443&cached=true&mode=2&userID=2169";
				    verificationTag        = "Cost Model";
				    appFormName            = "reportForm";
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);		    		
		    		break;
		    	case "Production":
		    		

				    //TODO softcode
				    appPageName = "Benchmarking";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=312&PageID=150861&cached=true&mode=2&userID=2169";
				    verificationTag        = "Total Discount Detail";
				    appFormName            = "hewittReport";
				    
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);
				    
				    appPageName = "Employer Search";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=251&PageID=151176&cached=true&mode=2&userID=2169";
				    verificationTag        = "Benefit Sponsor Report";
				    appFormName            = "EmployerSearch";
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);
		
				    appPageName = "Market Reports";
				    applicationPage        = "https://"+internServer+"/portal/server.pt?open=512&objID=363&PageID=151443&cached=true&mode=2&userID=2169";
				    verificationTag        = "Cost Model";
				    appFormName            = "reportForm";
				    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);	
				  break;
				  default:break;
		    }
		    /*
		    appPageName = "Network Compare";
		    applicationPage        = "https://www.chpmarketquest.com/portal/server.pt?open=512&objID=248&PageID=150472&cached=true&mode=2&userID=2169";
		    verificationTag        = "Summary by Geography";
		    appFormName            = "ncInputs";
		    testApplication(webClient, appPageName, applicationPage, verificationTag, appFormName);		    
		    */
		    
		    webClient.close();
		    //Log.println(form2.asXml());
		    Log.println("Done");
	    //}
	}

	private void testApplication(final WebClient webClient, String appPageName,
			String applicationPage, String verificationTag,
			String formName) throws IOException, MalformedURLException {
		
	    //TODO add a test here for page 2 being the home page.
		
		final HtmlPage page3 = webClient.getPage(applicationPage);
		
		
		
		webClient.waitForBackgroundJavaScript(10000);
		Log.println("\n\n"+appPageName+" page loaded.");	
		
		//Log.println(page3.asXml());
		
		final HtmlForm form2 = page3.getFormByName(formName);
		
		if(form2.asText().indexOf(verificationTag)!=-1){
			Log.println("PASS");
		}else{
			Log.println("FAIL - Could not find verification tag: "+verificationTag);
		}
	}	
	

}
/*  
webClient.setCssEnabled(false);

webClient.setIncorrectnessListener(new IncorrectnessListener() {

    @Override
    public void notify(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }
});
webClient.setCssErrorHandler(new ErrorHandler() {

    @Override
    public void warning(CSSParseException exception) throws CSSException {
        // TODO Auto-generated method stub

    }

    @Override
    public void fatalError(CSSParseException exception) throws CSSException {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(CSSParseException exception) throws CSSException {
        // TODO Auto-generated method stub

    }
});
webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

    @Override
    public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void scriptException(HtmlPage arg0, ScriptException arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
        // TODO Auto-generated method stub

    }
});
webClient.setHTMLParserListener(new HTMLParserListener() {

    @Override
    public void warning(String arg0, URL arg1, int arg2, int arg3, String arg4) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String arg0, URL arg1, int arg2, int arg3, String arg4) {
        // TODO Auto-generated method stub

    }
});
webClient.setThrowExceptionOnFailingStatusCode(false);
webClient.setThrowExceptionOnScriptError(false);			    
*/