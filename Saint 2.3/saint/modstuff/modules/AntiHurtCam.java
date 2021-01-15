package saint.modstuff.modules;

import saint.eventstuff.Event;
import saint.eventstuff.events.HurtCam;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class AntiHurtCam extends Module {
   public AntiHurtCam() {
      super("AntiHurtCam", -4343957, ModManager.Category.PLAYER);
      this.setTag("Anti Hurt Cam");
   }

   public void onEvent(Event event) {
      if (event instanceof HurtCam) {
         HurtCam hc = (HurtCam)event;
         hc.setCancelled(true);
      }

   }
}
