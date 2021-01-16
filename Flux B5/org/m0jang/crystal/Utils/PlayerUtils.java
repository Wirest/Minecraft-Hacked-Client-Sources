package org.m0jang.crystal.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class PlayerUtils {
   public static boolean isMoving() {
      return Minecraft.thePlayer.lastTickPosX != Minecraft.thePlayer.posX && Minecraft.thePlayer.lastTickPosZ != Minecraft.thePlayer.posZ || Minecraft.thePlayer.motionX != 0.0D || Minecraft.thePlayer.motionY != 0.0D || Minecraft.thePlayer.motionZ != 0.0D;
   }

   public static boolean isInventoryFull() {
      for(int index = 9; index <= 44; ++index) {
         ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack == null) {
            return false;
         }
      }

      return true;
   }

   public static double getDistanceToFall() {
      double distance = 0.0D;

      double i;
      for(i = Minecraft.thePlayer.posY; i > 0.0D; --i) {
         Block block = BlockUtils.getBlock(new BlockPos(Minecraft.thePlayer.posX, i, Minecraft.thePlayer.posZ));
         if (block.getMaterial() != Material.air && block.isSolidFullCube() && block.isCollidable()) {
            distance = i;
            break;
         }

         if (i < 0.0D) {
            break;
         }
      }

      i = Minecraft.thePlayer.posY - distance - 1.0D;
      return i;
   }

   public static boolean MovementInput() {
      return Minecraft.gameSettings.keyBindForward.getIsKeyPressed() || Minecraft.gameSettings.keyBindBack.getIsKeyPressed() || Minecraft.gameSettings.keyBindLeft.getIsKeyPressed() || Minecraft.gameSettings.keyBindRight.getIsKeyPressed() || Minecraft.gameSettings.keyBindSneak.getIsKeyPressed();
   }

   public static float[] aimAtLocation(double x, double y, double z, EnumFacing facing) {
      EntitySnowball temp = new EntitySnowball(Minecraft.theWorld);
      temp.posX = x + 0.5D;
      temp.posY = y + 0.5D;
      temp.posZ = z + 0.5D;
      temp.posX += (double)facing.getDirectionVec().getX() * 0.25D;
      temp.posY += (double)facing.getDirectionVec().getY() * 0.25D;
      temp.posZ += (double)facing.getDirectionVec().getZ() * 0.25D;
      return aimAtLocation(temp.posX, temp.posY, temp.posZ);
   }

   public static float[] aimAtLocation(double positionX, double positionY, double positionZ) {
      double x = positionX - Minecraft.thePlayer.posX;
      double y = positionY - Minecraft.thePlayer.posY;
      double z = positionZ - Minecraft.thePlayer.posZ;
      double distance = (double)MathHelper.sqrt_double(x * x + z * z);
      return new float[]{(float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F, (float)(-(Math.atan2(y, distance) * 180.0D / 3.141592653589793D))};
   }
}
