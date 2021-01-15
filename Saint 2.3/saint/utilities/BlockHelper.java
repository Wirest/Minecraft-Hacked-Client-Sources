package saint.utilities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public final class BlockHelper {
   private static Minecraft mc = Minecraft.getMinecraft();

   public static void bestTool(int x, int y, int z) {
      int blockId = Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
      int bestSlot = 0;
      float f = -1.0F;

      for(int i1 = 36; i1 < 45; ++i1) {
         try {
            ItemStack curSlot = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i1).getStack();
            if ((curSlot.getItem() instanceof ItemTool || curSlot.getItem() instanceof ItemSword || curSlot.getItem() instanceof ItemShears) && curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f) {
               bestSlot = i1 - 36;
               f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
            }
         } catch (Exception var8) {
         }
      }

      if (f != -1.0F) {
         Minecraft.getMinecraft().thePlayer.inventory.currentItem = bestSlot;
         mc.playerController.updateController();
      }

   }

   public static Block getBlockAtPos(BlockPos inBlockPos) {
      IBlockState s = mc.theWorld.getBlockState(inBlockPos);
      return s.getBlock();
   }

   public static float[] getBlockRotations(double x, double y, double z) {
      double var4 = x - mc.thePlayer.posX + 0.5D;
      double var6 = z - mc.thePlayer.posZ + 0.5D;
      double var8 = y - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - 1.0D);
      double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var6 * var6);
      float var12 = (float)(Math.atan2(var6, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      return new float[]{var12, (float)(-(Math.atan2(var8, var14) * 180.0D / 3.141592653589793D))};
   }

   public static boolean isInsideBlock(EntityPlayer player) {
      for(int x = MathHelper.floor_double(player.boundingBox.minX); x < MathHelper.floor_double(player.boundingBox.maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(player.boundingBox.minY); y < MathHelper.floor_double(player.boundingBox.maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(player.boundingBox.minZ); z < MathHelper.floor_double(player.boundingBox.maxZ) + 1; ++z) {
               Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                  if (boundingBox != null && player.boundingBox.intersectsWith(boundingBox)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static boolean isInLiquid() {
      boolean inLiquid = false;
      int y = (int)mc.thePlayer.boundingBox.minY;

      for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
            Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if (block != null && !(block instanceof BlockAir)) {
               if (!(block instanceof BlockLiquid)) {
                  return false;
               }

               inLiquid = true;
            }
         }
      }

      return inLiquid;
   }

   public static boolean isOnIce() {
      boolean onIce = false;
      int y = (int)(mc.thePlayer.boundingBox.minY - 1.0D);

      for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
            Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if (block != null && !(block instanceof BlockAir) && (block instanceof BlockPackedIce || block instanceof BlockIce)) {
               onIce = true;
            }
         }
      }

      return onIce;
   }

   public static boolean isOnLadder() {
      boolean onLadder = false;
      int y = (int)(mc.thePlayer.boundingBox.minY - 1.0D);

      for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
            Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if (block != null && !(block instanceof BlockAir)) {
               if (!(block instanceof BlockLadder) && !(block instanceof BlockLadder)) {
                  return false;
               }

               onLadder = true;
            }
         }
      }

      if (!onLadder && !mc.thePlayer.isOnLadder()) {
         return false;
      } else {
         return true;
      }
   }

   public static boolean isOnLiquid() {
      boolean onLiquid = false;
      int y = (int)(mc.thePlayer.boundingBox.minY - 0.01D);

      for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
            Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if (block != null && !(block instanceof BlockAir)) {
               if (!(block instanceof BlockLiquid)) {
                  return false;
               }

               onLiquid = true;
            }
         }
      }

      return onLiquid;
   }
}
