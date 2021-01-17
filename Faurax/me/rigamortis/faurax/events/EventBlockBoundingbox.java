package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

public class EventBlockBoundingbox extends EventCancellable implements Event
{
    private final Block block;
    public AxisAlignedBB boundingBox;
    private final double x;
    private final double y;
    private final double z;
    
    public EventBlockBoundingbox(final AxisAlignedBB bb, final Block block, final double x, final double y, final double z) {
        this.block = block;
        this.boundingBox = bb;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
    
    public void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
    
    public Block getBlock() {
        return this.block;
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
}
