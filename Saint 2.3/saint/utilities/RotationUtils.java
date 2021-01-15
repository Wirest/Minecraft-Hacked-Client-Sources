package saint.utilities;

import net.minecraft.util.MathHelper;

public final class RotationUtils {
   private static float silentYaw = 0.0F;
   private static float realYaw = 0.0F;

   public static float getRealYaw() {
      return realYaw;
   }

   public static void setRealYaw(float yaw) {
      realYaw = yaw;
   }

   public static float getSilentYaw() {
      return silentYaw;
   }

   public static void setSilentYaw(float yaw) {
      silentYaw = yaw;
   }

   public static Object[] updateRotation(float current, float target, float maxIncrease) {
      float angle = MathHelper.wrapAngleTo180_float(target - current);
      boolean aiming = true;
      if (angle > maxIncrease) {
         angle = maxIncrease;
         aiming = false;
      }

      if (angle < -maxIncrease) {
         angle = -maxIncrease;
         aiming = false;
      }

      return new Object[]{current + angle, aiming};
   }
}
