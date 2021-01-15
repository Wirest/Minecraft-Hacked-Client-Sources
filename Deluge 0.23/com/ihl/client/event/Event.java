package com.ihl.client.event;

public class Event {

    public static enum Type {
        PRE,
        POST,
        SEND,
        RECEIVE,
        CLICKL,
        CLICKM,
        CLICKR,
        PRESS,
        RELEASE,
        SCROLL
    }

    public Type type;
    public boolean cancelled;

    public Event(Type type) {
        this.type = type;
    }

    public void cancel() {
        cancelled = true;
    }

}
