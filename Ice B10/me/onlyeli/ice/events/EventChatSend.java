package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;

public class EventChatSend extends EventCancellable {

	public String message;

	public EventChatSend(String message) {
		this.message = message;
	}

}
