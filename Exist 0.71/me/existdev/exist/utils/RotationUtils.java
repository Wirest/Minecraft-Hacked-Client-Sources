package me.existdev.exist.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3d;

public class RotationUtils {
   // $FF: synthetic field
   public static float server_yaw;
   // $FF: synthetic field
   public static float server_pitch;

   // $FF: synthetic method
   public static float getDistanceYaw() {
      Minecraft.getMinecraft();
      return MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationYaw) - server_yaw;
   }

   // $FF: synthetic method
   public static float getDistancePitch() {
      Minecraft.getMinecraft();
      return MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch) - server_pitch;
   }

   // $FF: synthetic method
   public static float[] aimAtLocation(double x, double y, double z, EnumFacing facing) {
      Minecraft.getMinecraft();
      EntitySnowball temp = new EntitySnowball(Minecraft.theWorld);
      temp.posX = x + 0.5D;
      temp.posY = y - 2.7035252353D;
      temp.posZ = z + 0.5D;
      temp.posX += (double)facing.getDirectionVec().getX() * 0.25D;
      temp.posY += (double)facing.getDirectionVec().getY() * 0.25D;
      temp.posZ += (double)facing.getDirectionVec().getZ() * 0.25D;
      return aimAtLocation(temp.posX, temp.posY, temp.posZ);
   }

   // $FF: synthetic method
   public static float[] getBlockRotations(BlockPos block) {
      Minecraft.getMinecraft();
      EntitySnowball p_70625_1_ = new EntitySnowball(Minecraft.theWorld, (double)block.getX(), (double)block.getY(), (double)block.getZ());
      double var10000 = p_70625_1_.posX;
      Minecraft.getMinecraft();
      double var4 = var10000 - Minecraft.thePlayer.posX;
      var10000 = p_70625_1_.posZ;
      Minecraft.getMinecraft();
      double var8 = var10000 - Minecraft.thePlayer.posZ;
      var10000 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0D;
      Minecraft.getMinecraft();
      double var10001 = Minecraft.thePlayer.posY;
      Minecraft.getMinecraft();
      double var6 = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight());
      double var141 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
      float var12 = (float)(Math.atan2(var8, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      float var13 = (float)(-(Math.atan2(var6, var141) * 180.0D / 3.141592653589793D));
      Minecraft.getMinecraft();
      float pitch = updateRotation(Minecraft.thePlayer.rotationPitch, var13, 1000.0F);
      Minecraft.getMinecraft();
      float yaw = updateRotation(Minecraft.thePlayer.rotationYaw, var12, 1000.0F);
      return new float[]{var12, var13};
   }

   // $FF: synthetic method
   public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing) {
      Minecraft.getMinecraft();
      EntitySnowball temp = new EntitySnowball(Minecraft.theWorld);
      temp.posX = (double)x;
      temp.posY = (double)y;
      temp.posZ = (double)z;
      temp.posX += (double)facing.getDirectionVec().getX() * 0.25D;
      temp.posY += (double)facing.getDirectionVec().getY() * 0.25D;
      temp.posZ += (double)facing.getDirectionVec().getZ() * 0.25D;
      return getAnglesToEntity(temp);
   }

   // $FF: synthetic method
   public static float[] getAnglesToEntity(Entity e) {
      float[] var10000 = new float[2];
      float var10003 = getYawChangeToEntity(e);
      Minecraft.getMinecraft();
      var10000[0] = var10003 + Minecraft.thePlayer.rotationYaw;
      var10003 = getPitchChangeToEntity(e);
      Minecraft.getMinecraft();
      var10000[1] = var10003 + Minecraft.thePlayer.rotationPitch;
      return var10000;
   }

   // $FF: synthetic method
   public static float getYawChangeToEntity(Entity entity) {
      double var10000 = entity.posX;
      Minecraft.getMinecraft();
      double deltaX = var10000 - Minecraft.thePlayer.posX;
      var10000 = entity.posZ;
      Minecraft.getMinecraft();
      double deltaZ = var10000 - Minecraft.thePlayer.posZ;
      double yawToEntity;
      if(deltaZ < 0.0D && deltaX < 0.0D) {
         yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else if(deltaZ < 0.0D && deltaX > 0.0D) {
         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else {
         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
      }

      Minecraft.getMinecraft();
      return MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)yawToEntity));
   }

   // $FF: synthetic method
   public static float getPitchChangeToEntity(Entity entity) {
      double var10000 = entity.posX;
      Minecraft.getMinecraft();
      double deltaX = var10000 - Minecraft.thePlayer.posX;
      var10000 = entity.posZ;
      Minecraft.getMinecraft();
      double deltaZ = var10000 - Minecraft.thePlayer.posZ;
      var10000 = entity.posY - 1.6D + (double)entity.getEyeHeight();
      Minecraft.getMinecraft();
      double deltaY = var10000 - Minecraft.thePlayer.posY;
      double distanceXZ = (double)MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
      double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
      Minecraft.getMinecraft();
      return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)pitchToEntity);
   }

   // $FF: synthetic method
   private static float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
      float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
      if(var4 > p_70663_3_) {
         var4 = p_70663_3_;
      }

      if(var4 < -p_70663_3_) {
         var4 = -p_70663_3_;
      }

      return p_70663_1_ + var4;
   }

   // $FF: synthetic method
   public static void faceVectorPacketInstant(Vec3d vec) {
      float[] rotations = getNeededRotations(vec);
      Minecraft.getMinecraft();
      NetHandlerPlayClient var10000 = Minecraft.thePlayer.sendQueue;
      float var10003 = rotations[0];
      float var10004 = rotations[1];
      Minecraft.getMinecraft();
      var10000.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(var10003, var10004, Minecraft.thePlayer.onGround));
   }

   // $FF: synthetic method
   public static float[] getNeededRotations(Vec3d vec) {
      Vec3d eyesPos = getEyesPos();
      double diffX = vec.xCoord - eyesPos.xCoord + 0.5D;
      double diffY = vec.yCoord - eyesPos.yCoord + 0.5D;
      double diffZ = vec.zCoord - eyesPos.zCoord + 0.5D;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-(Math.atan2(diffY, diffXZ) * 180.0D / 3.141592653589793D));
      return new float[]{MathHelper.wrapAngleTo180_float(yaw), Minecraft.getMinecraft().gameSettings.keyBindJump.pressed?90.0F:MathHelper.wrapAngleTo180_float(pitch)};
   }

   // $FF: synthetic method
   public static Vec3d getEyesPos() {
      Minecraft.getMinecraft();
      double var10002 = Minecraft.thePlayer.posX;
      Minecraft.getMinecraft();
      double var10003 = Minecraft.thePlayer.posY;
      Minecraft.getMinecraft();
      var10003 += (double)Minecraft.thePlayer.getEyeHeight();
      Minecraft.getMinecraft();
      return new Vec3d(var10002, var10003, Minecraft.thePlayer.posZ);
   }

   // $FF: synthetic method
   public static float[] aimAtBlock(BlockPos pos) {
      EnumFacing[] arrenumFacing = EnumFacing.values();
      int n = arrenumFacing.length;
      boolean n2 = false;
      float yaw = 1.0F;
      float pitch = 1.0F;
      if(n < 0) {
         return new float[]{1.0F, 1.0F};
      } else {
         EnumFacing side = arrenumFacing[0];
         BlockPos neighbor = pos.offset(side);
         EnumFacing side2 = side.getOpposite();
         Vec3d hitVec = (new Vec3d(neighbor)).addVector(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D).normalize());
         yaw = getNeededRotations(hitVec)[0];
         pitch = getNeededRotations(hitVec)[1];
         if(canBeClicked(neighbor)) {
            return new float[]{yaw, pitch};
         } else {
            hitVec = (new Vec3d(pos)).addVector(0.5D, 0.5D, 0.5D).add((new Vec3d(side.getDirectionVec())).scale(0.5D).normalize());
            yaw = getNeededRotations(hitVec)[0];
            pitch = getNeededRotations(hitVec)[1];
            return new float[]{yaw, pitch};
         }
      }
   }

   // $FF: synthetic method
   private static float[] aimAtLocation(double positionX, double positionY, double positionZ) {
      Minecraft.getMinecraft();
      double x = positionX - Minecraft.thePlayer.posX;
      Minecraft.getMinecraft();
      double y = positionY - Minecraft.thePlayer.posY;
      Minecraft.getMinecraft();
      double z = positionZ - Minecraft.thePlayer.posZ;
      double distance = (double)MathHelper.sqrt_double(x * x + z * z);
      return new float[]{(float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F, (float)(-(Math.atan2(y, distance) * 180.0D / 3.141592653589793D))};
   }

   // $FF: synthetic method
   public static boolean canBeClicked(BlockPos pos) {
      return getBlock(pos).canCollideCheck(getState(pos), false);
   }

   // $FF: synthetic method
   public static IBlockState getState(BlockPos pos) {
      Minecraft.getMinecraft();
      return Minecraft.theWorld.getBlockState(pos);
   }

   // $FF: synthetic method
   public static Block getBlock(BlockPos pos) {
      Minecraft.getMinecraft();
      return Minecraft.theWorld.getBlockState(pos).getBlock();
   }
}
