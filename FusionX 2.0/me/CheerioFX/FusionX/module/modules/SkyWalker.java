// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import me.CheerioFX.FusionX.events.MoveEvent;
import net.minecraft.potion.Potion;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class SkyWalker extends Module
{
    private boolean nc;
    private boolean glide;
    private double speed;
    private double glideSpeed;
    private double lastDist;
    private double moveSpeed;
    
    public SkyWalker() {
        super("SkyWalker", 21, Category.MOVEMENT);
        this.speed = 0.8;
        this.glideSpeed = 0.035;
        this.glide = false;
        this.nc = false;
    }
    
    @Override
    public void onEnable() {
        this.moveSpeed = ((SkyWalker.mc.thePlayer == null) ? 0.2873 : this.getBaseMoveSpeed());
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.moveSpeed = this.getBaseMoveSpeed();
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            this.nc = true;
            this.speed = 0.5;
        }
        super.onUpdate();
    }
    
    @EventTarget
    public void onEvent(final EventPreMotionUpdates pre) {
        final double xDist = SkyWalker.mc.thePlayer.posX - SkyWalker.mc.thePlayer.prevPosX;
        final double zDist = SkyWalker.mc.thePlayer.posZ - SkyWalker.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        if (this.nc) {
            if (!SkyWalker.mc.thePlayer.movementInput.jump && !SkyWalker.mc.thePlayer.movementInput.sneak && this.glide && SkyWalker.mc.thePlayer.movementInput.moveForward == 0.0 && SkyWalker.mc.thePlayer.movementInput.moveStrafe == 0.0) {
                final EntityPlayerSP thePlayer = SkyWalker.mc.thePlayer;
                final EntityPlayerSP thePlayer2 = SkyWalker.mc.thePlayer;
                final EntityPlayerSP thePlayer3 = SkyWalker.mc.thePlayer;
                final double motionX = 0.0;
                thePlayer3.motionY = 0.0;
                thePlayer2.motionZ = 0.0;
                thePlayer.motionX = 0.0;
                pre.setCancelled(true);
            }
            else if (SkyWalker.mc.thePlayer.movementInput.jump) {
                SkyWalker.mc.thePlayer.motionY = this.speed;
            }
            else if (SkyWalker.mc.thePlayer.movementInput.sneak) {
                SkyWalker.mc.thePlayer.motionY = -this.speed;
            }
            else if (this.glide) {
                SkyWalker.mc.thePlayer.motionY = -this.glideSpeed;
            }
            else {
                SkyWalker.mc.thePlayer.motionY = 0.0;
            }
        }
        else if (SkyWalker.mc.thePlayer.isSneaking()) {
            SkyWalker.mc.thePlayer.motionY = -0.4;
        }
        else if (SkyWalker.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
            SkyWalker.mc.thePlayer.motionY = 0.4;
        }
        else if (this.glide) {
            SkyWalker.mc.thePlayer.motionY = -this.glideSpeed;
        }
        else {
            SkyWalker.mc.thePlayer.motionY = 0.0;
        }
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (SkyWalker.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = SkyWalker.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    @EventTarget
    public void onPreUpdate(final MoveEvent event) {
        if (!this.getState()) {
            return;
        }
        if (SkyWalker.mc.thePlayer.isAirBorne) {
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            final MovementInput movementInput = SkyWalker.mc.thePlayer.movementInput;
            float forward = movementInput.moveForward;
            float strafe = movementInput.moveStrafe;
            float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
            if (forward == 0.0f && strafe == 0.0f) {
                event.x = 0.0;
                event.z = 0.0;
            }
            else if (forward != 0.0f) {
                if (strafe >= 1.0f) {
                    yaw += ((forward > 0.0f) ? -45 : 45);
                    strafe = 0.0f;
                }
                else if (strafe <= -1.0f) {
                    yaw += ((forward > 0.0f) ? 45 : -45);
                    strafe = 0.0f;
                }
                if (forward > 0.0f) {
                    forward = 1.0f;
                }
                else if (forward < 0.0f) {
                    forward = -1.0f;
                }
            }
            final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
            final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
            final double motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
            final double motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
            event.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
            event.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
            SkyWalker.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
            SkyWalker.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        else {
            SkyWalker.mc.thePlayer.jumpMovementFactor = 0.02f;
            Timer.timerSpeed = 1.0f;
            SkyWalker.mc.thePlayer.speedInAir = 0.02f;
        }
    }
}
