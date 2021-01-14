package rip.autumn.events.game;

import rip.autumn.events.Event;

public final class KeyPressEvent implements Event {
   private final int keyCode;

   public KeyPressEvent(int keyCode) {
      this.keyCode = keyCode;
   }

   public int getKeyCode() {
      return this.keyCode;
   }
}
