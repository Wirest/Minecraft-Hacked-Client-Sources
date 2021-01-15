package com.ihl.client.event;

import net.minecraft.entity.Entity;

public class EventPlayerAttack extends Event {

    public Entity target;

    public EventPlayerAttack(Type type, Entity target) {
        super(type);
        this.target = target;
    }

}
