package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.block.Block;

public class EventCollideCheck
extends EventCancellable {
    public boolean collide;
    public Block block;

    public EventCollideCheck(boolean collide, Block block) {
        this.collide = collide;
        this.block = block;
    }

    public void setCollidable(boolean collide) {
        this.collide = collide;
    }

    public boolean getCollidable() {
        return this.collide;
    }
}

