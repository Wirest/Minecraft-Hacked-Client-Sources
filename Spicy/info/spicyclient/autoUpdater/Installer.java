package info.spicyclient.autoUpdater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

/**
 * @author xTrM_
 */
public class Installer {
	
	/**
	 * Function to save a file from a URL to a file location
	 * args: url, file 
	 */
	public static void saveFile(String url, String file) throws IOException {
		
		System.out.println("Installing " + url + " to " + file + "...");
		URL urle = new URL(url);
		
		System.out.println("Openning Stream...");
	    InputStream in = urle.openStream();
	    
	    System.out.println("Setting Output Stream...");
	    FileOutputStream fos = new FileOutputStream(new File(file));
	    
	    int length = -1;
	    byte[] buffer = new byte[1024];
	    
	    System.out.println("Reading bytes...");
	    while ((length = in.read(buffer)) > -1){
	        fos.write(buffer, 0, length);
	    }
	    
	    System.out.println("Closing Streams...");
	    fos.close();
	    in.close();
	    
	    
	    System.out.println("File " + file + " Installed!");
	}

}
