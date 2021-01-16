package org.m0jang.crystal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Wrapper {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static Minecraft getGame() {
      return Minecraft.getMinecraft();
   }

   public static void sendPacket(Packet p) {
      mc.getNetHandler().addToSendQueue(p);
   }

   public static Block getBlockAtPos(BlockPos inBlockPos) {
      IBlockState s = Minecraft.theWorld.getBlockState(inBlockPos);
      return s.getBlock();
   }

   public static void sendPacket(Packet packet, boolean fromPlayer) {
      if (fromPlayer) {
         getPlayer().sendQueue.addToSendQueue(packet);
      } else {
         getGame().getNetHandler().addToSendQueue(packet);
      }

   }

   public static BlockPos getBlockPos(BlockPos inBlockPos) {
      return inBlockPos;
   }

   public static void useCommand(String commandAndArguments) {
   }

   public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
      return getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + Minecraft.thePlayer.motionY + 0.1D - 1.2D, inPlayer.posZ));
   }

   public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double blocks) {
      blocks += (double)inPlayer.height;
      return getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + blocks, inPlayer.posZ));
   }

   public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
      return getBlockAtPos(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
   }

   public static BlockPos getBlockPos(double x, double y, double z) {
      return getBlockPos(new BlockPos(x, y, z));
   }

   public static Block getBlockUnderPlayer2(EntityPlayer inPlayer, double height) {
      return getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
   }

   public static Minecraft getMinecraft() {
      return Minecraft.getMinecraft();
   }

   public static EntityPlayerSP getPlayer() {
      getMinecraft();
      return Minecraft.thePlayer;
   }

   public static WorldClient getWorld() {
      getMinecraft();
      return Minecraft.theWorld;
   }

   public static FontRenderer getFontRenderer() {
      return getMinecraft().fontRendererObj;
   }

   public float getSpeed() {
      float vel = (float)Math.sqrt(Minecraft.thePlayer.motionX * Minecraft.thePlayer.motionX + Minecraft.thePlayer.motionZ * Minecraft.thePlayer.motionZ);
      return vel;
   }

   public float getDirections() {
      float var1 = Minecraft.thePlayer.rotationYaw;
      if (Minecraft.thePlayer.moveForward < 0.0F) {
         var1 += 180.0F;
      }

      float forward = 1.0F;
      if (Minecraft.thePlayer.moveForward < 0.0F) {
         forward = -0.5F;
      } else if (Minecraft.thePlayer.moveForward > 0.0F) {
         forward = 0.5F;
      } else {
         forward = 1.0F;
      }

      if (Minecraft.thePlayer.moveStrafing > 0.0F) {
         var1 -= 90.0F * forward;
      }

      if (Minecraft.thePlayer.moveStrafing < 0.0F) {
         var1 += 90.0F * forward;
      }

      var1 *= 0.017453292F;
      return var1;
   }

   public static void sendChatMessage(String msg) {
      sendPacket(new C01PacketChatMessage(msg));
   }

   public static PlayerControllerMP getPlayerController() {
      return getMinecraft().playerController;
   }

   public static NetHandlerPlayClient getSendQueue() {
      return getPlayer().sendQueue;
   }

   public static GameSettings getGameSettings() {
      getMinecraft();
      return Minecraft.gameSettings;
   }

   public static double getDistance(Entity e, BlockPos pos) {
      return (double)MathHelper.sqrt_float((float)(e.posX - (double)pos.getX()) * (float)(e.posX - (double)pos.getX()) + (float)(e.posY - (double)pos.getY()) * (float)(e.posY - (double)pos.getY()) + (float)(e.posZ - (double)pos.getZ()) * (float)(e.posZ - (double)pos.getZ()));
   }

   public static Block getBlock(BlockPos pos) {
      Minecraft.getMinecraft();
      return Minecraft.theWorld.getBlockState(pos).getBlock();
   }

   public static InventoryPlayer getInventory() {
      return getPlayer().inventory;
   }

   public static MovingObjectPosition getMouseOver() {
      return getMinecraft().objectMouseOver;
   }

   public static boolean isMoving(Entity e) {
      return e.motionX != 0.0D && e.motionZ != 0.0D && (e.motionY != 0.0D || e.motionY > 0.0D);
   }

   public static boolean isOnLiquid(AxisAlignedBB boundingBox) {
      boundingBox = boundingBox.contract(0.01D, 0.0D, 0.01D).offset(0.0D, -0.01D, 0.0D);
      boolean onLiquid = false;
      int y = (int)boundingBox.minY;

      for(int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0D); ++x) {
         for(int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0D); ++z) {
            Block block = getBlock(new BlockPos(x, y, z));
            if (block != Blocks.air) {
               if (!(block instanceof BlockLiquid)) {
                  return false;
               }

               onLiquid = true;
            }
         }
      }

      return onLiquid;
   }

   public static boolean isInLiquid(AxisAlignedBB par1AxisAlignedBB) {
      par1AxisAlignedBB = par1AxisAlignedBB.contract(0.001D, 0.001D, 0.001D);
      int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
      int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
      int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
      int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
      int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
      int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);
      if (getWorld().getChunkFromBlockCoords(new BlockPos(getPlayer().posX, getPlayer().posY, getPlayer().posZ)) == null) {
         return false;
      } else {
         new Vec3(0.0D, 0.0D, 0.0D);

         for(int var11 = var4; var11 < var5; ++var11) {
            for(int var12 = var6; var12 < var7; ++var12) {
               for(int var13 = var8; var13 < var9; ++var13) {
                  Block var14 = getBlock(new BlockPos(var11, var12, var13));
                  if (var14 instanceof BlockLiquid) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   public static String getDirection() {
	   //TODO           getRenderViewEntity ---> getRenderViewEntity
      return getMinecraft().getRenderViewEntity().func_174811_aO().name();
   }

   public Float[] getRotationToPosition(Entity e, BlockPos pos) {
      double x = (double)pos.getX() - e.posX;
      double y = (double)pos.getY() - (e.posY + (double)e.getEyeHeight());
      double z = (double)pos.getZ() - e.posZ;
      double helper = (double)MathHelper.sqrt_double(x * x + z * z);
      float newYaw = (float)Math.toDegrees(-Math.atan(x / z));
      float newPitch = (float)(-Math.toDegrees(Math.atan(y / helper)));
      if (z < 0.0D && x < 0.0D) {
         newYaw = (float)(90.0D + Math.toDegrees(Math.atan(z / x)));
      } else if (z < 0.0D && x > 0.0D) {
         newYaw = (float)(-90.0D + Math.toDegrees(Math.atan(z / x)));
      }

      return new Float[]{newYaw, newPitch};
   }

   public static boolean isInBlock(Entity e, float offset) {
      for(int x = MathHelper.floor_double(e.getEntityBoundingBox().minX); x < MathHelper.floor_double(e.getEntityBoundingBox().maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(e.getEntityBoundingBox().minY); y < MathHelper.floor_double(e.getEntityBoundingBox().maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(e.getEntityBoundingBox().minZ); z < MathHelper.floor_double(e.getEntityBoundingBox().maxZ) + 1; ++z) {
               Block block = getWorld().getBlockState(new BlockPos((double)x, (double)((float)y + offset), (double)z)).getBlock();
               if (block != null && !(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
                  AxisAlignedBB boundingBox = block.getCollisionBoundingBox(getWorld(), new BlockPos((double)x, (double)((float)y + offset), (double)z), getWorld().getBlockState(new BlockPos((double)x, (double)((float)y + offset), (double)z)));
                  if (boundingBox != null && e.getEntityBoundingBox().intersectsWith(boundingBox)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static float[] getBlockRotations(double x, double y, double z) {
      double var4 = x - Minecraft.thePlayer.posX + 0.5D;
      double var5 = z - Minecraft.thePlayer.posZ + 0.5D;
      double var6 = y - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - 1.0D);
      double var7 = (double)MathHelper.sqrt_double(var4 * var4 + var5 * var5);
      float var8 = (float)(Math.atan2(var5, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      return new float[]{var8, (float)(-(Math.atan2(var6, var7) * 180.0D / 3.141592653589793D))};
   }

   public static float getDistanceToGround(Entity e) {
      if (getPlayer().isCollidedVertically) {
         return 0.0F;
      } else {
         for(float a = (float)e.posY; a > 0.0F; --a) {
            int[] stairs = new int[]{53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180};
            int[] exemptIds = new int[]{6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157, 171, 175, 176, 177};
            Block block = getBlock(new BlockPos(e.posX, (double)(a - 1.0F), e.posZ));
            if (!(block instanceof BlockAir)) {
               if (Block.getIdFromBlock(block) != 44 && Block.getIdFromBlock(block) != 126) {
                  int[] array = stairs;
                  int length = stairs.length;

                  int length2;
                  int j;
                  for(length2 = 0; length2 < length; ++length2) {
                     j = array[length2];
                     if (Block.getIdFromBlock(block) == j) {
                        return (float)(e.posY - (double)a - 1.0D) < 0.0F ? 0.0F : (float)(e.posY - (double)a - 1.0D);
                     }
                  }

                  int[] array2 = exemptIds;
                  length2 = exemptIds.length;

                  for(j = 0; j < length2; ++j) {
                     int id = array2[j];
                     if (Block.getIdFromBlock(block) == id) {
                        return (float)(e.posY - (double)a) < 0.0F ? 0.0F : (float)(e.posY - (double)a);
                     }
                  }

                  return (float)(e.posY - (double)a + block.getBlockBoundsMaxY() - 1.0D);
               }

               return (float)(e.posY - (double)a - 0.5D) < 0.0F ? 0.0F : (float)(e.posY - (double)a - 0.5D);
            }
         }

         return 0.0F;
      }
   }

   public static Block getBlockatPosSpeed(EntityPlayer inPlayer, float x, float z) {
      double posX = inPlayer.posX + inPlayer.motionX * (double)x;
      double posZ = inPlayer.posZ + inPlayer.motionZ * (double)z;
      return getBlockAtPos(new BlockPos(posX, inPlayer.posY, posZ));
   }

   public static boolean isMoving() {
      return Minecraft.thePlayer.lastTickPosX != Minecraft.thePlayer.posX && Minecraft.thePlayer.lastTickPosZ != Minecraft.thePlayer.posZ;
   }

   public static boolean isOnLiquid() {
      boolean onLiquid = false;
      if (getBlockAtPosC(Minecraft.thePlayer, 0.30000001192092896D, 0.10000000149011612D, 0.30000001192092896D).getMaterial().isLiquid() && getBlockAtPosC(Minecraft.thePlayer, -0.30000001192092896D, 0.10000000149011612D, -0.30000001192092896D).getMaterial().isLiquid()) {
         onLiquid = true;
      }

      return onLiquid;
   }

   public static boolean isInLiquid() {
      boolean inLiquid = false;
      if (getBlockAtPosC(Minecraft.thePlayer, 0.30000001192092896D, 0.0D, 0.30000001192092896D).getMaterial().isLiquid() || getBlockAtPosC(Minecraft.thePlayer, -0.30000001192092896D, 0.0D, -0.30000001192092896D).getMaterial().isLiquid()) {
         inLiquid = true;
      }

      return inLiquid;
   }

   public static boolean isInFire() {
      boolean isInFire = false;
      if (getBlockAtPosC(Minecraft.thePlayer, 0.30000001192092896D, 0.0D, 0.30000001192092896D).getMaterial() == Material.fire || getBlockAtPosC(Minecraft.thePlayer, -0.30000001192092896D, 0.0D, -0.30000001192092896D).getMaterial() == Material.fire) {
         isInFire = true;
      }

      return isInFire;
   }

   public static String liquidCollision() {
      String colission = "";
      if (getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, 0.0D, 0.3100000023841858D).getMaterial().isLiquid()) {
         colission = "Positive";
      }

      if (getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, 0.0D, -0.3100000023841858D).getMaterial().isLiquid()) {
         colission = "Negative";
      }

      return colission;
   }

   public static boolean stairCollision() {
      boolean collision = false;
      if (getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, 0.0D, 0.3100000023841858D) instanceof BlockStairs || getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, 0.0D, -0.3100000023841858D) instanceof BlockStairs || getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, 0.0D, -0.3100000023841858D) instanceof BlockStairs || getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, 0.0D, 0.3100000023841858D) instanceof BlockStairs || getBlockatPosSpeed(Minecraft.thePlayer, 1.05F, 1.05F) instanceof BlockStairs) {
         collision = true;
      }

      return collision;
   }

   public static boolean isBlockUnderPlayer(Material material, float height) {
      return getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, (double)height, 0.3100000023841858D).getMaterial() == material && getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, (double)height, -0.3100000023841858D).getMaterial() == material && getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, (double)height, 0.3100000023841858D).getMaterial() == material && getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, (double)height, -0.3100000023841858D).getMaterial() == material;
   }

   public static boolean isBlockUnderPlayer2(float height) {
      return !(getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, (double)height, 0.3100000023841858D) instanceof BlockStairs) || !(getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, (double)height, -0.3100000023841858D) instanceof BlockStairs) || !(getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, (double)height, 0.3100000023841858D) instanceof BlockStairs) || !(getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, (double)height, -0.3100000023841858D) instanceof BlockStairs);
   }

   public static void blinkToPos(double[] startPos, BlockPos endPos, double slack) {
      double curX = startPos[0];
      double curY = startPos[1];
      double curZ = startPos[2];
      double endX = (double)endPos.getX() + 0.5D;
      double endY = (double)endPos.getY() + 1.0D;
      double endZ = (double)endPos.getZ() + 0.5D;
      double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);

      for(int count = 0; distance > slack; ++count) {
         distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
         if (count > 120) {
            break;
         }

         boolean next = false;
         double diffX = curX - endX;
         double diffY = curY - endY;
         double diffZ = curZ - endZ;
         double offset = (count & 1) == 0 ? 0.4D : 0.1D;
         if (diffX < 0.0D) {
            if (Math.abs(diffX) > offset) {
               curX += offset;
            } else {
               curX += Math.abs(diffX);
            }
         }

         if (diffX > 0.0D) {
            if (Math.abs(diffX) > offset) {
               curX -= offset;
            } else {
               curX -= Math.abs(diffX);
            }
         }

         if (diffY < 0.0D) {
            if (Math.abs(diffY) > 0.25D) {
               curY += 0.25D;
            } else {
               curY += Math.abs(diffY);
            }
         }

         if (diffY > 0.0D) {
            if (Math.abs(diffY) > 0.25D) {
               curY -= 0.25D;
            } else {
               curY -= Math.abs(diffY);
            }
         }

         if (diffZ < 0.0D) {
            if (Math.abs(diffZ) > offset) {
               curZ += offset;
            } else {
               curZ += Math.abs(diffZ);
            }
         }

         if (diffZ > 0.0D) {
            if (Math.abs(diffZ) > offset) {
               curZ -= offset;
            } else {
               curZ -= Math.abs(diffZ);
            }
         }

         Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
      }

   }
}
