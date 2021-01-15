package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.Module;

public class AntiInvis extends Module {
   public AntiInvis() {
      super("AntiInvis");
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         Iterator var3 = mc.theWorld.loadedEntityList.iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            Entity e = (Entity)o;
            if (e != null && e != mc.thePlayer && !e.isDead) {
               e.setInvisible(false);
            }
         }
      }

   }
}
