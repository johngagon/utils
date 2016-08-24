package jhg.log;

import static java.lang.System.out;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

import jhg.util.MapUtil;
import jhg.util.Table;

public class Log {
	public static final String NO_USER = "NOUSER";
	public static final String NO_SESSION = "NOSESSION";
	public static final String NO_SERVER = "NOSERVER";
	public static final String NO_REQUEST = "GET - http://localhost:8080/myapp/controller/index.html?somename=someval&somename1=someval1";
	public static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	static long freeHeapMemory = Runtime.getRuntime().freeMemory();
	static long totalHeap= Runtime.getRuntime().totalMemory();
	static long maxHeap = Runtime.getRuntime().maxMemory();
	
	private Map<String,String> userSessionMap;
	private PrintStream out;
	private boolean onOff;
	private long timer;
	
	private long start;
	private long stop;
	
	private String server;
	private String indentStr;
	public Log(PrintStream out){
		this.out = out;
		this.onOff = false;
		this.timer = 0L;
		//this.user = NO_USER;
		//this.session = NO_SESSION;
		this.server = NO_SERVER;
		this.userSessionMap = new Hashtable<String,String>();
		this.start = 0L;
		this.stop = 0L;
		this.indentStr = "";
	}
	

	public static void main(String[] args) {
		

		sdivider(100,"*");
		
		Log l = new Log(System.out);
		l.logstart();
		l.app("Accounting System","localhost:8080");
		l.indent();
		l.login("jgagon", "BFC48E7FAD12D108B536A84BF7A6795B");

		l.identify("BFC48E7FAD12D108B536A84BF7A6795B","192.168.62.214", "49959", "No other location info");
		
		l.http(null);
		Map<String,String> paramMap = new Hashtable<String,String>();
		paramMap.put("firstname","Joe");
		paramMap.put("lastname","Joe");
		
		l.start();
		
		l.memory();
		l.event("jgagon","Search ",paramMap);
		l.trace("Controller.search(..)");
		l.debug("Controller.search(..):i:"+5);
		l.debug("Controller.search(..)","i",String.valueOf(50));
		l.debug("Controller.search(..)",paramMap);
		l.sql("SELECT * FROM CONTACTS WHERE NAME='JOE'");
		l.trace("Controller.forwardingResultScreen(..)");
		l.result("jgagon","Found 50 records");
		l.memory();
		delay(1000);
		l.stop();
		
		String[] header = {"id","name","address","city","state","zip","yearly_income","phone"};
		String[] row1 = {"1","John","123 Main St.","New Orleans","LA","22111","$200,000.00","(800)212-3343"};
		String[] row2 = {"2","Frank","423 Main St.","St. Louis","MO","33111","$150,000.00","(899)292-3343"};
		String[] row3 = {"3","Mark","523 Main St.","New Orleans","LA","44111","$250,000.00","(909)232-4333"};
		String[][] data = {header,row1,row2,row3};
		Table t = new Table(data);	
		l.detail("Address Book",t);
		
		l.test("mytest",false);
		l.test("mytest2", "blue","bleu");
		l.error("InvalidArgument :'3'");
		l.sessionTimeout("BFC48E7FAD12D108B536A84BF7A6795B");
		l.logout("BFC48E7FAD12D108B536A84BF7A6795B");
		l.outdent();
		l.outdent();
		l.app("Accounting System","localhost:8080");
		l.logstop();
		sdivider(100,"*");

	}	

	public void indent(){
		indentStr = indentStr + "  ";
	}
	public void outdent(){
		if(indentStr.length()>=2){
			int endIndex = indentStr.length();
			indentStr = indentStr.substring(0, endIndex-2);
		}
	}
	public void detail(String tableName, Table t){
		out.print("DETL  ["+timeStamp()+"] "+indentStr+" Table: '"+tableName+"'");
		
		sdivider(100,"-");
		
		out.print(t.formatted());
		
		sdivider(100,"-");
		
	}
	public void logstart(){
		out.println("LOG   ["+timeStamp()+"] Start");
	}
	public void logstop(){
		out.println("LOG   ["+timeStamp()+"] Stop");
	}	
	public void divider(int length, String s){
		out.print("\n");
		for(int i=0;i<length;i++){
			out.print(s);
		}
		out.print("\n");
	}		
	public void login(String aUser, String aSession){
		//this.user = aUser;
		//this.session = aSession;
		userSessionMap.put(aSession, aUser);
		String user = userSessionMap.get(aSession);
		out.println("AUTH  ["+timeStamp()+"] "+indentStr+"[User:"+user+"]"+"Login with session:'"+aSession+"'.");
	}
	public void logout(String session){
		String user = userSessionMap.get(session);
		out.println("AUTH  ["+timeStamp()+"] "+indentStr+"[User:"+user+"]"+"Logout with session:'"+session+"'.");
		//this.user = NO_USER;
		//this.session = NO_SESSION;
		
	}
	public void sessionTimeout(String aSession){
		String user = userSessionMap.get(aSession);
		out.println("SESS  ["+timeStamp()+"] "+indentStr+"[User:"+user+"] "+"Session: '"+aSession+"' Timeout.");
		userSessionMap.remove(aSession);
		//session = NO_SESSION;
		//testException();
	}
	public void testException(){
		try{
			String[] args = new String[0];
			String s = args[10];
			System.out.println(s);
		}catch(Throwable e){
			//sdivider(100,"#");
			System.out.println(parseStackTrace(e,"jhg.log",false));
			//sdivider(100,"#");
			//newline();
		}			
	}
	
