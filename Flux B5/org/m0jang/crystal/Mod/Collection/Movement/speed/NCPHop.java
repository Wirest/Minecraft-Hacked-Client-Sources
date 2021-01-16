package org.m0jang.crystal.Mod.Collection.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;
import org.m0jang.crystal.Utils.BlockUtils;
import org.m0jang.crystal.Utils.MathUtils;

public class NCPHop extends SubModule {
   private double lastDist;
   private int stage;
   private double moveSpeed;
   private final double boostSpeed = 2.0D;

   public NCPHop() {
      super("NCPHop", "Speed");
   }

   public void onEnable() {
      super.onEnable();
      Minecraft.thePlayer.stepHeight = 0.0F;
   }

   public void onDisable() {
      super.onDisable();
      this.moveSpeed = this.getBaseMoveSpeed();
      Timer.timerSpeed = 1.0F;
      Minecraft.thePlayer.motionX = 0.0D;
      Minecraft.thePlayer.motionZ = 0.0D;
      Minecraft.thePlayer.stepHeight = 0.6F;
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      double movementInput = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
      double strafe = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
      this.lastDist = Math.sqrt(movementInput * movementInput + strafe * strafe);
   }

   @EventTarget
   public void onMove(EventMove event) {
      if (!BlockUtils.isInLiquid() && !Minecraft.thePlayer.isInWater()) {
         if (Minecraft.thePlayer.capabilities.isFlying) {
            return;
         }

         Timer.timerSpeed = 1.0888F;
         if (Minecraft.thePlayer.onGround) {
            this.stage = 2;
            Timer.timerSpeed = 1.0F;
            Minecraft.thePlayer.motionX *= 1.3085D;
            Minecraft.thePlayer.motionZ *= 1.3085D;
         }

         if (MathUtils.round(Minecraft.thePlayer.posY - (double)((int)Minecraft.thePlayer.posY), 3) == MathUtils.round(0.138D, 3)) {
            --Minecraft.thePlayer.motionY;
            event.setY(event.getY() - 0.0931D);
            Minecraft.thePlayer.posY -= 0.0931D;
            Minecraft.thePlayer.motionX *= 1.4085D;
            Minecraft.thePlayer.motionZ *= 1.4085D;
         }

         if (this.stage != 2 || Minecraft.thePlayer.moveForward == 0.0F && Minecraft.thePlayer.moveStrafing == 0.0F) {
            if (this.stage == 3) {
               double forward = 0.66D * (this.lastDist - this.getBaseMoveSpeed());
               this.moveSpeed = this.lastDist - forward;
            } else {
               if (Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox.offset(0.0D, Minecraft.thePlayer.motionY, 0.0D)).size() > 0 || Minecraft.thePlayer.isCollidedVertically) {
                  Timer.timerSpeed = 1.0F;
                  Minecraft.thePlayer.motionX *= 1.4085D;
                  Minecraft.thePlayer.motionZ *= 1.4085D;
                  this.stage = 1;
               }

               this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
            }
         } else {
            event.setY(Minecraft.thePlayer.motionY = 0.39936D);
            Timer.timerSpeed = 1.0F;
            Minecraft.thePlayer.motionX *= 1.4085D;
            Minecraft.thePlayer.motionZ *= 1.4085D;
            this.moveSpeed *= 2.0D;
         }

         this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
         float forward1 = MovementInput.moveForward;
         float strafe = MovementInput.moveStrafe;
         float yaw = Minecraft.thePlayer.rotationYaw;
         if (forward1 == 0.0F && strafe == 0.0F) {
            event.setX(0.0D);
            event.setZ(0.0D);
         } else if (forward1 != 0.0F) {
            if (strafe >= 1.0F) {
               yaw += (float)(forward1 > 0.0F ? -45 : 45);
               strafe = 0.0F;
            } else if (strafe <= -1.0F) {
               yaw += (float)(forward1 > 0.0F ? 45 : -45);
               strafe = 0.0F;
            }

            if (forward1 > 0.0F) {
               forward1 = 1.0F;
            } else if (forward1 < 0.0F) {
               forward1 = -1.0F;
            }
         }

         double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
         double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         event.setX((double)forward1 * this.moveSpeed * mx + (double)strafe * this.moveSpeed * mz);
         event.setZ((double)forward1 * this.moveSpeed * mz - (double)strafe * this.moveSpeed * mx);
         if (forward1 == 0.0F && strafe == 0.0F) {
            event.setX(0.0D);
            event.setZ(0.0D);
         }

         ++this.stage;
         Minecraft.thePlayer.motionX *= 1.1085D;
         Minecraft.thePlayer.motionZ *= 1.1085D;
      }

   }

   private double getBaseMoveSpeed() {
      double baseSpeed = 0.2872D;
      if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
      }

      return baseSpeed;
   }
}
