package saint.modstuff.modules;

import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockBB;
import saint.modstuff.Module;

public class AntiCactus extends Module {
   public AntiCactus() {
      super("AntiCactus");
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof BlockBB) {
         BlockBB bb = (BlockBB)event;
         if (bb.getBlock() instanceof BlockCactus) {
            bb.setBoundingBox(new AxisAlignedBB((double)bb.getX(), (double)bb.getY(), (double)bb.getZ(), (double)((float)bb.getX() + 1.0F), (double)((float)bb.getY() + 1.0F), (double)((float)bb.getZ() + 1.0F)));
         }
      }

   }
}
