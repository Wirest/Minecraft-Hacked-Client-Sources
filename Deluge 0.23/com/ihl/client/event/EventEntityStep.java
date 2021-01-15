package com.ihl.client.event;

import net.minecraft.entity.Entity;

public class EventEntityStep extends Event {

    public Entity entity;

    public EventEntityStep(Type type, Entity entity) {
        super(type);
        this.entity = entity;
    }

}
