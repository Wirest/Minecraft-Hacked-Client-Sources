package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.types.EventType;
import com.mentalfrostbyte.jello.event.types.EventType2;

public class EventPlayerUpdate implements Event {
    private EventType2 type;
    public double x;
    public double y;
    public double z;

    public EventPlayerUpdate(EventType2 type) {
        this.type = type;
    }

    public EventType2 getType() {
        return this.type;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}

