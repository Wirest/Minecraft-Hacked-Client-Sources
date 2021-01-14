package rip.autumn.events.render;

import rip.autumn.events.Cancellable;
import rip.autumn.events.Event;

public final class Render3DEvent extends Cancellable implements Event {
   private final float partialTicks;

   public Render3DEvent(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
