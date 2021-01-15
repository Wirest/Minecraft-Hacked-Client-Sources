// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class MovementUtils
{
    private static Minecraft mc;
    
    static {
        MovementUtils.mc = Minecraft.getMinecraft();
    }
    
    public static double getSpeed() {
        return Math.sqrt(MovementUtils.mc.thePlayer.motionX * MovementUtils.mc.thePlayer.motionX + MovementUtils.mc.thePlayer.motionZ * MovementUtils.mc.thePlayer.motionZ);
    }
    
    public static void setSpeed(final double speed) {
        MovementUtils.mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * speed;
        MovementUtils.mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * speed;
    }
    
    public static float getDirection() {
        float yaw = MovementUtils.mc.thePlayer.rotationYaw;
        final float forward = MovementUtils.mc.thePlayer.moveForward;
        final float strafe = MovementUtils.mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }
}
