package rip.autumn.module.impl.movement;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.UseItemEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.combat.AuraMod;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.utils.PlayerUtils;

@Label("No Slow")
@Category(ModuleCategory.MOVEMENT)
@Aliases({"noslow", "noslowdown"})
public final class NoSlowMod extends Module {
   public final BoolOption ncp = new BoolOption("NCP", true);

   public NoSlowMod() {
      this.addOptions(new Option[]{this.ncp});
   }

   @Listener(UseItemEvent.class)
   public final void onUseItem(UseItemEvent event) {
      event.setCancelled();
   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      if (this.ncp.getValue() && this.isBlocking() && mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
         if (event.isPre()) {
            mc.playerController.syncCurrentPlayItem();
            mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
         } else {
            mc.playerController.syncCurrentPlayItem();
            mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
         }
      }

   }

   private boolean isBlocking() {
      return PlayerUtils.isHoldingSword() && mc.thePlayer.isBlocking() && !AuraMod.blocking;
   }
}
