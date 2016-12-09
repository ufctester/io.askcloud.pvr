package io.askcloud.pvr.api.main;

import java.io.File;

import io.askcloud.pvr.api.HTPC;

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
		HTPC.getInstance().printFileSizes(new File(directory));
		
		//exit the java program
		System.exit(0);
		
	}
}
