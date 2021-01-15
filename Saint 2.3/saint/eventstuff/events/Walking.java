package saint.eventstuff.events;

import saint.eventstuff.Event;

public class Walking extends Event {
   boolean walk = false;

   public void setSafeWalk(boolean walk) {
      this.walk = walk;
   }

   public boolean getSafeWalk() {
      return this.walk;
   }
}
