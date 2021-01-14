package com.mentalfrostbyte.jello.event.events;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public class EventEntityCollision
implements Event {
	private Entity entity;
    private Block block;
    //private Location location;
    private AxisAlignedBB boundingBox;
    private boolean cancel;

    public EventEntityCollision(Entity entity, /*Location location,*/ AxisAlignedBB boundingBox, Block block) {
        this.entity = entity;
        //this.location = location;
        this.boundingBox = boundingBox;
        this.block = block;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Entity getEntity() {
        return this.entity;
    }

    //public Location getLocation() {
    //    return this.location;
    //}
    
    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Block getBlock() {
        return this.block;
    }
}