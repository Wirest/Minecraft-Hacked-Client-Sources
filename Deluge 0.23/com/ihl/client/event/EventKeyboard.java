package com.ihl.client.event;

public class EventKeyboard extends Event {

    public int code;
    public char keyChar;

    public EventKeyboard(Type type, int code, char keyChar) {
        super(type);
        this.code = code;
        this.keyChar = keyChar;
    }

}
