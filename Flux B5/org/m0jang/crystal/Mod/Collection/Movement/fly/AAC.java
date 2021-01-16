package org.m0jang.crystal.Mod.Collection.Movement.fly;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventPacketSend;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;

public class AAC extends SubModule {
   private double startY;
   private boolean canFly;

   public AAC() {
      super("AAC", "Fly");
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (Minecraft.thePlayer.fallDistance >= 4.0F && !this.canFly) {
         this.startY = Minecraft.thePlayer.posY;
         Minecraft.thePlayer.motionY = 0.1D;
         this.canFly = true;
      }

      if (Minecraft.thePlayer.onGround || Minecraft.thePlayer.isInWater()) {
         this.canFly = false;
      }

      if (this.canFly) {
         this.setSpeed(0.25F);
         if (Minecraft.thePlayer.posY <= this.startY) {
            Minecraft.thePlayer.motionY = 0.8D;
         }
      }

   }

   @EventTarget
   public void onPackedSend(EventPacketSend event) {
      if (Minecraft.thePlayer.fallDistance > 4.0F && event.packet instanceof C03PacketPlayer) {
         C03PacketPlayer packet = (C03PacketPlayer)event.packet;
         packet.onGround = true;
         Minecraft.thePlayer.fallDistance = 0.0F;
         event.packet = packet;
      }

   }

   public void setSpeed(float speed) {
      double yaw = (double)Minecraft.thePlayer.rotationYaw;
      boolean isMoving = Minecraft.thePlayer.moveForward != 0.0F || Minecraft.thePlayer.moveStrafing != 0.0F;
      boolean isMovingForward = Minecraft.thePlayer.moveForward > 0.0F;
      boolean isMovingBackward = Minecraft.thePlayer.moveForward < 0.0F;
      boolean isMovingRight = Minecraft.thePlayer.moveStrafing > 0.0F;
      boolean isMovingLeft = Minecraft.thePlayer.moveStrafing < 0.0F;
      boolean isMovingSideways = isMovingLeft || isMovingRight;
      boolean isMovingStraight = isMovingForward || isMovingBackward;
      if (isMoving) {
         if (isMovingForward && !isMovingSideways) {
            yaw += 0.0D;
         } else if (isMovingBackward && !isMovingSideways) {
            yaw += 180.0D;
         } else if (isMovingForward && isMovingLeft) {
            yaw += 45.0D;
         } else if (isMovingForward) {
            yaw -= 45.0D;
         } else if (!isMovingStraight && isMovingLeft) {
            yaw += 90.0D;
         } else if (!isMovingStraight && isMovingRight) {
            yaw -= 90.0D;
         } else if (isMovingBackward && isMovingLeft) {
            yaw += 135.0D;
         } else if (isMovingBackward) {
            yaw -= 135.0D;
         }

         yaw = Math.toRadians(yaw);
         Minecraft.thePlayer.motionX = -Math.sin(yaw) * (double)speed;
         Minecraft.thePlayer.motionZ = Math.cos(yaw) * (double)speed;
      }

   }
}
