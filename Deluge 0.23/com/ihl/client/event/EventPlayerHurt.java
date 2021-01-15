package com.ihl.client.event;

public class EventPlayerHurt extends Event {

    public float oldHealth, newHealth;

    public EventPlayerHurt(Type type, float oldHealth, float newHealth) {
        super(type);
        this.oldHealth = oldHealth;
        this.newHealth = newHealth;
    }

}
