package rip.autumn.module.impl.movement;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.utils.PlayerUtils;
import rip.autumn.utils.Stopwatch;

@Label("Anti Fall")
@Category(ModuleCategory.MOVEMENT)
@Aliases({"antifall", "antivoid", "novoid"})
public final class AntiFallMod extends Module {
   private final Stopwatch fallStopwatch = new Stopwatch();
   public final DoubleOption distance = new DoubleOption("Distance", 5.0D, 3.0D, 10.0D, 0.5D);

   public AntiFallMod() {
      this.addOptions(new Option[]{this.distance});
   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      EntityPlayerSP player = mc.thePlayer;
      if ((double)player.fallDistance > (Double)this.distance.getValue() && !player.capabilities.isFlying && this.fallStopwatch.elapsed(250L) && !PlayerUtils.isBlockUnder()) {
         mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + (Double)this.distance.getValue() + 1.0D, player.posZ, false));
         this.fallStopwatch.reset();
      }

   }
}
