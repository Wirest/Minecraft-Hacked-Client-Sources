package moonx.ohare.client.module.impl.player;

import java.awt.Color;
import java.util.Objects;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.BoundingBoxEvent;
import moonx.ohare.client.event.impl.player.MotionEvent;
import moonx.ohare.client.event.impl.player.PushEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;

public class Freecam extends Module {
    private double x,y,z,yaw,pitch;
    public Freecam() {
        super("Freecam", Category.PLAYER, new Color(0,160,255).getRGB());
        setDescription("Spawn nigga and be a creep");
    }

    @Override
    public void onEnable() {
        if (Objects.nonNull(getMc().theWorld)) {
            this.x = getMc().thePlayer.posX;
            this.y = getMc().thePlayer.posY;
            this.z = getMc().thePlayer.posZ;
            this.yaw = getMc().thePlayer.rotationYaw;
            this.pitch = getMc().thePlayer.rotationPitch;
            final EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(getMc().theWorld, getMc().thePlayer.getGameProfile());
            entityOtherPlayerMP.inventory = getMc().thePlayer.inventory;
            entityOtherPlayerMP.inventoryContainer = getMc().thePlayer.inventoryContainer;
            entityOtherPlayerMP.setPositionAndRotation(this.x, getMc().thePlayer.getEntityBoundingBox().minY, this.z, getMc().thePlayer.rotationYaw, getMc().thePlayer.rotationPitch);
            entityOtherPlayerMP.rotationYawHead = getMc().thePlayer.rotationYawHead;
            entityOtherPlayerMP.setSneaking(getMc().thePlayer.isSneaking());
            getMc().theWorld.addEntityToWorld(-6969, entityOtherPlayerMP);
        }
    }

    @Override
    public void onDisable() {
        if (Objects.nonNull(getMc().theWorld)) {
            getMc().thePlayer.jumpMovementFactor = 0.02f;
            getMc().thePlayer.setPosition(this.x, this.y, this.z);
            getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + 0.01, getMc().thePlayer.posZ, getMc().thePlayer.onGround));
            getMc().thePlayer.noClip = false;
            getMc().theWorld.removeEntityFromWorld(-6969);
            getMc().thePlayer.motionY = 0.0;
            getMc().thePlayer.rotationPitch = (float) pitch;
            getMc().thePlayer.rotationYaw = (float) yaw;
            yaw = pitch = 0;
        }
        getMc().renderGlobal.loadRenderers();
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        getMc().thePlayer.setVelocity(0.0, 0.0, 0.0);
        getMc().thePlayer.jumpMovementFactor = 1;
        if (getMc().currentScreen == null) {
            if (GameSettings.isKeyDown(getMc().gameSettings.keyBindJump)) {
                getMc().thePlayer.motionY += 1;
            }
            if (GameSettings.isKeyDown(getMc().gameSettings.keyBindSneak)) {
                getMc().thePlayer.motionY -= 1;
            }
        }
        getMc().thePlayer.noClip = true;
        getMc().thePlayer.renderArmPitch = 5000.0f;
    }

    @Handler
    public void onMotion(MotionEvent event) {
        setMoveSpeed(event, 1);
        if (!GameSettings.isKeyDown(getMc().gameSettings.keyBindSneak) && !GameSettings.isKeyDown(getMc().gameSettings.keyBindJump)) {
            event.setY(1 * 2.0 * -(getMc().thePlayer.rotationPitch / 180.0f) * (int) getMc().thePlayer.movementInput.moveForward);
        }
    }

    @Handler
    public void onPacket(PacketEvent event) {
        if (event.isSending()) {
            event.setCanceled(true);
        }
    }

    @Handler
    public void onBB(BoundingBoxEvent event) {
        event.setBoundingBox(null);
    }

    @Handler
    public void onPush(PushEvent event) {
        event.setCanceled(true);
    }

    private void setMoveSpeed(final MotionEvent event, final double speed) {
        double forward = getMc().thePlayer.movementInput.moveForward;
        double strafe = getMc().thePlayer.movementInput.moveStrafe;
        float yaw = getMc().thePlayer.rotationYaw;
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
        }
    }
}
