package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.BlockUtils;

public class SmartMoving extends Module {
   public SmartMoving() {
      super("Strafe", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   private void onUpdate(EventMove event) {
      if (!Crystal.INSTANCE.getMods().get(Speed.class).isEnabled()) {
         double moveSpeed = Math.max(getBaseMoveSpeed(), getBaseMoveSpeed());
         MovementInput var10000 = Minecraft.thePlayer.movementInput;
         float forward = MovementInput.moveForward;
         var10000 = Minecraft.thePlayer.movementInput;
         float strafe = MovementInput.moveStrafe;
         float yaw = Minecraft.thePlayer.rotationYaw;
         if (forward == 0.0F && strafe == 0.0F) {
            event.x = 0.0D;
            event.z = 0.0D;
         } else if (forward != 0.0F) {
            if (strafe >= 1.0F) {
               yaw += (float)(forward > 0.0F ? -45 : 45);
               strafe = 0.0F;
            } else if (strafe <= -1.0F) {
               yaw += (float)(forward > 0.0F ? 45 : -45);
               strafe = 0.0F;
            }

            if (forward > 0.0F) {
               forward = 1.0F;
            } else if (forward < 0.0F) {
               forward = -1.0F;
            }
         }

         double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
         double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         event.x = (double)forward * moveSpeed * mx + (double)strafe * moveSpeed * mz;
         event.z = (double)forward * moveSpeed * mz - (double)strafe * moveSpeed * mx;
         Minecraft.thePlayer.stepHeight = 0.6F;
         if (forward == 0.0F && strafe == 0.0F) {
            event.x = 0.0D;
            event.z = 0.0D;
         }

      }
   }

   public static void SmartMove(EventMove event, double speed) {
      double moveSpeed = Math.max(getBaseMoveSpeed(), speed);
      MovementInput var10000 = Minecraft.thePlayer.movementInput;
      float forward = MovementInput.moveForward;
      var10000 = Minecraft.thePlayer.movementInput;
      float strafe = MovementInput.moveStrafe;
      float yaw = Minecraft.thePlayer.rotationYaw;
      if (forward == 0.0F && strafe == 0.0F) {
         event.x = 0.0D;
         event.z = 0.0D;
      } else if (forward != 0.0F) {
         if (strafe >= 1.0F) {
            yaw += (float)(forward > 0.0F ? -45 : 45);
            strafe = 0.0F;
         } else if (strafe <= -1.0F) {
            yaw += (float)(forward > 0.0F ? 45 : -45);
            strafe = 0.0F;
         }

         if (forward > 0.0F) {
            forward = 1.0F;
         } else if (forward < 0.0F) {
            forward = -1.0F;
         }
      }

      double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      event.x = (double)forward * moveSpeed * mx + (double)strafe * moveSpeed * mz;
      event.z = (double)forward * moveSpeed * mz - (double)strafe * moveSpeed * mx;
      Minecraft.thePlayer.stepHeight = 0.6F;
      if (forward == 0.0F && strafe == 0.0F) {
         event.x = 0.0D;
         event.z = 0.0D;
      }

   }

   public static double getBaseMoveSpeed() {
      double baseSpeed;
      if (BlockUtils.isInLiquid()) {
         baseSpeed = 0.1D;
      } else {
         baseSpeed = 0.2873D;
      }

      if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
      }

      return baseSpeed;
   }
}
