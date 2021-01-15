package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventBBSet implements Event {

	public Block block;
	public BlockPos pos;
	public AxisAlignedBB boundingBox;

	public EventBBSet(Block block, BlockPos pos, AxisAlignedBB boundingBox) {
		this.block = block;
		this.pos = pos;
		this.boundingBox = boundingBox;
	}

}
