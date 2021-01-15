package saint.modstuff.modules;

import net.minecraft.init.Items;
import saint.eventstuff.Event;
import saint.eventstuff.events.FOVChanges;
import saint.modstuff.Module;

public class NoFOV extends Module {
   public NoFOV() {
      super("NoFOV");
   }

   public void onEvent(Event event) {
      if (event instanceof FOVChanges) {
         FOVChanges fov = (FOVChanges)event;
         float newfov = 1.0F;
         if (mc.thePlayer.isUsingItem() && mc.thePlayer.getItemInUse().getItem() == Items.bow) {
            int duration = mc.thePlayer.getItemInUseDuration();
            float dd = (float)duration / 20.0F;
            if (dd > 1.0F) {
               dd = 1.0F;
            } else {
               dd *= dd;
            }

            newfov *= 1.0F - dd * 0.15F;
         }

         fov.setFOV(newfov);
      }

   }
}
