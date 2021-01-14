package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;

public class EventRenderNameTag
extends EventCancellable {
   EntityLivingBase e;
	public EventRenderNameTag(EntityLivingBase e){
		this.e = e;
	}
	public EntityLivingBase getEntity() {
		return e;
	}
	public void setEntity(EntityLivingBase e) {
		this.e = e;
	}
    
	
	
}

