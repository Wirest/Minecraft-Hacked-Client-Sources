package saint.eventstuff.events;

import saint.eventstuff.Cancellable;
import saint.eventstuff.Event;

public class ChatMessage extends Event implements Cancellable {
   private String message;
   private boolean cancel;

   public ChatMessage(String message) {
      this.message = message;
   }

   public String getMessage() {
      return this.message;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean shouldCancel) {
      this.cancel = shouldCancel;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
