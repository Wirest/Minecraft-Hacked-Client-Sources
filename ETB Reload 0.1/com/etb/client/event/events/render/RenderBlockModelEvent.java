package com.etb.client.event.events.render;

import com.etb.client.event.Event;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class RenderBlockModelEvent extends Event {
    private final Block block;

    private final BlockPos pos;

    public RenderBlockModelEvent(Block block, BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }

    public Block getBlock() {
        return block;
    }

    public BlockPos getPos() {
        return pos;
    }
}