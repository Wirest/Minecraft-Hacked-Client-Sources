package com.ihl.client.event;

public class EventPlayerMove extends Event {

    public double x, y, z;

    public EventPlayerMove(Type type, double x, double y, double z) {
        super(type);
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
