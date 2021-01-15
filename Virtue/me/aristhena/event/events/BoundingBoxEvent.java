// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.event.events;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import me.aristhena.event.Event;

public class BoundingBoxEvent extends Event
{
    private Block block;
    private BlockPos blockPos;
    private AxisAlignedBB boundingBox;
    
    public BoundingBoxEvent(final Block block, final BlockPos pos, final AxisAlignedBB boundingBox) {
        this.block = block;
        this.blockPos = pos;
        this.boundingBox = boundingBox;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
    
    public void setBlock(final Block block) {
        this.block = block;
    }
    
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
    
    public void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}
