package me.onlyeli.ice.events;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import me.onlyeli.ice.events.Location;
import me.onlyeli.ice.events.Events;

public class EventBlockBoundingBox extends Events {

	private AxisAlignedBB box;
	private Block block;
	private Location location;
	private Entity collidingEntity;

	public EventBlockBoundingBox(AxisAlignedBB box, Block block,
			Location location, Entity collidingEntity){
		this.box = box;
		this.block = block;
		this.location = location;
		this.collidingEntity = collidingEntity;
	}

	
	
	public AxisAlignedBB getBox(){
		return box;
	}

	public void setBox(AxisAlignedBB box){
		this.box = box;
	}

	public Block getBlock(){
		return block;
	}

	public void setBlock(Block block){
		this.block = block;
	}

	public Location getLocation(){
		return location;
	}

	public void setLocation(Location location){
		this.location = location;
	}



	public Entity getCollidingEntity(){
		return collidingEntity;
	}

}
