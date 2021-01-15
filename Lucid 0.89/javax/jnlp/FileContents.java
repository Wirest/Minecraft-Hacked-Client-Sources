package javax.jnlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * A JNLP API description of a file available for access through the webstart layer
 * 
 * Note: This JNLP API implementation is limited to the absolute required minimum
 * for Slick applications and will grow as features are required.
 *
 * @author kevin
 */
public interface FileContents {
	/**
	 * Get an input stream to read from the file
	 * 
	 * @return The input stream to read from the file
	 * @throws IOException Indicates a failure to access the file
	 */
	public InputStream getInputStream() throws IOException;
	
	/**
	 * Get a stream to write out to the file
	 * 
	 * @param create True if we should create the file if required 
	 * @return The stream to write to the file
	 * @throws IOException Indictes a failure to access the file
	 */
	public OutputStream getOutputStream(boolean create) throws IOException;
}
