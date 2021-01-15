package saint.eventstuff.events;

import saint.eventstuff.Event;

public class Reach extends Event {
   private float reach;

   public Reach(float reach) {
      this.reach = reach;
   }

   public float getReach() {
      return this.reach;
   }

   public void setReach(float reach) {
      this.reach = reach;
   }
}
