package rip.autumn.module.impl.player;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import rip.autumn.annotations.Label;
import rip.autumn.events.game.TickEvent;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.events.player.BlockRenderEvent;
import rip.autumn.events.player.BoundingBoxEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.events.player.PushEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Bind;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.MovementUtils;
import rip.autumn.utils.PlayerUtils;

@Label("Phase")
@Bind("X")
@Category(ModuleCategory.PLAYER)
public final class PhaseMod extends Module {
   private final EnumOption mode;
   private int moveUnder;

   public PhaseMod() {
      this.mode = new EnumOption("Mode", PhaseMod.Mode.NORMAL);
      this.setMode(this.mode);
      this.addOptions(new Option[]{this.mode});
   }

   public void onEnabled() {
   }

   @Listener(TickEvent.class)
   public final void onTick(TickEvent event) {
      if (mc.thePlayer != null && this.moveUnder == 1) {
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0D, mc.thePlayer.posZ, false));
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
         this.moveUnder = 0;
      }

      if (mc.thePlayer != null && this.moveUnder == 1488) {
         double mx = -Math.sin(Math.toRadians((double)mc.thePlayer.rotationYaw));
         double mz = Math.cos(Math.toRadians((double)mc.thePlayer.rotationYaw));
         double x = (double)mc.thePlayer.movementInput.moveForward * mx + (double)mc.thePlayer.movementInput.moveStrafe * mz;
         double z = (double)mc.thePlayer.movementInput.moveForward * mz - (double)mc.thePlayer.movementInput.moveStrafe * mx;
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.posY, Double.NEGATIVE_INFINITY, true));
         this.moveUnder = 0;
      }

   }

   @Listener(BoundingBoxEvent.class)
   public final void onBoundingBox(BoundingBoxEvent event) {
      switch((PhaseMod.Mode)this.mode.getValue()) {
      case NORMAL:
         if (PlayerUtils.isInsideBlock()) {
            event.setBoundingBox((AxisAlignedBB)null);
         }
         break;
      case VANILLA:
         if (mc.thePlayer.isCollidedHorizontally && !PlayerUtils.isInsideBlock()) {
            double mx = -Math.sin(Math.toRadians((double)mc.thePlayer.rotationYaw));
            double mz = Math.cos(Math.toRadians((double)mc.thePlayer.rotationYaw));
            double x = (double)mc.thePlayer.movementInput.moveForward * mx + (double)mc.thePlayer.movementInput.moveStrafe * mz;
            double z = (double)mc.thePlayer.movementInput.moveForward * mz - (double)mc.thePlayer.movementInput.moveStrafe * mx;
            event.setBoundingBox((AxisAlignedBB)null);
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
            this.moveUnder = 69;
         }

         if (PlayerUtils.isInsideBlock()) {
            event.setBoundingBox((AxisAlignedBB)null);
         }
      }

   }

   @Listener(PushEvent.class)
   public final void onPush(PushEvent event) {
      event.setCancelled();
   }

   @Listener(BlockRenderEvent.class)
   public final void onBlockRender(BlockRenderEvent event) {
      event.setCancelled();
   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      switch((PhaseMod.Mode)this.mode.getValue()) {
      case NORMAL:
         if (!event.isPre()) {
            double multiplier = 0.3D;
            double mx = -Math.sin(Math.toRadians((double)mc.thePlayer.rotationYaw));
            double mz = Math.cos(Math.toRadians((double)mc.thePlayer.rotationYaw));
            double x = (double)mc.thePlayer.movementInput.moveForward * multiplier * mx + (double)mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
            double z = (double)mc.thePlayer.movementInput.moveForward * multiplier * mz - (double)mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
            if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
               mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + 0.001D, mc.thePlayer.posZ + z, false));

               for(int i = 1; i < 10; ++i) {
                  mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.22D, mc.thePlayer.posZ, false));
               }

               mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
            }
         }
         break;
      case VANILLA:
         if (this.mode.getValue() == PhaseMod.Mode.VANILLA && mc.gameSettings.keyBindSneak.isPressed() && !PlayerUtils.isInsideBlock()) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0D, mc.thePlayer.posZ, true));
            this.moveUnder = 2;
         }
      }

   }

   @Listener(ReceivePacketEvent.class)
   public final void onReceive(ReceivePacketEvent event) {
      if (event.getPacket() instanceof S02PacketChat) {
         S02PacketChat packet = (S02PacketChat)event.getPacket();
         if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
            event.setCancelled();
         }
      }

      if (this.mode.getValue() == PhaseMod.Mode.VANILLA && event.getPacket() instanceof S08PacketPlayerPosLook && this.moveUnder == 2) {
         this.moveUnder = 1;
      }

      if (this.mode.getValue() == PhaseMod.Mode.VANILLA && event.getPacket() instanceof S08PacketPlayerPosLook && this.moveUnder == 69) {
         this.moveUnder = 1488;
      }

   }

   @Listener(MoveEvent.class)
   public final void onMove(MoveEvent event) {
      switch((PhaseMod.Mode)this.mode.getValue()) {
      case NORMAL:
         if (PlayerUtils.isInsideBlock()) {
            EntityPlayerSP var10001;
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
               var10001 = mc.thePlayer;
               event.y = var10001.motionY += 0.09000000357627869D;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
               var10001 = mc.thePlayer;
               event.y = var10001.motionY -= 0.0D;
            } else {
               event.y = mc.thePlayer.motionY = 0.0D;
            }

            MovementUtils.setSpeed(event, 0.3D);
            if (mc.thePlayer.ticksExisted % 2 == 0) {
               var10001 = mc.thePlayer;
               event.y = var10001.motionY += 0.09000000357627869D;
            }
         }
         break;
      case VANILLA:
         if (PlayerUtils.isInsideBlock()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
               event.y = mc.thePlayer.motionY = 0.5D;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
               event.y = mc.thePlayer.motionY = -0.5D;
            } else {
               event.y = mc.thePlayer.motionY = 0.0D;
            }

            MovementUtils.setSpeed(event, 0.28D);
         }
      }

   }

   private static enum Mode {
      NORMAL,
      VANILLA;
   }
}
