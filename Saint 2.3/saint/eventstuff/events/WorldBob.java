package saint.eventstuff.events;

import saint.eventstuff.Cancellable;
import saint.eventstuff.Event;

public class WorldBob extends Event implements Cancellable {
   private boolean cancel;
   private float partialTicks;

   public WorldBob(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean shouldCancel) {
      this.cancel = shouldCancel;
   }

   public void setPartialTicks(float partialTicks) {
      this.partialTicks = partialTicks;
   }
}