	public void app(String nameOfApp, String serverName) {
		onOff = !onOff;
		this.server = serverName;
		long runningTime = 0L;
		if(onOff){
			timer = System.currentTimeMillis();
		}else{
			runningTime = System.currentTimeMillis() - timer;
		}
		String runningTimeMessage = (onOff)?"": " Running time: "+formatRunningTime(runningTime)+".";
		out.println("APP   ["+timeStamp()+"] "+indentStr+"["+convertToStringRepresentation(totalHeap)+"/"+convertToStringRepresentation(maxHeap)+"] : "+formatOnOff(onOff)+" "+nameOfApp+" on server:'"+server+"'."+runningTimeMessage);
	}
	public void identify(String session, String remoteHostIP, String remotePort, String remoteInfo){
		String user = userSessionMap.get(session);
		out.println("IDEN  ["+timeStamp()+"] "+indentStr+"["+remoteHostIP+":"+remotePort+"] [User:"+user+"] : "+remoteInfo+".");
	}
	public void http(HttpServletRequest request){
		String requestStr = NO_REQUEST;
		String sessionID = NO_SESSION;
		String user = NO_USER;
		if(request!=null){
			requestStr = formatRequest(request);
			sessionID = request.getRequestedSessionId();
			user = userSessionMap.get(sessionID);
		}
		out.println("HTTP  ["+timeStamp()+"] "+indentStr+"[User:"+user+"] "+requestStr+"'.");//Server,user,application authentication
	}
	public void event(String user, String action, Map<String,String> paramMap){
		out.println("EVENT ["+timeStamp()+"] "+indentStr+"[User:"+user+"] Event:'"+action+"' P:"+MapUtil.mapToString(paramMap)+"."); 
	}
	public void result(String user, String resultMessage){
		out.println("RSULT ["+timeStamp()+"] "+indentStr+"[User:"+user+"] Result:'"+resultMessage+"."); 
	}	
	public void trace(String call){
		out.println("TRACE ["+timeStamp()+"] "+indentStr+"  "+call+"."); 
	}	
	public void debug(String call){
		out.println("DEBUG ["+timeStamp()+"] "+indentStr+"  Call:"+call+"."); 
	}	
	public void debug(String call, String name, String value){
		out.println("DEBUG ["+timeStamp()+"] "+indentStr+"  Call:"+call+"  "+name+":'"+value+"' ."); 
	}	
	public void debug(String call, Map<String,String> map){
		out.println("DEBUG ["+timeStamp()+"] "+indentStr+"  Call:"+call+"  "+MapUtil.mapToString(map)+"' ."); 
	}	
	
	public void error(String errMsg){
		out.println("ERROR ["+timeStamp()+"] "+indentStr+"  "+errMsg+"."); 
	}	
	public void sql(String sql){
		out.println("SQL   ["+timeStamp()+"] "+indentStr+"  "+sql+"."); 
	}	
	public void memory(){
		out.println("MEM   ["+timeStamp()+"] "+indentStr+"  "+convertToStringRepresentation(totalHeap)+"/"+convertToStringRepresentation(maxHeap)+".");
	}
	public void start(){
		start = System.currentTimeMillis();
		out.println("PROFS ["+timeStamp()+"] "+indentStr+"  "+formatRunningTime(0L)+".  SCTM:"+start);
		
	}
	public void stop(){
		stop = System.currentTimeMillis();
		long diff = stop - start;
		out.println("PROFE ["+timeStamp()+"] "+indentStr+"  "+formatRunningTime(diff)+".  SCTM:"+stop+", SCTMdelta:"+diff+"ms .");
		
	}	
	public void test(String testName, boolean passFail){
		String passFailStr = passFail?"Pass":"Fail!!!!!!!!!!";
		out.println("TEST  ["+timeStamp()+"] "+indentStr+"  Test:'"+testName+"' Result: "+passFailStr+".");
	}
	public void test(String testName, String expected, String actual){
		
		boolean passFail = expected.equals(actual);
		String passFailStr = passFail?"Pass":"Fail";
		
		
		out.println("TEST  ["+timeStamp()+"] "+indentStr+"  Test:'"+testName+"' Expected:'"+expected+"' Actual:'"+actual+"' Result: "+passFailStr+".");
	}	
	
