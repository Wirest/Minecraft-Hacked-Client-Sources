package saint.modstuff.modules;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class NoSlowdown extends Module {
   public NoSlowdown() {
      super("NoSlowdown", -12302521, ModManager.Category.MOVEMENT);
      this.setTag("No Slowdown");
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (mc.thePlayer.isBlocking()) {
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
         }
      } else if (event instanceof PostMotion && mc.thePlayer.isBlocking()) {
         mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
      }

   }
}
