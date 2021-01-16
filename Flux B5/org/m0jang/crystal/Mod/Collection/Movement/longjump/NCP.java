package org.m0jang.crystal.Mod.Collection.Movement.longjump;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;
import org.m0jang.crystal.Utils.Location;
import org.m0jang.crystal.Utils.MathUtils;

public class NCP extends SubModule {
   private int stage = 1;
   private double moveSpeed = 0.1873D;
   private double lastDist;
   private double boost = 4.5D;
   private Location prevGround;

   public NCP() {
      super("NCP", "LongJump");
   }

   private double getBaseMoveSpeed() {
      return 0.1873D;
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      Timer.timerSpeed = 1.0F;
      this.moveSpeed = this.getBaseMoveSpeed();
      this.lastDist = 0.0D;
      this.stage = 4;
      super.onDisable();
   }

   @EventTarget
   public void onMove(EventMove event) {
      if (MathUtils.roundToPlace(Minecraft.thePlayer.posY - (double)((int)Minecraft.thePlayer.posY), 3) == MathUtils.roundToPlace(0.41D, 3)) {
         Minecraft.thePlayer.motionY = 0.0D;
      }

      if (Minecraft.thePlayer.moveStrafing < 0.0F && Minecraft.thePlayer.moveForward < 0.0F) {
         this.stage = 1;
      }

      if (MathUtils.round(Minecraft.thePlayer.posY - (double)((int)Minecraft.thePlayer.posY), 3) == MathUtils.round(0.943D, 3)) {
         Minecraft.thePlayer.motionY = 0.0D;
      }

      if (this.stage == 1 && (Minecraft.thePlayer.moveForward != 0.0F || Minecraft.thePlayer.moveStrafing != 0.0F) && Minecraft.thePlayer.isCollidedVertically) {
         this.stage = 2;
         this.prevGround = new Location(Minecraft.thePlayer.getPosition());
         this.moveSpeed = this.boost * this.getBaseMoveSpeed() - 0.01D;
      } else if (this.stage == 2) {
         this.stage = 3;
         Minecraft.thePlayer.motionY = 0.424D;
         event.y = 0.424D;
         this.moveSpeed *= 2.149802D;
      } else if (this.stage == 3) {
         this.stage = 4;
         double difference = 0.66D * (this.lastDist - this.getBaseMoveSpeed());
         this.moveSpeed = this.lastDist - difference;
      } else if (this.stage == 4) {
         this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
         if (Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox.offset(0.0D, Minecraft.thePlayer.motionY, 0.0D)).size() > 0 || Minecraft.thePlayer.isCollidedVertically) {
            this.prevGround = null;
            this.stage = 1;
         }

         if (this.prevGround != null && this.prevGround.distanceTo(new Location(Minecraft.thePlayer.getPosition())) > 12.0D) {
            event.x = 0.0D;
            event.z = 0.0D;
            return;
         }

         if (event.y < 0.0D) {
            event.y *= 0.67D;
         }
      }

      this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
      float forward = MovementInput.moveForward;
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
      event.x = (double)forward * this.moveSpeed * mx + (double)strafe * this.moveSpeed * mz;
      event.z = (double)forward * this.moveSpeed * mz - (double)strafe * this.moveSpeed * mx;
      if (forward == 0.0F && strafe == 0.0F) {
         event.x = 0.0D;
         event.z = 0.0D;
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
      double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
      this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
   }
}
