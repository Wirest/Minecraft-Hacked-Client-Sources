package me.aristhena.lucid.eventapi.events;

import me.aristhena.lucid.eventapi.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BoundingBoxEvent extends Event
{
    public Block block;
    public BlockPos pos;
    public AxisAlignedBB boundingBox;
    
    public BoundingBoxEvent(Block block, BlockPos pos, AxisAlignedBB boundingBox)
    {
	this.block = block;
	this.pos = pos;
	this.boundingBox = boundingBox;
    }
}
