/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.MathHelper
 */
package delta.utils;

import delta.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class MovementUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void setSpeed(double d) {
        MovementUtils.setSpeed((Entity)MovementUtils.mc.thePlayer, d);
    }

    public static double getSpeed(EntityLivingBase entityLivingBase) {
        return Math.sqrt(MovementUtils.square(entityLivingBase.motionX) + MovementUtils.square(entityLivingBase.motionZ));
    }

    public static float getDirection() {
        return MovementUtils.getDirection((EntityLivingBase)MovementUtils.mc.thePlayer);
    }

    public static void setSpeed(Entity entity, double d) {
        entity.motionX = (double)(-MathHelper.sin((float)MovementUtils.getDirection())) * d;
        entity.motionZ = (double)MathHelper.cos((float)MovementUtils.getDirection()) * d;
    }

    public static double square(double d) {
        return d * d;
    }

    public static double getSpeed() {
        return MovementUtils.getSpeed((EntityLivingBase)MovementUtils.mc.thePlayer);
    }

    static Minecraft getMinecraft() {
        return mc;
    }

    public static float getDirection(EntityLivingBase entityLivingBase) {
        float f = entityLivingBase.rotationYaw;
        float f2 = entityLivingBase.moveForward;
        float f3 = entityLivingBase.moveStrafing;
        f += (float)(f2 < 0.0f ? 180 : 0);
        if (f3 < 0.0f) {
            f += (float)(f2 == 0.0f ? 90 : (f2 < 0.0f ? -45 : 45));
        }
        if (f3 > 0.0f) {
            f -= (float)(f2 == 0.0f ? 90 : (f2 < 0.0f ? -45 : 45));
        }
        return f * ((float)Math.PI / 180);
    }

    public static boolean isMoving() {
        return Wrapper.mc.thePlayer.moveForward != 0.0f || Wrapper.mc.thePlayer.moveStrafing != 0.0f;
    }
}

