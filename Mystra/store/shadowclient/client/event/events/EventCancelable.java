package store.shadowclient.client.event.events;

import store.shadowclient.client.event.Event;

public class EventCancelable extends Event {
	private boolean cancelled = false;

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
