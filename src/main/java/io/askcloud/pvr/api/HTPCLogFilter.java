/**
 * 
 */
package io.askcloud.pvr.api;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author ufctester
 *
 */
public class HTPCLogFilter implements Filter {

	//default is everything except the exlude list.  This can be overwritten
	protected static Set<String> ENABLED_LOGGERS = new LinkedHashSet<String>();
	protected static String QUICK_TEST_FILTER = null;
	
    static
    {
    	ENABLED_LOGGERS = new LinkedHashSet<String>();
//    	ENABLED_LOGGERS.add("io.askcloud.pvr.api");
    	ENABLED_LOGGERS.add(HTPC.class.getName());
//    	ENABLED_LOGGERS.add("io.askcloud.pvr.api.main");
//    	ENABLED_LOGGERS.add("io.askcloud.pvr.api.kodi");
    	
    	ENABLED_LOGGERS.add(HTPC.LOG_DOWNLOAD_TR);
    	ENABLED_LOGGERS.add(HTPC.LOG_LOAD_KODI_STATUS_TR);
    	ENABLED_LOGGERS.add(HTPC.LOG_DOWNLOAD_KODI_MONITOR_THREAD_TR);
    }
    
	/**
	 * 
	 */
	public HTPCLogFilter() {
		super();
	}    
	/**
	 * 
	 */
	public HTPCLogFilter(String testFilter) {
		super();
		QUICK_TEST_FILTER = testFilter;
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
	 */
	@Override
	public boolean isLoggable(LogRecord record) {
		if (record == null)
			return false;
		
		Level level = record.getLevel();
		String loggerName=record.getLoggerName();
		String message = record.getMessage() == null ? "" : record.getMessage();
		
		if(QUICK_TEST_FILTER != null)
		{
			if (loggerName.contains(QUICK_TEST_FILTER))
				return true;	
		}
		else if(ENABLED_LOGGERS.isEmpty())
		{
			return true;
		}
		else
		{
			for (String pattern : ENABLED_LOGGERS) {
				if (loggerName.contains(pattern))
					return true;				
			}
		}


		return false;
	}

}
