package VenusClient.online.Event.impl;

import VenusClient.online.Event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class EventMotionUpdate extends Event {

    public Minecraft mc = Minecraft.getMinecraft();

    private double posX;

    private double lastPosX;

    private double posY;

    private double lastPosY;

    private double posZ;

    private double lastPosZ;

    private float yaw;

    private float lastYaw;

    private float pitch;

    private float lastPitch;

    private boolean onGround;

    private Type type;

    public EventMotionUpdate(double posX, double posY, double posZ, float yaw, float pitch, boolean onGround, Type type) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.type = type;
    }

    public boolean isPre() {
        return (this.type == Type.PRE);
    }

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getLastPosX() {
        return this.lastPosX;
    }

    public void setLastPosX(double lastPosX) {
        this.lastPosX = lastPosX;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getLastPosY() {
        return this.lastPosY;
    }

    public void setLastPosY(double lastPosY) {
        this.lastPosY = lastPosY;
    }

    public double getPosZ() {
        return this.posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public double getLastPosZ() {
        return this.lastPosZ;
    }

    public void setLastPosZ(double lastPosZ) {
        this.lastPosZ = lastPosZ;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getLastYaw() {
        return this.lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getLastPitch() {
        return this.lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        PRE, POST;
    }

    public void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }

    public double getBaseMoveSpeed() {
        double baseSpeed = 0.2875D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            baseSpeed *= 1.0D + 0.2D * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        return baseSpeed;
    }

    public double getJumpBoostModifier(double baseJumpHeight) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += ((amplifier + 1) * 0.1F);
        }
        return baseJumpHeight;
    }


}
