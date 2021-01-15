package saint.eventstuff.events;

import saint.eventstuff.Event;

public class RightClicked extends Event {
   int delay;

   public RightClicked(int delay) {
      this.delay = delay;
   }

   public int getDelay() {
      return this.delay;
   }

   public void setDelay(int delay) {
      this.delay = delay;
   }
}
