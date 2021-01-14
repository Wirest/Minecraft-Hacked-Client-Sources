package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.util.BlockPos;

public class EventClickBlock
extends EventCancellable {
    BlockPos blockpos;

    public EventClickBlock(BlockPos blockpos) {
        this.blockpos = blockpos;
    }

    public BlockPos getBlockPos() {
        return this.blockpos;
    }
}

