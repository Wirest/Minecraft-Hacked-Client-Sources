package rip.autumn.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public final class PlayerUtils {
   private static final Minecraft mc = Minecraft.getMinecraft();

   public static boolean isHoldingSword() {
      return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
   }

   public static boolean isOnSameTeam(EntityPlayer entity) {
      if (entity.getTeam() != null && mc.thePlayer.getTeam() != null) {
         char c1 = entity.getDisplayName().getFormattedText().charAt(1);
         char c2 = mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
         return c1 == c2;
      } else {
         return false;
      }
   }

   public static boolean isInLiquid() {
      boolean inLiquid = false;
      AxisAlignedBB playerBB = mc.thePlayer.getEntityBoundingBox();
      int y = (int)playerBB.minY;

      for(int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
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

   public static boolean isOnLiquid() {
      boolean onLiquid = false;
      AxisAlignedBB playerBB = mc.thePlayer.getEntityBoundingBox();
      WorldClient world = mc.theWorld;
      int y = (int)playerBB.offset(0.0D, -0.01D, 0.0D).minY;

      for(int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
            Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
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

   public static boolean isOnHypixel() {
      return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.contains("hypixel");
   }

   public static void damage() {
      double offset = 0.060100000351667404D;
      NetHandlerPlayClient netHandler = mc.getNetHandler();
      EntityPlayerSP player = mc.thePlayer;
      double x = player.posX;
      double y = player.posY;
      double z = player.posZ;

      for(int i = 0; (double)i < (double)getMaxFallDist() / 0.05510000046342611D + 1.0D; ++i) {
         netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404D, z, false));
         netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4D, z, false));
         netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291D + 6.01000003516674E-8D, z, false));
      }

      netHandler.addToSendQueueSilent(new C03PacketPlayer(true));
   }

   public static float getMaxFallDist() {
      PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
      int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
      return (float)(mc.thePlayer.getMaxFallHeight() + f);
   }

   public static boolean isBlockUnder() {
      EntityPlayerSP player = mc.thePlayer;
      WorldClient world = mc.theWorld;
      AxisAlignedBB pBb = player.getEntityBoundingBox();
      double height = player.posY + (double)player.getEyeHeight();

      for(int offset = 0; (double)offset < height; offset += 2) {
         if (!world.getCollidingBoundingBoxes(player, pBb.offset(0.0D, (double)(-offset), 0.0D)).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public static boolean isInsideBlock() {
      EntityPlayerSP player = mc.thePlayer;
      WorldClient world = mc.theWorld;
      AxisAlignedBB bb = player.getEntityBoundingBox();

      for(int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
               Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
               AxisAlignedBB boundingBox;
               if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)))) != null && player.getEntityBoundingBox().intersectsWith(boundingBox)) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
