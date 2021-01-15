package com.ihl.client.event;

import net.minecraft.util.MovingObjectPosition;

public class EventPlayerPickBlock extends Event {

    public MovingObjectPosition mop;

    public EventPlayerPickBlock(Type type, MovingObjectPosition mop) {
        super(type);
        this.mop = mop;
    }

}
