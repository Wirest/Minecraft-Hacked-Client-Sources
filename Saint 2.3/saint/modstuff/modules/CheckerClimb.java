package saint.modstuff.modules;

import net.minecraft.util.AxisAlignedBB;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockBB;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class CheckerClimb extends Module {
   public CheckerClimb() {
      super("CheckerClimb", -1, ModManager.Category.PLAYER);
      this.setTag("Checker Climb");
   }

   public void onEvent(Event event) {
      if (event instanceof BlockBB) {
         BlockBB bb = (BlockBB)event;
         if ((double)bb.getY() > mc.thePlayer.posY) {
            bb.setBoundingBox((AxisAlignedBB)null);
         }
      }

   }
}
