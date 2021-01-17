// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import net.minecraft.client.Minecraft;

public class MoveUtils
{
    public static Minecraft mc;
    public static Object instance;
    
    static {
        MoveUtils.mc = Minecraft.getMinecraft();
    }
    
    public static double getPosForSetPosX(final double value) {
        Minecraft.getMinecraft();
        final double yaw = Math.toRadians(Minecraft.thePlayer.rotationYaw);
        final double x = -Math.sin(yaw) * value;
        return x;
    }
    
    public static double getPosForSetPosZ(final double value) {
        Minecraft.getMinecraft();
        final double yaw = Math.toRadians(Minecraft.thePlayer.rotationYaw);
        final double z = Math.cos(yaw) * value;
        return z;
    }
    
    public static float getDirection() {
        float var1 = Minecraft.thePlayer.rotationYaw;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Minecraft.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }
}
