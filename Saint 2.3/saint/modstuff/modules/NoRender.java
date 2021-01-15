package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.Module;

public class NoRender extends Module {
   public NoRender() {
      super("NoRender");
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         Iterator var3 = mc.theWorld.loadedEntityList.iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            Entity entity = (Entity)o;
            if (entity instanceof EntityItem) {
               mc.theWorld.removeEntity(entity);
            }
         }
      }

   }
}
