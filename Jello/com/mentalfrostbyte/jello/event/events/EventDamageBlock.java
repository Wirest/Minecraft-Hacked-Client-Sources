package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class EventDamageBlock
extends EventCancellable
implements Event {
    private BlockPos blockPos = null;
    private EnumFacing enumFacing = null;

    public EventDamageBlock(BlockPos blockPos, EnumFacing enumFacing) {
        this.setBlockPos(blockPos);
        this.setEnumFacing(enumFacing);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }

    public void setEnumFacing(EnumFacing enumFacing) {
        this.enumFacing = enumFacing;
    }
}