	@SuppressWarnings("boxing")
	public static String formatRunningTime(long millis){
		long seconds = ( TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis) )); 
		long milliseconds = millis % 1000;
		return String.format("%d min, %d sec %d ms", TimeUnit.MILLISECONDS.toMinutes(millis),seconds,milliseconds);
	}
	
	static final String formatOnOff(boolean b){
		return (b)?"Starting":"Stopping";
	}
	
	
	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;

	public static String convertToStringRepresentation(final long value){
	    final long[] dividers = new long[] { T, G, M, K, 1 };
	    final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
	    if(value < 1)
	        throw new IllegalArgumentException("Invalid file size: " + value);
	    String result = null;
	    for(int i = 0; i < dividers.length; i++){
	        final long divider = dividers[i];
	        if(value >= divider){
	            result = format(value, divider, units[i]);
	            break;
	        }
	    }
	    return result;
	}

	private static String format(final long value,
	    final long divider,
	    final String unit){
	    final double result =
	        divider > 1 ? (double) value / (double) divider : (double) value;
	    return String.format("%.1f %s", Double.valueOf(result), unit);
	}	
	private static String formatRequest(HttpServletRequest request){
		return request.getMethod()+" - "+request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()+request.getRequestURI()+"?"+request.getQueryString();
	}
	private static void delay(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	private static String parseStackTrace(Throwable e, String packageFilter, boolean showTrace){
		//e.printStackTrace();
		StringBuilder sb = new StringBuilder();
		sb.append("STACK ["+timeStamp()+"] "); 
		StackTraceElement[] stArr = e.getStackTrace();
		sb.append(e.getClass()+" Message:'"+e.getMessage()+"', stack:"+stArr.length+"  ");
		if(showTrace){
			boolean first = true;
			for(StackTraceElement ste:stArr){
				if(first){
					sb.append("\n");
					first = false;
				}
				if(ste.getClassName().startsWith(packageFilter)){
					sb.append("  "+ste.toString());
				}
			}
		}
		return sb.toString();
	}
	
	private static String timeStamp(){
		return date_format.format(new Date());
	}

	public static void sdivider(int length, String s){
		System.out.print("\n");
		for(int i=0;i<length;i++){
			System.out.print(s);
		}
		System.out.print("\n");
	}	
	public static void newline(){
		System.out.println("");
	}		
	
	/*
	 * centralize all the log utils here. fix references. combine the best of all of them.
	 * 
	 * 
	 * 
	 * 20160224 - Server's logging
	 * 
	 * APP   - Application startup (load on startup), shutdown, (running time interval - memory)
	 * 
	 * HTTP  - Server,user,application authentication
	 * 
	 * A_REQ - User actions in application, file uploaded
	 * A_RES - System response summary (file written/download in response
	 * 
	 * DEBUG - Code calls
	 * EXCEP - Caught exceptions
	 * STACK - uncaught 
	 * MEMOR - memory
	 * PERFM - performance
	 * SQL   - 
	 * FILE  - 
	 * URL   - 
	 * DATA  - 
	 *       [list] [map:map] {json} 
	 *       csv,csv
	 *       fw    fixedwidt   
	 *       <xml><html></html></xml>
	 * PRINT -*************************************************************
	 * PRINT (blank line)
	 * FORM  - explain formats.
 
	 * 
	 * APP   20160224-11:50:00:000 Running  accounting/ "Accounting" on server http://192.144.122.22:8080
	 * APP   20160224-11:50:00:000 Stopping accounting/ "Accounting" on server http://192.144.122.22:8080
	 * 
	 * 
	 * HTTP  20160224-11:50:00:000 jgagon @206.29.32.201 3kdk2-34232@aol.com accounting:"ACME Accounting Application"
	 * HTTP  20160224-11:50:00:000 jgagon /login
	 * HTTP  20160224-11:50:00:000 jgagon /path?r=1&z=12342
	 * HTTP  20160224-11:50:00:000 jgagon /logoff
	 * HTTP  20160224-11:50:00:000 jgagon /timeout
	 * 
	 * EVENT 20160224-11:50:00:000 jgagon runReport 'Annual Statement'
	 * RESLT 20160224-11:50:00:000 jgagon runReport renderered 53 pages / access denied / failure.
	 * 
	 * DEBUG 20160224-11:50:00:001 com.mycorp.bl.Parser.parseName(134,"Jon,Q.,Public,123 Main St.")                <--parameters. 
	 *                             '                                                                                                    '<--max len
	 * DEBUG 20160224-11:50:00:001 com.mycorp.bl.Parser.parseName
	 * 		                             id: 134
	 *                                 line: "Jon,Q.,Public,123 Main St."
	 * DEBUG 20160224-11:50:00:001 com.mycorp.bl.Parser.parseName:"John Q. Public"                                 <--return value
	 * EXCEP 20160224-11:50:00:001 com.mycorp.bl.Parser.parseName! java.sql.SQLException - column 'weight' doesn't exist.                <--caught with message.
	 * STACK (stack trace)                                                                                                               <--rethrown out.
	 * MEM   20160224-11:50:00:001 :label:          Total 3.2 MB     Used: 1.2 MB      Remaining 2.0 MB
	 * PROF  20160224-11:50:00:001 :from: - :to:    54 ms
	 * PROF  20160224-11:50:00:001 :fromlast:       54 ms
	 * SQL   20160224-11:50:00:001 connection: jdbc:odbc:corphost:3929/db joeblow 
	 * SQL   20160224-11:50:00:001 query-ex : 'select * from table'
	 * SQL   20160224-11:50:00:001 query-rs :  5829 rows
	 * SQL   20160224-11:50:00:001 trans-ex : 'insert into mytable (myfield) values ('data')'
	 * SQL   20160224-11:50:00:001 trans-rs : inserted 1 row (updated 50 rows / deleted 1 row), created table. etc.
	 * 
	 * 
	 * FILE  20160224-11:50:00:001 Read  c:/files/myfile.txt
	 * URL   20160224-11:50:00:001 Read  http://somews.api.rest?user=joeblow
	 * 
	 * FORM  HTTP - fixed width
	 *       000-004 L,'HTTP '
	 *       005-013 L,time
	 *       014-020 L,username
	 *       
	 * LOG   STACK ON
	 * LOG   DEBUG OFF
	 * LOG   CHANNELS: DEBUG HTTP
	 * LOG   CHANNELS ON: 
	 * LOG   CHANNELS OFF:      
	 * 
	 * (all log has a channel and time)
	 * 
	 * Features:
	 * 
	 * Can store logging and output the channel
	 * 
	 */


}
/*
x Auth type : null
x Char enc  : utf-8
x Conxt type: null
x Conxt path: /benchmarking (redundant with req uri)      
Local addr: 0:0:0:0:0:0:0:1      
Local name: 0:0:0:0:0:0:0:1
Local port: 8080
* Method    : GET
Path Info : null
Path Trnsl: null
Protocol  : HTTP/1.1
* QueryStr  : null
RemoteAddr: 0:0:0:0:0:0:0:1
RemoteHost: 0:0:0:0:0:0:0:1
RemotePort: 49959
RemoteUser: null
*ReqSessID : BFC48E7FAD12D108B536A84BF7A6795B
*Req URI   : /benchmarking/grails/portlet/index.dispatch
*Scheme    : http
*ServerName: localhost
*Servl Path: /grails/portlet/index.dispatch

method - reqsessid - scheme://servername:localport/requri?querystring

 */

