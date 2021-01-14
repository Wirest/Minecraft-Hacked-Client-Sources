package com.etb.client.module.modules.movement;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.MotionEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.potion.Potion;

public class LongJump extends Module {
    private double moveSpeed, lastDist;
    private int level;
    private NumberValue<Double> boostval = new NumberValue("Boost", 3.0D, 1.0D, 5.0D, 0.1D);
    private BooleanValue vanilla = new BooleanValue("Vanilla", false);

    public LongJump() {
        super("LongJump", Category.MOVEMENT, new Color(0, 255, 150, 255).getRGB());
        setDescription("Jump and zoom");
        setRenderlabel("Long Jump");
        addValues(vanilla, boostval);
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.isOnLiquid() || mc.thePlayer.isInLiquid()) return;
        if (vanilla.isEnabled()) {
            setMoveSpeed(event,3);
            if (mc.thePlayer.isMoving()) {
                if (mc.thePlayer.onGround) {
                    event.setY(mc.thePlayer.motionY = 0.41);
                }
            } else {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
            }
        } else {
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;
            float yaw = mc.thePlayer.rotationYaw;
            if (forward == 0.0F && strafe == 0.0F) {
                event.setX(0);
                event.setZ(0);
            }
            if (forward != 0 && strafe != 0) {
                forward = forward * Math.sin(Math.PI / 4);
                strafe = strafe * Math.cos(Math.PI / 4);
            }
            if (level != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
                if (level == 2) {
                    ++level;
                    double motionY = 0.40123128;
                    if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                        if (mc.thePlayer.isPotionActive(Potion.jump)) motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                        event.setY(mc.thePlayer.motionY = motionY);
                        moveSpeed *= 2.149;
                    }
                } else if (level == 3) {
                    ++level;
                    double difference = 0.763D * (lastDist - getBaseMoveSpeed());
                    moveSpeed = lastDist - difference;
                } else {
                    if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                        level = 1;
                    }
                    moveSpeed = lastDist - lastDist / 159.0D;
                }
            } else {
                level = 2;
                double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? boostval.getValue() : boostval.getValue() + 1.1;
                moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
            }
            moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
            final double mx = -Math.sin(Math.toRadians(yaw));
            final double mz = Math.cos(Math.toRadians(yaw));
            event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
            event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null) return;
        setSuffix(vanilla.isEnabled() ? "Vanilla":"Hypixel");
        if (mc.thePlayer.isOnLiquid() || mc.thePlayer.isInLiquid()) return;
        if (!event.isPre()) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
        }
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
        double n = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        if (mc.thePlayer != null) {
            moveSpeed = getBaseMoveSpeed();
        }
        lastDist = 0.0D;
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        level = 0;
        lastDist = 0.0D;
    }
}
