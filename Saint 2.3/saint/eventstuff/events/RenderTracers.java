package saint.eventstuff.events;

import saint.eventstuff.Event;

public class RenderTracers extends Event {
   private float partialTicks;

   public RenderTracers(float partialTicks2) {
      this.partialTicks = partialTicks2;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
