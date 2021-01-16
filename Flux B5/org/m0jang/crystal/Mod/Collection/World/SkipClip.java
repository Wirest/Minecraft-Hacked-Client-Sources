package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventBBSet;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;

public class SkipClip extends Module {
   private TimeHelper timer;
   private int resetNext;

   public SkipClip() {
      super("SkipClip", Category.World, false);
   }

   public void onEnable() {
      this.timer = new TimeHelper();
      super.onEnable();
   }

   public void onDisable() {
      Timer.timerSpeed = 1.0F;
      Minecraft.thePlayer.noClip = false;
      super.onDisable();
      this.timer.reset();
   }

   @EventTarget
   public void onTick(EventTick event) {
      this.setRenderName(String.format("%s %s", this.getName()));
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      --this.resetNext;
      double xOff = 0.0D;
      double zOff = 0.0D;
      double multiplier = 2.6D;
      double mx = Math.cos(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
      double mz = Math.sin(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
      xOff = (double)MovementInput.moveForward * 2.6D * mx + (double)MovementInput.moveStrafe * 2.6D * mz;
      zOff = (double)MovementInput.moveForward * 2.6D * mz - (double)MovementInput.moveStrafe * 2.6D * mx;
      if (this.isInsideBlock() && Minecraft.thePlayer.isSneaking()) {
         this.resetNext = 1;
      }

      if (this.resetNext > 0) {
         Minecraft.thePlayer.boundingBox.offsetAndUpdate(xOff, 0.0D, zOff);
      }

   }

   @EventTarget
   private void onSetBB(EventBBSet event) {
      if (this.isInsideBlock() && Minecraft.gameSettings.keyBindJump.pressed || !this.isInsideBlock() && event.boundingBox != null && event.boundingBox.maxY > Minecraft.thePlayer.boundingBox.minY && Minecraft.thePlayer.isSneaking()) {
         event.boundingBox = null;
      }

   }

   private boolean isInsideBlock() {
      for(int x = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1; ++z) {
               Minecraft.getMinecraft();
               Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.theWorld, new BlockPos(x, y, z), Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)));
                  if (block instanceof BlockHopper) {
                     boundingBox = new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
                  }

                  if (boundingBox != null && Minecraft.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }
}
