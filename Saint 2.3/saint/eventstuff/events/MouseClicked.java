package saint.eventstuff.events;

import saint.eventstuff.Event;

public class MouseClicked extends Event {
   private int button;

   public MouseClicked(int button) {
      this.button = button;
   }

   public int getButton() {
      return this.button;
   }
}
