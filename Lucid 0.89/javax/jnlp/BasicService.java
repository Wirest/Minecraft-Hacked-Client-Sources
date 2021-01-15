package javax.jnlp;

import java.io.File;
import java.net.URL;

/**
 * The JNLP basic service interface. Allows access to basic settings.
 * 
 * Note: This JNLP API implementation is limited to the absolute required minimum
 * for Slick applications and will grow as features are required.
 *
 * @author kevin
 */
public interface BasicService {
	/**
	 * Get the code base where the application data is homed
	 * 
	 * @return The code base where the application data is homed
	 */
	public URL getCodeBase();
}
