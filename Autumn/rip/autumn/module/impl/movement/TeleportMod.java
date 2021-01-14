package rip.autumn.module.impl.movement;

import java.util.Iterator;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import org.lwjgl.input.Mouse;
import rip.autumn.annotations.Label;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.events.packet.SendPacketEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.Logger;
import rip.autumn.utils.MovementUtils;
import rip.autumn.utils.PlayerUtils;
import rip.autumn.utils.Stopwatch;
import rip.autumn.utils.pathfinding.CustomVec3;
import rip.autumn.utils.pathfinding.PathfindingUtils;

@Label("Teleport")
@Category(ModuleCategory.MOVEMENT)
public final class TeleportMod extends Module {
   private final EnumOption mode;
   private final Stopwatch timer;
   private CustomVec3 target;
   private int stage;

   public TeleportMod() {
      this.mode = new EnumOption("Mode", TeleportMod.Mode.MINEPLEX);
      this.timer = new Stopwatch();
   }

   public void onEnabled() {
      this.stage = 0;
   }

   @Listener(SendPacketEvent.class)
   public final void onSendPacket(SendPacketEvent event) {
      if (this.stage == 1 && !this.timer.elapsed(6000L)) {
         event.setCancelled();
      }

   }

   @Listener(ReceivePacketEvent.class)
   public final void onReceivePacket(ReceivePacketEvent event) {
      if (this.stage == 1 && !this.timer.elapsed(6000L) && (event.getPacket() instanceof S02PacketChat || event.getPacket() instanceof S45PacketTitle)) {
         event.setCancelled();
      }

   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      int x = mc.objectMouseOver.getBlockPos().getX();
      int y = mc.objectMouseOver.getBlockPos().getY() + 1;
      int z = mc.objectMouseOver.getBlockPos().getZ();
      switch(this.stage) {
      case 0:
         if (Mouse.isButtonDown(1) && !mc.thePlayer.isSneaking() && mc.inGameHasFocus) {
            this.timer.reset();
            this.target = new CustomVec3((double)x, (double)y, (double)z);
            this.killPlayer();
            this.stage = 1;
         }
         break;
      case 1:
         if (this.timer.elapsed(6000L)) {
            mc.getNetHandler().addToSendQueueSilent(new C0CPacketInput(0.0F, 0.0F, true, true));
            Iterator var5 = PathfindingUtils.computePath(new CustomVec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), this.target).iterator();

            while(var5.hasNext()) {
               CustomVec3 vec3 = (CustomVec3)var5.next();
               mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
            }

            Logger.log("Teleported");
            mc.thePlayer.setPosition(this.target.getX(), this.target.getY(), this.target.getZ());
            this.stage = 0;
         }
      }

      if (mc.thePlayer.hurtTime == 9) {
         this.timer.reset();
      }

   }

   @Listener(MoveEvent.class)
   public final void onMove(MoveEvent event) {
      if (this.stage == 1 & !this.timer.elapsed(6000L)) {
         MovementUtils.setSpeed(event, 0.0D);
      }

   }

   private void killPlayer() {
      NetHandlerPlayClient netHandler = mc.getNetHandler();

      for(int i = 0; i < 20; ++i) {
         double offset = 0.060100000351667404D;

         for(int j = 0; (double)j < (double)PlayerUtils.getMaxFallDist() / 0.060100000351667404D + 1.0D; ++j) {
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.060100000351667404D, mc.thePlayer.posZ, false));
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 5.000000237487257E-4D, mc.thePlayer.posZ, false));
         }
      }

      netHandler.addToSendQueueSilent(new C03PacketPlayer(true));
   }

   private static enum Mode {
      MINEPLEX;
   }
}
