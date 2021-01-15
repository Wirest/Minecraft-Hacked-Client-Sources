package awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf;

import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;

/**
 * Any event bus implements this interface
 * 
 * @author TCB
 *
 */
public interface HEfbhs {
	/**
	 * Registers an {@link JAHfb} to this bus
	 */
	public void register(JAHfb listener);
	
	/**
	 * Unregisters an {@link JAHfb} from this bus.
	 * Only unregisters the first occurrence of the specified listener.
	 * @param listener {@link JAHfb} to unregister
	 */
	public void unregister(JAHfb listener);
	
	/**
	 * Posts an {@link Event} and returns the posted event.
	 * @param event {@link Event} to dispatch
	 * @return {@link Event} the posted event
	 */
	public <T extends Event> T post(T event);
}
