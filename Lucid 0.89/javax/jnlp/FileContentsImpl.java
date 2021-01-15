package javax.jnlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Basic implementation of file content using a local file in the playground data directory
 * for the particular application
 *
 * Note: This JNLP API implementation is limited to the absolute required minimum
 * for Slick applications and will grow as features are required.
 * 
 * @author kevin
 */
public class FileContentsImpl implements FileContents {
	/** The url locating the file */
	private URL url;
	/** The file being accessed */
	private File file;
	
	/**
	 * Create a new file wrapped aroudn the specified file
	 * 
	 * @param url The URL we want to access
	 */
	public FileContentsImpl(URL url) {
		this.url = url;
		String fileLocation = url.toString().substring(url.getProtocol().length()+2);
		file = new File(fileLocation);
	}
	
	/**
	 * @see javax.jnlp.FileContents#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}
	
	/**
	 * @see javax.jnlp.FileContents#getOutputStream(boolean)
	 */
	public OutputStream getOutputStream(boolean create) throws IOException {
		if (create) {
			file.getParentFile().mkdirs();
		}
		return new FileOutputStream(file);
	}
}
