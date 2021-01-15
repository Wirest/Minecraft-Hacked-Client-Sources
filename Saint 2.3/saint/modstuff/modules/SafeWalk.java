package saint.modstuff.modules;

import saint.eventstuff.Event;
import saint.eventstuff.events.Walking;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class SafeWalk extends Module {
   public SafeWalk() {
      super("SafeWalk", -8388608, ModManager.Category.PLAYER);
      this.setTag("Safe Walk");
   }

   public void onEvent(Event event) {
      if (event instanceof Walking) {
         ((Walking)event).setSafeWalk(true);
      }

   }
}
