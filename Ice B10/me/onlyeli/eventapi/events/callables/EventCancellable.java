package me.onlyeli.eventapi.events.callables;

import me.onlyeli.eventapi.events.Cancellable;
import me.onlyeli.eventapi.events.Event;

/**
 * Abstract example implementation of the Cancellable interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class EventCancellable implements Event, Cancellable {

	private boolean cancelled;

	protected EventCancellable() {
	}

	/**
	 * @see me.onlyeli.eventapi.events.Cancellable.isCancelled
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @see me.onlyeli.eventapi.events.Cancellable.setCancelled
	 */
	@Override
	public void setCancelled(boolean state) {
		cancelled = state;
	}

}
