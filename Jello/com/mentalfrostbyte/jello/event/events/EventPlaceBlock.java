package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;


public class EventPlaceBlock extends EventCancellable implements Event
{
    private BlockPos blockPos;
    private EnumFacing enumFacing;
    
    public EventPlaceBlock() {
        this.blockPos = null;
        this.enumFacing = null;
        this.setBlockPos(blockPos);
        this.setEnumFacing(enumFacing);
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
    
    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }
    
    public void setEnumFacing(final EnumFacing enumFacing) {
        this.enumFacing = enumFacing;
    }
}
