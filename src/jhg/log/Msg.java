package jhg.log;


/**
 * For use with existing logging systems.
 * Helps format messages for logs without being redundant. 
 * For example: grails log.debug puts out this:
 * 
 * 2016-08-25 09:53:34,165 [http-bio-8080-exec-1] DEBUG benchmarking.ReportStoreService
 * 
 * Therefore we don't need date time, scheme, port, thread, logging level, calling class. 
 * 
 * We still need: 
 * 
 * Subcategory
 * table formatting sometimes
 * hard rules that work with console
 * max length
 * method name/line number
 * 
 * Api would be:   
 * 	Msg.sql(connection_id, sql)				
 * 	Msg.sqlException(connection_id,message)
 * 
 * 2016-08-25 09:53:34,165 [http-bio-8080-exec-1] DEBUG benchmarking.DbService [SQL:''],etc.
 * Problem is loss of alignment control because of classname.
 * 
 * @author jgagon
 *
 */
public class Msg {

}
