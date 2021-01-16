package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventBBSet;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.BlockUtils;
import org.m0jang.crystal.Utils.EntityUtils;
import org.m0jang.crystal.Utils.TimeHelper;
import org.m0jang.crystal.Values.Value;

public class Phase extends Module {
   public static Value Mode = new Value("Phase", String.class, "Mode", "Latest", new String[]{"Latest", "Vanilla", "Spider", "New"});
   private TimeHelper timer;
   private TimeHelper timerr;
   private int resetNext;

   public Phase() {
      super("Phase", Category.World, Mode);
   }

   public void onEnable() {
      this.timer = new TimeHelper();
      this.timerr = new TimeHelper();
      super.onEnable();
   }

   public void onDisable() {
      Timer.timerSpeed = 1.0F;
      Minecraft.thePlayer.noClip = false;
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      Timer.timerSpeed = 1.0F;
      double mz2;
      double x;
      double z;
      double mx2;
      if (!Mode.getSelectedOption().equalsIgnoreCase("Vanilla") && !Mode.getSelectedOption().equalsIgnoreCase("Spider")) {
         if (Mode.getSelectedOption().equalsIgnoreCase("New") || Mode.getSelectedOption().equalsIgnoreCase("Latest")) {
            mx2 = Math.cos(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
            mz2 = Math.sin(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
            x = (double)MovementInput.moveForward * 0.3D * mx2 + (double)MovementInput.moveStrafe * 0.3D * mz2;
            z = (double)MovementInput.moveForward * 0.3D * mz2 - (double)MovementInput.moveStrafe * 0.3D * mx2;
            if (event.state == EventState.POST && Minecraft.thePlayer.isCollidedHorizontally && !Minecraft.thePlayer.isOnLadder() && !BlockUtils.isInsideBlock() && this.timer.hasPassed(150.0D)) {
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + x, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + z, true));
               if (!Mode.getSelectedOption().equalsIgnoreCase("New")) {
                  if (Mode.getSelectedOption().equalsIgnoreCase("Latest")) {
                     EntityUtils.blinkToPos(new double[]{Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ}, new BlockPos(Minecraft.thePlayer.posX + x, Double.MIN_VALUE, Minecraft.thePlayer.posZ + z), 0.0D, new double[]{0.0D, 0.0D});
                  }
               } else {
                  for(int i = 0; i < 1; ++i) {
                     Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + x, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + z, true));
                  }

                  Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX + x, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + z);
               }

               this.timer.reset();
            }
         }
      } else if (event.state == EventState.PRE) {
         if (Mode.getSelectedOption().equalsIgnoreCase("Spider")) {
            if (this.timer.hasPassed(150.0D) && Minecraft.thePlayer.isCollidedHorizontally) {
               float yaw = Minecraft.thePlayer.rotationYaw;
               if (Minecraft.thePlayer.moveForward < 0.0F) {
                  yaw += 180.0F;
               }

               if (Minecraft.thePlayer.moveStrafing > 0.0F) {
                  yaw -= 90.0F * (Minecraft.thePlayer.moveForward < 0.0F ? -0.5F : (Minecraft.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
               }

               if (Minecraft.thePlayer.moveStrafing < 0.0F) {
                  yaw += 90.0F * (Minecraft.thePlayer.moveForward < 0.0F ? -0.5F : (Minecraft.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
               }

               double horizontalMultiplier = 0.3D;
               double xOffset = (double)((float)Math.cos((double)(yaw + 90.0F) * 3.141592653589793D / 180.0D)) * 0.3D;
               double zOffset = (double)((float)Math.sin((double)(yaw + 90.0F) * 3.141592653589793D / 180.0D)) * 0.3D;
               double yOffset = 0.0D;

               for(int i = 0; i < 3; ++i) {
                  yOffset += 0.01D;
                  this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - yOffset, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
                  this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + xOffset * (double)i, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + zOffset * (double)i, Minecraft.thePlayer.onGround));
               }
            }
         } else if (Mode.getSelectedOption().equalsIgnoreCase("Vanilla")) {
            mx2 = Math.cos(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
            mz2 = Math.sin(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
            x = (double)MovementInput.moveForward * 2.6D * mx2 + (double)MovementInput.moveStrafe * 2.6D * mz2;
            z = (double)MovementInput.moveForward * 2.6D * mz2 - (double)MovementInput.moveStrafe * 2.6D * mx2;
            if (Minecraft.thePlayer.isCollidedHorizontally && !Minecraft.thePlayer.isOnLadder() && BlockUtils.isInsideBlock() && this.timer.hasPassed(150.0D) && Minecraft.thePlayer.isSneaking()) {
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + x, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + z, false));
               EntityUtils.blinkToPos(new double[]{Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ}, new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ), 1.0D, new double[]{x, z});
               this.timer.reset();
            }
         } else if (!Minecraft.thePlayer.isCollidedHorizontally) {
            this.timer.reset();
         }
      }

   }

   @EventTarget
   private void onSetBB(EventBBSet event) {
      if (BlockUtils.isInsideBlock() && Minecraft.gameSettings.keyBindJump.pressed || !BlockUtils.isInsideBlock() && event.boundingBox != null && event.boundingBox.maxY > Minecraft.thePlayer.boundingBox.minY && Minecraft.thePlayer.isSneaking()) {
         event.boundingBox = null;
      }

   }

   @EventTarget
   public void onBBSet(EventBBSet event) {
      if (!Mode.getSelectedOption().equalsIgnoreCase("Vanilla") && !Mode.getSelectedOption().equalsIgnoreCase("New") && !Mode.getSelectedOption().equalsIgnoreCase("Latest") || this.timer.hasPassed(150.0D)) {
         Minecraft.thePlayer.noClip = true;
         if ((double)event.pos.getY() > Minecraft.thePlayer.posY + (double)(BlockUtils.isInsideBlock() ? 0 : 1)) {
            event.boundingBox = null;
         }

         if (Minecraft.thePlayer.isCollidedHorizontally && (double)event.pos.getY() > Minecraft.thePlayer.boundingBox.minY - 0.4D) {
            event.boundingBox = null;
         }

         if (BlockUtils.isInsideBlock() && Minecraft.gameSettings.keyBindJump.pressed || !BlockUtils.isInsideBlock() && event.boundingBox != null && event.boundingBox.maxY > Minecraft.thePlayer.boundingBox.minY && Minecraft.thePlayer.isSneaking()) {
            event.boundingBox = null;
         }

      }
   }
}
