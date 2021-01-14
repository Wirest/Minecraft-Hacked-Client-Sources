package com.mentalfrostbyte.jello.event.events;

import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;

public class BlockRender implements Event
{
    private final Block block;
    private final BlockPos pos;
    
    public BlockRender(final Block block, final BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
}
