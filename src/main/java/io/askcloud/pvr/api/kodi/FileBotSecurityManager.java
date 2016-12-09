/**
 * 
 */
package io.askcloud.pvr.api.kodi;

import java.security.Permission;

/**
 * @author ufctester
 *
 */
public class FileBotSecurityManager extends SecurityManager {

	/**
	 * 
	 */
	public FileBotSecurityManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void checkPermission(Permission perm) {
//		// TODO Auto-generated method stub
//		super.checkPermission(perm);
	}
	
	@Override
	public void checkExit(int status) {
		throw new SecurityException();
	}
}
