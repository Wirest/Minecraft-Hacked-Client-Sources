package javax.jnlp;

/**
 * The playground version of the JNLP API Service Manager
 *
 * Note: This JNLP API implementation is limited to the absolute required minimum
 * for Slick applications and will grow as features are required.
 * 
 * @author kevin
 */
public class ServiceManager {
	/** The persistence service implementation */
	private static PersistenceService per = new PersistenceServiceImpl();
	/** The basic service implementation */
	private static BasicService basic = new BasicServiceImpl();
	
	/**
	 * Pure static
	 */
	private ServiceManager() {
	}
	
	/**
	 * Lookup a service based on a class name. Only a limited set of services
	 * are supported.
	 *  
	 * @param name The name of the service to retrieve
	 * @return The service requested
	 */
	public static Object lookup(String name) {
		if (name.equals("javax.jnlp.PersistenceService")) {
			return per;
		}
		if (name.equals("javax.jnlp.BasicService")) {
			return basic;
		}
		
		throw new RuntimeException("Service "+name+" not supported in Playground");
	}
}
