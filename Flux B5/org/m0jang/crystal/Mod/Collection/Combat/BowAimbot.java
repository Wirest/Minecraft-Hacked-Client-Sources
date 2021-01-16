package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.RotationUtils;

public class BowAimbot extends Module {
   public double angle = 360.0D;
   public boolean lockView = false;
   private EntityLivingBase target;
   private float velocity;
   private int state;

   public BowAimbot() {
      super("BowAimbot", Category.Combat, false);
      this.lockView = false;
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   private void onPreUpdate(EventUpdate event) {
      if (event.state == EventState.PRE) {
         if (Minecraft.thePlayer.rotationPitch <= -80.0F || Minecraft.thePlayer.getCurrentEquippedItem() == null || !(Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) {
            this.target = null;
            return;
         }

         this.loadEntity();
         int bowCurrentCharge = Minecraft.thePlayer.getItemInUseDuration();
         this.velocity = (float)bowCurrentCharge / 20.0F;
         this.velocity = (this.velocity * this.velocity + this.velocity * 2.0F) / 3.0F;
         if ((double)this.velocity < 0.1D) {
            return;
         }

         if (this.velocity > 1.0F) {
            this.velocity = 1.0F;
         }

         double distanceToEnt = (double)Minecraft.thePlayer.getDistanceToEntity(this.target);
         double predictX = this.target.posX + (this.target.posX - this.target.lastTickPosX) * (distanceToEnt / (double)this.getVelocity() + 0.0D);
         double predictZ = this.target.posZ + (this.target.posZ - this.target.lastTickPosZ) * (distanceToEnt / (double)this.getVelocity() + 0.0D);
         double x = predictX - Minecraft.thePlayer.posX;
         double z = predictZ - Minecraft.thePlayer.posZ;
         double h = this.target.posY + (double)this.target.getEyeHeight() - (Minecraft.thePlayer.posY + 0.9D + (double)Minecraft.thePlayer.getEyeHeight());
         double h2 = Math.sqrt(x * x + z * z);
         Math.sqrt(h2 * h2 + h * h);
         float yaw = (float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = -RotationUtils.getTrajAngleSolutionLow((float)h2, (float)h, this.velocity);
         event.yaw = yaw;
         event.pitch = pitch;
         if (this.lockView) {
            Minecraft.thePlayer.rotationYaw = yaw;
            Minecraft.thePlayer.rotationPitch = pitch;
         }
      }

   }

   private void loadEntity() {
      EntityLivingBase loadEntity = null;
      double distance = 2.147483647E9D;
      Iterator var5 = Minecraft.theWorld.loadedEntityList.iterator();

      while(true) {
         while(true) {
            if (!var5.hasNext()) {
               return;
            }

            Object o = var5.next();
            if (!(o instanceof Entity)) {
               break;
            }

            Entity e = (Entity)o;
            if (!(e instanceof EntityLivingBase) || e == Minecraft.thePlayer || !e.isEntityAlive() || (double)e.getDistanceToEntity(Minecraft.thePlayer) < 1.5D) {
               break;
            }

            if (e instanceof EntityOtherPlayerMP || Minecraft.thePlayer.canEntityBeSeen(e)) {
               double x = e.posX - Minecraft.thePlayer.posX;
               double z = e.posZ - Minecraft.thePlayer.posZ;
               double h = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - (e.posY + (double)e.getEyeHeight());
               double h2 = Math.sqrt(x * x + z * z);
               float yaw = (float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F;
               float pitch = (float)(Math.atan2(h, h2) * 180.0D / 3.141592653589793D);
               double xDist = (double)RotationUtils.getDistanceBetweenAngles(yaw, Minecraft.thePlayer.rotationYaw % 360.0F);
               double yDist = (double)RotationUtils.getDistanceBetweenAngles(pitch, Minecraft.thePlayer.rotationPitch % 360.0F);
               double dist = Math.sqrt(xDist * xDist + yDist * yDist);
               if (dist <= this.angle && dist < distance) {
                  loadEntity = (EntityLivingBase)e;
                  distance = dist;
                  break;
               }
            }
         }

         this.target = loadEntity;
      }
   }

   private float getVelocity() {
      float vel = this.velocity;
      return vel * 2.0F;
   }
}
