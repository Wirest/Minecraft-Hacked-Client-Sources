package rip.autumn.events;

public class Cancellable {
   private boolean cancelled;

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public void setCancelled() {
      this.cancelled = true;
   }
}
