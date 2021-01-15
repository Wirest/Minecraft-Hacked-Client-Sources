package javax.jnlp;

import java.io.File;
import java.net.URL;

/**
 * The playground implementation of the persistence service based around a data
 * file in the application cache.
 *
 * Note: This JNLP API implementation is limited to the absolute required minimum
 * for Slick applications and will grow as features are required.
 * 
 * @author kevin
 */
public class PersistenceServiceImpl implements PersistenceService {
	/**
	 * @see javax.jnlp.PersistenceService#get(java.net.URL)
	 */
	public FileContents get(URL url) {
		return new FileContentsImpl(url);
	}
	
	/**
	 * @see javax.jnlp.PersistenceService#create(java.net.URL, long)
	 */
	public long create(URL url, long length) {
		// do nothing, we don't care
		return length;
	}
	
	/**
	 * @see javax.jnlp.PersistenceService#delete(java.net.URL)
	 */
	public void delete(URL url) {
		String fileLocation = url.toString().substring(url.getProtocol().length()+2);
		File file = new File(fileLocation);
		
		file.delete();
	}
}
