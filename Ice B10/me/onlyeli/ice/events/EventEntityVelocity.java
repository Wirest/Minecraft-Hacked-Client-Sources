package me.onlyeli.ice.events;

import me.onlyeli.ice.events.Events;
import net.minecraft.entity.Entity;

public class EventEntityVelocity extends Events {

	private Entity entity;
	private double motionX, motionY, motionZ;

	public EventEntityVelocity(Entity entity, double motionX, double motionY,
			double motionZ){
		this.entity = entity;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public Entity getEntity(){
		return entity;
	}

	public void setEntity(Entity entity){
		this.entity = entity;
	}

	public double getMotionX(){
		return motionX;
	}

	public void setMotionX(double motionX){
		this.motionX = motionX;
	}

	public double getMotionY(){
		return motionY;
	}

	public void setMotionY(double motionY){
		this.motionY = motionY;
	}

	public double getMotionZ(){
		return motionZ;
	}

	public void setMotionZ(double motionZ){
		this.motionZ = motionZ;
	}

}
