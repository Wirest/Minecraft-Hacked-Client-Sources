package saint.eventstuff.events;

import saint.eventstuff.Cancellable;
import saint.eventstuff.Event;

public class HurtCam extends Event implements Cancellable {
   boolean cancel;

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean shouldCancel) {
      this.cancel = shouldCancel;
   }
}
