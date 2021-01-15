package saint.modstuff.modules;

import saint.eventstuff.Event;
import saint.eventstuff.events.OnUpdate;
import saint.modstuff.Module;

public class NoBob extends Module {
   public NoBob() {
      super("NoBob");
   }

   public void onEvent(Event event) {
      if (event instanceof OnUpdate) {
         mc.thePlayer.distanceWalkedModified = 0.0F;
      }

   }
}
