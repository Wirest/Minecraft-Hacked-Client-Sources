package info.spicyclient.events.listeners;

import info.spicyclient.events.Event;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;

public class EventPlayerRender extends Event<EventPlayerRender> {
	
	public EventPlayerRender(AbstractClientPlayer entity) {
		
		this.entity = entity;
		
	}
	
	public AbstractClientPlayer entity;
	
}
