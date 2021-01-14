package rip.autumn.events.game;

import rip.autumn.events.Cancellable;
import rip.autumn.events.Event;

public final class SendMessageEvent extends Cancellable implements Event {
   private final String message;

   public SendMessageEvent(String message) {
      this.message = message;
   }

   public String getMessage() {
      return this.message;
   }
}
