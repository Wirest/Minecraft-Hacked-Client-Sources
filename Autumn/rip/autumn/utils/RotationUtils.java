package rip.autumn.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public final class RotationUtils {
   private static final Minecraft mc = Minecraft.getMinecraft();

   public static float[] getRotations(double posX, double posY, double posZ) {
      EntityPlayerSP player = mc.thePlayer;
      double x = posX - player.posX;
      double y = posY - (player.posY + (double)player.getEyeHeight());
      double z = posZ - player.posZ;
      double dist = (double)MathHelper.sqrt_double(x * x + z * z);
      float yaw = (float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(y, dist) * 180.0D / 3.141592653589793D));
      return new float[]{yaw, pitch};
   }

   public static float[] getRotationsEntity(EntityLivingBase entity) {
      return PlayerUtils.isOnHypixel() && mc.thePlayer.isMoving() ? getRotations(entity.posX + MathUtils.randomNumber(0.03D, -0.03D), entity.posY + (double)entity.getEyeHeight() - 0.4D + MathUtils.randomNumber(0.07D, -0.07D), entity.posZ + MathUtils.randomNumber(0.03D, -0.03D)) : getRotations(entity.posX, entity.posY + (double)entity.getEyeHeight() - 0.4D, entity.posZ);
   }
}
