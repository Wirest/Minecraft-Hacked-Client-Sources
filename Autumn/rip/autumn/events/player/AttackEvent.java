package rip.autumn.events.player;

import net.minecraft.entity.Entity;
import rip.autumn.events.Cancellable;
import rip.autumn.events.Event;

public final class AttackEvent extends Cancellable implements Event {
   private final Entity entity;

   public AttackEvent(Entity entity) {
      this.entity = entity;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
