package saint.modstuff.modules;

import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.TimeHelper;

public class Parkour extends Module {
   private final TimeHelper time = new TimeHelper();

   public Parkour() {
      super("Parkour", -102980, ModManager.Category.PLAYER);
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         boolean shouldTrigger = !mc.thePlayer.onGround && BlockHelper.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) instanceof BlockAir && BlockHelper.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ)) instanceof BlockAir && BlockHelper.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2.0D, mc.thePlayer.posZ)) instanceof BlockAir && BlockHelper.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 3.0D, mc.thePlayer.posZ)) instanceof BlockAir;
         if (!Saint.getModuleManager().getModuleUsingName("fly").isEnabled() && !Saint.getModuleManager().getModuleUsingName("glide").isEnabled() && shouldTrigger && this.time.hasReached(100L)) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ, true));
            this.time.reset();
         }
      }

   }
}
