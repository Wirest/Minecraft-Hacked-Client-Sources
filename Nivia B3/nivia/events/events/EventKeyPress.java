package nivia.events.events;

import nivia.events.Event;

public class EventKeyPress implements Event {
	private final int eventKey;

	public EventKeyPress(final int eventKey) {
		this.eventKey = eventKey;
	}
	public int getEventKey() {
		return eventKey;
	}
}
