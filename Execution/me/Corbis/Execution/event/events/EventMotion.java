package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class EventMotion extends Event{
    public double x, y, z;
    boolean safeWalk;
    public EventMotion(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        Minecraft.getMinecraft().thePlayer.motionX = x;
    }
    public void setY(double y) {
        this.y = y;
        Minecraft.getMinecraft().thePlayer.motionY = y;
    }
    public double getY() {
        return y;

    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
        Minecraft.getMinecraft().thePlayer.motionZ = z;
    }

    public boolean isSafeWalk() {
        return safeWalk;
    }

    public void setSafeWalk(boolean safeWalk) {
        this.safeWalk = safeWalk;
    }

    public double getLegitMotion() {
        return 0.41999998688697815D;
    }

    public double getMovementSpeed() {
        double baseSpeed = 0.2873D;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public double getMovementSpeed(double baseSpeed) {
        double speed = baseSpeed;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            return speed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return speed;
    }

    public double getMotionY(double mY) {
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
            mY += (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1;
        }
        return mY;
    }




}
