package com.etb.client.module.modules.player;

import java.awt.Color;
import java.util.Objects;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.BoundingBoxEvent;
import com.etb.client.event.events.player.MotionEvent;
import com.etb.client.event.events.player.PushEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;

/**
 * made by Xen for ETB Reloaded
 *
 * @since 6/10/2019
 **/
public class Freecam extends Module {
    private double x,y,z,yaw,pitch;
    public Freecam() {
        super("Freecam", Category.PLAYER, new Color(0,160,255).getRGB());
        setDescription("Noclip look at people");
    }

    @Override
    public void onEnable() {
        if (Objects.nonNull(mc.theWorld)) {
            this.x = mc.thePlayer.posX;
            this.y = mc.thePlayer.posY;
            this.z = mc.thePlayer.posZ;
            this.yaw = mc.thePlayer.rotationYaw;
            this.pitch = mc.thePlayer.rotationPitch;
            final EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
            entityOtherPlayerMP.inventory = mc.thePlayer.inventory;
            entityOtherPlayerMP.inventoryContainer = mc.thePlayer.inventoryContainer;
            entityOtherPlayerMP.setPositionAndRotation(this.x, mc.thePlayer.getEntityBoundingBox().minY, this.z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            entityOtherPlayerMP.rotationYawHead = mc.thePlayer.rotationYawHead;
            entityOtherPlayerMP.setSneaking(mc.thePlayer.isSneaking());
            mc.theWorld.addEntityToWorld(-6969, entityOtherPlayerMP);
        }
    }

    @Override
    public void onDisable() {
        if (Objects.nonNull(mc.theWorld)) {
            mc.thePlayer.jumpMovementFactor = 0.02f;
            mc.thePlayer.setPosition(this.x, this.y, this.z);
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, mc.thePlayer.onGround));
            mc.thePlayer.noClip = false;
            mc.theWorld.removeEntityFromWorld(-6969);
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.rotationPitch = (float) pitch;
            mc.thePlayer.rotationYaw = (float) yaw;
            yaw = pitch = 0;
        }
        mc.renderGlobal.loadRenderers();
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
        mc.thePlayer.jumpMovementFactor = 1;
        if (mc.currentScreen == null) {
            if (GameSettings.isKeyDown(mc.gameSettings.keyBindJump)) {
                final EntityPlayerSP thePlayer = mc.thePlayer;
                thePlayer.motionY += 1;
            }
            if (GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
                final EntityPlayerSP thePlayer2 = mc.thePlayer;
                thePlayer2.motionY -= 1;
            }
        }
        mc.thePlayer.noClip = true;
        mc.thePlayer.renderArmPitch = 5000.0f;
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        setMoveSpeed(event, 1);
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && !GameSettings.isKeyDown(mc.gameSettings.keyBindJump)) {
            event.setY(1 * 2.0 * -(mc.thePlayer.rotationPitch / 180.0f) * (int) mc.thePlayer.movementInput.moveForward);
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer) event.setCanceled(true);
        if (event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C0APacketAnimation)
            event.setCanceled(true);
    }

    @Subscribe
    public void onBB(BoundingBoxEvent event) {
        event.setBoundingBox(null);
    }

    @Subscribe
    public void onPush(PushEvent event) {
        event.setCanceled(true);
    }

    private void setMoveSpeed(final MotionEvent event, final double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
        }
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + (0.2 * amplifier);
        }
        return baseSpeed;
    }

}
