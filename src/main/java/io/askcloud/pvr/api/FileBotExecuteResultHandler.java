/**
 * 
 */
package io.askcloud.pvr.api;

import java.util.logging.Logger;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;

/**
 * @author ufctester
 *
 */
public class FileBotExecuteResultHandler extends DefaultExecuteResultHandler {

	private static final String CLASS_NAME = HTPC.class.getName();
	private static final Logger LOG = HTPC.LOG_FILE_BOT;
	
	/**
	 * 
	 */
	public FileBotExecuteResultHandler() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onProcessComplete(int exitValue) {
		LOG.entering(CLASS_NAME, "onProcessComplete");
		LOG.exiting(CLASS_NAME, "onProcessComplete");
		super.onProcessComplete(exitValue);
		System.exit(0);
	}
	
	@Override
	public void onProcessFailed(ExecuteException e) {
		LOG.entering(CLASS_NAME, "onProcessFailed",e);
		LOG.exiting(CLASS_NAME, "onProcessFailed");
		super.onProcessFailed(e);
		System.exit(1);
	}

}
