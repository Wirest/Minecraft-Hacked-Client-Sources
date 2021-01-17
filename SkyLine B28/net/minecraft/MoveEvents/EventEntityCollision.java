package net.minecraft.MoveEvents;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;
import skyline.specc.helper.loc.Loc;

public class EventEntityCollision extends Event {

	private Entity entity;
	private Block block;
	private Loc loc;
	private AxisAlignedBB boundingBox;

	public EventEntityCollision(Entity entity, Loc loc, AxisAlignedBB boundingBox,
			Block block) {
		this.entity = entity;
		this.loc = loc;
		this.boundingBox = boundingBox;
		this.block = block;
	}

	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(AxisAlignedBB boundingBox) {
		this.boundingBox = boundingBox;
	}

	public Entity getEntity() {
		return entity;
	}

	public Loc getLocation() {
		return loc;
	}

	public Block getBlock() {
		return block;
	}

}
