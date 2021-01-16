package org.m0jang.crystal.Utils;

import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockUtils {
   private EntityOtherPlayerMP player;

   public static String getBlockName(Block block) {
      if (block == Blocks.air) {
         return null;
      } else {
         Item item = Item.getItemFromBlock(block);
         ItemStack itemStack = item != null ? new ItemStack(Item.getByNameOrId(block.getUnlocalizedName()), 1, 0) : null;
         String name = itemStack == null ? block.getLocalizedName() : item.getItemStackDisplayName(itemStack);
         return name.length() > 5 && name.substring(0, 5).equals("tile.") ? block.getUnlocalizedName() : name;
      }
   }

   public static boolean isOnLiquid() {
      Minecraft.getMinecraft();
      AxisAlignedBB par1AxisAlignedBB = Minecraft.thePlayer.boundingBox.offset(0.0D, -0.01D, 0.0D).contract(0.001D, 0.001D, 0.001D);
      int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
      int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
      int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
      int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
      int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
      int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

      for(int var11 = var4; var11 < var5; ++var11) {
         for(int var12 = var6; var12 < var7; ++var12) {
            for(int var13 = var8; var13 < var9; ++var13) {
               BlockPos pos = new BlockPos(var11, var12, var13);
               Minecraft.getMinecraft();
               Block var14 = Minecraft.theWorld.getBlockState(pos).getBlock();
               if (!(var14 instanceof BlockAir) && !(var14 instanceof BlockLiquid)) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public static Block getBlock(double x, double y, double z) {
      Minecraft.getMinecraft();
      return Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
   }

   public static Block getBlock(BlockPos block) {
      Minecraft.getMinecraft();
      return Minecraft.theWorld.getBlockState(block).getBlock();
   }

   public static Block getBlock(Entity entity, double offsetY) {
      if (entity == null) {
         return null;
      } else {
         int y = (int)entity.getEntityBoundingBox().offset(0.0D, offsetY, 0.0D).minY;

         for(int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper.floor_double(entity.getEntityBoundingBox().maxX) + 1; ++x) {
            int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ);
            if (z < MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1) {
               return Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            }
         }

         return null;
      }
   }

   public static boolean isInLiquid() {
      Minecraft.getMinecraft();
      AxisAlignedBB par1AxisAlignedBB = Minecraft.thePlayer.boundingBox.contract(0.001D, 0.001D, 0.001D);
      int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
      int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
      int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
      int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
      int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
      int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

      for(int var11 = var4; var11 < var5; ++var11) {
         for(int var12 = var6; var12 < var7; ++var12) {
            for(int var13 = var8; var13 < var9; ++var13) {
               BlockPos pos = new BlockPos(var11, var12, var13);
               Minecraft.getMinecraft();
               Block var14 = Minecraft.theWorld.getBlockState(pos).getBlock();
               if (var14 instanceof BlockLiquid) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean CanStep() {
      Minecraft.getMinecraft();
      AxisAlignedBB par1AxisAlignedBB = Minecraft.thePlayer.boundingBox.contract(0.0D, 0.001D, 0.0D);
      int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
      int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);

      for(int y = var6; y < var7; ++y) {
         BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, (double)y, Minecraft.thePlayer.posZ);
         Minecraft.getMinecraft();
         Block var14 = Minecraft.theWorld.getBlockState(pos).getBlock();
         if (var14.isFullBlock()) {
            return true;
         }
      }

      return false;
   }

   public static boolean isOnLadder() {
      boolean onLadder = false;
      int y = (int)Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, 1.0D, 0.0D).minY;

      for(int x = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
            Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if (Objects.nonNull(block) && !(block instanceof BlockAir)) {
               if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                  return false;
               }

               onLadder = true;
            }
         }
      }

      if (!onLadder && !Minecraft.thePlayer.isOnLadder()) {
         return false;
      } else {
         return true;
      }
   }

   public static boolean isInsideBlock() {
      Minecraft.getMinecraft();
      int x = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX);

      while(true) {
         Minecraft.getMinecraft();
         if (x >= MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1) {
            return false;
         }

         Minecraft.getMinecraft();
         int y = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minY);

         while(true) {
            Minecraft.getMinecraft();
            if (y >= MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxY) + 1) {
               ++x;
               break;
            }

            Minecraft.getMinecraft();
            int z = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ);

            while(true) {
               Minecraft.getMinecraft();
               if (z >= MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1) {
                  ++y;
                  break;
               }

               Minecraft.getMinecraft();
               Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  Minecraft.getMinecraft();
                  WorldClient var10001 = Minecraft.theWorld;
                  BlockPos var10002 = new BlockPos(x, y, z);
                  Minecraft.getMinecraft();
                  AxisAlignedBB boundingBox;
                  if ((boundingBox = block.getCollisionBoundingBox(var10001, var10002, Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)))) != null) {
                     Minecraft.getMinecraft();
                     if (Minecraft.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                        return true;
                     }
                  }
               }

               ++z;
            }
         }
      }
   }

   public static float[] getBlockRotations(double x, double y, double z) {
      double var4 = x - Minecraft.thePlayer.posX + 0.5D;
      double var6 = z - Minecraft.thePlayer.posZ + 0.5D;
      double var8 = y - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - 1.0D);
      double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var6 * var6);
      float var12 = (float)(Math.atan2(var6, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      return new float[]{var12, (float)(-Math.atan2(var8, var14) * 180.0D / 3.141592653589793D)};
   }

   public static float[] getFacingRotations(BlockPos pos, EnumFacing facing) {
      EntityXPOrb temp = new EntityXPOrb(Minecraft.theWorld);
      temp.posX = (double)pos.getX() + 0.5D;
      temp.posY = (double)pos.getY() + 0.5D;
      temp.posZ = (double)pos.getZ() + 0.5D;
      temp.posX += (double)facing.getDirectionVec().getX() * 0.25D;
      temp.posZ += (double)facing.getDirectionVec().getZ() * 0.25D;
      temp.posY += 0.5D;
      return getRotationsNeeded(temp);
   }

   private static float[] getRotationsNeeded(Entity entity) {
      double posX = entity.posX - Minecraft.thePlayer.posX;
      double posY = entity.posY - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double posZ = entity.posZ - Minecraft.thePlayer.posZ;
      double var14 = (double)MathHelper.sqrt_double(posX * posX + posZ * posZ);
      float yaw = (float)(Math.atan2(posZ, posX) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(posY, var14) * 180.0D / 3.141592653589793D));
      return new float[]{yaw, pitch};
   }
}
