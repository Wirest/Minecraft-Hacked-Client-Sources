package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

public class EventBlockBB implements Event {

	public Block block;
	public AxisAlignedBB boundingBox;
	public int x, y, z;

	public Block getBlock() {
		return block;
	}

	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setBlock(Block block, AxisAlignedBB boundingBox, int x, int y, int z) {
		this.block = block;
		this.boundingBox = boundingBox;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setBoundingBox(AxisAlignedBB boundingBox) {
		this.boundingBox = boundingBox;
	}

}