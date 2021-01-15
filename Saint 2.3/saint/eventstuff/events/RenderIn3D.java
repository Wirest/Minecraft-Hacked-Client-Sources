package saint.eventstuff.events;

import saint.eventstuff.Event;

public class RenderIn3D extends Event {
   public float partialTicks;

   public RenderIn3D(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
