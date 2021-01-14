package rip.autumn.module.impl.world;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.BlockDamagedEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;

@Label("Fast Break")
@Category(ModuleCategory.WORLD)
@Aliases({"fastbreak"})
public final class FastBreakMod extends Module {
   @Listener(MotionUpdateEvent.class)
   public final void onUpdate(MotionUpdateEvent event) {
      if (event.isPre()) {
         mc.playerController.blockHitDelay = 0;
      }

   }

   @Listener(BlockDamagedEvent.class)
   public void onBlockDamaged(BlockDamagedEvent event) {
      PlayerControllerMP playerController = mc.playerController;
      BlockPos pos = event.getBlockPos();
      mc.thePlayer.swingItem();
      playerController.curBlockDamageMP += this.getBlock((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()).getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, pos) * 0.186F;
   }

   public Block getBlock(double posX, double posY, double posZ) {
      BlockPos pos = new BlockPos((int)posX, (int)posY, (int)posZ);
      return mc.theWorld.getChunkFromBlockCoords(pos).getBlock(pos);
   }
}
