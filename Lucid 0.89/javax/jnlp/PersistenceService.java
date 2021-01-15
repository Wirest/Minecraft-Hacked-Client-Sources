package javax.jnlp;

import java.io.File;
import java.net.URL;

/**
 * A limited version of the JNLP Persistence Service
 *
 * Note: This JNLP API implementation is limited to the absolute required minimum
 * for Slick applications and will grow as features are required.
 * 
 * @author kevin
 */
public interface PersistenceService {
	/**
	 * Get the contents of a file at the specified URL. This URL should be relative to the
	 * code base retrieved from the BasicService
	 * 
	 * @see BasicService
	 * @param url The URL to access
	 * @return The handle to the file requested
	 */
	public FileContents get(URL url);
	
	/**
	 * Create a file at the specified location. The URL specified should be relative to the
	 * code base retrieved from the BasicService
	 * 
	 * @see BasicService
	 * @param url The URL pointing to the location to create
	 * @param length The size of the potential content to reserve
	 * @return The size that was reserved
	 */
	public long create(URL url, long length);
	
	/**
	 * Delete the file specified. The URL specified should be relative to the code base retrieved
	 * from the BasicService
	 * 
	 * @see BasicService
	 * @param url The URL pointing to the location to delete
	 */
	public void delete(URL url);
}
