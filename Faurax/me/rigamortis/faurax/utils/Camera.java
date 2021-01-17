package me.rigamortis.faurax.utils;

import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;

public class Camera
{
    private final Minecraft mc;
    private Timer timer;
    private double posX;
    private double posY;
    private double posZ;
    private float rotationYaw;
    private float rotationPitch;
    
    public Camera(final Entity entity) {
        this.mc = Minecraft.getMinecraft();
        if (this.timer == null) {
            this.timer = this.mc.timer;
        }
        this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks;
        this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks;
        this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks;
        this.setRotationYaw(entity.rotationYaw);
        this.setRotationPitch(entity.rotationPitch);
        if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            this.setRotationYaw(this.getRotationYaw() + (player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks));
            this.setRotationPitch(this.getRotationPitch() + (player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks));
        }
        else if (entity instanceof EntityLivingBase) {
            final EntityLivingBase living = (EntityLivingBase)entity;
            this.setRotationYaw(living.rotationYawHead);
        }
    }
    
    public Camera(final Entity entity, final double offsetX, final double offsetY, final double offsetZ, final double offsetRotationYaw, final double offsetRotationPitch) {
        this.mc = Minecraft.getMinecraft();
        this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks;
        this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks;
        this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks;
        this.setRotationYaw(entity.rotationYaw);
        this.setRotationPitch(entity.rotationPitch);
        if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            this.setRotationYaw(this.getRotationYaw() + (player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks));
            this.setRotationPitch(this.getRotationPitch() + (player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks));
        }
        this.posX += offsetX;
        this.posY += offsetY;
        this.posZ += offsetZ;
        this.rotationYaw += (float)offsetRotationYaw;
        this.rotationPitch += (float)offsetRotationPitch;
    }
    
    public Camera(final double posX, final double posY, final double posZ, final float rotationYaw, final float rotationPitch) {
        this.mc = Minecraft.getMinecraft();
        this.setPosX(posX);
        this.posY = posY;
        this.posZ = posZ;
        this.setRotationYaw(rotationYaw);
        this.setRotationPitch(rotationPitch);
    }
    
    public double getPosX() {
        return this.posX;
    }
    
    public void setPosX(final double posX) {
        this.posX = posX;
    }
    
    public double getPosY() {
        return this.posY;
    }
    
    public void setPosY(final double posY) {
        this.posY = posY;
    }
    
    public double getPosZ() {
        return this.posZ;
    }
    
    public void setPosZ(final double posZ) {
        this.posZ = posZ;
    }
    
    public float getRotationYaw() {
        return this.rotationYaw;
    }
    
    public void setRotationYaw(final float rotationYaw) {
        this.rotationYaw = rotationYaw;
    }
    
    public float getRotationPitch() {
        return this.rotationPitch;
    }
    
    public void setRotationPitch(final float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }
    
    public static float[] getRotation(final double posX1, final double posY1, final double posZ1, final double posX2, final double posY2, final double posZ2) {
        final float[] rotation = new float[2];
        final double diffX = posX2 - posX1;
        final double diffZ = posZ2 - posZ1;
        final double diffY = posY2 - posY1;
        final double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
        final double pitch = -Math.toDegrees(Math.atan(diffY / dist));
        rotation[1] = (float)pitch;
        double yaw = 0.0;
        if (diffZ >= 0.0 && diffX >= 0.0) {
            yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
        }
        else if (diffZ >= 0.0 && diffX <= 0.0) {
            yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
        }
        else if (diffZ <= 0.0 && diffX >= 0.0) {
            yaw = -90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
        }
        else if (diffZ <= 0.0 && diffX <= 0.0) {
            yaw = 90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
        }
        rotation[0] = (float)yaw;
        return rotation;
    }
}
