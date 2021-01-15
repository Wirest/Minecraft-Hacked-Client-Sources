package awfdd.ksksk.zabejhf.ihae;

import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf.ZAhbhjefb;

/**
 * An abstract filter class
 * 
 * @author TCB
 *
 */
public abstract class zabekf implements ehgewgr {
	private ZAhbhjefb ZAhbhjefb;

	/**
	 * A no-arg constructor must be present if this filter is set through
	 * {@link Subscribe#filter()}.
	 */
	private zabekf() {}

	/**
	 * Returns the method context that uses this filter.
	 * @return {@link ZAhbhjefb}
	 */
	public ZAhbhjefb getZAhbhjefb() {
		return this.ZAhbhjefb;
	}

	/**
	 * This method is called after the IFilter constructor has
	 * been called. Only called if the filter has been set through
	 * {@link Subscribe#filter()}.
	 */
	protected void init() { }

	@Override
	public final void init(ZAhbhjefb entry) {
		this.ZAhbhjefb = entry;
		this.init();
	}

	@Override
	public abstract boolean filter(Event event);
}
