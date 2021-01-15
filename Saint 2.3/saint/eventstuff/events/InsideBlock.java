package saint.eventstuff.events;

import saint.eventstuff.Cancellable;
import saint.eventstuff.Event;

public class InsideBlock extends Event implements Cancellable {
   private boolean cancel;

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean shouldCancel) {
      this.cancel = shouldCancel;
   }
}
