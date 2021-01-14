package store.shadowclient.client.event.events;

import store.shadowclient.client.event.Event;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

public class EventRendererLivingEntity extends Event {
	   public RendererLivingEntity rendererLivingEntity;
	   public float f1;
	   public float f2;
	   public float f3;
	   public float f4;
	   public float f5;
	   public float f6;
	   public EntityLivingBase entity;

	   public EventRendererLivingEntity(RendererLivingEntity rendererLivingEntity, EntityLivingBase entity, float f1, float f2, float f3, float f4, float f5, float f6) {
	      this.rendererLivingEntity = rendererLivingEntity;
	      this.f1 = f1;
	      this.f2 = f2;
	      this.f3 = f3;
	      this.f4 = f4;
	      this.f5 = f5;
	      this.f6 = f6;
	      this.entity = entity;
	   }
	}
