package awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw;


import awfdd.ksksk.ap.zajkb.rgds.Event;

/**
 * An abstract cancellable event class
 * 
 * @author TCB
 *
 */
public abstract class EventCancellable extends Event implements ZAhbhjerCancellable {
	private boolean isCancelled;

	@Override
	public void setCancelled() {
		this.isCancelled = true;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}
}