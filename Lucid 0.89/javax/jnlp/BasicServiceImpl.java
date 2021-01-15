package javax.jnlp;

import java.io.File;
import java.net.URL;

/**
 * Simple implementation of the BasicService based around the playground ID
 *
 * Note: This JNLP API implementation is limited to the absolute required minimum
 * for Slick applications and will grow as features are required.
 * 
 * @author kevin
 */
public class BasicServiceImpl implements BasicService {
	/**
	 * @see javax.jnlp.BasicService#getCodeBase()
	 */
	public URL getCodeBase() {
		String id = System.getProperty("pg.id");
		File file = new File(".");
		File path = new File(file, id);
		File data = new File(path, "data");
		
		data.mkdirs();
		
		try {
			return data.toURL();
		} catch (Exception e) {
			throw new RuntimeException("Unable to use specified data location: "+data);
		}
	}
}
