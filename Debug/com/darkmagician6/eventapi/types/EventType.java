package com.darkmagician6.eventapi.types;

public enum EventType
{
    PRE, 
    POST, 
    SEND, 
    RECIEVE;
    
    private static final EventType[] $VALUES;
    
//    public static EventType[] values() {
//        return EventType.$VALUES.clone();
//    }
//    
//    public static EventType valueOf(final String name) {
//        return Enum.valueOf(EventType.class, name);
//    }
    
    static {
        $VALUES = new EventType[] { EventType.PRE, EventType.POST, EventType.SEND, EventType.RECIEVE };
    }
}
