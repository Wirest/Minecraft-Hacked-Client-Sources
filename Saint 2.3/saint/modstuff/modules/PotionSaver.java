package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import saint.eventstuff.Event;
import saint.eventstuff.events.OldSpeed;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class PotionSaver extends Module {
   public PotionSaver() {
      super("PotionSaver", -7607808, ModManager.Category.COMBAT);
   }

   private boolean isMoving() {
      return mc.thePlayer.motionX != 0.0D && mc.thePlayer.motionZ != 0.0D || !mc.thePlayer.onGround;
   }

   public void onEvent(Event event) {
      if (event instanceof OldSpeed) {
         OldSpeed pre = (OldSpeed)event;
         if (!this.isMoving()) {
            pre.setCancelled(true);
            Iterator var4 = mc.thePlayer.getActivePotionEffects().iterator();

            while(var4.hasNext()) {
               PotionEffect effect = (PotionEffect)var4.next();
               effect.incrementDuration();
            }

            this.setColor(-7607808);
         } else {
            this.setColor(-7829368);
         }
      }

   }
}
