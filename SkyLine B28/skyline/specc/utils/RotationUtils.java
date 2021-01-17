package skyline.specc.utils;

import net.minecraft.client.Mineman;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;


public class RotationUtils
{
    public static float[] getRotations(final Entity ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Mineman.getMinecraft().thePlayer.posX;
        final double zDiff = z - Mineman.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Mineman.getMinecraft().thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180 / 3.141592653589793) - 60.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }

}
