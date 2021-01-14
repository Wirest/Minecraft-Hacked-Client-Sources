package rip.autumn.events.render;

import net.minecraft.entity.EntityLivingBase;
import rip.autumn.events.Cancellable;
import rip.autumn.events.Event;

public final class RenderNametagEvent extends Cancellable implements Event {
   private final EntityLivingBase entity;
   private String renderedName;

   public RenderNametagEvent(EntityLivingBase entity, String renderedName) {
      this.entity = entity;
      this.renderedName = renderedName;
   }

   public EntityLivingBase getEntity() {
      return this.entity;
   }

   public String getRenderedName() {
      return this.renderedName;
   }

   public void setRenderedName(String renderedName) {
      this.renderedName = renderedName;
   }
}
