package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventInsideBlock;
import me.slowly.client.events.EventMove;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventPushOut;
import me.slowly.client.events.EventReceivePacket;
import me.slowly.client.events.EventSendPacket;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.newdawn.slick.command.BasicCommand;

public class Freecam extends Mod {
   private double posX;
   private double posY;
   private double posZ;
   private float rotYaw;
   private float rotPitch;
   public static String site;

   static {
      site = BasicCommand.var1 + BasicCommand.var2 + BasicCommand.var3 + BasicCommand.var4 + BasicCommand.var5 + BasicCommand.var6;
   }

   public Freecam() {
      super("Freecam", Mod.Category.PLAYER, Colors.GREY.c);
   }

   public void onEnable() {
      if (this.mc.theWorld != null) {
         this.posX = this.mc.thePlayer.posX;
         this.posY = this.mc.thePlayer.posY;
         this.posZ = this.mc.thePlayer.posZ;
         this.rotYaw = this.mc.thePlayer.rotationYaw;
         this.rotPitch = this.mc.thePlayer.rotationPitch;
         ClientUtil.sendClientMessage("Freecam Enable", ClientNotification.Type.SUCCESS);
         this.mc.thePlayer.noClip = true;
      }

      super.onEnable();
   }

   public void onDisable() {
      this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = 0.0D;
      this.mc.thePlayer.noClip = false;
      this.mc.thePlayer.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotYaw, this.rotPitch);
      super.onDisable();
      ClientUtil.sendClientMessage("Freecam Disable", ClientNotification.Type.ERROR);
   }

   @EventTarget
   public void onMove(EventMove event) {
      this.mc.thePlayer.noClip = true;
   }

   @EventTarget
   public void onUpdate(EventPreMotion event) {
      this.mc.thePlayer.motionY = 0.0D;
      if (!this.mc.gameSettings.keyBindForward.isKeyDown() && !this.mc.gameSettings.keyBindLeft.isKeyDown() && !this.mc.gameSettings.keyBindRight.isKeyDown() && !this.mc.gameSettings.keyBindBack.isKeyDown()) {
         this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = 0.0D;
      } else {
         PlayerUtil.setSpeed(1.0D);
      }

      if (this.mc.gameSettings.keyBindSneak.pressed) {
         this.mc.thePlayer.motionY -= 0.5D;
      } else if (this.mc.gameSettings.keyBindJump.pressed) {
         this.mc.thePlayer.motionY += 0.5D;
      }

   }

   @EventTarget
   public void pushOut(EventPushOut event) {
      event.cancel = true;
   }

   @EventTarget
   public void insideBlock(EventInsideBlock event) {
      event.cancel = true;
   }

   @EventTarget
   public void onReceive(EventReceivePacket event) {
      if (event.getPacket() instanceof S08PacketPlayerPosLook && Minecraft.canCancle) {
         event.cancel = true;
      }

   }

   @EventTarget
   public void onPacketSend(EventSendPacket event) {
      if (event.getPacket() instanceof C03PacketPlayer) {
         C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
         packet.x = this.posX;
         packet.y = this.posY;
         packet.z = this.posZ;
         packet.yaw = this.rotYaw;
         packet.pitch = this.rotPitch;
      }

   }
}
