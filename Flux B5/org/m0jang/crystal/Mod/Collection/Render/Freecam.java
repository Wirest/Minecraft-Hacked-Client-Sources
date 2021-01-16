package org.m0jang.crystal.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MovementInput;
import org.m0jang.crystal.Events.EventBBSet;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Events.EventPacketSend;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class Freecam extends Module {
   private double oldX;
   private double oldY;
   private double oldZ;
   private float oldYaw;
   private float oldPitch;
   private EntityOtherPlayerMP player;

   public Freecam() {
      super("Freecam", Category.Render, false);
   }

   public void onEnable() {
      if (Minecraft.theWorld == null) {
         this.setEnabled(false);
      } else {
         Minecraft.thePlayer.noClip = true;
         this.oldX = Minecraft.thePlayer.posX;
         this.oldY = Minecraft.thePlayer.posY;
         this.oldZ = Minecraft.thePlayer.posZ;
         this.oldYaw = Minecraft.thePlayer.rotationYaw;
         this.oldPitch = Minecraft.thePlayer.rotationPitch;
         (this.player = new EntityOtherPlayerMP(Minecraft.theWorld, Minecraft.thePlayer.getGameProfile())).clonePlayer(Minecraft.thePlayer, true);
         this.player.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
         this.player.rotationYawHead = Minecraft.thePlayer.rotationYaw;
         this.player.rotationPitch = Minecraft.thePlayer.rotationPitch;
         this.player.setSneaking(Minecraft.thePlayer.isSneaking());
         Minecraft.theWorld.addEntityToWorld(-1337, this.player);
         super.onEnable();
      }
   }

   public void onDisable() {
      Minecraft.thePlayer.noClip = false;
      Minecraft.thePlayer.capabilities.isFlying = false;
      Minecraft.thePlayer.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch);
      Minecraft.theWorld.removeEntity(this.player);
      super.onDisable();
   }

   @EventTarget
   public void onBBSet(EventBBSet event) {
      event.boundingBox = null;
   }

   @EventTarget
   public void onPacketSend(EventPacketSend event) {
      if (event.packet instanceof C03PacketPlayer || event.packet instanceof C0BPacketEntityAction || event.packet instanceof C0APacketAnimation || event.packet instanceof C02PacketUseEntity || event.packet instanceof C09PacketHeldItemChange || event.packet instanceof C07PacketPlayerDigging) {
         event.setCancelled(true);
      }

   }

   @EventTarget
   public void onUpdate(EventMove event) {
      Minecraft.thePlayer.noClip = true;
      event.x = 0.0D;
      event.y = 0.0D;
      event.z = 0.0D;
      Minecraft.thePlayer.jumpMovementFactor *= 5.0F;
      if (Minecraft.gameSettings.keyBindSneak.pressed) {
         event.y = -0.3D;
      } else if (Minecraft.gameSettings.keyBindJump.pressed) {
         event.y = 0.3D;
      }

      double mx2 = Math.cos(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
      double mz2 = Math.sin(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
      double x = (double)MovementInput.moveForward * 0.3D * mx2 + (double)MovementInput.moveStrafe * 0.3D * mz2;
      double z = (double)MovementInput.moveForward * 0.3D * mz2 - (double)MovementInput.moveStrafe * 0.3D * mx2;
      event.setX(x);
      event.setZ(z);
   }
}
