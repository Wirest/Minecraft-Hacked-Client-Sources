package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.entity.Entity;

public class EventEntityStep
extends EventCancellable {
    private float stepHeight;
    private Entity entity;
    private byte type;

    public EventEntityStep(Entity entity, float stepHeight, byte pre) {
        this.entity = entity;
        this.stepHeight = stepHeight;
        this.type = pre;
    }

    public float getStepHeight() {
        return this.stepHeight;
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public byte getType() {
        return this.type;
    }
}

