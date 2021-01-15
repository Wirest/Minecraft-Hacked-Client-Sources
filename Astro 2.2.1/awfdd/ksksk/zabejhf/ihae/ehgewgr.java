package awfdd.ksksk.zabejhf.ihae;

import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf.ZAhbhjefb;

/**
 * This interface must be implemented in order to create a functional and valid filter.
 * 
 * @author TCB
 *
 */
public interface ehgewgr {
	/**
	 * This method is called after the {@link ehgewgr} no-arg constructor has
	 * been called. Only called if the filter has been set through
	 * {@link Subscribe#filter()}. The {@link ZAhbhjefb} parameter can be used
	 * to retrieve the listener object.
	 * @param context {@link ZAhbhjefb}
	 */
	public void init(ZAhbhjefb context);

	/**
	 * This method filters the incoming {@link Event}.
	 * Return false to cancel the event for the specific
	 * listening method this filter belongs to. 
	 * This does not cancel the event itself, it just prevents
	 * the event from being received by the listening method
	 * this filter belongs to.
	 * @param event {@link Event}
	 * @return boolean
	 */
	public boolean filter(Event event);
}
