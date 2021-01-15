package awfdd.ksksk.ap.zajkb.rgds;

import awfdd.ksksk.ap.zajkb.Aefbe;
import awfdd.ksksk.ap.zajkb.ZAhbhjer;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;

/**
 * An abstract event class
 * 
 * @author TCB
 *
 */
public abstract class Event implements ZAhbhjer {
	private Aefbe context = null;

	public EventType getEventType() {
		return type;
	}

	public void setEventType(EventType eventType) {
		this.type = eventType;
	}

	private EventType type;

	public Event() {
		this.type = EventType.SINGLE;
	}

	public Event(EventType type) {
		this.type = type;
	}

	@Override
	public final Event setContext(Aefbe context) {
		this.context = context;
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final <T extends Aefbe> T getContext(Class<T> type) {
		if(type == null) return (T) this.context;
		if(this.context != null && this.context.getClass() == type) {
			return (T) this.context;
		}
		return null;
	}

	/**
	 * Returns the context of this event
	 * @return {@link zajg123} the context
	 */
	public final Aefbe getContext() {
		return this.context;
	}
}