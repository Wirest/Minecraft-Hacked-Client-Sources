package rip.autumn.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import rip.autumn.events.player.MoveEvent;

public final class MovementUtils {
   private static final Minecraft mc = Minecraft.getMinecraft();

   public static void setSpeed(MoveEvent moveEvent, double moveSpeed) {
      setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, (double)mc.thePlayer.movementInput.moveStrafe, (double)mc.thePlayer.movementInput.moveForward);
   }

   public static void setSpeed(MoveEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
      double forward = pseudoForward;
      double strafe = pseudoStrafe;
      float yaw = pseudoYaw;
      if (pseudoForward != 0.0D) {
         if (pseudoStrafe > 0.0D) {
            yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? -45 : 45);
         } else if (pseudoStrafe < 0.0D) {
            yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? 45 : -45);
         }

         strafe = 0.0D;
         if (pseudoForward > 0.0D) {
            forward = 1.0D;
         } else if (pseudoForward < 0.0D) {
            forward = -1.0D;
         }
      }

      if (strafe > 0.0D) {
         strafe = 1.0D;
      } else if (strafe < 0.0D) {
         strafe = -1.0D;
      }

      double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
      moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
   }

   public static double getBaseMoveSpeed() {
      double baseSpeed = 0.2875D;
      if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
         baseSpeed *= 1.0D + 0.2D * (double)(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
      }

      return baseSpeed;
   }

   public static double getJumpBoostModifier(double baseJumpHeight) {
      if (mc.thePlayer.isPotionActive(Potion.jump)) {
         int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
         baseJumpHeight += (double)((float)(amplifier + 1) * 0.1F);
      }

      return baseJumpHeight;
   }
}
