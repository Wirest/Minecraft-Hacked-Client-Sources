package saint.eventstuff.events;

import saint.eventstuff.Event;

public class Xray extends Event {
   boolean allow = false;
   int opacity = 553648127;

   public int getOpacity() {
      return this.opacity;
   }

   public void setOpacity(int opacity) {
      this.opacity = opacity;
   }

   public boolean shouldRay() {
      return this.allow;
   }

   public void setRay(boolean allow) {
      this.allow = allow;
   }
}