/*
x Auth type : null
x Char enc  : utf-8
x Conxt type: null
* Conxt path: /benchmarking      
Local addr: 0:0:0:0:0:0:0:1      
Local name: 0:0:0:0:0:0:0:1
Local port: 8080
* Method    : GET
Path Info : null
Path Trnsl: null
Protocol  : HTTP/1.1
* QueryStr  : null
RemoteAddr: 0:0:0:0:0:0:0:1
RemoteHost: 0:0:0:0:0:0:0:1
RemotePort: 49959
RemoteUser: null
*ReqSessID : BFC48E7FAD12D108B536A84BF7A6795B
*Req URI   : /benchmarking/grails/portlet/index.dispatch
*Scheme    : http
*ServerName: localhost
*Servl Path: /grails/portlet/index.dispatch

request.getMethod()+" - "+request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()+request.getRequestURI()+"?"+request.getQueryString()

method - reqsessid - scheme://servername:localport/requri?querystring

	String s =
	request.getAuthType()+","
	+request.getCharacterEncoding()+","
	+request.getContentType()+","
	+request.getContextPath()+","
	+request.getLocalAddr()+","
	+request.getLocalName()+","
	+request.getLocalPort()+","
	+request.getMethod()+","
	+request.getPathInfo()+","
	+request.getPathTranslated()+","
	+request.getProtocol()+","
	+request.getQueryString()+","
	+request.getRemoteAddr()+","
	+request.getRemoteHost()+","
	+request.getRemotePort()+","
	+request.getRemoteUser()+","
	+request.getRequestedSessionId()+","
	+request.getRequestURI()+","
	+request.getScheme()+","
	+request.getServerName()+","
	+request.getServletPath()
	;
*/