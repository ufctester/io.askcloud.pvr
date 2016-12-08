package io.askcloud.pvr.api;

import java.io.File;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

public class PrintRecursiveDirectoryFileSizes extends PlexRequest {

	private String directory = null;
	public PrintRecursiveDirectoryFileSizes() {
		super();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		PrintRecursiveDirectoryFileSizes print = new PrintRecursiveDirectoryFileSizes();
		print.run();
	}
	
	@Override
	void run() {
		PlexPVRManager.getInstance().printFileSizes(new File(directory));
		
	}
}
