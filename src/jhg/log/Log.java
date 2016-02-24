package jhg.log;

public class Log {

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
