package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;

public class EventSendChat extends EventCancellable {
    private String message;

    public EventSendChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
