package com.ihl.client.event;

public class EventChatSend extends Event {

    public String message;

    public EventChatSend(Type type, String message) {
        super(type);
        this.message = message;
    }

}
